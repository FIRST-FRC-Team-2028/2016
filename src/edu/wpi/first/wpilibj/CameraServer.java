package edu.wpi.first.wpilibj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.RawData;
import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.vision.USBCamera;

//replicates CameraServer.cpp in java lib
@objid ("d08553c7-e396-4019-a66d-1625f1d57033")
public class CameraServer {
    @objid ("2e5c8c56-ca6e-4488-8fe0-65627c0b6605")
    private static final int kPort = 1180;

    @objid ("1ff98b86-6117-4986-b39f-204418d3bde2")
    private static final byte[] kMagicNumber = { 0x01, 0x00, 0x00, 0x00 };

    @objid ("3a1c1007-1968-409b-bf45-1f6da5df8e9f")
    private static final int kSize640x480 = 0;

    @objid ("f1c6eacf-c39f-46e8-97a7-928f1945b6f0")
    private static final int kSize320x240 = 1;

    @objid ("42d2adf0-725f-44ea-99d9-ba7ab0cd84a7")
    private static final int kSize160x120 = 2;

    @objid ("7ee7a823-6c28-45a5-b171-ae50630133cd")
    private static final int kHardwareCompression = -1;

    @objid ("20d72712-08b9-4993-a2ad-028e471558b4")
    private static final String kDefaultCameraName = "cam1";

    @objid ("6669b080-f08a-40d4-9f8b-9b6f3b27bde1")
    private static final int kMaxImageSize = 200000;

    @objid ("9674049d-4252-4344-9beb-d5c97b247604")
    private Thread serverThread;

    @objid ("b01ed79a-4841-4206-bd35-34d563448924")
    private int m_quality;

    @objid ("b0db05e4-1cc1-4c0a-b5b3-b97f5f0ccc67")
    private boolean m_autoCaptureStarted;

    @objid ("3c8a7ac4-04d6-43b2-9a58-5f100439e797")
    private boolean m_hwClient = true;

    @objid ("546f9bb5-160e-4da1-97b4-79c3241835fe")
    private Deque<ByteBuffer> m_imageDataPool;

    @objid ("4908ba3d-03ea-4025-a0e5-18678e139ce6")
    private static CameraServer server;

    @objid ("71bb46c7-faa6-489c-98e6-4713bd6a95bc")
    private USBCamera m_camera;

    @objid ("f40a3eda-5f5f-4774-a694-7cb631528bfd")
    private CameraData m_imageData;

    @objid ("ec05baca-924f-4138-beac-aae6014fcafb")
    public static CameraServer getInstance() {
        if (server == null) {
            server = new CameraServer();
        }
        return server;
    }

    @objid ("cdf8d616-3548-441d-96e3-be41b5fe8673")
    private CameraServer() {
        m_quality = 50;
        m_camera = null;
        m_imageData = null;
        m_imageDataPool = new ArrayDeque<>(3);
        for (int i = 0; i < 3; i++) {
            m_imageDataPool.addLast(ByteBuffer.allocateDirect(kMaxImageSize));
        }
        serverThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        serve();
                    } catch (IOException e) {
                        // do stuff here
                    } catch (InterruptedException e) {
                        // do stuff here
                    }
                }
            });
        serverThread.setName("CameraServer Send Thread");
        serverThread.start();
    }

    @objid ("85fa83ad-eb79-454f-b7f9-5fecbece6d73")
    private synchronized void setImageData(RawData data, int start) {
        if (m_imageData != null && m_imageData.data != null) {
            m_imageData.data.free();
            if (m_imageData.data.getBuffer() != null)
                m_imageDataPool.addLast(m_imageData.data.getBuffer());
            m_imageData = null;
        }
        m_imageData = new CameraData(data, start);
        notifyAll();
    }

    /**
     * Manually change the image that is served by the MJPEG stream. This can be
     * called to pass custom annotated images to the dashboard. Note that, for
     * 640x480 video, this method could take between 40 and 50 milliseconds to
     * complete.
     * 
     * This shouldn't be called if {@link #startAutomaticCapture} is called.
     * @param image The IMAQ image to show on the dashboard
     */
    @objid ("3bba800b-ec5e-4108-9e8c-199d8152b571")
    public void setImage(Image image) {
        // handle multi-threadedness
        
        /* Flatten the IMAQ image to a JPEG */
        
        RawData data = NIVision.imaqFlatten(image,
                                            NIVision.FlattenType.FLATTEN_IMAGE,
                                            NIVision.CompressionType.COMPRESSION_JPEG, 10 * m_quality);
        ByteBuffer buffer = data.getBuffer();
        boolean hwClient;
        
        synchronized (this) {
            hwClient = m_hwClient;
        }
        
        /* Find the start of the JPEG data */
        int index = 0;
        if (hwClient) {
            while (index < buffer.limit() - 1) {
                if ((buffer.get(index) & 0xff) == 0xFF
                    && (buffer.get(index + 1) & 0xff) == 0xD8)
                    break;
                index++;
            }
        }
        
        if (buffer.limit() - index - 1 <= 2) {
            throw new VisionException("data size of flattened image is less than 2. Try another camera! ");
        }
        
        setImageData(data, index);
    }

    /**
     * Start automatically capturing images to send to the dashboard.
     * You should call this method to just see a camera feed on the dashboard
     * without doing any vision processing on the roboRIO. {@link #setImage}
     * shouldn't be called after this is called.
     * This overload calles {@link #startAutomaticCapture(String)} with the
     * default camera name
     */
    @objid ("93f07927-85ed-4528-985b-9fe4f678ecec")
    public void startAutomaticCapture() {
        startAutomaticCapture(USBCamera.kDefaultCameraName);
    }

    /**
     * Start automatically capturing images to send to the dashboard.
     * 
     * You should call this method to just see a camera feed on the dashboard
     * without doing any vision processing on the roboRIO. {@link #setImage}
     * shouldn't be called after this is called.
     * @param cameraName The name of the camera interface (e.g. "cam1")
     */
    @objid ("8e4d5ad4-5d68-43de-ac81-8d1b1d50bf90")
    public void startAutomaticCapture(String cameraName) {
        USBCamera camera = new USBCamera(cameraName);
        camera.openCamera();
        startAutomaticCapture(camera);
    }

    @objid ("fb988f2b-5af9-42aa-b8ab-517f59018015")
    public synchronized void startAutomaticCapture(USBCamera camera) {
        if (m_autoCaptureStarted) return;
        m_autoCaptureStarted = true;
        m_camera = camera;
        
        m_camera.startCapture();
        
        Thread captureThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    capture();
                }
            });
        captureThread.setName("Camera Capture Thread");
        captureThread.start();
    }

    @objid ("f7bca934-aa99-4f43-8381-3df9b24059d6")
    protected void capture() {
        Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        while (true) {
            boolean hwClient;
            ByteBuffer dataBuffer = null;
            synchronized (this) {
                hwClient = m_hwClient;
                if (hwClient) {
                    dataBuffer = m_imageDataPool.removeLast();
                }
            }
        
            try {
                if (hwClient && dataBuffer != null) {
                    // Reset the image buffer limit
                    dataBuffer.limit(dataBuffer.capacity() - 1);
                    m_camera.getImageData(dataBuffer);
                    setImageData(new RawData(dataBuffer), 0);
                } else {
                    m_camera.getImage(frame);
                    setImage(frame);
                }
            } catch (VisionException ex) {
                DriverStation.reportError("Error when getting image from the camera: " + ex.getMessage(), true);
                if (dataBuffer != null) {
                    synchronized (this) {
                        m_imageDataPool.addLast(dataBuffer);
                        Timer.delay(.1);
                    }
                }
            }
        }
    }

    /**
     * check if auto capture is started
     */
    @objid ("b8274bca-f543-4f7d-91aa-2b8965d2859e")
    public synchronized boolean isAutoCaptureStarted() {
        return m_autoCaptureStarted;
    }

    /**
     * Sets the size of the image to use. Use the public kSize constants
     * to set the correct mode, or set it directory on a camera and call
     * the appropriate autoCapture method
     * @param size The size to use
     */
    @objid ("941912b7-841b-4af0-bed4-ab7f434a8bc0")
    public synchronized void setSize(int size) {
        if (m_camera == null) return;
        switch (size) {
        case kSize640x480:
            m_camera.setSize(640, 480);
            break;
        case kSize320x240:
            m_camera.setSize(320, 240);
            break;
        case kSize160x120:
            m_camera.setSize(160, 120);
            break;
        }
    }

    /**
     * Set the quality of the compressed image sent to the dashboard
     * @param quality The quality of the JPEG image, from 0 to 100
     */
    @objid ("0f76e6ed-e56f-4e72-8f4c-91c8bdf947fb")
    public synchronized void setQuality(int quality) {
        m_quality = quality > 100 ? 100 : quality < 0 ? 0 : quality;
    }

    /**
     * Get the quality of the compressed image sent to the dashboard
     * @return The quality, from 0 to 100
     */
    @objid ("a1baa431-592f-4b82-9b74-7cb9cfabd4cd")
    public synchronized int getQuality() {
        return m_quality;
    }

    /**
     * Run the M-JPEG server.
     * 
     * This function listens for a connection from the dashboard in a background
     * thread, then sends back the M-JPEG stream.
     * @throws IOException if the Socket connection fails
     * @throws InterruptedException if the sleep is interrupted
     */
    @objid ("701fcba8-04f5-4ade-a7ec-fa476bcd79c6")
    protected void serve() throws IOException, InterruptedException {
        ServerSocket socket = new ServerSocket();
        socket.setReuseAddress(true);
        InetSocketAddress address = new InetSocketAddress(kPort);
        socket.bind(address);
        
        while (true) {
            try {
                Socket s = socket.accept();
        
                DataInputStream is = new DataInputStream(s.getInputStream());
                DataOutputStream os = new DataOutputStream(s.getOutputStream());
        
                int fps = is.readInt();
                int compression = is.readInt();
                int size = is.readInt();
        
                if (compression != kHardwareCompression) {
                    DriverStation.reportError("Choose \"USB Camera HW\" on the dashboard", false);
                    s.close();
                    continue;
                }
        
                // Wait for the camera
                synchronized (this) {
                    System.out.println("Camera not yet ready, awaiting image");
                    if (m_camera == null) wait();
                    m_hwClient = compression == kHardwareCompression;
                    if (!m_hwClient) setQuality(100 - compression);
                    else if (m_camera != null) m_camera.setFPS(fps);
                    setSize(size);
                }
        
                long period = (long) (1000 / (1.0 * fps));
                while (true) {
                    long t0 = System.currentTimeMillis();
                    CameraData imageData = null;
                    synchronized (this) {
                        wait();
                        imageData = m_imageData;
                        m_imageData = null;
                    }
        
                    if (imageData == null) continue;
                    // Set the buffer position to the start of the data,
                    // and then create a new wrapper for the data at
                    // exactly that position
                    imageData.data.getBuffer().position(imageData.start);
                    byte[] imageArray = new byte[imageData.data.getBuffer().remaining()];
                    imageData.data.getBuffer().get(imageArray, 0, imageData.data.getBuffer().remaining());
        
                    // write numbers
                    try {
                        os.write(kMagicNumber);
                        os.writeInt(imageArray.length);
                        os.write(imageArray);
                        os.flush();
                        long dt = System.currentTimeMillis() - t0;
        
                        if (dt < period) {
                            Thread.sleep(period - dt);
                        }
                    } catch (IOException | UnsupportedOperationException ex) {
                        DriverStation.reportError(ex.getMessage(), true);
                        break;
                    } finally {
                        imageData.data.free();
                        if (imageData.data.getBuffer() != null) {
                            synchronized (this) {
                                m_imageDataPool.addLast(imageData.data.getBuffer());
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                DriverStation.reportError(ex.getMessage(), true);
                continue;
            }
        }
    }

    @objid ("af11d9af-9dd1-4736-9b73-7493aae71dee")
    private class CameraData {
        @objid ("b41b1a4a-7d00-4975-9512-611b6d4ae1b9")
         int start;

        @objid ("f9eace22-177e-4edc-8acc-9df04e1b9ec0")
         RawData data;

        @objid ("4f89574b-9492-474b-831d-2da281e05a0a")
        public CameraData(RawData d, int s) {
            data = d;
            start = s;
        }

    }

}

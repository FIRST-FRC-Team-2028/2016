/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.vision;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.HSLImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import static com.ni.vision.NIVision.Image.*;
import static com.ni.vision.NIVision.Priv_ReadJPEGString_C;
import static edu.wpi.first.wpilibj.Timer.delay;

/**
 * Axis M1011 network camera
 */
@objid ("be90ff3e-07f3-4413-85e4-f14802d330a1")
public class AxisCamera {
    @objid ("169725b7-2655-4347-8b4c-4d998d79a9a2")
    private static final String[] kWhiteBalanceStrings = {"auto", "hold", "fixed_outdoor1", "fixed_outdoor2", "fixed_indoor",
                    "fixed_fluor1", "fixed_fluor2",};

    @objid ("4f7ad4d1-75e9-4b92-92a9-fc0860f359e2")
    private static final String[] kExposureControlStrings = {"auto", "hold", "flickerfree50", "flickerfree60",};

    @objid ("ea3e5089-9ad0-4d9e-9486-b9e7cc1250bf")
    private static final String[] kResolutionStrings = {"640x480", "480x360", "320x240", "240x180", "176x144", "160x120",};

    @objid ("e289adf8-1835-49ce-bf04-1d2121d9607b")
    private static final String[] kRotationStrings = {"0", "180",};

    @objid ("b9b8d145-4b20-48d2-8300-b9617f7c18c4")
    private static final int kImageBufferAllocationIncrement = 1000;

    @objid ("55feb7bd-2f16-48a0-9c1d-c7d6bdeb961d")
    private String m_cameraHost;

    @objid ("a2c4698a-8f63-4e1f-8560-56807fccd466")
    private Socket m_cameraSocket;

    @objid ("74891fae-9e37-46c7-915c-f324c7a90c2a")
    private ByteBuffer m_imageData = ByteBuffer.allocate(5000);

    @objid ("5a0ddac6-16fc-4066-a615-6089f4da3df0")
    private final Object m_imageDataLock = new Object();

    @objid ("1483ea31-0b27-4770-86e1-7f66bb267ec3")
    private boolean m_freshImage = false;

    @objid ("cf03643a-e155-4879-9b19-cd63f67ad212")
    private int m_brightness = 50;

    @objid ("a867ee1c-ac4c-4f5c-b3b9-41c8918fb4fd")
    private WhiteBalance m_whiteBalance = WhiteBalance.kAutomatic;

    @objid ("770beec4-1d06-42f6-a6f7-37b2a0e285e5")
    private int m_colorLevel = 50;

    @objid ("e74da143-b68e-41c2-b5cc-f830a87ea6d6")
    private ExposureControl m_exposureControl = ExposureControl.kAutomatic;

    @objid ("c5770a42-23d1-41e8-82fc-67241fb7195e")
    private int m_exposurePriority = 50;

    @objid ("8018a625-5cbb-48fc-b4f2-d8cfb5a44d6e")
    private int m_maxFPS = 0;

    @objid ("594a3c4f-5a54-4ea8-aa5d-c763a962b788")
    private Resolution m_resolution = Resolution.k640x480;

    @objid ("d7d49595-0074-4800-98d4-9fd87a8810f9")
    private int m_compression = 50;

    @objid ("977e39df-e85b-43f2-962e-05d75c191c32")
    private Rotation m_rotation = Rotation.k0;

    @objid ("7825ef52-2ec1-4327-bb17-b4ba23bc3789")
    private final Object m_parametersLock = new Object();

    @objid ("d0b116d3-9ec7-4549-8970-4b1db3b21101")
    private boolean m_parametersDirty = true;

    @objid ("9e5d36d5-ccb4-43c6-8936-75203e760632")
    private boolean m_streamDirty = true;

    @objid ("5841a4c4-b2db-4344-b614-076a6f37c9c6")
    private boolean m_done = false;

    /**
     * Thread spawned by AxisCamera constructor to receive images from cam
     */
    @objid ("9f7a57e7-e537-4630-820f-a11fd0757c97")
    private Thread m_captureThread = new Thread(new Runnable() {
        @Override
        public void run() {
            int consecutiveErrors = 0;
            // Loop on trying to setup the camera connection. This happens in a background
            // thread so it shouldn't effect the operation of user programs.
            while (!m_done) {
                String requestString = "GET /mjpg/video.mjpg HTTP/1.1\n" +
                        "User-Agent: HTTPStreamClient\n" +
                        "Connection: Keep-Alive\n" +
                        "Cache-Control: no-cache\n" +
                        "Authorization: Basic RlJDOkZSQw==\n\n";
                try {
                    m_cameraSocket = AxisCamera.this.createCameraSocket(requestString);
                    AxisCamera.this.readImagesFromCamera();
                    consecutiveErrors = 0;
                } catch (IOException e) {
                    consecutiveErrors++;
                    if (consecutiveErrors > 5) {
                        e.printStackTrace();
                    }
                }
                delay(0.5);
            }
        }
    });

    /**
     * AxisCamera constructor
     * @param cameraHost The host to find the camera at, typically an IP address
     */
    @objid ("b4052aa4-3cc0-42a9-98ed-910fc3853d83")
    public AxisCamera(String cameraHost) {
        m_cameraHost = cameraHost;
        m_captureThread.start();
    }

    /**
     * Return true if the latest image from the camera has not been retrieved by calling GetImage() yet.
     * @return true if the image has not been retrieved yet.
     */
    @objid ("2de625fb-0c95-4b59-99e2-dcf3677dc28f")
    public boolean isFreshImage() {
        return m_freshImage;
    }

    /**
     * Get an image from the camera and store it in the provided image.
     * @param image The imaq image to store the result in. This must be an HSL or RGB image.
     * @return <code>true</code> upon success, <code>false</code> on a failure
     */
    @objid ("b079a0b7-67f7-40e4-b061-9cf005c397d9")
    public boolean getImage(Image image) {
        if (m_imageData.limit() == 0) {
            return false;
        }
        
        synchronized (m_imageDataLock) {
            Priv_ReadJPEGString_C(image, m_imageData.array());
        }
        
        m_freshImage = false;
        return true;
    }

    /**
     * Get an image from the camera and store it in the provided image.
     * @param image The image to store the result in. This must be an HSL or RGB image
     * @return true upon success, false on a failure
     */
    @objid ("67656093-7ab3-444c-97e7-4c686509d486")
    public boolean getImage(ColorImage image) {
        return this.getImage(image.image);
    }

    /**
     * Instantiate a new image object and fill it with the latest image from the camera.
     * @return a pointer to an HSLImage object
     */
    @objid ("5fb31370-1e53-4ce0-8231-012f447ad2ba")
    public HSLImage getImage() throws NIVisionException {
        HSLImage image = new HSLImage();
        this.getImage(image);
        return image;
    }

    /**
     * Request a change in the brightness of the camera images.
     * @param brightness valid values 0 .. 100
     */
    @objid ("735514a7-fd51-463a-a87a-5efee431ae71")
    public void writeBrightness(int brightness) {
        if (brightness < 0 || brightness > 100) {
            throw new IllegalArgumentException("Brightness must be from 0 to 100");
        }
        
        synchronized (m_parametersLock) {
            if (m_brightness != brightness) {
                m_brightness = brightness;
                m_parametersDirty = true;
            }
        }
    }

    /**
     * @return The configured brightness of the camera images
     */
    @objid ("123623e9-7e72-45a5-b85b-2ccb9825b687")
    public int getBrightness() {
        synchronized (m_parametersLock) {
            return m_brightness;
        }
    }

    /**
     * Request a change in the white balance on the camera.
     * @param whiteBalance Valid values from the <code>WhiteBalance</code> enum.
     */
    @objid ("1eff68a0-2cc5-4026-a92d-e7621f7a1a29")
    public void writeWhiteBalance(WhiteBalance whiteBalance) {
        synchronized (m_parametersLock) {
            if (m_whiteBalance != whiteBalance) {
                m_whiteBalance = whiteBalance;
                m_parametersDirty = true;
            }
        }
    }

    /**
     * @return The configured white balances of the camera images
     */
    @objid ("7600aeda-f4c4-4950-b0e3-66c0cb1dd1f0")
    public WhiteBalance getWhiteBalance() {
        synchronized (m_parametersLock) {
            return m_whiteBalance;
        }
    }

    /**
     * Request a change in the color level of the camera images.
     * @param colorLevel valid values are 0 .. 100
     */
    @objid ("02528471-8dad-4a9c-a1f8-cfa6682026c2")
    public void writeColorLevel(int colorLevel) {
        if (colorLevel < 0 || colorLevel > 100) {
            throw new IllegalArgumentException("Color level must be from 0 to 100");
        }
        
        synchronized (m_parametersLock) {
            if (m_colorLevel != colorLevel) {
                m_colorLevel = colorLevel;
                m_parametersDirty = true;
            }
        }
    }

    /**
     * @return The configured color level of the camera images
     */
    @objid ("3e58d7cf-c63b-4eeb-b486-ab8e72f42a7c")
    public int getColorLevel() {
        synchronized (m_parametersLock) {
            return m_colorLevel;
        }
    }

    /**
     * Request a change in the camera's exposure mode.
     * @param exposureControl A mode to write in the <code>Exposure</code> enum.
     */
    @objid ("cbe8cba6-5372-4e3b-953d-aa0d7d5bd65d")
    public void writeExposureControl(ExposureControl exposureControl) {
        synchronized (m_parametersLock) {
            if (m_exposureControl != exposureControl) {
                m_exposureControl = exposureControl;
                m_parametersDirty = true;
            }
        }
    }

    /**
     * @return The configured exposure control mode of the camera
     */
    @objid ("069b6f54-e5a9-4964-9248-74fb45c422f1")
    public ExposureControl getExposureControl() {
        synchronized (m_parametersLock) {
            return m_exposureControl;
        }
    }

    /**
     * Request a change in the exposure priority of the camera.
     * @param exposurePriority Valid values are 0, 50, 100.
     * 0 = Prioritize image quality
     * 50 = None
     * 100 = Prioritize frame rate
     */
    @objid ("ddd35215-d8f9-4deb-8bff-8eb8be85d495")
    public void writeExposurePriority(int exposurePriority) {
        if (exposurePriority != 0 && exposurePriority != 50 && exposurePriority != 100) {
            throw new IllegalArgumentException("Exposure priority must be 0, 50, or 100");
        }
        
        synchronized (m_parametersLock) {
            if (m_exposurePriority != exposurePriority) {
                m_exposurePriority = exposurePriority;
                m_parametersDirty = true;
            }
        }
    }

    /**
     * @return The configured exposure priority of the camera
     */
    @objid ("5888b725-3242-4fc8-b947-7eb8fe532593")
    public int getExposurePriority() {
        synchronized (m_parametersLock) {
            return m_exposurePriority;
        }
    }

    /**
     * Write the maximum frames per second that the camera should send
     * Write 0 to send as many as possible.
     * @param maxFPS The number of frames the camera should send in a second, exposure permitting.
     */
    @objid ("ce6588d4-d467-4da2-b53a-c7d06c254868")
    public void writeMaxFPS(int maxFPS) {
        synchronized (m_parametersLock) {
            if (m_maxFPS != maxFPS) {
                m_maxFPS = maxFPS;
                m_parametersDirty = true;
                m_streamDirty = true;
            }
        }
    }

    /**
     * @return The configured maximum FPS of the camera
     */
    @objid ("cc18958b-4576-4681-b4e1-d81d396e0d22")
    public int getMaxFPS() {
        synchronized (m_parametersLock) {
            return m_maxFPS;
        }
    }

    /**
     * Write resolution value to camera.
     * @param resolution The camera resolution value to write to the camera.
     */
    @objid ("1ecd4b97-a41a-490f-9916-ed26de165d3c")
    public void writeResolution(Resolution resolution) {
        synchronized (m_parametersLock) {
            if (m_resolution != resolution) {
                m_resolution = resolution;
                m_parametersDirty = true;
                m_streamDirty = true;
            }
        }
    }

    /**
     * @return The configured resolution of the camera (not necessarily the same
     * resolution as the most recent image, if it was changed recently.)
     */
    @objid ("986a38ff-c940-4a88-9e5f-b56e21f0908c")
    public Resolution getResolution() {
        synchronized (m_parametersLock) {
            return m_resolution;
        }
    }

    /**
     * Write the compression value to the camera.
     * @param compression Values between 0 and 100.
     */
    @objid ("39254cec-e05f-4f8c-b828-c4bfadf3d796")
    public void writeCompression(int compression) {
        if (compression < 0 || compression > 100) {
            throw new IllegalArgumentException("Compression must be from 0 to 100");
        }
        
        synchronized (m_parametersLock) {
            if (m_compression != compression) {
                m_compression = compression;
                m_parametersDirty = true;
                m_streamDirty = true;
            }
        }
    }

    /**
     * @return The configured compression level of the camera images
     */
    @objid ("54d2850f-7021-4f32-b969-061bc11b3095")
    public int getCompression() {
        synchronized (m_parametersLock) {
            return m_compression;
        }
    }

    /**
     * Write the rotation value to the camera.
     * If you mount your camera upside down, use this to adjust the image for you.
     * @param rotation A value from the {@link Rotation} enum
     */
    @objid ("632a4b9f-40a1-4293-82c2-cd596a5c3eb3")
    public void writeRotation(Rotation rotation) {
        synchronized (m_parametersLock) {
            if (m_rotation != rotation) {
                m_rotation = rotation;
                m_parametersDirty = true;
                m_streamDirty = true;
            }
        }
    }

    /**
     * @return The configured rotation mode of the camera
     */
    @objid ("86ea3b8f-7150-4837-8dd6-a5e2e6174063")
    public Rotation getRotation() {
        synchronized (m_parametersLock) {
            return m_rotation;
        }
    }

    /**
     * This function actually reads the images from the camera.
     */
    @objid ("7894ecf1-44f5-4f89-a092-311225a24292")
    private void readImagesFromCamera() throws IOException {
        DataInputStream cameraInputStream = new DataInputStream(m_cameraSocket.getInputStream());
        
        while (!m_done) {
            String line = cameraInputStream.readLine();
        
            if (line.startsWith("Content-Length: ")) {
                int contentLength = Integer.valueOf(line.substring(16));
        
                /* Skip the next blank line */
                cameraInputStream.readLine();
                contentLength -= 4;
        
                /* The next four bytes are the JPEG magic number */
                byte[] data = new byte[contentLength];
                cameraInputStream.readFully(data);
        
                synchronized (m_imageDataLock) {
                    if (m_imageData.capacity() < data.length) {
                        m_imageData = ByteBuffer.allocate(data.length + kImageBufferAllocationIncrement);
                    }
        
                    m_imageData.clear();
                    m_imageData.limit(contentLength);
                    m_imageData.put(data);
        
                    m_freshImage = true;
                }
        
                if (this.writeParameters()) {
                    break;
                }
        
                /* Skip the boundary and Content-Type header */
                cameraInputStream.readLine();
                cameraInputStream.readLine();
            }
        }
        
        m_cameraSocket.close();
    }

    /**
     * Send a request to the camera to set all of the parameters.  This is called
     * in the capture thread between each frame. This strategy avoids making lots
     * of redundant HTTP requests, accounts for failed initial requests, and
     * avoids blocking calls in the main thread unless necessary.
     * <p>
     * This method does nothing if no parameters have been modified since it last
     * completely successfully.
     * @return <code>true</code> if the stream should be restarted due to a
     * parameter changing.
     */
    @objid ("8c2d55b3-0044-4721-82dc-252c07f6147c")
    private boolean writeParameters() {
        if (m_parametersDirty) {
            String request = "GET /axis-cgi/admin/param.cgi?action=update";
        
            synchronized (m_parametersLock) {
                request += "&ImageSource.I0.Sensor.Brightness=" + m_brightness;
                request += "&ImageSource.I0.Sensor.WhiteBalance=" + kWhiteBalanceStrings[m_whiteBalance.ordinal()];
                request += "&ImageSource.I0.Sensor.ColorLevel=" + m_colorLevel;
                request += "&ImageSource.I0.Sensor.Exposure=" + kExposureControlStrings[m_exposureControl.ordinal()];
                request += "&ImageSource.I0.Sensor.ExposurePriority=" + m_exposurePriority;
                request += "&Image.I0.Stream.FPS=" + m_maxFPS;
                request += "&Image.I0.Appearance.Resolution=" + kResolutionStrings[m_resolution.ordinal()];
                request += "&Image.I0.Appearance.Compression=" + m_compression;
                request += "&Image.I0.Appearance.Rotation=" + kRotationStrings[m_rotation.ordinal()];
            }
        
            request += " HTTP/1.1\n";
            request += "User-Agent: HTTPStreamClient\n";
            request += "Connection: Keep-Alive\n";
            request += "Cache-Control: no-cache\n";
            request += "Authorization: Basic RlJDOkZSQw==\n\n";
        
            try {
                Socket socket = this.createCameraSocket(request);
                socket.close();
        
                m_parametersDirty = false;
        
                if (m_streamDirty) {
                    m_streamDirty = false;
                    return true;
                } else {
                    return false;
                }
            } catch (IOException | NullPointerException e) {
                return false;
            }
        
        }
        return false;
    }

    /**
     * Create a socket connected to camera
     * Used to create a connection for reading images and setting parameters
     * @param requestString The initial request string to send upon successful connection.
     * @return The created socket
     */
    @objid ("7840ca2b-3a56-4bb2-a2a7-4262382a153f")
    private Socket createCameraSocket(String requestString) throws IOException {
        /* Connect to the server */
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(m_cameraHost, 80), 5000);
        
        /* Send the HTTP headers */
        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(requestString.getBytes());
        return socket;
    }

    @objid ("00b397f4-c231-4184-b786-9438fcc33fbb")
    @Override
    public String toString() {
        return "AxisCamera{" +
                        "FreshImage=" + isFreshImage() +
                        ", Brightness=" + getBrightness() +
                        ", WhiteBalance=" + getWhiteBalance() +
                        ", ColorLevel=" + getColorLevel() +
                        ", ExposureControl=" + getExposureControl() +
                        ", ExposurePriority=" + getExposurePriority() +
                        ", MaxFPS=" + getMaxFPS() +
                        ", Resolution=" + getResolution() +
                        ", Compression=" + getCompression() +
                        ", Rotation=" + getRotation() +
                        '}';
    }

    @objid ("0c36fc71-82f2-4077-b0b0-033d9db3adc4")
    public enum WhiteBalance {
        kAutomatic,
        kHold,
        kFixedOutdoor1,
        kFixedOutdoor2,
        kFixedIndoor,
        kFixedFluorescent1,
        kFixedFluorescent2;
    }

    @objid ("4718056e-cb95-4e7b-86b6-d58f6762bef2")
    public enum ExposureControl {
        kAutomatic,
        kHold,
        kFlickerFree50Hz,
        kFlickerFree60Hz;
    }

    @objid ("99b55a2d-2148-4819-92c9-8cd327d25a0d")
    public enum Resolution {
        k640x480,
        k480x360,
        k320x240,
        k240x180,
        k176x144,
        k160x120;
    }

    @objid ("dce0df37-b9c1-4613-94dc-f3760663f6ae")
    public enum Rotation {
        k0,
        k180;
    }

}

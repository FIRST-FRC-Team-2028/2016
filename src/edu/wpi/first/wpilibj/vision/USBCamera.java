package edu.wpi.first.wpilibj.vision;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import static com.ni.vision.NIVision.Image.*;
import static edu.wpi.first.wpilibj.Timer.delay;

@objid ("fc7a5d2a-69b7-4c13-be9d-0730a725b11d")
public class USBCamera {
    @objid ("00ca9668-888a-4355-8e6f-87c056a85ef3")
    public static String kDefaultCameraName = "cam0";

    @objid ("a7fa9dad-875c-42ef-924c-332a03cf3c65")
    private static String ATTR_VIDEO_MODE = "AcquisitionAttributes::VideoMode";

    @objid ("6b4ef6ac-6180-44ec-a9ff-cf2fe8d505b4")
    private static String ATTR_WB_MODE = "CameraAttributes::WhiteBalance::Mode";

    @objid ("1025109e-c695-4d91-bb9a-14393ddccc81")
    private static String ATTR_WB_VALUE = "CameraAttributes::WhiteBalance::Value";

    @objid ("4ba9f6e9-74be-43e3-9d85-155633d6d5f1")
    private static String ATTR_EX_MODE = "CameraAttributes::Exposure::Mode";

    @objid ("6d23f042-dbd6-41e5-831e-7c2c42d3fb85")
    private static String ATTR_EX_VALUE = "CameraAttributes::Exposure::Value";

    @objid ("fcce6cd4-dbde-4c0a-93b0-74255bc84306")
    private static String ATTR_BR_MODE = "CameraAttributes::Brightness::Mode";

    @objid ("0f2df194-2160-44a4-abfc-e75ddc3be138")
    private static String ATTR_BR_VALUE = "CameraAttributes::Brightness::Value";

    @objid ("efbfaca2-7f85-44f1-817c-8bb2220cc62e")
    private Pattern m_reMode = Pattern.compile("(?<width>[0-9]+)\\s*x\\s*(?<height>[0-9]+)\\s+(?<format>.*?)\\s+(?<fps>[0-9.]+)\\s*fps");

    @objid ("5d22bf6e-9532-4ad7-856b-2072dea7e285")
    private String m_name = kDefaultCameraName;

    @objid ("1ff312c1-2c9e-4cdc-a602-bb13085158b8")
    private int m_id = -1;

    @objid ("7969a35c-be30-443b-886e-38303b3f0bbd")
    private boolean m_active = false;

    @objid ("e387547b-702b-4d87-8184-978cf0cabeaf")
    private boolean m_useJpeg = true;

    @objid ("b6455a49-24e7-4cd7-81fd-2063a97fb074")
    private int m_width = 320;

    @objid ("c81f4085-cf49-4083-b04c-2be91759bc35")
    private int m_height = 240;

    @objid ("d4bcfa33-7287-4c96-b9c8-7789839db045")
    private int m_fps = 30;

    @objid ("35afa6c6-7a6b-466e-a401-d2ecfa0a9738")
    private String m_whiteBalance = "auto";

    @objid ("5864813c-805c-40a6-b89f-78636c2996b1")
    private int m_whiteBalanceValue = -1;

    @objid ("5faf7071-ac51-4a3f-aee9-c376197e4ada")
    private String m_exposure = "auto";

    @objid ("d5be81c5-e695-48b9-a574-6e5f5f7e9eb3")
    private int m_exposureValue = -1;

    @objid ("1c6230db-d30b-48ce-ae3e-4235eb19e0ff")
    private int m_brightness = 50;

    @objid ("427acdc2-b6a0-4b07-8a7f-6b41aa9f8fd2")
    private boolean m_needSettingsUpdate = true;

    @objid ("942b5cb0-2049-4163-8b5a-10bcb7f0ce83")
    public USBCamera() {
        openCamera();
    }

    @objid ("2613656c-584f-4fbe-a6fe-59a0b57246b0")
    public USBCamera(String name) {
        m_name = name;
        openCamera();
    }

    @objid ("34f866a5-46ea-40e6-9ffd-3e52757edc43")
    public synchronized void openCamera() {
        if (m_id != -1) return; // Camera is already open
        for (int i=0; i<3; i++) {
            try {
                m_id = NIVision.IMAQdxOpenCamera(m_name,
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            } catch (VisionException e) {
                if (i == 2)
                    throw e;
                delay(2.0);
                continue;
            }
            break;
        }
    }

    @objid ("b86132e5-cba6-4309-b49f-cb00ce1d483c")
    public synchronized void closeCamera() {
        if (m_id == -1)
            return;
        NIVision.IMAQdxCloseCamera(m_id);
        m_id = -1;
    }

    @objid ("2b299d45-33e7-4a2c-ab60-19855a0aa765")
    public synchronized void startCapture() {
        if (m_id == -1 || m_active)
            return;
        NIVision.IMAQdxConfigureGrab(m_id);
        NIVision.IMAQdxStartAcquisition(m_id);
        m_active = true;
    }

    @objid ("304d8ea7-8428-43e6-a99e-db50775ef3c3")
    public synchronized void stopCapture() {
        if (m_id == -1 || !m_active)
            return;
        NIVision.IMAQdxStopAcquisition(m_id);
        NIVision.IMAQdxUnconfigureAcquisition(m_id);
        m_active = false;
    }

    @objid ("536e00e0-9027-483f-b1fd-09c60514be2e")
    public synchronized void updateSettings() {
        boolean wasActive = m_active;
        // Stop acquistion, close and reopen camera
        if (wasActive)
            stopCapture();
        if (m_id != -1)
            closeCamera();
        openCamera();
        
        // Video Mode
        NIVision.dxEnumerateVideoModesResult enumerated = NIVision.IMAQdxEnumerateVideoModes(m_id);
        NIVision.IMAQdxEnumItem foundMode = null;
        int foundFps = 1000;
        for (NIVision.IMAQdxEnumItem mode : enumerated.videoModeArray) {
            Matcher m = m_reMode.matcher(mode.Name);
            if (!m.matches())
                continue;
            if (Integer.parseInt(m.group("width")) != m_width)
                continue;
            if (Integer.parseInt(m.group("height")) != m_height)
                continue;
            double fps = Double.parseDouble(m.group("fps"));
            if (fps < m_fps)
                continue;
            if (fps > foundFps)
                continue;
            String format = m.group("format");
            boolean isJpeg = format.equals("jpeg") || format.equals("JPEG");
            if ((m_useJpeg && !isJpeg) || (!m_useJpeg && isJpeg))
                continue;
            foundMode = mode;
            foundFps = (int)fps;
        }
        if (foundMode != null) {
            System.out.println("found mode " + foundMode.Value + ": " + foundMode.Name);
            if (foundMode.Value != enumerated.currentMode)
                NIVision.IMAQdxSetAttributeU32(m_id, ATTR_VIDEO_MODE, foundMode.Value);
        }
        
        // White Balance
        if (m_whiteBalance == "auto")
            NIVision.IMAQdxSetAttributeString(m_id, ATTR_WB_MODE, "Auto");
        else {
            NIVision.IMAQdxSetAttributeString(m_id, ATTR_WB_MODE, "Manual");
            if (m_whiteBalanceValue != -1)
                NIVision.IMAQdxSetAttributeI64(m_id, ATTR_WB_VALUE, m_whiteBalanceValue);
        }
        
        // Exposure
        if (m_exposure == "auto")
            NIVision.IMAQdxSetAttributeString(m_id, ATTR_EX_MODE, "AutoAperaturePriority");
        else {
            NIVision.IMAQdxSetAttributeString(m_id, ATTR_EX_MODE, "Manual");
            if (m_exposureValue != -1) {
                long minv = NIVision.IMAQdxGetAttributeMinimumI64(m_id, ATTR_EX_VALUE);
                long maxv = NIVision.IMAQdxGetAttributeMaximumI64(m_id, ATTR_EX_VALUE);
                long val = minv + (long)(((double)(maxv - minv)) * (((double)m_exposureValue) / 100.0));
                NIVision.IMAQdxSetAttributeI64(m_id, ATTR_EX_VALUE, val);
            }
        }
        
        // Brightness
        NIVision.IMAQdxSetAttributeString(m_id, ATTR_BR_MODE, "Manual");
        long minv = NIVision.IMAQdxGetAttributeMinimumI64(m_id, ATTR_BR_VALUE);
        long maxv = NIVision.IMAQdxGetAttributeMaximumI64(m_id, ATTR_BR_VALUE);
        long val = minv + (long)(((double)(maxv - minv)) * (((double)m_brightness) / 100.0));
        NIVision.IMAQdxSetAttributeI64(m_id, ATTR_BR_VALUE, val);
        
        // Restart acquisition
        if (wasActive)
            startCapture();
    }

    @objid ("1a42be77-f782-4d4a-bbb6-921182699c6f")
    public synchronized void setFPS(int fps) {
        if (fps != m_fps) {
            m_needSettingsUpdate = true;
            m_fps = fps;
        }
    }

    @objid ("cdf119b3-2d38-4899-8fb3-80d616333eb6")
    public synchronized void setSize(int width, int height) {
        if (width != m_width || height != m_height) {
            m_needSettingsUpdate = true;
            m_width = width;
            m_height = height;
        }
    }

    /**
     * Set the brightness, as a percentage (0-100).
     */
    @objid ("50f2f694-e073-4de9-b664-71d42ff19ed3")
    public synchronized void setBrightness(int brightness) {
        if (brightness > 100)
            m_brightness = 100;
        else if (brightness < 0)
            m_brightness = 0;
        else
            m_brightness = brightness;
        m_needSettingsUpdate = true;
    }

    /**
     * Get the brightness, as a percentage (0-100).
     */
    @objid ("9138bb74-ea17-4fb4-b924-2efd5425828a")
    public synchronized int getBrightness() {
        return m_brightness;
    }

    /**
     * Set the white balance to auto.
     */
    @objid ("8a64e0ef-7650-4e9a-8f76-b76b17f80fa8")
    public synchronized void setWhiteBalanceAuto() {
        m_whiteBalance = "auto";
        m_whiteBalanceValue = -1;
        m_needSettingsUpdate = true;
    }

    /**
     * Set the white balance to hold current.
     */
    @objid ("f52798c1-9cce-4bae-a34f-6603c836b643")
    public synchronized void setWhiteBalanceHoldCurrent() {
        m_whiteBalance = "manual";
        m_whiteBalanceValue = -1;
        m_needSettingsUpdate = true;
    }

    /**
     * Set the white balance to manual, with specified color temperature.
     */
    @objid ("fad56d4e-71eb-4956-8295-bc69fae97671")
    public synchronized void setWhiteBalanceManual(int value) {
        m_whiteBalance = "manual";
        m_whiteBalanceValue = value;
        m_needSettingsUpdate = true;
    }

    /**
     * Set the exposure to auto aperature.
     */
    @objid ("2fc091f0-47ce-4df8-9d70-411bef710fe6")
    public synchronized void setExposureAuto() {
        m_exposure = "auto";
        m_exposureValue = -1;
        m_needSettingsUpdate = true;
    }

    /**
     * Set the exposure to hold current.
     */
    @objid ("a128dab9-0b02-4839-84ad-2034db4f833e")
    public synchronized void setExposureHoldCurrent() {
        m_exposure = "manual";
        m_exposureValue = -1;
        m_needSettingsUpdate = true;
    }

    /**
     * Set the exposure to manual, as a percentage (0-100).
     */
    @objid ("1c7c2cde-4e9d-4c14-bc64-1672edb63244")
    public synchronized void setExposureManual(int value) {
        m_exposure = "manual";
        if (value > 100)
            m_exposureValue = 100;
        else if (value < 0)
            m_exposureValue = 0;
        else
            m_exposureValue = value;
        m_needSettingsUpdate = true;
    }

    @objid ("2f696ff4-94ff-40da-94c6-3d56a1bb681d")
    public synchronized void getImage(Image image) {
        if (m_needSettingsUpdate || m_useJpeg) {
            m_needSettingsUpdate = false;
            m_useJpeg = false;
            updateSettings();
        }
        
        NIVision.IMAQdxGrab(m_id, image, 1);
    }

    @objid ("330d8c34-e652-4e27-ab91-8b1987e86114")
    public synchronized void getImageData(ByteBuffer data) {
        if (m_needSettingsUpdate || !m_useJpeg) {
            m_needSettingsUpdate = false;
            m_useJpeg = true;
            updateSettings();
        }
        
        NIVision.IMAQdxGetImageData(m_id, data, NIVision.IMAQdxBufferNumberMode.BufferNumberModeLast, 0);
                data.limit(data.capacity() - 1);
        data.limit(getJpegSize(data));
    }

    @objid ("76b900aa-a4fb-440c-942a-525e20bde5c7")
    private static int getJpegSize(ByteBuffer data) {
        if (data.get(0) != (byte) 0xff || data.get(1) != (byte) 0xd8)
            throw new VisionException("invalid image");
        int pos = 2;
        while (true) {
            try {
                byte b = data.get(pos);
                if (b != (byte) 0xff)
                    throw new VisionException("invalid image at pos " + pos + " (" + data.get(pos) + ")");
                b = data.get(pos+1);
                if (b == (byte) 0x01 || (b >= (byte) 0xd0 && b <= (byte) 0xd7)) // various
                    pos += 2;
                else if (b == (byte) 0xd9) // EOI
                    return pos + 2;
                else if (b == (byte) 0xd8) // SOI
                    throw new VisionException("invalid image");
                else if (b == (byte) 0xda) { // SOS
                    int len = ((data.get(pos+2) & 0xff) << 8) | (data.get(pos+3) & 0xff);
                    pos += len + 2;
                    // Find next marker.  Skip over escaped and RST markers.
                    while (data.get(pos) != (byte) 0xff || data.get(pos+1) == (byte) 0x00 || (data.get(pos+1) >= (byte) 0xd0 && data.get(pos+1) <= (byte) 0xd7))
                        pos += 1;
                } else { // various
                    int len = ((data.get(pos+2) & 0xff) << 8) | (data.get(pos+3) & 0xff);
                    pos += len + 2;
                }
            } catch (IndexOutOfBoundsException ex) {
                throw new VisionException("invalid image: could not find jpeg end " + ex.getMessage());
            }
        }
    }

    @objid ("4cd066f7-3f55-4cd7-bbd5-7d33365ad2bc")
    public class WhiteBalance {
        @objid ("14cf56a2-3b18-48b6-a20c-5de5040037dc")
        public static final int kFixedIndoor = 3000;

        @objid ("7e4e2a4e-6a6a-4c90-9fa2-7ed310a368a0")
        public static final int kFixedOutdoor1 = 4000;

        @objid ("6757679e-d884-4168-affc-a76f73179dea")
        public static final int kFixedOutdoor2 = 5000;

        @objid ("c57604f0-2dd8-4669-8b8a-94ad2227aa90")
        public static final int kFixedFluorescent1 = 5100;

        @objid ("bf9e7e6e-8ea0-4b38-846d-e5bbf108d3f4")
        public static final int kFixedFlourescent2 = 5200;

    }

}

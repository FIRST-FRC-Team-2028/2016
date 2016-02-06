/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision;

/**
 * A color image represented in RGB color space at 3 bytes per pixel.
 * @author dtjones
 */
@objid ("a6d0f7df-4f57-4770-a585-c259a1553ba8")
public class RGBImage extends ColorImage {
    /**
     * Create a new 0x0 image.
     */
    @objid ("9439b9ec-22fa-4d71-b69d-98ef54d4e378")
    public RGBImage() throws NIVisionException {
        super(NIVision.ImageType.IMAGE_RGB);
    }

    @objid ("22cb37a4-50c4-492d-b8b8-820728e46ddb")
    RGBImage(RGBImage sourceImage) {
        super(sourceImage);
    }

    /**
     * Create a new image by loading a file.
     * @param fileName The path of the file to load.
     */
    @objid ("0affbb34-dd17-4c0d-8305-748134726e65")
    public RGBImage(String fileName) throws NIVisionException {
        super(NIVision.ImageType.IMAGE_RGB);
        NIVision.imaqReadFile(image, fileName);
    }

}

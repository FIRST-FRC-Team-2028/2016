/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision;

/**
 * A color image represented in HSL color space at 3 bytes per pixel.
 * @author dtjones
 */
@objid ("98a63817-a5bc-4551-809e-efa4db9bb7f4")
public class HSLImage extends ColorImage {
    /**
     * Create a new 0x0 image.
     */
    @objid ("1e050cad-7174-4458-83e1-db733e43d43f")
    public HSLImage() throws NIVisionException {
        super(NIVision.ImageType.IMAGE_HSL);
    }

    @objid ("7a90051c-b9c2-4565-afee-1a65663cc031")
    HSLImage(HSLImage sourceImage) {
        super(sourceImage);
    }

    /**
     * Create a new image by loading a file.
     * @param fileName The path of the file to load.
     */
    @objid ("7626a361-828a-44d5-b890-5aee37f7332c")
    public HSLImage(String fileName) throws NIVisionException {
        super(NIVision.ImageType.IMAGE_HSL);
        NIVision.imaqReadFile(image, fileName);
    }

}

/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision;

/**
 * Class representing a generic image.
 * @author dtjones
 */
@objid ("82cc9d4a-0291-49bc-ac42-b3d0d8b0c4e4")
public abstract class ImageBase {
    @objid ("9e4efddb-c408-4983-847d-8f2b86dbc576")
     static final int DEFAULT_BORDER_SIZE = 3;

    /**
     * Pointer to the image memory
     */
    @objid ("7d1f4bf3-31e0-4e17-bb9c-cf040d8695fd")
    public final Image image;

    @objid ("2a813ceb-b9a9-4da1-b096-d9f1a244cb95")
    ImageBase(com.ni.vision.NIVision.ImageType type) throws NIVisionException {
        image = NIVision.imaqCreateImage(type, DEFAULT_BORDER_SIZE);
    }

    /**
     * Creates a new image pointing to the same data as the source image. The
     * imgae data is not copied, it is just referenced by both objects. Freeing
     * one will free both.
     * @param sourceImage The image to reference
     */
    @objid ("a26f52c2-2b75-4406-b067-beaa0107955a")
    ImageBase(ImageBase sourceImage) {
        image = sourceImage.image;
    }

    /**
     * Write the image to a file.
     * 
     * Supported extensions:
     * .aipd or .apd AIPD
     * .bmp BMP
     * .jpg or .jpeg JPEG
     * .jp2 JPEG2000
     * .png PNG
     * .tif or .tiff TIFF
     * @param fileName The path to write the image to.
     */
    @objid ("de89337e-d36f-4ffe-abe0-bb363af3e986")
    public void write(String fileName) throws NIVisionException {
        NIVision.RGBValue value = new NIVision.RGBValue();
        NIVision.imaqWriteFile(image, fileName, value);
        value.free();
    }

    /**
     * Release the memory associated with an image.
     */
    @objid ("db6de52d-525e-4aad-bc8d-9048b0d61fd1")
    public void free() throws NIVisionException {
        image.free();
    }

    /**
     * Get the height of the image in pixels.
     * @return The height of the image.
     */
    @objid ("f0373cd8-2dc3-4ad2-bca8-99bd3562d1be")
    public int getHeight() throws NIVisionException {
        return NIVision.imaqGetImageSize(image).height;
    }

    /**
     * Get the width of the image in pixels.
     * @return The width of the image.
     */
    @objid ("12ee8710-8aad-4c42-a89f-c949434d57d4")
    public int getWidth() throws NIVisionException {
        return NIVision.imaqGetImageSize(image).width;
    }

}

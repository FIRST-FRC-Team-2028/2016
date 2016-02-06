/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision.CurveOptions;
import com.ni.vision.NIVision.DetectEllipsesResult;
import com.ni.vision.NIVision.EllipseDescriptor;
import com.ni.vision.NIVision.ROI;
import com.ni.vision.NIVision.ShapeDetectionOptions;
import com.ni.vision.NIVision;

/**
 * A grey scale image represented at a byte per pixel.
 * @author dtjones
 */
@objid ("6104874a-eda7-4c14-aa4c-97cbbe4c47ed")
public class MonoImage extends ImageBase {
    /**
     * Create a new 0x0 image.
     */
    @objid ("dff7f584-8e4e-40a3-81da-c4811a816d5f")
    public MonoImage() throws NIVisionException {
        super(NIVision.ImageType.IMAGE_U8);
    }

    @objid ("035d45c8-63f4-4c5d-a1aa-8de2345cfc9e")
    MonoImage(MonoImage sourceImage) {
        super(sourceImage);
    }

    @objid ("7fe0966e-694d-4808-8d99-905ee7014790")
    public DetectEllipsesResult detectEllipses(EllipseDescriptor ellipseDescriptor, CurveOptions curveOptions, ShapeDetectionOptions shapeDetectionOptions, ROI roi) throws NIVisionException {
        return NIVision.imaqDetectEllipses(image, ellipseDescriptor, curveOptions, shapeDetectionOptions, roi);
    }

    @objid ("be8240eb-6206-4b57-a102-1a9da995ebc5")
    public DetectEllipsesResult detectEllipses(EllipseDescriptor ellipseDescriptor) throws NIVisionException {
        return NIVision.imaqDetectEllipses(image, ellipseDescriptor, null, null, null);
    }

}

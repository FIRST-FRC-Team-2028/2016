/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision;

/**
 * Class to store commonly used information about a particle.
 * @author dtjones
 */
@objid ("7f5d7a0f-72ff-4954-817e-47d7929c6232")
public class ParticleAnalysisReport {
    /**
     * The height of the image in pixels.
     */
    @objid ("ed70e75d-32fa-44f0-b990-48a1862f5eac")
    public final int imageHeight;

    /**
     * The width of the image in pixels.
     */
    @objid ("98707091-a137-4a98-a4ed-467a6dcb27d6")
    public final int imageWidth;

    /**
     * X-coordinate of the point representing the average position of the
     * total particle mass, assuming every point in the particle has a constant density
     */
    @objid ("9692afee-43cb-41e6-bdaa-6c3805246bc0")
    public final int center_mass_x; // MeasurementType: IMAQ_MT_CENTER_OF_MASS_X

    /**
     * Y-coordinate of the point representing the average position of the
     * total particle mass, assuming every point in the particle has a constant density
     */
    @objid ("8c61c320-a76d-4680-bca5-de59823ccfc6")
    public final int center_mass_y; // MeasurementType: IMAQ_MT_CENTER_OF_MASS_Y

    /**
     * Center of mass x value normalized to -1.0 to +1.0 range.
     */
    @objid ("03ae3891-80f1-4512-84e5-f37e9dfc5b25")
    public final double center_mass_x_normalized;

    /**
     * Center of mass y value normalized to -1.0 to +1.0 range.
     */
    @objid ("e307a007-2a04-47c5-8382-8767363a61c8")
    public final double center_mass_y_normalized;

    /**
     * Area of the particle
     */
    @objid ("ebda600e-dde9-4851-b61d-ded199226879")
    public final double particleArea; // MeasurementType: IMAQ_MT_AREA

    /**
     * Bounding Rectangle
     */
    @objid ("40004c77-2356-4122-9e5b-258aa8ef7434")
    public final int boundingRectLeft; // left/top/width/height

    /**
     * Bounding Rectangle
     */
    @objid ("fc01ad05-82a7-4894-b225-cf5ef1ea796b")
    public final int boundingRectTop;

    /**
     * Bounding Rectangle
     */
    @objid ("ae017010-1c60-4cd7-9733-3507050f064f")
    public final int boundingRectWidth;

    /**
     * Bounding Rectangle
     */
    @objid ("7ad7d61e-bafc-4a09-b25d-02757080ae83")
    public final int boundingRectHeight;

    /**
     * Percentage of the particle Area covering the Image Area.
     */
    @objid ("0eff91ad-d41c-42b9-a454-c969830592a2")
    public final double particleToImagePercent; // MeasurementType: IMAQ_MT_AREA_BY_IMAGE_AREA

    /**
     * Percentage of the particle Area in relation to its Particle and Holes Area
     */
    @objid ("9833c58e-dc0e-4902-996f-270a220c408d")
    public final double particleQuality; // MeasurementType: IMAQ_MT_AREA_BY_PARTICLE_AND_HOLES_AREA

    @objid ("b1575537-c674-4ec7-a474-a753862e2243")
    ParticleAnalysisReport(BinaryImage image, int index) throws NIVisionException {
        imageHeight = image.getHeight();
        imageWidth = image.getWidth();
        center_mass_x = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
        center_mass_y = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
        center_mass_x_normalized = (2.0 * center_mass_x / imageWidth) - 1.0;
        center_mass_y_normalized = (2.0 * center_mass_y / imageHeight) - 1.0;
        particleArea = NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_AREA);
        boundingRectLeft = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
        boundingRectTop = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
        boundingRectWidth = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
        boundingRectHeight = (int) NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
        particleToImagePercent = NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
        particleQuality = NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_AREA_BY_PARTICLE_AND_HOLES_AREA);
    }

    @objid ("9da04b53-89f6-4d64-8ed9-aafe7dbbaa00")
    static double getParticleToImagePercent(BinaryImage image, int index) throws NIVisionException {
        return NIVision.imaqMeasureParticle(image.image, index, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
    }

    /**
     * Get string representation of the particle analysis report.
     * @return A string representation of the particle analysis report.
     */
    @objid ("3a1825e4-a239-4125-9c61-06932d5e8816")
    public String toString() {
        return "Particle Report: \n" +
                       "    Image Height    : " + imageHeight + "\n" +
                       "    Image Width     : " + imageWidth + "\n" +
                       "    Center of mass  : ( " + center_mass_x + " , " + center_mass_y + " )\n" +
                       "      normalized    : ( " + center_mass_x_normalized + " , " + center_mass_y_normalized + " )\n" +
                       "    Area            : " + particleArea + "\n" +
                       "      percent       : " + particleToImagePercent + "\n" +
                       "    Bounding Rect   : ( " + boundingRectLeft + " , " + boundingRectTop + " ) " + boundingRectWidth + "*" + boundingRectHeight + "\n" +
                       "    Quality         : " + particleQuality + "\n";
    }

}

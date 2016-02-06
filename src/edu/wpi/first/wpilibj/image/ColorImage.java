/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision;

/**
 * A class representing a color image.
 * 
 * @author dtjones
 */
@objid ("5c7f82ed-6972-4f3e-a911-27c1c95ba389")
public abstract class ColorImage extends ImageBase {
    @objid ("60357b02-fcc3-47f5-8e34-b219ef6d1093")
    ColorImage(com.ni.vision.NIVision.ImageType type) throws NIVisionException {
        super(type);
    }

    @objid ("a5047c5a-1ec5-499a-9df7-ebf504b48412")
    ColorImage(ColorImage sourceImage) {
        super(sourceImage);
    }

    @objid ("b7d10029-eade-4393-a027-9ed3ed8aab0c")
    private BinaryImage threshold(com.ni.vision.NIVision.ColorMode colorMode, int low1, int high1, int low2, int high2, int low3, int high3) throws NIVisionException {
        BinaryImage res = new BinaryImage();
        NIVision.Range range1 = new NIVision.Range(low1, high1);
        NIVision.Range range2 = new NIVision.Range(low2, high2);
        NIVision.Range range3 = new NIVision.Range(low3, high3);
        NIVision.imaqColorThreshold(res.image, image, 1, colorMode, range1, range2, range3);
        res.free();
        range1.free();
        range2.free();
        range3.free();
        return res;
    }

    /**
     * Return a mask of the areas of the image that fall within the given ranges for color values
     * @param redLow The lower red limit.
     * @param redHigh The upper red limit.
     * @param greenLow The lower green limit.
     * @param greenHigh The upper green limit.
     * @param blueLow The lower blue limit.
     * @param blueHigh The upper blue limit.
     * @return A BinaryImage masking the areas which match the given thresholds.
     */
    @objid ("cd3fbc2a-7ca2-4fe1-a423-32f67c5ab9fa")
    public BinaryImage thresholdRGB(int redLow, int redHigh, int greenLow, int greenHigh, int blueLow, int blueHigh) throws NIVisionException {
        return threshold(NIVision.ColorMode.RGB, redLow, redHigh, greenLow, greenHigh, blueLow, blueHigh);
    }

    /**
     * Return a mask of the areas of the image that fall within the given ranges for color values
     * @param hueLow The lower hue limit.
     * @param hueHigh The upper hue limit.
     * @param saturationLow The lower saturation limit.
     * @param saturationHigh The upper saturation limit.
     * @param luminenceLow The lower luminence limit.
     * @param luminenceHigh The upper luminence limit.
     * @return A BinaryImage masking the areas which match the given thresholds.
     */
    @objid ("ecfa71b0-acd4-4830-8568-ae4cc90f8c28")
    public BinaryImage thresholdHSL(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int luminenceLow, int luminenceHigh) throws NIVisionException {
        return threshold(NIVision.ColorMode.HSL, hueLow, hueHigh, saturationLow, saturationHigh, luminenceLow, luminenceHigh);
    }

    /**
     * Return a mask of the areas of the image that fall within the given ranges for color values
     * @param hueLow The lower hue limit.
     * @param hueHigh The upper hue limit.
     * @param saturationLow The lower saturation limit.
     * @param saturationHigh The upper saturation limit.
     * @param valueLow The upper value limit.
     * @param valueHigh The lower value limit.
     * @return A BinaryImage masking the areas which match the given thresholds.
     */
    @objid ("3fa57435-1171-413e-8ce9-9ba55f351aec")
    public BinaryImage thresholdHSV(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int valueLow, int valueHigh) throws NIVisionException {
        return threshold(NIVision.ColorMode.HSV, hueLow, hueHigh, saturationLow, saturationHigh, valueLow, valueHigh);
    }

    /**
     * Return a mask of the areas of the image that fall within the given ranges for color values
     * @param hueLow The lower hue limit.
     * @param hueHigh The upper hue limit.
     * @param saturationLow The lower saturation limit.
     * @param saturationHigh The upper saturation limit.
     * @param intansityLow The lower intensity limit.
     * @param intensityHigh The upper intensity limit.
     * @return A BinaryImage masking the areas which match the given thresholds.
     */
    @objid ("372ca99f-2aa7-4ce3-867b-637006e04908")
    public BinaryImage thresholdHSI(int hueLow, int hueHigh, int saturationLow, int saturationHigh, int intansityLow, int intensityHigh) throws NIVisionException {
        return threshold(NIVision.ColorMode.HSI, hueLow, hueHigh, saturationLow, saturationHigh, intansityLow, intensityHigh);
    }

    @objid ("ad76cd0b-097a-4c0c-ac5e-e6b89ba244c0")
    MonoImage extractFirstColorPlane(com.ni.vision.NIVision.ColorMode mode) throws NIVisionException {
        MonoImage result = new MonoImage();
        NIVision.imaqExtractColorPlanes(image, mode, result.image, null, null);
        result.free();
        return result;
    }

    @objid ("d12a263b-a135-4d5c-b887-4575b0a5b2c7")
    MonoImage extractSecondColorPlane(com.ni.vision.NIVision.ColorMode mode) throws NIVisionException {
        MonoImage result = new MonoImage();
        NIVision.imaqExtractColorPlanes(image, mode, null, result.image, null);
        result.free();
        return result;
    }

    @objid ("c1db5b73-3218-4b18-9300-c1dec6ac7da0")
    MonoImage extractThirdColorPlane(com.ni.vision.NIVision.ColorMode mode) throws NIVisionException {
        MonoImage result = new MonoImage();
        NIVision.imaqExtractColorPlanes(image, mode, null, null, result.image);
        result.free();
        return result;
    }

    /**
     * Get the red color plane from the image when represented in RGB color space.
     * @return The red color plane from the image.
     */
    @objid ("77afc96a-a6b3-487b-afa9-8d4eacb4694c")
    public MonoImage getRedPlane() throws NIVisionException {
        return extractFirstColorPlane(NIVision.ColorMode.RGB);
    }

    /**
     * Get the green color plane from the image when represented in RGB color space.
     * @return The green color plane from the image.
     */
    @objid ("aa52f970-052a-42ad-bc57-63bda84896de")
    public MonoImage getGreenPlane() throws NIVisionException {
        return extractSecondColorPlane(NIVision.ColorMode.RGB);
    }

    /**
     * Get the blue color plane from the image when represented in RGB color space.
     * @return The blue color plane from the image.
     */
    @objid ("8246a944-3f08-4790-b874-d7da2f52f624")
    public MonoImage getBluePlane() throws NIVisionException {
        return extractThirdColorPlane(NIVision.ColorMode.RGB);
    }

    /**
     * Get the hue color plane from the image when represented in HSL color space.
     * @return The hue color plane from the image.
     */
    @objid ("ec7e9ac8-6552-4aaf-add9-672880e4f67b")
    public MonoImage getHSLHuePlane() throws NIVisionException {
        return extractFirstColorPlane(NIVision.ColorMode.HSL);
    }

    /**
     * Get the hue color plane from the image when represented in HSV color space.
     * @return The hue color plane from the image.
     */
    @objid ("cd13971e-105b-40f0-98fd-5b6701038be4")
    public MonoImage getHSVHuePlane() throws NIVisionException {
        return extractFirstColorPlane(NIVision.ColorMode.HSV);
    }

    /**
     * Get the hue color plane from the image when represented in HSI color space.
     * @return The hue color plane from the image.
     */
    @objid ("938b424f-f83c-443b-ad6f-54ff2f17f22c")
    public MonoImage getHSIHuePlane() throws NIVisionException {
        return extractFirstColorPlane(NIVision.ColorMode.HSI);
    }

    /**
     * Get the saturation color plane from the image when represented in HSL color space.
     * @return The saturation color plane from the image.
     */
    @objid ("cd195614-0b04-4774-a1c8-206dab460b14")
    public MonoImage getHSLSaturationPlane() throws NIVisionException {
        return extractSecondColorPlane(NIVision.ColorMode.HSL);
    }

    /**
     * Get the saturation color plane from the image when represented in HSV color space.
     * @return The saturation color plane from the image.
     */
    @objid ("a9d59cfd-67c5-4747-9b5c-b7b5080152c2")
    public MonoImage getHSVSaturationPlane() throws NIVisionException {
        return extractSecondColorPlane(NIVision.ColorMode.HSV);
    }

    /**
     * Get the saturation color plane from the image when represented in HSI color space.
     * @return The saturation color plane from the image.
     */
    @objid ("a1ed689d-46f2-4bce-a71f-9446e830e775")
    public MonoImage getHSISaturationPlane() throws NIVisionException {
        return extractSecondColorPlane(NIVision.ColorMode.HSI);
    }

    /**
     * Get the luminance color plane from the image when represented in HSL color space.
     * @return The luminance color plane from the image.
     */
    @objid ("19ac7a04-7935-45c5-b8a3-689feda68aaf")
    public MonoImage getLuminancePlane() throws NIVisionException {
        return extractThirdColorPlane(NIVision.ColorMode.HSL);
    }

    /**
     * Get the value color plane from the image when represented in HSV color space.
     * @return The value color plane from the image.
     */
    @objid ("02149aaa-3687-4155-9453-913227fabf60")
    public MonoImage getValuePlane() throws NIVisionException {
        return extractThirdColorPlane(NIVision.ColorMode.HSV);
    }

    /**
     * Get the intensity color plane from the image when represented in HSI color space.
     * @return The intensity color plane from the image.
     */
    @objid ("5e9e178e-e3ef-447a-b65a-6d1804682503")
    public MonoImage getIntensityPlane() throws NIVisionException {
        return extractThirdColorPlane(NIVision.ColorMode.HSI);
    }

    @objid ("b983b21c-ed3c-4b06-8f86-55aa71e4dbc9")
    ColorImage replaceFirstColorPlane(com.ni.vision.NIVision.ColorMode mode, MonoImage plane) throws NIVisionException {
        NIVision.imaqReplaceColorPlanes(image, image, mode, plane.image, null, null);
        return this;
    }

    @objid ("a0b0e5a6-744f-48ad-9657-bf343c6cfa51")
    ColorImage replaceSecondColorPlane(com.ni.vision.NIVision.ColorMode mode, MonoImage plane) throws NIVisionException {
        NIVision.imaqReplaceColorPlanes(image, image, mode, null, plane.image, null);
        return this;
    }

    @objid ("487b6b73-2392-4076-b5d8-9f1d15504d10")
    ColorImage replaceThirdColorPlane(com.ni.vision.NIVision.ColorMode mode, MonoImage plane) throws NIVisionException {
        NIVision.imaqReplaceColorPlanes(image, image, mode, null, null, plane.image);
        return this;
    }

    /**
     * Set the red color plane from the image when represented in RGB color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("289c01c3-5aab-4595-b86a-272752acbffc")
    public ColorImage replaceRedPlane(MonoImage plane) throws NIVisionException {
        return replaceFirstColorPlane(NIVision.ColorMode.RGB, plane);
    }

    /**
     * Set the green color plane from the image when represented in RGB color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("89ad75e1-b974-470f-98f5-3fcc9b986167")
    public ColorImage replaceGreenPlane(MonoImage plane) throws NIVisionException {
        return replaceSecondColorPlane(NIVision.ColorMode.RGB, plane);
    }

    /**
     * Set the blue color plane from the image when represented in RGB color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("cb4456f1-80ab-4f88-b2c5-3d659eeac640")
    public ColorImage replaceBluePlane(MonoImage plane) throws NIVisionException {
        return replaceThirdColorPlane(NIVision.ColorMode.RGB, plane);
    }

    /**
     * Set the hue color plane from the image when represented in HSL color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("ba616231-8b2a-489c-8b70-38d16d4e6277")
    public ColorImage replaceHSLHuePlane(MonoImage plane) throws NIVisionException {
        return replaceFirstColorPlane(NIVision.ColorMode.HSL, plane);
    }

    /**
     * Set the hue color plane from the image when represented in HSV color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("0ceec183-6eec-4403-a46e-64582d53dfa8")
    public ColorImage replaceHSVHuePlane(MonoImage plane) throws NIVisionException {
        return replaceFirstColorPlane(NIVision.ColorMode.HSV, plane);
    }

    /**
     * Set the hue color plane from the image when represented in HSI color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("a5b36c6c-1ca6-4591-844a-0fa01d74a358")
    public ColorImage replaceHSIHuePlane(MonoImage plane) throws NIVisionException {
        return replaceFirstColorPlane(NIVision.ColorMode.HSI, plane);
    }

    /**
     * Set the saturation color plane from the image when represented in HSL color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("1144b534-44dc-4393-a7d5-df4e71c7b06d")
    public ColorImage replaceHSLSaturationPlane(MonoImage plane) throws NIVisionException {
        return replaceSecondColorPlane(NIVision.ColorMode.HSL, plane);
    }

    /**
     * Set the saturation color plane from the image when represented in HSV color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("8d7f3dcf-db3c-413c-b927-ce9908b516a7")
    public ColorImage replaceHSVSaturationPlane(MonoImage plane) throws NIVisionException {
        return replaceSecondColorPlane(NIVision.ColorMode.HSV, plane);
    }

    /**
     * Set the saturation color plane from the image when represented in HSI color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("9ea9f625-3ceb-48e6-98f0-cc11e4e63efe")
    public ColorImage replaceHSISaturationPlane(MonoImage plane) throws NIVisionException {
        return replaceSecondColorPlane(NIVision.ColorMode.HSI, plane);
    }

    /**
     * Set the luminance color plane from the image when represented in HSL color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("c582119b-5759-4672-a707-ecea76f591f3")
    public ColorImage replaceLuminancePlane(MonoImage plane) throws NIVisionException {
        return replaceThirdColorPlane(NIVision.ColorMode.HSL, plane);
    }

    /**
     * Set the value color plane from the image when represented in HSV color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("d52b8103-99b5-4b04-b5c3-0192105fb130")
    public ColorImage replaceValuePlane(MonoImage plane) throws NIVisionException {
        return replaceThirdColorPlane(NIVision.ColorMode.HSV, plane);
    }

    /**
     * Set the intensity color plane from the image when represented in HSI color space.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @param plane The MonoImage representing the new color plane.
     * @return The resulting image.
     */
    @objid ("d1b71e8b-34bc-4485-b245-4ee4cc523d9a")
    public ColorImage replaceIntensityPlane(MonoImage plane) throws NIVisionException {
        return replaceThirdColorPlane(NIVision.ColorMode.HSI, plane);
    }

    /**
     * Calculates the histogram of each plane of a color image and redistributes
     * pixel values across the desired range while maintaining pixel value
     * groupings.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @return The modified image.
     */
    @objid ("6d1a7916-9539-4c7c-9439-b36b6505906d")
    public ColorImage colorEqualize() throws NIVisionException {
        NIVision.imaqColorEqualize(image, image, 1);
        return this;
    }

    /**
     * Calculates the histogram of each plane of a color image and redistributes
     * pixel values across the desired range while maintaining pixel value
     * groupings for the Luminance plane only.
     * This does not create a new image, but modifies this one instead. Create a
     * copy before hand if you need to continue using the original.
     * @return The modified image.
     */
    @objid ("6b560e6b-863a-43ad-9ef3-c78bd8d4a74d")
    public ColorImage luminanceEqualize() throws NIVisionException {
        NIVision.imaqColorEqualize(image, image, 0);
        return this;
    }

}

/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.image;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.util.SortedVector;

/**
 * An image where each pixel is treated as either on or off.
 * 
 * @author dtjones
 */
@objid ("c33db46e-7967-4249-ae33-d646f399ca4b")
public class BinaryImage extends MonoImage {
    @objid ("ec115248-ef2f-4565-a8d5-c138aa99ae24")
    private int numParticles = -1;

    @objid ("11415bc1-4b63-4204-8098-e387430fc992")
    BinaryImage() throws NIVisionException {
    }

    @objid ("6caec8f7-a4c1-4976-82b6-845cd8721629")
    BinaryImage(BinaryImage sourceImage) {
        super(sourceImage);
    }

    /**
     * Returns the number of particles.
     * @return The number of particles
     */
    @objid ("56b64fb5-6e47-4622-b7ce-c36cad5bc305")
    public int getNumberParticles() throws NIVisionException {
        if (numParticles < 0)
            numParticles = NIVision.imaqCountParticles(image, 1);
        return numParticles;
    }

    /**
     * Get a particle analysis report for the particle at the given index.
     * @param index The index of the particle to report on.
     * @return The ParticleAnalysisReport for the particle at the given index
     */
    @objid ("ed8c7252-b49f-4018-a781-50ec773fce2f")
    public ParticleAnalysisReport getParticleAnalysisReport(int index) throws NIVisionException {
        if (!(index < getNumberParticles())) throw new IndexOutOfBoundsException();
        return new ParticleAnalysisReport(this, index);
    }

    /**
     * Gets all the particle analysis reports ordered from largest area to smallest.
     * @param size The number of particles to return
     * @return An array of ParticleReports from largest area to smallest
     */
    @objid ("7c9ad3ce-fa3b-40a3-b545-1e9dc9fc4826")
    public ParticleAnalysisReport[] getOrderedParticleAnalysisReports(int size) throws NIVisionException {
        if (size > getNumberParticles())
            size = getNumberParticles();
        ParticleSizeReport[] reports = new ParticleSizeReport[size];
        SortedVector sorter = new SortedVector(new SortedVector.Comparator() {
            public int compare(Object object1, Object object2) {
                ParticleSizeReport p1 = (ParticleSizeReport) object1;
                ParticleSizeReport p2 = (ParticleSizeReport) object2;
                if (p1.size < p2.size)
                    return -1;
                else if (p1.size > p2.size)
                    return 1;
                return 0;
            }
        });
        for (int i = 0; i < getNumberParticles(); i++)
            sorter.addElement(new ParticleSizeReport(i));
        sorter.setSize(size);
        sorter.copyInto(reports);
        ParticleAnalysisReport[] finalReports = new ParticleAnalysisReport[reports.length];
        for (int i = 0; i < finalReports.length; i++)
            finalReports[i] = reports[i].getParticleAnalysisReport();
        return finalReports;
    }

    /**
     * Gets all the particle analysis reports ordered from largest area to smallest.
     * @return An array of ParticleReports from largest are to smallest
     */
    @objid ("2d11bdae-dffc-4c30-9c19-304db4001cd2")
    public ParticleAnalysisReport[] getOrderedParticleAnalysisReports() throws NIVisionException {
        return getOrderedParticleAnalysisReports(getNumberParticles());
    }

    @objid ("b9f3cf7e-32b6-451f-adbd-121cfc5780c6")
    public void write(String fileName) throws NIVisionException {
        NIVision.RGBValue colorTable = new NIVision.RGBValue(0, 0, 255, 0);
        try {
            NIVision.imaqWriteFile(image, fileName, colorTable);
        } finally {
            colorTable.free();
        }
    }

    /**
     * removeSmallObjects filters particles based on their size.
     * The algorithm erodes the image a specified number of times and keeps the
     * particles from the original image that remain in the eroded image.
     * @throws NIVisionException
     * @param connectivity8 true to use connectivity-8 or false for connectivity-4 to determine
     * whether particles are touching. For more information about connectivity, see Chapter 9,
     * Binary Morphology, in the NI Vision Concepts manual.
     * @param erosions the number of erosions to perform
     * @return a BinaryImage after applying the filter
     */
    @objid ("c41df05d-ea9e-41f6-a7ed-08d1a9ee506b")
    public BinaryImage removeSmallObjects(boolean connectivity8, int erosions) throws NIVisionException {
        BinaryImage result = new BinaryImage();
        NIVision.imaqSizeFilter(result.image, image, connectivity8 ? 1 : 0, erosions, NIVision.SizeType.KEEP_LARGE, null);
        result.free();
        return result;
    }

    /**
     * removeLargeObjects filters particles based on their size.
     * The algorithm erodes the image a specified number of times and discards the
     * particles from the original image that remain in the eroded image.
     * @throws NIVisionException
     * @param connectivity8 true to use connectivity-8 or false for connectivity-4 to determine
     * whether particles are touching. For more information about connectivity, see Chapter 9,
     * Binary Morphology, in the NI Vision Concepts manual.
     * @param erosions the number of erosions to perform
     * @return a BinaryImage after applying the filter
     */
    @objid ("4718429b-d98c-46a0-b7e5-891e8bf2fa3a")
    public BinaryImage removeLargeObjects(boolean connectivity8, int erosions) throws NIVisionException {
        BinaryImage result = new BinaryImage();
        NIVision.imaqSizeFilter(result.image, image, connectivity8 ? 1 : 0, erosions, NIVision.SizeType.KEEP_SMALL, null);
        return result;
    }

    @objid ("cf5dc877-12be-4733-a906-f65a1a128b7f")
    public BinaryImage convexHull(boolean connectivity8) throws NIVisionException {
        BinaryImage result = new BinaryImage();
        NIVision.imaqConvexHull(result.image, image, connectivity8 ? 1 : 0);
        return result;
    }

    @objid ("7aeaabf7-a1db-4209-9afd-9ff019bc57c6")
    public BinaryImage particleFilter(com.ni.vision.NIVision.ParticleFilterCriteria2[] criteria) throws NIVisionException {
        BinaryImage result = new BinaryImage();
        NIVision.ParticleFilterOptions2 options = new NIVision.ParticleFilterOptions2(0, 0, 0, 1);
        NIVision.imaqParticleFilter4(result.image, image, criteria, options, null);
        options.free();
        return result;
    }

    @objid ("5c2ec7d0-702d-4c97-aa9e-f0fa7d1185fc")
    private class ParticleSizeReport {
        @objid ("8377b1fc-d856-4854-87a4-e49347303952")
         final int index;

        @objid ("2b9a5a2c-844c-4d3b-bfa9-a23a9452dfdf")
         final double size;

        @objid ("285d101b-5433-442b-933c-43eae792670c")
        public ParticleSizeReport(int index) throws NIVisionException {
            if ((!(index < BinaryImage.this.getNumberParticles())) || index < 0)
                throw new IndexOutOfBoundsException();
            this.index = index;
            size = ParticleAnalysisReport.getParticleToImagePercent(BinaryImage.this, index);
        }

        @objid ("0fcdd07b-e7ff-48b2-b8a0-69834d4d0b73")
        public ParticleAnalysisReport getParticleAnalysisReport() throws NIVisionException {
            return new ParticleAnalysisReport(BinaryImage.this, index);
        }

    }

}

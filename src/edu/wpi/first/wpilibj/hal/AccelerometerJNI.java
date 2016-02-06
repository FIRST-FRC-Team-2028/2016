package edu.wpi.first.wpilibj.hal;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("d7000afd-cdf4-469c-bed3-608770ba704d")
public class AccelerometerJNI extends JNIWrapper {
    @objid ("d220d460-ce30-4faa-ac8b-54390edf014d")
    public static native void setAccelerometerActive(boolean active);

    @objid ("c5ff2849-cc5b-4cf3-908d-cc0c499ae528")
    public static native void setAccelerometerRange(int range);

    @objid ("2df993eb-449b-4e75-99b3-2d93c2ff757b")
    public static native double getAccelerometerX();

    @objid ("3d8ae39a-d92c-4db4-907f-7c577d42365e")
    public static native double getAccelerometerY();

    @objid ("8e7a59d5-45b4-48cf-aa33-6eadcd819e53")
    public static native double getAccelerometerZ();

}

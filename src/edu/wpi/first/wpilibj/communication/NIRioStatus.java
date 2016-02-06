package edu.wpi.first.wpilibj.communication;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("be426876-fd0f-4856-8b42-2a71b1f0e711")
public class NIRioStatus {
// TODO: Should this file be auto-generated?
    @objid ("99cc32be-c4ff-4da9-91b0-c21ea1d5458f")
    public static final int kRioStatusOffset = -63000;

    @objid ("d6b7d09d-2467-4366-a6b9-529d5c39d244")
    public static final int kRioStatusSuccess = 0;

    @objid ("52445c23-b16d-4fe6-b267-054b667d3d9b")
    public static final int kRIOStatusBufferInvalidSize = kRioStatusOffset - 80;

    @objid ("02dbaa53-7dd6-4dd1-bb87-8b8ae3d61f15")
    public static final int kRIOStatusOperationTimedOut = -52007;

    @objid ("f45431a8-ec85-4221-88ba-bb3d8ad91b2b")
    public static final int kRIOStatusFeatureNotSupported = kRioStatusOffset - 193;

    @objid ("d17dd708-49f8-4a2e-a6e0-87103411f040")
    public static final int kRIOStatusResourceNotInitialized = -52010;

}

/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author brad
 */
@objid ("3c4953a0-adc1-4cb5-b651-c81b45685a8e")
public interface MotorSafety {
    @objid ("1906b9cc-3fb8-4a04-ba97-5f14c844f259")
    public static final double DEFAULT_SAFETY_EXPIRATION = 0.1;

    @objid ("7319f461-93ed-4585-8872-3a86988dc762")
    void setExpiration(double timeout);

    @objid ("2b534066-f092-4ca0-9bee-c75c077546b1")
    double getExpiration();

    @objid ("cbf228ad-3be0-4fb3-a23a-77119c361a17")
    boolean isAlive();

    @objid ("2cb56c15-225e-4e97-a59a-20cd0f463029")
    void stopMotor();

    @objid ("b0da8902-4bcd-45b1-bae5-4538fc9dc61e")
    void setSafetyEnabled(boolean enabled);

    @objid ("b86778d7-fe0a-4691-96e7-2286754ebfd0")
    boolean isSafetyEnabled();

    @objid ("b8533488-494c-4bd0-acac-28e369049110")
    String getDescription();

}

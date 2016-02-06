/*----------------------------------------------------------------------------*/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.command;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * A {@link StartCommand} will call the {@link Command#start() start()} method of another command when it is initialized
 * and will finish immediately.
 * 
 * @author Joe Grinstead
 */
@objid ("0b14f173-d4e3-4cfe-b295-f817d484fb52")
public class StartCommand extends Command {
    /**
     * The command to fork
     */
    @objid ("c30f5ac2-3a5e-463f-996d-970c613e3d31")
    private Command m_commandToFork;

    /**
     * Instantiates a {@link StartCommand} which will start the
     * given command whenever its {@link Command#initialize() initialize()} is called.
     * @param commandToStart the {@link Command} to start
     */
    @objid ("7e96447e-78d8-4601-bada-b58d34d3c956")
    public StartCommand(Command commandToStart) {
        super("Start(" + commandToStart + ")");
        m_commandToFork = commandToStart;
    }

    @objid ("c7bb3651-ceed-443d-87ee-15f3aaea2490")
    protected void initialize() {
        m_commandToFork.start();
    }

    @objid ("e00e352d-2896-4c81-a1ee-9f4045a5399e")
    protected void execute() {
    }

    @objid ("92a46b1f-3508-48db-be14-500340e4fde2")
    protected boolean isFinished() {
        return true;
    }

    @objid ("37f0f1f6-fa2d-4b9b-b056-5455f149eea3")
    protected void end() {
    }

    @objid ("4fc7ee90-294a-4c07-82db-a402605fec91")
    protected void interrupted() {
    }

}

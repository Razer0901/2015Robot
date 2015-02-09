package org.team5735.robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TestCommand extends Command {

    public TestCommand() {
    	
    	System.out.println("$$$$$$$$$$$Constructor$$$$$$$$$$$");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("$$$$$$$$$$$$$$Init$$$$$$$$$$$$$$$$$$$");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.println("###################It Works!###########");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

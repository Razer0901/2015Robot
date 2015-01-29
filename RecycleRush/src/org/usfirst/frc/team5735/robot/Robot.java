
package org.usfirst.frc.team5735.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
    
	Joystick xboxController;
	
	
    public void robotInit() {
    	xboxController = new Joystick(0);
    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
        
    }
    
    public void testPeriodic() {
    
    }
    
}

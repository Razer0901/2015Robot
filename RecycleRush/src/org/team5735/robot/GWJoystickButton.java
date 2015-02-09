package org.team5735.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.Trigger.ButtonScheduler;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class GWJoystickButton extends JoystickButton {

	public GWJoystickButton(GenericHID joystick, int buttonNumber) {
		super(joystick, buttonNumber);
		// TODO Auto-generated constructor stub
	}
	@Override
    public void whenPressed(final Command command) {
        new ButtonScheduler() {

 
            public void execute() {
                        command.start();
            }

            protected void start() {
            	if(get()){
            		System.out.println("!!!!!!runstuff!!!!!!!!!!!");

            		Scheduler.getInstance().addButton(this);
            	}
            }
        } .start();
    }
}

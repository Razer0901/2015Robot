package org.team5735.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Robot extends IterativeRobot {

	RobotDrive driveTrain;
	Joystick driveStick;
	VictorSP liftMotor;
	CameraServer server;
	Encoder driveTrainRightEncoder;
	Encoder driveTrainLeftEncoder;
	
	int driveTrainLeftMotorPort = 0;
	int driveTrainRightMotorPort = 1;
	int driveStickPort = 0;
	int liftMotorPort = 2;
	
	int joystickMoveAxis = 5;
	int joystickRotateAxis = 0;
	int joystickTurboAxis = 3;
	int joystickLiftAxis = 2;
	int joystickLiftUp = 5;
	int joystickLiftDown = 6;
	
	double driveTrainMoveScaling = 0.4;
	double driveTrainRotateScaling = 0.4;
	
	
    public void arcadeDrive(GenericHID joystick, final int moveAxis, final double moveScaling, final int rotateAxis, final double rotateScaling, final int turboAxis, boolean squaredInputs){
    	double moveValue = joystick.getRawAxis(moveAxis)*(joystick.getRawAxis(turboAxis)+1)*moveScaling;
    	double rotateValue = joystick.getRawAxis(rotateAxis)*rotateScaling;
    	driveTrain.arcadeDrive(moveValue, rotateValue, squaredInputs);
    }
	
    public void robotInit() {
    	
    	server = CameraServer.getInstance();
        server.setQuality(50);
        server.startAutomaticCapture("cam0");
    	
    	driveTrain = new RobotDrive(driveTrainLeftMotorPort,driveTrainRightMotorPort);
    	driveStick = new Joystick(driveStickPort);
    	liftMotor = new VictorSP(liftMotorPort);
    	
    	driveTrainRightEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	driveTrainRightEncoder.reset();
    	driveTrainLeftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	driveTrainLeftEncoder.reset();

    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
        while (isOperatorControl() && isEnabled()){
        	
        	if(driveStick.getRawButton(joystickLiftUp)){
        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis));
        	}else if(driveStick.getRawButton(joystickLiftDown)){
        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*-1);
        	}else{
        		liftMotor.set(0);
        	}
        	
        	arcadeDrive(driveStick,joystickMoveAxis,driveTrainMoveScaling,joystickRotateAxis,driveTrainRotateScaling,joystickTurboAxis,true);
        	Timer.delay(0.01);
        }
    }
    

    public void testPeriodic() {
    
    }
    
}
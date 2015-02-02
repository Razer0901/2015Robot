package org.team5735.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Robot extends IterativeRobot {
	
	int loopCount = 0 ;
	
	RobotDrive driveTrain;
	Joystick driveStick;
	VictorSP liftMotor;
	CameraServer server;
	Encoder driveTrainRightEncoder;
	Encoder driveTrainLeftEncoder;
	Button testButton;
	StatusOut statusOut;
	
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
	int joystickTestButton = 1;
	
	double driveTrainMoveScaling = 0.5;
	double driveTrainRotateScaling = 0.4;
	
	
    public void arcadeDrive(GenericHID joystick, final int moveAxis, final double moveScaling, final int rotateAxis, final double rotateScaling, final int turboAxis, boolean squaredInputs){
    	double moveValue = joystick.getRawAxis(moveAxis)*(joystick.getRawAxis(turboAxis)+1)*moveScaling;
    	double rotateValue = joystick.getRawAxis(rotateAxis)*rotateScaling;
    	driveTrain.arcadeDrive(moveValue, rotateValue, squaredInputs);
    }
	
    public void robotInit() {
    	
//    	server = CameraServer.getInstance();
//        server.setQuality(50);
//        server.startAutomaticCapture("cam0");
    	
    	driveTrain = new RobotDrive(driveTrainLeftMotorPort,driveTrainRightMotorPort);
    	driveStick = new Joystick(driveStickPort);
    	liftMotor = new VictorSP(liftMotorPort);
    	testButton = new JoystickButton(driveStick,joystickTestButton);
    	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
    	statusOut = new StatusOut(this);
    	statusOut.start();
    	System.out.println("&&&&&&&&&&&&&&&&&&&");

    	driveTrainRightEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	driveTrainRightEncoder.reset();
    	driveTrainRightEncoder.setMaxPeriod(.1);
    	driveTrainRightEncoder.setMinRate(10);
    	driveTrainRightEncoder.setDistancePerPulse((6*Math.PI)/2048);
    	driveTrainRightEncoder.setReverseDirection(true);
    	driveTrainRightEncoder.setSamplesToAverage(7);
    	
    	driveTrainLeftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
    	driveTrainLeftEncoder.reset();
    	driveTrainLeftEncoder.setMaxPeriod(.1);
    	driveTrainLeftEncoder.setMinRate(10);
    	driveTrainLeftEncoder.setDistancePerPulse((6*Math.PI)/2048);
    	driveTrainLeftEncoder.setReverseDirection(false);
    	driveTrainLeftEncoder.setSamplesToAverage(7);

    }

    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
    	
        while (isOperatorControl() && isEnabled()){
//        	loopCount ++;
//        	if (loopCount > 200){
//        		System.out.println(driveTrainLeftEncoder.get());
//        		loopCount = 0;
//        	}
        	

        	if(driveStick.getRawButton(joystickTestButton)){
            	System.out.print("Left: ");
           		System.out.println(driveTrainLeftEncoder.get());
           		System.out.println(driveTrainLeftEncoder.getDistance());
           		System.out.println();
           		System.out.print("Right: ");
            	System.out.println(driveTrainRightEncoder.get());
            	System.out.println(driveTrainRightEncoder.getDistance());
            	System.out.println();
        	}
        	
        	if(driveStick.getRawButton(joystickLiftUp)){
        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*1);
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
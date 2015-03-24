package org.team5735.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class PIDRobot extends IterativeRobot {
	
	int loopCount = 0 ;
	
	RobotDrive driveTrain;
	Joystick driveStick;
	VictorSP liftMotor;
	CameraServer camServer;
	Encoder driveTrainRightEncoder;
	Encoder driveTrainLeftEncoder;
	Encoder liftEncoder;
	DigitalOutput ledGreen;
	DigitalOutput ledRed;
	DigitalInput limitSwitch;
//	Lift lift;
//	LiftThread thread;
	private PIDController controller;
	
	
	//Computer Ports
	int driveStickPort = 0;
	
	//PWM Ports
	int driveTrainLeftMotorPort = 0;
	int driveTrainRightMotorPort = 1;
	int liftMotorPort = 2;
	
	//DIO Ports
	int driveTrainRightEncoderPortA = 0;
	int driveTrainRightEncoderPortB = 1;
	int driveTrainLeftEncoderPortA = 2;
	int driveTrainLeftEncoderPortB = 3;
	int liftEncoderPortA = 4;
	int liftEncoderPortB = 5;
	int limitSwitchPort = 6;
	int ledRedPort = 8;
	int ledGreenPort = 9;
	
	//Joystick Axis
	int joystickRotateAxis = 0;
	int joystickLiftAxis = 2;
	int joystickTurboAxis = 3;
	int joystickMoveAxis = 5;
	
	//Joystick Buttons
	int joystickTestButton = 1;
	int joystickResetButton = 2;
	int joystickLedButton = 3;
	int joystickLiftUp = 5;
	int joystickLiftDown = 6;
	int joystickFirstLiftButton = 7;
	int joystickSecondLiftButton = 8;
	int joystickTogglePIDButton = 4;
	
	//DriveTrainScaling
	double driveTrainMoveScaling = 0.5;
	double driveTrainRotateScaling = 0.5;
	
	boolean isFirstButtonPressed = false;
	boolean isSecondButtonPressed = false;
	boolean isTogglePIDButtonPressed = false;
	
	//Redeclaration of arcadeDrive
    public void arcadeDrive(GenericHID joystick, final int moveAxis, final double moveScaling, final int rotateAxis, final double rotateScaling, final int turboAxis, boolean squaredInputs){
    	double moveValue = joystick.getRawAxis(moveAxis)*(joystick.getRawAxis(turboAxis)+1)*moveScaling;
    	double rotateValue = joystick.getRawAxis(rotateAxis)*rotateScaling;
    	driveTrain.arcadeDrive(moveValue, rotateValue, squaredInputs);
    }
	
    public void robotInit() {
    	//Init Webcam
    	camServer = CameraServer.getInstance();
        camServer.setQuality(50);
        camServer.startAutomaticCapture("cam0");
    	
        //Create RobotDrive
    	driveTrain = new RobotDrive(driveTrainLeftMotorPort,driveTrainRightMotorPort);
    	
    	//Create Joystick
    	driveStick = new Joystick(driveStickPort);
    	
    	//Create DIOs
    	ledGreen = new DigitalOutput(ledGreenPort);
    	ledRed = new DigitalOutput(ledRedPort);
    	limitSwitch = new DigitalInput(limitSwitchPort);
    	
    	//Create MotorController
    	liftMotor = new VictorSP(liftMotorPort);
    	
    	//Set Up Right Encoder
    	driveTrainRightEncoder = new Encoder(driveTrainRightEncoderPortA, driveTrainRightEncoderPortB, true, Encoder.EncodingType.k4X);
    	driveTrainRightEncoder.reset();
    	driveTrainRightEncoder.setMaxPeriod(.1);
    	driveTrainRightEncoder.setMinRate(10);
    	driveTrainRightEncoder.setDistancePerPulse((6*Math.PI)/2048*125/120);
    	driveTrainRightEncoder.setSamplesToAverage(10);
    	
    	//Set up Left Encoder
    	driveTrainLeftEncoder = new Encoder(driveTrainLeftEncoderPortA, driveTrainLeftEncoderPortB, false, Encoder.EncodingType.k4X);
    	driveTrainLeftEncoder.reset();
    	driveTrainLeftEncoder.setMaxPeriod(.1);
    	driveTrainLeftEncoder.setMinRate(10);
    	driveTrainLeftEncoder.setDistancePerPulse((6*Math.PI)/2048*125/120);
    	driveTrainLeftEncoder.setSamplesToAverage(10);
    	
    	//Set up Lift Encoder
    	liftEncoder = new Encoder(liftEncoderPortA, liftEncoderPortB, false, Encoder.EncodingType.k4X);
    	liftEncoder.reset();
    	liftEncoder.setMaxPeriod(.1);
    	liftEncoder.setMinRate(10);
    	liftEncoder.setDistancePerPulse(41.5/7566*4);
    	liftEncoder.setSamplesToAverage(10);
    	controller = new PIDController(0.01, 0.002, 0.3, 0.01, liftEncoder, liftMotor);
    }

    public void autonomousPeriodic() {
    	while(isAutonomous()&& isEnabled()){
    		driveTrainRightEncoder.reset();
    		driveTrainLeftEncoder.reset();
    		
    		if (loopCount == 100){
    			while(driveTrainLeftEncoder.getDistance() <30 && driveTrainRightEncoder.getDistance() <30){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While 5 loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() < 1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() > -1*34*Math.PI*90/360){
    				driveTrain.drive(-0.25, 1);
    				System.out.println("Running While 6 loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() <12 && driveTrainRightEncoder.getDistance() <12){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While 7 loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() > -1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() < 1*34*Math.PI*90/360){
    				driveTrain.drive(-0.25, -1);
    				System.out.println("Running While 8 loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() <12 && driveTrainRightEncoder.getDistance() <12){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While 9 loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    		}
    		loopCount++;
    		Timer.delay(0.01);
    	}
    }

    public void teleopPeriodic() {
    	while (isOperatorControl() && isEnabled()){
        	if(driveStick.getRawButton(joystickFirstLiftButton)&&!isFirstButtonPressed){
        		controller.setSetpoint(0);
        		isFirstButtonPressed = true;
        	}
        	else if (!driveStick.getRawButton(joystickFirstLiftButton)&&isFirstButtonPressed){
        		isFirstButtonPressed = false;
        	}
        	
        	
        	if(driveStick.getRawButton(joystickSecondLiftButton)&&!isSecondButtonPressed){
        		controller.setSetpoint(20);
        		isSecondButtonPressed = true;
        	}
        	else if (!driveStick.getRawButton(joystickSecondLiftButton)&&isSecondButtonPressed){
        		isSecondButtonPressed = false;
        	}
        	
        	if(driveStick.getRawButton(joystickTogglePIDButton)&&!isTogglePIDButtonPressed){
        		if(controller.isEnable()){
        			controller.disable();
        		}else{
        			controller.enable();
        		}
//        		if (thread.isShouldStop()){
//        			thread.setShouldStop(false);
//        		}else{
//        			thread.setShouldStop(true);
//        		}
        		isTogglePIDButtonPressed = true;
        	}
        	else if (!driveStick.getRawButton(joystickTogglePIDButton)&&isTogglePIDButtonPressed){
        		isTogglePIDButtonPressed = false;
        	}
        	
        	if(driveStick.getRawButton(joystickLedButton)){
            	ledGreen.set(true);
            	ledRed.set(false);
        	}else if(!driveStick.getRawButton(joystickLedButton)){
        		ledGreen.set(false);
        		ledRed.set(true);
        	}
        	
        	if(!limitSwitch.get()){
        		liftEncoder.reset();
        	}
        	
        	if(driveStick.getRawButton(joystickTestButton)){
            	System.out.print("Left: ");
           		System.out.println(driveTrainLeftEncoder.getDistance());
           		System.out.println();
           		System.out.print("Right: ");
            	System.out.println(driveTrainRightEncoder.getDistance());
            	System.out.println();
            	System.out.print("Lift: ");
            	System.out.println(liftEncoder.getDistance());
            	System.out.println();
        	}
        	
        	if(driveStick.getRawButton(joystickResetButton)){
        		driveTrainLeftEncoder.reset();
        		driveTrainRightEncoder.reset();
        		liftEncoder.reset();
        		loopCount = 0;
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
    	controller.disable();
//        thread.setShouldStop(true);
    }
    

    public void testPeriodic() {
    
    }
    
}
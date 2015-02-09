package org.team5735.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends IterativeRobot {
	
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
	
	
	int driveStickPort = 0;
	int driveTrainLeftMotorPort = 0;
	int driveTrainRightMotorPort = 1;
	int liftMotorPort = 2;
	int ledRedPort = 8;
	int ledGreenPort = 9;
	
	int joystickMoveAxis = 5;
	int joystickRotateAxis = 0;
	int joystickTurboAxis = 3;
	int joystickLiftAxis = 2;
	int joystickLiftUp = 5;
	int joystickLiftDown = 6;
	int joystickTestButton = 1;
	int joystickResetButton = 2;
	int joystickLedButton = 3;
	
	double driveTrainMoveScaling = 0.5;
	double driveTrainRotateScaling = 0.5;
	
	
    public void arcadeDrive(GenericHID joystick, final int moveAxis, final double moveScaling, final int rotateAxis, final double rotateScaling, final int turboAxis, boolean squaredInputs){
    	double moveValue = joystick.getRawAxis(moveAxis)*(joystick.getRawAxis(turboAxis)+1)*moveScaling;
    	double rotateValue = joystick.getRawAxis(rotateAxis)*rotateScaling;
    	driveTrain.arcadeDrive(moveValue, rotateValue, squaredInputs);
    }
	
    public void robotInit() {
    	
    	
    	camServer = CameraServer.getInstance();
        camServer.setQuality(50);
        camServer.startAutomaticCapture("cam0");
    	
    	driveTrain = new RobotDrive(driveTrainLeftMotorPort,driveTrainRightMotorPort);
    	driveStick = new Joystick(driveStickPort);
    	
    	ledGreen = new DigitalOutput(ledGreenPort);
    	ledRed = new DigitalOutput(ledRedPort);
    	
    	liftMotor = new VictorSP(liftMotorPort);

    	driveTrainRightEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
    	driveTrainRightEncoder.reset();
    	driveTrainRightEncoder.setMaxPeriod(.1);
    	driveTrainRightEncoder.setMinRate(10);
    	driveTrainRightEncoder.setDistancePerPulse(-1*(6*Math.PI)/2048*125/120);
    	driveTrainRightEncoder.setSamplesToAverage(10);
    	
    	driveTrainLeftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
    	driveTrainLeftEncoder.reset();
    	driveTrainLeftEncoder.setMaxPeriod(.1);
    	driveTrainLeftEncoder.setMinRate(10);
    	driveTrainLeftEncoder.setDistancePerPulse((6*Math.PI)/2048*125/120);
    	driveTrainLeftEncoder.setSamplesToAverage(10);

    }

    public void autonomousPeriodic() {
    	while(isAutonomous()&& isEnabled()){
    		if (loopCount == 100){
    			
    			while(driveTrainLeftEncoder.getDistance() <30 && driveTrainRightEncoder.getDistance() <30){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() < 1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() > -1*34*Math.PI*90/360){
    				driveTrain.drive(-0.25, 1);
    				System.out.println("Running While loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() <12 && driveTrainRightEncoder.getDistance() <12){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() > -1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() < 1*34*Math.PI*90/360){
    				driveTrain.drive(-0.25, -1);
    				System.out.println("Running While loop");
    			}
    			driveTrain.drive(0,0);
    			driveTrainRightEncoder.reset();
    			driveTrainLeftEncoder.reset();
    			Timer.delay(1);
    			
    			while(driveTrainLeftEncoder.getDistance() <12 && driveTrainRightEncoder.getDistance() <12){
    				driveTrain.drive(-0.25, 0);
    				System.out.println("Running While loop");
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
//        	loopCount ++;
//        	if (loopCount > 200){
//        		System.out.println(driveTrainLeftEncoder.get());
//        		loopCount = 0;
//        	}
        	

        	if(driveStick.getRawButton(joystickLedButton)){
            	ledGreen.set(true);
            	ledRed.set(false);
        	}else if(!driveStick.getRawButton(joystickLedButton)){
        		ledGreen.set(false);
        		ledRed.set(true);
        	}
        	
        	if(driveStick.getRawButton(joystickTestButton)){
            	System.out.print("Left: ");
           		System.out.println(driveTrainLeftEncoder.getDistance());
           		System.out.println();
           		System.out.print("Right: ");
            	System.out.println(driveTrainRightEncoder.getDistance());
            	System.out.println();
        	}
        	
        	if(driveStick.getRawButton(joystickResetButton)){
        		driveTrainLeftEncoder.reset();
        		driveTrainRightEncoder.reset();
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
    }
    

    public void testPeriodic() {
    
    }
    
}
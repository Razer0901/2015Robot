package org.team5735.robot;

//import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
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
	int jumperState;
	boolean isLiftReset = false;
	
	//Create objects
	RobotDrive driveTrain;
	Joystick driveStick;
	VictorSP liftMotor;
	//CameraServer camServer;
	Encoder driveTrainRightEncoder;
	Encoder driveTrainLeftEncoder;
	Encoder liftEncoder;
	DigitalOutput ledGreen;
	DigitalInput limitSwitch;
	DigitalInput jumperOne;
	DigitalInput jumperTwo;
	Lift lift;
	LiftThread thread;
	
	//Computer Ports
	static final int DRIVE_STICK_PORT = 0;
	
	//Joystick Buttons
	static final int BUTTON_A = 1;
	static final int BUTTON_B = 2;
	static final int BUTTON_X = 3;
	static final int BUTTON_Y = 4;
	static final int BUTTON_LEFT = 5;
	static final int BUTTON_RIGHT = 6;
	static final int BUTTON_BACK = 7;
	static final int BUTTON_START = 8;
	
	//PWM Ports
	static final int DRIVE_TRAIN_LEFT_MOTOR_PORT = 0;
	static final int DRIVE_TRAIN_RIGHT_MOTOR_PORT = 1;
	static final int LIFT_MOTOR_PORT = 2;
	
	//DIO Ports
	int driveTrainRightEncoderPortA = 0;
	int driveTrainRightEncoderPortB = 1;
	int driveTrainLeftEncoderPortA = 2;
	int driveTrainLeftEncoderPortB = 3;
	int liftEncoderPortA = 4;
	int liftEncoderPortB = 5;
	int limitSwitchPort = 6;
	int jumperOnePort = 7;
	int jumperTwoPort = 8;
	int ledGreenPort = 9;
	
	//Joystick Axis
	int joystickRotateAxis = 0;
	int joystickLiftAxis = 2;
	int joystickTurboAxis = 3;
	int joystickMoveAxis = 5;
	
	//Joystick Buttons Assignment
	static final int JOYSTICK_LOG_BUTTON = BUTTON_A;
	static final int JOYSTICK_ENCODER_RESET_BUTTON = BUTTON_B;
	static final int JOYSTICK_LED_BUTTON = BUTTON_X;
	static final int JOYSTICK_TOGGLE_PID_BUTTON = BUTTON_Y;
	static final int JOYSTICK_LIFT_UP_BUTTON = BUTTON_LEFT;
	static final int JOYSTICK_LIFT_DOWN_BUTTON = BUTTON_RIGHT;
	static final int JOYSTICK_ENABLE_PID_BUTTON = BUTTON_BACK;
	static final int JOYSTICK_DISABLE_PID_BUTTON = BUTTON_START;
	
	//DriveTrainScaling
	double driveTrainMoveScaling = 0.5;
	double driveTrainRotateScaling = 0.7;
	
	//Button Pressed
	boolean isPIDEnableButtonPressed = false;
	boolean isPIDDisableButtonPressed = false;
	boolean isLiftUpButtonPressed = false;
	boolean isLiftDownButtonPressed = false;
	
	//Redeclaration of arcadeDrive
    public void arcadeDrive(GenericHID joystick, final int moveAxis, final double moveScaling, final int rotateAxis, final double rotateScaling, final int turboAxis, boolean squaredInputs){
    	double moveValue = joystick.getRawAxis(moveAxis)*(joystick.getRawAxis(turboAxis)+1)*moveScaling;
    	double rotateValue = joystick.getRawAxis(rotateAxis)*rotateScaling;
    	driveTrain.arcadeDrive(moveValue, rotateValue, squaredInputs);
    }
    
    public void autoForward(double distance, long timeLimit){
    	long currentTime = System.currentTimeMillis();
		driveTrainRightEncoder.reset();
		driveTrainLeftEncoder.reset();
		while(driveTrainLeftEncoder.getDistance() <distance && driveTrainRightEncoder.getDistance() <distance && isEnabled() && (System.currentTimeMillis()- currentTime) < timeLimit ){
			driveTrain.drive(-0.25, 0);
			System.out.println("Running While Forward loop");
		}
		driveTrain.drive(0,0);
    }
    
    public void autoBackward(double distance,long timeLimit){
    	long currentTime = System.currentTimeMillis();
		driveTrainRightEncoder.reset();
		driveTrainLeftEncoder.reset();
		while(driveTrainLeftEncoder.getDistance() >-1*distance && driveTrainRightEncoder.getDistance() >-1*distance && isEnabled() && (System.currentTimeMillis()- currentTime) < timeLimit){
			driveTrain.drive(0.25, 0);
			System.out.println("Running While Backwards loop");
		}
		driveTrain.drive(0,0);
    }
    
    public void autoRight(long timeLimit){
    	long currentTime = System.currentTimeMillis();
		driveTrainRightEncoder.reset();
		driveTrainLeftEncoder.reset();
		while(driveTrainLeftEncoder.getDistance() < 1*32*Math.PI*90/360 && driveTrainRightEncoder.getDistance() > -1*34*Math.PI*90/360 && isEnabled() && (System.currentTimeMillis()- currentTime) < timeLimit){
			driveTrain.drive(-0.4, 1);
			System.out.println("Running While Right loop");
		}
		driveTrain.drive(0,0);
    }
    
    public void autoLeft(long timeLimit){
    	long currentTime = System.currentTimeMillis();
		driveTrainRightEncoder.reset();
		driveTrainLeftEncoder.reset();
		while(driveTrainLeftEncoder.getDistance() > -1*32*Math.PI*90/360 && driveTrainRightEncoder.getDistance() < 1*34*Math.PI*90/360 && isEnabled() && (System.currentTimeMillis()- currentTime) < timeLimit){
			driveTrain.drive(-0.4, -1);
			System.out.println("Running While Left loop");
		}
    }
	
    public void robotInit() {
    	//Init Webcam
    	//camServer = CameraServer.getInstance();
        //camServer.setQuality(50);
        //camServer.startAutomaticCapture("cam0");
    	
        //Create RobotDrive
    	driveTrain = new RobotDrive(DRIVE_TRAIN_LEFT_MOTOR_PORT,DRIVE_TRAIN_RIGHT_MOTOR_PORT);
    	
    	//Create Joystick
    	driveStick = new Joystick(DRIVE_STICK_PORT);
    	
    	//Create DIOs
    	ledGreen = new DigitalOutput(ledGreenPort);
    	limitSwitch = new DigitalInput(limitSwitchPort);
    	jumperOne = new DigitalInput(jumperOnePort);
    	jumperTwo = new DigitalInput(jumperTwoPort);
    	
    	//Create MotorController
    	liftMotor = new VictorSP(LIFT_MOTOR_PORT);
    	
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
    	
    	//Set up Lift
    	lift = new Lift(liftEncoder, liftMotor,0.05,0.30);

    }

    public void autonomousPeriodic() {
    	if (jumperOne.get()){
    		if (jumperTwo.get()){
        		jumperState = 0;
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        		System.out.println(jumperState);
    		}else{
    			jumperState = 2;
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        		System.out.println(jumperState);
    		}
    	}else{
    		if (jumperTwo.get()){
        		jumperState = 1;
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        		System.out.println(jumperState);
    		}else{
    			jumperState = 3;
        		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        		System.out.println(jumperState);
    		}
    	}
    	
    	resetLift();
    	thread = new LiftThread(lift);
    	thread.start();
		lift.setLiftEnable(true);
    	
    	loopCount = 0;
		driveTrainRightEncoder.reset();
		driveTrainLeftEncoder.reset();
		
		
		if (jumperState == 0){
	    	while(isAutonomous()&& isEnabled()){
	    		loopCount ++;
	    		if (loopCount == 100){
	    			
	    		}
	    	}
		}else if (jumperState == 1){
	    	while(isAutonomous()&& isEnabled()){
	    		loopCount ++;
	    		if (loopCount == 100){
	    			autoForward(107, 10000);
	    			Timer.delay(0.5);
	    		}
	    	}
		}else if (jumperState == 2){
	    	while(isAutonomous()&& isEnabled()){
	    		loopCount ++;
	    		if (loopCount == 100){
	    			autoForward(12,10000);
	    			Timer.delay(0.5);
	    			autoRight(5000);
	    			Timer.delay(0.5);
	    			autoForward(107, 10000);
	    			Timer.delay(0.5);
	    		}
	    	}
		}else if (jumperState == 3){
	    	while(isAutonomous()&& isEnabled()){
	    		loopCount ++;
	    		if (loopCount == 100){
	    			autoForward(12,10000);
	    			Timer.delay(0.5);
	    			autoRight(5000);
	    			Timer.delay(0.5);
	    			autoForward(107, 10000);
	    			Timer.delay(0.5);
	    		}
	    	}
		}
    	
    	thread.setShouldStop(true); // stop the left thread
    	thread = null;
//    	while(isAutonomous()&& isEnabled()){
//    		driveTrainRightEncoder.reset();
//    		driveTrainLeftEncoder.reset();
//    		
//    		if (loopCount == 100){
//    			while(driveTrainLeftEncoder.getDistance() <30 && driveTrainRightEncoder.getDistance() <30){
//    				driveTrain.drive(-0.25, 0);
//    				System.out.println("Running While loop");
//    			}
//    			driveTrain.drive(0,0);
//    			driveTrainRightEncoder.reset();
//    			driveTrainLeftEncoder.reset();
//    			Timer.delay(1);
//    			
//    			while(driveTrainLeftEncoder.getDistance() < 1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() > -1*34*Math.PI*90/360){
//    				driveTrain.drive(-0.25, 1);
//    				System.out.println("Running While loop");
//    			}
//    			driveTrain.drive(0,0);
//    			driveTrainRightEncoder.reset();
//    			driveTrainLeftEncoder.reset();
//    			Timer.delay(1);
//    			
//    			while(driveTrainLeftEncoder.getDistance() <12 && driveTrainRightEncoder.getDistance() <12){
//    				driveTrain.drive(-0.25, 0);
//    				System.out.println("Running While loop");
//    			}
//    			driveTrain.drive(0,0);
//    			driveTrainRightEncoder.reset();
//    			driveTrainLeftEncoder.reset();
//    			Timer.delay(1);
//    			
//    			while(driveTrainLeftEncoder.getDistance() > -1*34*Math.PI*90/360 && driveTrainRightEncoder.getDistance() < 1*34*Math.PI*90/360){
//    				driveTrain.drive(-0.25, -1);
//    				System.out.println("Running While loop");
//    			}
//    			driveTrain.drive(0,0);
//    			driveTrainRightEncoder.reset();
//    			driveTrainLeftEncoder.reset();
//    			Timer.delay(1);
//    			
//    			while(driveTrainLeftEncoder.getDistance() <48 && driveTrainRightEncoder.getDistance() <48){
//    				driveTrain.drive(-0.25, 0);
//    				System.out.println("Running While loop");
//    			}
//    			driveTrain.drive(0,0);
//    			driveTrainRightEncoder.reset();
//    			driveTrainLeftEncoder.reset();
//    			Timer.delay(1);
//    		}
//    		loopCount++;
//    		Timer.delay(0.01);
//    	}
    }

    public void teleopPeriodic() {
    	thread = new LiftThread(lift);
    	thread.start();

		lift.setTargetPosition(liftEncoder.getDistance());
		lift.setLiftEnable(true);
		
    	while (isOperatorControl() && isEnabled()){
        	
    		//Joystick PID Enable Button
        	if(driveStick.getRawButton(JOYSTICK_ENABLE_PID_BUTTON)&&!isPIDEnableButtonPressed){
    			lift.setTargetPosition(liftEncoder.getDistance());
    			isPIDEnableButtonPressed = true;
    			lift.setLiftEnable(true);
        	}
        	else if (!driveStick.getRawButton(JOYSTICK_ENABLE_PID_BUTTON)&&isPIDEnableButtonPressed){
        		isPIDEnableButtonPressed = false;
        	}
        	
    		//Joystick PID Disable Button
        	if(driveStick.getRawButton(JOYSTICK_DISABLE_PID_BUTTON)&&!isPIDDisableButtonPressed){
        		lift.setLiftEnable(false);
        		isPIDDisableButtonPressed = true;
        	}
        	else if (!driveStick.getRawButton(JOYSTICK_DISABLE_PID_BUTTON)&&isPIDDisableButtonPressed){
        		isPIDDisableButtonPressed = false;
        	}
        	
        	//Limit Switch Reset
        	if(!limitSwitch.get()){
        		liftMotor.set(0);
        		moveLift(-1,0.15);
        	}
        	
        	//Joystick Log Button
        	if(driveStick.getRawButton(JOYSTICK_LOG_BUTTON)){
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

        	//Joystick Reset Encoders
        	if(driveStick.getRawButton(JOYSTICK_ENCODER_RESET_BUTTON)){
        		driveTrainLeftEncoder.reset();
        		driveTrainRightEncoder.reset();
        		lift.setTargetPosition(0);
        		liftEncoder.reset();
        		loopCount = 0;
        		isLiftReset = false;
        	}
        	
        	//Joystick LED Button
        	if(driveStick.getRawButton(JOYSTICK_LED_BUTTON)){
            	ledGreen.set(true);
        	}else if(!driveStick.getRawButton(JOYSTICK_LED_BUTTON)){
        		ledGreen.set(false);
        	}
        	
        	
        	if(isLiftReset){
	        	if(driveStick.getRawButton(JOYSTICK_LIFT_UP_BUTTON)&&!(liftEncoder.getDistance()>-1)){
	        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*1);
	        	}else if(driveStick.getRawButton(JOYSTICK_LIFT_DOWN_BUTTON)){
	        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*-1);
	        	}else{
	        		liftMotor.set(0);
	        	}
        	}else{
	        	if(driveStick.getRawButton(JOYSTICK_LIFT_UP_BUTTON)){
	        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*1);
	        	}else if(driveStick.getRawButton(JOYSTICK_LIFT_DOWN_BUTTON)){
	        		liftMotor.set(driveStick.getRawAxis(joystickLiftAxis)*-1);
	        	}else{
	        		liftMotor.set(0);
	        	}
        	}
        	
        	if(driveStick.getRawButton(JOYSTICK_LIFT_DOWN_BUTTON)&&!isLiftDownButtonPressed&&(driveStick.getRawAxis(joystickLiftAxis)>=0.01)){
        		isLiftDownButtonPressed = true;
        		lift.setOverride(true);
        	}else if(!driveStick.getRawButton(JOYSTICK_LIFT_DOWN_BUTTON)&&isLiftDownButtonPressed){
        		isLiftDownButtonPressed = false;
        		lift.setTargetPosition(liftEncoder.getDistance());
    			lift.setOverride(false);
        	}
        	
        	if(driveStick.getRawButton(JOYSTICK_LIFT_UP_BUTTON)&&!isLiftUpButtonPressed&&(driveStick.getRawAxis(joystickLiftAxis)>=0.01)){
        		isLiftUpButtonPressed = true;
        		lift.setOverride(true);
        	}else if(!driveStick.getRawButton(JOYSTICK_LIFT_UP_BUTTON)&&isLiftUpButtonPressed){
        		isLiftUpButtonPressed = false;
        		lift.setTargetPosition(liftEncoder.getDistance());
        		lift.setOverride(false);
        	}
        	
        	
        	arcadeDrive(driveStick,joystickMoveAxis,driveTrainMoveScaling,joystickRotateAxis,driveTrainRotateScaling,joystickTurboAxis,true);
        	Timer.delay(0.01);
        	
        }
    	System.out.println("*********************************");
    	System.out.println("**********************************");
        thread.setShouldStop(true);
        thread = null;
    }

	private boolean resetLift() {
		boolean isLiftOverride = lift.isOverride();
		lift.setOverride(true);
		int resetLiftLoopCount = 0;
		liftMotor.set(0.4);
		double oldDistance = liftEncoder.getDistance();
		while(limitSwitch.get()&&resetLiftLoopCount <1000  && isEnabled()){
			Timer.delay(0.01);
			if (oldDistance >liftEncoder.getDistance()){
				lift.setOverride(isLiftOverride);
				return false;
			}
			oldDistance = liftEncoder.getDistance();
			resetLiftLoopCount ++;
		}
		liftMotor.set(0);
		liftEncoder.reset();
		boolean isLiftMove = moveLift(-1,0.15);
		lift.setOverride(isLiftOverride);
		isLiftReset = true;
		if (resetLiftLoopCount > 1000){
			return false;
		}else{
			return isLiftMove;
		}
	}

	private boolean moveLift(double difference,double power) {
		boolean isLiftOverride = lift.isOverride();
		double currentPosition = liftEncoder.getDistance();
		double targetPosition = currentPosition+difference;
		int resetLiftLoopCount = 0;
		lift.setOverride(true);
		if (difference < 0){
			while(liftEncoder.getDistance()>targetPosition&&resetLiftLoopCount < 1000 && isEnabled()){
				System.out.println(liftEncoder.getDistance());
				liftMotor.set(-1*power);
				Timer.delay(0.01);
				resetLiftLoopCount++;
			}
		}else{
			while(liftEncoder.getDistance()<targetPosition&&resetLiftLoopCount < 1000 && isEnabled()){
				System.out.println(liftEncoder.getDistance());
				liftMotor.set(power);
				Timer.delay(0.01);
				resetLiftLoopCount++;
			}
		}
		liftMotor.set(0);
		lift.setOverride(isLiftOverride);
		if (resetLiftLoopCount > 1000){
			return false;
		}else{
			return true;
		}
	}
    

    public void testPeriodic() {
    
    }
    public void disabledInit() {
    	if (thread != null){
    		thread.setShouldStop(true);
    		thread = null;
    	}
    }
}
package org.usfirst.frc.team2035.robot.commands;

import org.usfirst.frc.team2035.robot.Robot;
import org.usfirst.frc.team2035.robot.RobotMap;
import org.usfirst.frc.team2035.robot.subsystems.*;

import edu.wpi.first.wpilibj.Timer;


public class WinAutonomous extends CommandBase {
	
	private final DriveTrain DRIVE;
	private final Forklift LIFTER;
	private final Rollers ROLLER;
	private Timer autonomousTimer;
	private Timer liftTimer;
	private Timer driveTimer;
	private Timer rollerTimer;
	private double drivenTime;
	private double storedTime;
	private Vision vision;
	private ImageProcess process;
	private double lowTime;
	private double highTime;
	
	public WinAutonomous() {
		super("drive");
		DRIVE = Robot.getDriveTrain();
		LIFTER = Robot.getForklift();
		ROLLER = Robot.getRollers();
		vision = new Vision();
		process = new ImageProcess();
	}
	
	
	public void initialize() {
		autonomousTimer = new Timer();
		liftTimer = new Timer();
		driveTimer = new Timer();
		rollerTimer = new Timer();
		vision.init();
		process.initProcessImage();
		process.setTote(false);
		lowTime = .1;
		highTime = 1.1;
	}
	
	public void execute() {
		/*
		double currentTime = autonomousTimer.get();
		if(currentTime > 0.0 && currentTime < .5){ //Change time that is needed 
			DRIVE.drive(MOTOR_ON_SPEED);
		} else if (currentTime >.541 && currentTime < 5.0){
			//LIFT.lift(); //change that is needed
		}
		*/
		autonomousTimer.start();
		storedTime= autonomousTimer.get();
		while(autonomousTimer.get() < 15.0)
		{
			//grabs and processes an image every 3 seconds
			if(storedTime > lowTime && storedTime < highTime)
			{
				vision.saveImage();
				process.processImage();
			}
			//Debugging here
			System.out.println("Tote: " + process.foundTote());
			System.out.println("Time: " + storedTime);
			System.out.println("Low: " + lowTime);
			System.out.println("High: " + highTime);
			System.out.println("Time: " + storedTime);
			//If a tote is found
			if(process.foundTote())
			{
				//drive until tote is 1 foot away with the rollers out
				driveTimer.start();
				while(Robot.getDistance() > 12)
				{
					DRIVE.drive(RobotMap.AUTONOMOUS_SPEED);		
					ROLLER.rollerOut();
				}
				//when a tote is in range, stop driving, retract rollers, lean the forklift forward and spin inward				
				driveTimer.stop();
				drivenTime = driveTimer.get();
				//if()
				LIFTER.extendForklift();
				if(ROLLER.getOut())
				{
					ROLLER.rollerIn();
				}
				rollerTimer.start();
				while(rollerTimer.get() < .3)
				{
					ROLLER.spinIn();
				}
				//release the rollers, start to lift the tote for 2 seconds
				if(!ROLLER.getOut())
				{
					ROLLER.rollerOut();
				}
				liftTimer.start();
				if(liftTimer.get() <= 2)
				{
					LIFTER.setliftforklift(); //CHANGE THIS METHOD NAME!!!!!
				}
				//drive in reverse with the same power for the same amount of time it took to get to the tote
				driveTimer.reset();
				driveTimer.start();
				while(driveTimer.get() < drivenTime)
				{
					DRIVE.drive(RobotMap.AUTONOMOUS_SPEED * -1);
				}
			}
			//if a tote is not found then, turn looking for a tote
			else
			{
				DRIVE.rotate(.2);
				if(storedTime > lowTime)
				{
					lowTime += 3.0;
					highTime += 3.0;
				}	
			}
			storedTime = autonomousTimer.get();
		}
		autonomousTimer.stop();
	}
	
	public boolean isFinished() {
		if (autonomousTimer.get() > 15.0) {
            return true;
        }
        return false;
	}
	
	public void interrupted() 
	{

	}
	public void end()
	{
		DRIVE.drive(0);
	}	
}

package org.usfirst.frc.team2035.robot.commands;

import org.usfirst.frc.team2035.robot.Robot;
import org.usfirst.frc.team2035.robot.subsystems.Forklift;

public class TiltIn extends CommandBase {
	
	private Forklift tilter;

	public TiltIn() {
		super("Tilt In");
		tilter = Robot.getForklift();
	}

	@Override
	protected void initialize() {
		//tilter.retractForklift(); Commented out so as to stop errors with a nonexistent method in Forklift Dom
	}

	@Override
	protected void execute() {

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub

	}

}

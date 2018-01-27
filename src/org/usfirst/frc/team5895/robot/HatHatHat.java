package org.usfirst.frc.team5895.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.frc.team5895.robot.framework.Waiter;

public class HatHatHat {
	private Solenoid leftSolenoid1,leftSolenoid2,rightSolenoid1,rightSolenoid2;
	private DigitalInput hatSensor;
	private enum Mode_Type {LEFT_EXTEND,RIGHT_EXTEND,HOLDING}; //create states
	private Mode_Type mode = Mode_Type.HOLDING;
	private boolean leftSolenoidState = false, rightSolenoidState = false; //assume false = down, true = up 
	
	public HatHatHat() {
		//re-enter port when we know!
		leftSolenoid1 = new Solenoid(1); 
		leftSolenoid2 = new Solenoid(2);
		rightSolenoid1 = new Solenoid(3);
		rightSolenoid2 = new Solenoid(4);
		hatSensor = new DigitalInput(5);
	}
	public void hold() {
		mode = Mode_Type.HOLDING;
	}
	public void leftExtend() {
		mode = Mode_Type.LEFT_EXTEND;
	}
	public void rightExtend() {
		mode = Mode_Type.RIGHT_EXTEND;
	}
	public void update() {
		if(hatSensor.get()) { //assume no cube = true
			mode = Mode_Type.HOLDING;
		}
		
		switch(mode) {
			case HOLDING:
				leftSolenoidState = false;
				rightSolenoidState = false;
				break;
			case LEFT_EXTEND:
				leftSolenoidState = true;
				rightSolenoidState = false;
				break;
			case RIGHT_EXTEND:
				leftSolenoidState = false;
				rightSolenoidState = true;
				break;	
			default:
				mode = Mode_Type.HOLDING; //if incorrect mode happens, change to holding.
		}
		leftSolenoid1.set(leftSolenoidState);
		leftSolenoid2.set(leftSolenoidState);
		rightSolenoid1.set(rightSolenoidState);
		rightSolenoid2.set(rightSolenoidState);
		
		if(mode == Mode_Type.LEFT_EXTEND || mode == Mode_Type.RIGHT_EXTEND) {
			Waiter.waitFor(2000); //wait for 2 seconds while the arm is extending
		}
		
	}
}
package org.usfirst.frc.team5895.robot;

import org.usfirst.frc.team5895.robot.framework.Waiter;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

public class Limelight {

	// make sure to use the new library that doesn't have "wpilibj" in the path
	private NetworkTable table;

	// input
	private boolean hasTarget;
	private double horizontalOffset; // -27 to 27 degrees
	private double verticalOffset; // -20.5 to 20.5 degrees
	private double area; // % of image
	private double rotation; // -90 to 0 degrees
	private double latency; // ms (Add at least 11ms for image capture latency)

	// output
	private LedMode led = LedMode.ON;
	private CamMode cam = CamMode.VISION_PROCESSING; // operation mode
	private double pipeline = 0; // current pipeline

	/**
	 * Sets enum types of LED mode: ON, OFF, BLINKING
	 * 
	 */
	public enum LedMode {
		ON(0),OFF(1),BLINKING(2);

		private double value;

		LedMode(double value){
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	}

	/**
	 * Sets enum types of Camera mode: VISION_PROCESSING (Vision mode), DRIVER_CAMERA (Raw Image)
	 *
	 */
	public enum CamMode {
		VISION_PROCESSING(0),DRIVER_CAMERA(1);

		private double value;

		CamMode(double value){
			this.value = value;
		}

		public double getValue() {
			return value;
		}
	}
	/**
	 * Start NetworkTable
	 * Initialize NetworkTable of Limelight
	 * 
	 */
	public Limelight() {
		NetworkTableInstance.getDefault().startClient(); //
		table = NetworkTableInstance.getDefault().getTable("limelight");
	}

	/**
	 * Update all methods in need of routine refreshing
	 * 
	 */
	public void update() {
		updateHasTarget();
		updateHorizontalOffset();
		updateVerticalOffset();
		updateTargetArea();
		updateRotation();
		updateLatency();
		updateLedMode();
		updateCamMode();
	}

	/**
	 * seeks for a cube assuming that its sees one and does not already possess one.
	 * @param intake cube intake instance
	 * @param drive drive train instance.
	 */
	public boolean seek(CubeIntake intake, DriveTrain drive) {
		if (!intake.hasCube() && hasTarget) {
			intake.down();
			intake.intake();
			double threshold = 10, speed = 0.2;
			// turn to face cube

			DriverStation.reportError("horizontal: " + horizontalOffset, false);
			if (Math.abs(horizontalOffset) > threshold) {
				DriverStation.reportError("turning", false);
				if (horizontalOffset > 0)
					drive.turnTo(horizontalOffset+10);
				else 
					drive.turnTo(horizontalOffset-10);
				//Waiter.waitFor(2000);
			}
			// go forward
			else {
				DriverStation.reportError("forward", false);
				drive.arcadeDrive(-speed, 0);
				//Waiter.waitFor(2000);
				
			}
			return true;
		}

		return false; //no target || has cube
	}
	
	public void autoSeek(CubeIntake intake, DriveTrain drive) {
		while (seek(intake, drive)) {}
		drive.arcadeDrive(0, 0);
	}

	/**
	 * Update boolean hasTarget
	 */
	public void updateHasTarget() {
		double val = table.getEntry("tv").getDouble(-1);
		if (val == 0d) {
			hasTarget = false;
		} else if (val == 1d) {
			hasTarget = true;
		} 
	}
	
	public void updateHorizontalOffset() {
		horizontalOffset = table.getEntry("tx").getDouble(-1);
	}
	
	public void updateVerticalOffset() {
		verticalOffset = table.getEntry("ty").getDouble(-1);
	}
	
	
	public void updateTargetArea() {
		area = table.getEntry("ta").getDouble(-1);
	}
	
	public void updateRotation() {
		rotation = table.getEntry("ts").getDouble(-1);
	}
	
	public void updateLatency() {
		latency = table.getEntry("tl").getDouble(-1);
	}
	
	public void updateLedMode() {
		table.getEntry("ledMode").setDouble(led.getValue());
	}
	
	public void updateCamMode() {
		table.getEntry("camMode").setDouble(cam.getValue());
	}
	
	public void updatePipeline() {
		table.getEntry("pipeline").setDouble(pipeline);
	}
	
	public void setLedMode(LedMode led) {
		this.led = led;
	}
	
	public void setCamMode(CamMode cam) {
		this.cam = cam;
	}
	
	public void setPipeline(double pipeline) {
		this.pipeline = Math.max(Math.min(pipeline, 9), 0);
	}
	
	public boolean hasTarget() {
		return hasTarget;
	}
	
	public double getHorizontalOffset() {
		return horizontalOffset;
	}
	
	public double getVerticalOffset() {
		return verticalOffset;
	}
	
	public double getArea() {
		return area;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public double getLatency() {
		return latency;
	}
	
	public LedMode getLED() {
		return led;
	}
	
	public CamMode getCAM() {
		return cam;
	}
	
	public double getPipeline() {
		return pipeline;
	}

}

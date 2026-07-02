package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.lib.FeedforwardController;
import org.firstinspires.ftc.teamcode.lib.PIDController;

// Two-motor flywheel class
@Config
public class FlywheelGraces {
    private DcMotorEx flywheelMaster;
    private DcMotorEx flywheel2;
    double targetVel; // Target velocity of flywheels

    // Flywheel Constants
    public static double kP, kI, kD; // PID constants
    public static double kRadial; // Radial feedforward constants
    // Feedforward constants: static gain (power needed to start moving), and velocity gain (linear
    // --> power needed per unit of velocity)
    public static double kS, kV;

    // PID components
    PIDController pidController;
    // Feedforward is different from feedback (PID). It predicts what we will need, and PID handles small errors.
    FeedforwardController ffController;
    VoltageSensor voltageSensor;
    public static double volts; // public static because it's not acc for the flywheel; it's the voltage in general
    double currentPower; // Current flywheel power
    // Feedforward to compensate for turning by the robot and turret; needed for moving while shooting
    double radialFF = 0;

    // Moves both motors at a certain power
    public void setPower(double power) {
        flywheelMaster.setPower(power);
        flywheel2.setPower(power);
    }

    // Sets radial feedforward value to input
    public void setRadialFF(double radialSpeed) {
        radialFF = kRadial * radialSpeed;
    }

    // Calculates flywheel power based on PIDF values and given velocity
    public double calculatePower(double velocity) {
        double power; // What will be returned
        targetVel = velocity; // Given input target velocity
        double curVel = getCurVel(); // Current flywheel velocity

        // What the PID Controller calculates
        double pidCalculation = pidController.calculate(curVel, targetVel);
        // kS + kV * target velocity (see notes on feedforward above)
        double ffCalculation = ffController.calculate(targetVel);
        volts = voltageSensor.getVoltage(); // Current voltage
        // Power is divided by voltage to have consistent flywheel regardless of battery level
        power = (pidCalculation + ffCalculation + radialFF) / volts;
        currentPower = power * volts; // Current power is saved, if it is needed

        return power; // Power needed to run flywheel to correct velocity
    }

    // Gets motor's current velocity
    public double getCurVel() {
        return flywheelMaster.getVelocity();
    }

    // Initialization method
    public void init(DcMotorEx flywheelMasterMotor, DcMotorEx flywheel2Motor, VoltageSensor vSensor) {
        // Assign hardware references passed in from the OpMode
        flywheelMaster = flywheelMasterMotor;
        flywheel2 = flywheel2Motor;
        voltageSensor = vSensor;

        // Configure master motor: direction, reset encoder, float when zero power, run without encoder
        flywheelMaster.setDirection(DcMotorEx.Direction.FORWARD);
        flywheelMaster.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMaster.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheelMaster.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Configure second motor, reversed since it's mounted opposite the master
        flywheel2.setDirection(DcMotorEx.Direction.REVERSE);
        flywheel2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        flywheel2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // Build PID and feedforward controllers from the tuned static constants above
        pidController = new PIDController(kP, kI, kD);
        ffController = new FeedforwardController(kS, kV);
    }
}
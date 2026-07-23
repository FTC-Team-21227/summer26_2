package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.acmerobotics.dashboard.*;

import org.firstinspires.ftc.teamcode.lib.FeedforwardController;
import org.firstinspires.ftc.teamcode.lib.PIDController;

@Config
public class Lift {
    private DcMotorEx liftMotorMaster; // Using encoder
    private DcMotorEx liftMotor2; // Set to same power as master motor
    private PIDController pidController;
    private FeedforwardController ffController;
    private VoltageSensor voltageSensor;
    public static double volts;

    // Config
    public static double liftPosition;
    public static double kP, kI, kD; // PID
    public static double kS, kV; // Feedforward values

    public Lift(HardwareMap hardwareMap) { // Constructor used at init
        // Initialize components
        liftMotorMaster = hardwareMap.get(DcMotorEx.class, "liftMotorMaster");
        liftMotor2 = hardwareMap.get(DcMotorEx.class, "liftMotor2");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");
        pidController = new PIDController(kP, kI, kD);
        ffController = new FeedforwardController(kS, kV);

        // Motor settings
        liftMotorMaster.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotorMaster.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorMaster.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        liftMotorMaster.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(double power) {
        liftMotorMaster.setPower(power);
        liftMotor2.setPower(power);
    }

    public double getPower() {
        return liftMotorMaster.getPower();
    }

    public int getLiftPosition() {
        return liftMotorMaster.getCurrentPosition();
    }

}

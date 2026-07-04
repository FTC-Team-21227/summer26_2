package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
/*
 * This OpMode illustrates how to program your robot to drive field relative.  This means
 * that the robot drives the direction you push the joystick regardless of the current orientation
 * of the robot.
 *
 * This OpMode assumes that you have four mecanum wheels each on its own motor named:
 *   front_left_motor, front_right_motor, back_left_motor, back_right_motor
 *
 *   and that the left motors are flipped such that when they turn clockwise the wheel moves backwards
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 */

// Code for hardware training's drivetrain + claw robot
@TeleOp(name = "Robot: Robot Relative Mecanum Drive")
public class teleopG extends OpMode {
    // This declares the four motors needed
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    Servo leftClawServo;
    Servo rightClawServo;

    // Servo position constants
    double openRightClawPos = 1;
    double closeRightClawPos = 0;
    double openLeftClawPos = 1;
    double closeLeftClawPos = 0;

    @Override
    public void init() {
        // Initialize motors and servos
        frontLeftDrive = hardwareMap.get(DcMotor.class, "W_FL");
        frontRightDrive = hardwareMap.get(DcMotor.class, "W_FR");
        backLeftDrive = hardwareMap.get(DcMotor.class, "W_BL");
        backRightDrive = hardwareMap.get(DcMotor.class, "W_BR");
        leftClawServo = hardwareMap.get(Servo.class, "leftClaw");
        rightClawServo = hardwareMap.get(Servo.class, "rightClaw");

        // Set directions for motors and servos
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftClawServo.setDirection(Servo.Direction.FORWARD);
        rightClawServo.setDirection(Servo.Direction.FORWARD);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
        telemetry.addLine("Left stick for driving");
        telemetry.addLine("Right stick for rotating");
        telemetry.addLine("Dpad left for closing claw");
        telemetry.addLine("Dpad right for opening claw");

        // Driving gamepad logic
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        drive(forward, right, gamepad1.right_stick_x);

        // Claw gamepad logic
        if (gamepad1.dpadLeftWasPressed()) {
            rightClawServo.setPosition(closeRightClawPos);
            leftClawServo.setPosition(closeLeftClawPos);
        }
        else if (gamepad1.dpadRightWasPressed()) {
            rightClawServo.setPosition(openRightClawPos);
            leftClawServo.setPosition(openLeftClawPos);
        }
    }

    // Drive function for four-wheel mecanum drive
    public void drive(double forward, double right, double rotate) {
        // This calculates the power needed for each wheel based on the amount of forward,
        // strafe right, and rotate
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxPower = 1.0;
        double maxSpeed = 1.0;  // make this slower for outreaches

        // This is needed to make sure we don't pass > 1.0 to any wheel
        // It allows us to keep all of the motors in proportion to what they should
        // be and not get clipped
        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.
        frontLeftDrive.setPower(maxSpeed * (frontLeftPower / maxPower));
        frontRightDrive.setPower(maxSpeed * (frontRightPower / maxPower));
        backLeftDrive.setPower(maxSpeed * (backLeftPower / maxPower));
        backRightDrive.setPower(maxSpeed * (backRightPower / maxPower));
    }
}
package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class teleopG extends OpMode {
    DcMotor W_FL;
    DcMotor W_BL;
    DcMotor W_FR;
    DcMotor W_BR;
    private Servo servoL;
    private Servo servoR;

    @Override
    public void init() {
        W_FL = hardwareMap.get(DcMotor.class, "W_FL");
        W_BL = hardwareMap.get(DcMotor.class, "W_BL");
        W_FR = hardwareMap.get(DcMotor.class, "W_FR");
        W_BR = hardwareMap.get(DcMotor.class, "W_BR");

        W_FL.setDirection(DcMotor.Direction.REVERSE);
        W_BL.setDirection(DcMotor.Direction.FORWARD);
        W_FR.setDirection(DcMotor.Direction.FORWARD);
        W_BR.setDirection(DcMotor.Direction.FORWARD);

        servoR = hardwareMap.get(Servo.class, "servoR");
        servoL = hardwareMap.get(Servo.class, "servoL");
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        W_FL.setPower(forward + right + rotate);
        W_BL.setPower(forward - right + rotate);
        W_FR.setPower(forward - right - rotate);
        W_BR.setPower(forward + right - rotate);

        // close
        if (gamepad1.xWasPressed()) {
            servoL.setPosition(0.65);
            servoR.setPosition(0.3);
        }
        // open
        if (gamepad1.bWasPressed()) {
            servoL.setPosition(0.8);
            servoR.setPosition(0.2);
        }
    }
}

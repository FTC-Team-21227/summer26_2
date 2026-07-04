package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends OpMode {
    private Servo servoL;
    private Servo servoR;
    double leftServoPos = 0;
    double rightServoPos = 0;

    @Override
    public void init() {
        servoR = hardwareMap.get(Servo.class, "servoR");
        servoL = hardwareMap.get(Servo.class, "servoL");
    }

    // Open Pos: left 0.8, right 0.2
    // Closed Pos: left 0.65, right 0.3

    @Override
    public void loop() {
        if (gamepad1.dpadLeftWasPressed()) { leftServoPos += 0.05; }
        if (gamepad1.dpadRightWasPressed()) { leftServoPos -= 0.05; }
        if (gamepad1.xWasPressed()) { rightServoPos += 0.05; }
        if (gamepad1.bWasPressed()) { rightServoPos -= 0.05; }

        servoL.setPosition(leftServoPos);
        servoR.setPosition(rightServoPos);

        telemetry.addData("left servo", leftServoPos);
        telemetry.addData("right servo", rightServoPos);
    }
}
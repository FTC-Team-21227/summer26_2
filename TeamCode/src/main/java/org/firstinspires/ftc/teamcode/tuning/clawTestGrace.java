package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class clawTestGrace extends OpMode {
    Servo leftClawServo;
    Servo rightClawServo;
    @Override
    public void init() {
        leftClawServo = hardwareMap.get(Servo.class, "servoL");
        rightClawServo = hardwareMap.get(Servo.class, "servoR");

        leftClawServo.setDirection(Servo.Direction.FORWARD);
        rightClawServo.setDirection(Servo.Direction.FORWARD);

        double leftClawPos;
        double rightClawPos;
    }

    @Override
    public void loop() {
        // Claw gamepad logic
        if (gamepad1.dpadLeftWasPressed()) {
//            rightClawServo.setPosition(closeRightClawPos);
//            leftClawServo.setPosition(closeLeftClawPos);
        }
        else if (gamepad1.dpadRightWasPressed()) {
//            rightClawServo.setPosition(openRightClawPos);
//            leftClawServo.setPosition(openLeftClawPos);
        }
    }
}

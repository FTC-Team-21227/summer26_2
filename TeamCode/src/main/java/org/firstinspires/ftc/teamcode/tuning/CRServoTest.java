package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

// Intake servo test for hardware training
@TeleOp(name="CR Servo Test")
public class CRServoTest extends OpMode {
    CRServo intakeServo; // Continuous rotation servo
    boolean spinIntake = false;
    double spinPower = 1;
    @Override
    public void init() {
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
    }

    @Override
    public void loop() {
        if (gamepad1.aWasPressed()) {
            spinIntake = !spinIntake; // Turn on and off
        }
        if (gamepad1.dpadUpWasPressed()) { // Adjust power up
            if(spinPower + 0.05 <= 1) {spinPower += 0.05;}
        }
        if (gamepad1.dpadDownWasPressed()) { // Adjust power down
            if(spinPower - 0.05 >= 0) {spinPower -= 0.05;}
        }
        if (spinIntake) {intakeServo.setPower(spinPower);} // Spin servo if boolean is true
        else {intakeServo.setPower(0);}

        // Telemetry lines
        telemetry.addData("Intake on", spinIntake); // Whether intake is on or off
        telemetry.addData("Intake speed", spinPower); // How fast intake would be spinning
    }
}

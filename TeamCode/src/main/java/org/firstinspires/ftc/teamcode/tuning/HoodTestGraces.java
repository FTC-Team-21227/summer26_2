package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.HoodGraces;

/** OpMode used to test hood positions so we can measure the angles when the hood servo is at
 * position 0 and 1. Also to determine how much to clip the servo range by (we don't want it to shoot
 * too flat or too high).
 */

@Config // Allows hoodPos to be editable through Dashboard
@TeleOp(name="Hood Test G")
public class HoodTestGraces extends OpMode {
    public static double hoodPos; // public static so it can be edited through Dashboard
    private HoodGraces hood; // Declare your objects here

    @Override
    public void init() {
        hood = new HoodGraces(hardwareMap); // Construct in init() because the OpMode is constructed right before init()
        // Set telemetry to display on both the Driver Station (DS) and Dashboard
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        // Difference between yWasPressed and y: y is true for how many loops go through, so if you
        // use just gamepad.y, it will increment a LOT in just one press because many loops will go
        // by just within the time of one press. gamepad.yWasPressed detects the edge, and will only
        // count once.
        if (gamepad1.yWasPressed()) {hoodPos += 0.01;} // Increment hood position using gamepad
        if (gamepad1.aWasPressed()) {hoodPos -= 0.01;}

        hood.setPosition(hoodPos); // Set hood position

        telemetry.addData("Hood position: ", hoodPos); // Write in telemetry
        telemetry.update(); // Update telemetry
    }
}

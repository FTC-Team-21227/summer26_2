package org.firstinspires.ftc.teamcode.tuning;

//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.subsystems.FlywheelGraces;
@Config
@TeleOp (name = "TuneNewFlywheelGraces")
/**
 * runs two flywheel motors
 */
public class TuneFlywheelGraces extends LinearOpMode {
    FlywheelGraces f;
    VoltageSensor v;
    public static int targetVel = 0;
    public static double power1 = 0;
    public static double power2 = 0;
    public void runOpMode(){
//        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        // FlywheelGraces needs the motors and voltage sensor pulled from hardwareMap directly,
        // since its init() takes hardware objects rather than the hardwareMap itself
        DcMotorEx flywheelMaster = hardwareMap.get(DcMotorEx.class, "flywheel");
        DcMotorEx flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        v = hardwareMap.get(VoltageSensor.class,"Control Hub");

        f = new FlywheelGraces();
        f.init(flywheelMaster, flywheel2, v);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        waitForStart();
        while (opModeIsActive()){
            double k = 0;
            if (gamepad1.rightBumperWasPressed()){
                targetVel += 100;
            }
            if (gamepad1.leftBumperWasPressed()){
//                power2 += 0.01;
                targetVel -= 100;
            }
            if (gamepad1.b){
                targetVel = 0;
                power1 = 0;
                power2 = 0;
                // Use FlywheelGraces' setPower() to zero both motors at once instead of reaching into fields directly
                f.setPower(0);
            }
            else {
//                f.FLYWHEEL_MASTER.setPower(power1);
//                f.FLYWHEEL2.setPower(power2);
                // calculatePower() runs PID + feedforward together to get power, then setPower() applies it to both motors
                double power = f.calculatePower(targetVel);
                f.setPower(power);
            }
            telemetry.addData("power", power1); // power1/power2 unused now, kept for reference; see "power" below for actual applied value
            telemetry.addData("target v", targetVel);
            telemetry.addData("motor v", f.getCurVel());
            telemetry.addData("motor volts", k);
            telemetry.update();
        }
    }
}
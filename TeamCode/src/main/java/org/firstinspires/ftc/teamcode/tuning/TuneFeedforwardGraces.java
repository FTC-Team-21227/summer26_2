package org.firstinspires.ftc.teamcode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.subsystems.FlywheelGraces;

public class TuneFeedforwardGraces extends OpMode {
    FlywheelGraces flywheel;
    ElapsedTime t;
    VoltageSensor voltageSensor;

    double POWER_PER_SEC = 0.1;
    double POWER_MAX = 1;

    public void init() {
        DcMotorEx flywheelMaster = hardwareMap.get(DcMotorEx.class, "flywheel");
        DcMotorEx flywheel2 = hardwareMap.get(DcMotorEx.class, "flywheel2");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        flywheel = new FlywheelGraces();
        flywheel.init(flywheelMaster, flywheel2, voltageSensor);

        t = new ElapsedTime();
        telemetry.update();
    }

    public void start() {
        t.reset();
    }

    public void loop() {
        double time = t.seconds();
        double power = power(time);

        flywheel.setPower(power); // uses your class's setPower, drives both motors

        double vel = flywheel.getCurVel(); // uses your class's velocity getter
        double bv = voltageSensor.getVoltage();
        double vapp = power * bv;

        RobotLog.dd("" + vel, "" + vapp); // graph vapp (y) vs vel (x)

        telemetry.addData("vel", vel);
        telemetry.addData("seconds", time);
        telemetry.addData("power", power);
        telemetry.addData("voltage applied", vapp);
        telemetry.addData("battery voltage", bv);
        telemetry.update();
    }

    public double power(double seconds) {
        return Math.min(POWER_PER_SEC * seconds, POWER_MAX);
    }
}
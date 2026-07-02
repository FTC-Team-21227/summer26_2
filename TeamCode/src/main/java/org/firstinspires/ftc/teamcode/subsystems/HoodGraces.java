package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HoodGraces {
    private Servo hood;
    // Answer: 0.24-0.74 range clips go to around 46.5 to 60 degrees, limiting the full 40-67 degree range
    private double hoodLowAngle; // Answer: 40 degrees in radians, maps to servo pos 0
    private double hoodHighAngle; // Answer: 67 degrees in radians, maps to servo pos 1

    public HoodGraces(HardwareMap hardwareMap) {
        hood = hardwareMap.get(Servo.class, "hood");
        hood.setDirection(Servo.Direction.FORWARD);
        hood.scaleRange(0, 1); // Full-range to simplify the interpolation and future changes. We will manually clip.
    }

    // Public method to set position
    public void setPosition(double pos) {
        hood.setPosition(pos);
    }

    // Public method to get position
    public double getPosition() {
        return hood.getPosition();
    }

    // Sets hood position to a certain angle (relative to ground/horizontal) by linearly interpolating
    // This gives us our shooting angle.
    // We use radians because the kinematic equation uses radians.
    public void turnToAngle(double angle){ // In radians
        setPosition((angle - hoodLowAngle) / (hoodHighAngle - hoodLowAngle));
    }

    // Returns angle of hood in radians
    public double getAngle() {
        return hood.getPosition() * (hoodHighAngle - hoodLowAngle) + hoodLowAngle;
    }
}

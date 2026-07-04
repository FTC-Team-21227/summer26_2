package org.firstinspires.ftc.teamcode.autons; // make sure this aligns with class location

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

@Autonomous(name="auton 1")
public class auton1 extends OpMode {
    private Follower follower;
    private Timer pathTimer;

    // Path states
    private enum PathState{
        STATE_1,
        STATE_2
    }
    private PathState pathState; // State manager instance

    // Define poses here: x, y, and heading values
    private final Pose startPose = new Pose(15, 100, Math.toRadians(90)); // Start Pose of our robot.
    private final Pose scorePose = new Pose(56, 89, Math.toRadians(135)); // Scoring Pose of our robot. It is facing the goal at a 135 degree angle.

    // Declare paths
    private Path scorePreload;
    private PathChain grabPickup1;
    private Path scorePickup1;

    // Creating/defining each path
    public void buildPaths() {
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierCurve(scorePose, new Pose(60,85,Math.toRadians(180)), scorePose))
                .setConstantHeadingInterpolation(scorePose.getHeading())
                .build();

        scorePickup1 = new Path(new BezierLine(scorePose, scorePose));
        scorePickup1.setLinearHeadingInterpolation(startPose.getHeading(),scorePose.getHeading());
    }

    // Method to update paths in loop while auton is running
    public void autonomousPathUpdate() {
        switch (pathState) {
            case STATE_1:
                follower.getConstraints().setBrakingStart(4); // If you want to change braking strength--optional
                follower.followPath(scorePreload);
                setPathState(PathState.STATE_2);
                break;
            case STATE_2:
                if (!follower.isBusy()) { // Checks if the previous path is finished
                    setPathState(PathState.STATE_1); //every time set path state is called, pathtimer is reset
                }
                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(PathState pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {
        // These loop the movements of the robot, these must be called continuously in order to work
        autonomousPathUpdate();
        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        pathTimer = new Timer();
        follower.getConstraints().setBrakingStrength(1);
        buildPaths();
        follower.setStartingPose(startPose);
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        setPathState(PathState.STATE_1);
    }
}
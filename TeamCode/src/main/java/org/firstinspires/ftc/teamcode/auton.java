package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Pedro Auto", group = "Autonomous")
public class auton extends OpMode {

    private Follower follower;
    private ElapsedTime timer;

    private PathState pathState;

    /*
     * TODO: CHANGE THESE POSES TO MATCH YOUR FIELD SIDE.
     *
     * Pedro uses inches.
     * Heading is in radians.
     * Use Math.toRadians(degrees).
     */

    // These poses are set positions where we want to drive through in the Auton mode.
    private final Pose startPose = new Pose(0, 0, Math.toRadians(0));

    private final Pose scorePreloadPose = new Pose(0, 0, Math.toRadians(0));
    private final Pose pickupPose = new Pose(0, 0, Math.toRadians(0));
    private final Pose scoreCyclePose = new Pose(0, 0, Math.toRadians(0));
    private final Pose parkPose = new Pose(0, 0, Math.toRadians(90));

    // The following PathChains are collections of Paths chained together.
    private PathChain scorePreload;
    private PathChain goToPickup;
    private PathChain scoreCycle;
    private PathChain park;

    private enum PathState {
        START,
        SCORE_PRELOAD,
        GO_TO_PICKUP,
        PICKUP,
        SCORE_CYCLE,
        PARK,
        IDLE
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        timer = new ElapsedTime();

        follower.setStartingPose(startPose);

        buildPaths();

        pathState = PathState.START;

        telemetry.addLine("Pedro Auto Initialized");
        telemetry.update();
    }

    private void buildPaths() {
        scorePreload = follower.pathBuilder()
                .addPath(new BezierLine(startPose, scorePreloadPose))
                .setLinearHeadingInterpolation(
                        startPose.getHeading(),
                        scorePreloadPose.getHeading()
                )
                .build();

        goToPickup = follower.pathBuilder()
                .addPath(new BezierLine(scorePreloadPose, pickupPose))
                .setLinearHeadingInterpolation(
                        scorePreloadPose.getHeading(),
                        pickupPose.getHeading()
                )
                .build();

        scoreCycle = follower.pathBuilder()
                .addPath(new BezierLine(pickupPose, scoreCyclePose))
                .setLinearHeadingInterpolation(
                        pickupPose.getHeading(),
                        scoreCyclePose.getHeading()
                )
                .build();

        park = follower.pathBuilder()
                .addPath(new BezierLine(scoreCyclePose, parkPose))
                .setLinearHeadingInterpolation(
                        scoreCyclePose.getHeading(),
                        parkPose.getHeading()
                )
                .build();
    }

    // RECALL: start() is a standard method of OpModes!
    @Override
    public void start() {
        timer.reset();
        setPathState(PathState.START);
    }

    // RECALL: loop() is a standard method of OpModes!
    @Override
    public void loop() {
        follower.update();

        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("Pose", follower.getPose());
        telemetry.addData("Busy", follower.isBusy());
        telemetry.update();
    }

    private void autonomousPathUpdate() {
        // JAVA REVIEW! A switch statement is essentially an if-else statement that revolves
        // around the value of one variable! - In this case, it's pathState :)
        // This is how our state machine functions - each path state is another state.
        switch (pathState) {

            case START:
                /*
                 * Start first path.
                 */
                follower.followPath(scorePreload);
                setPathState(PathState.SCORE_PRELOAD);
                break;

            case SCORE_PRELOAD:
                /*
                 * Wait until robot reaches preload scoring pose.
                 */
                if (!follower.isBusy()) {
                    scorePreloadAction();

                    follower.followPath(goToPickup);
                    setPathState(PathState.GO_TO_PICKUP);
                }
                break;

            case GO_TO_PICKUP:
                /*
                 * Wait until robot reaches pickup position.
                 */
                if (!follower.isBusy()) {
                    startPickupAction();
                    setPathState(PathState.PICKUP);
                }
                break;

            case PICKUP:
                /*
                 * Give intake/claw time to grab.
                 * Replace this timer with a sensor check if you have one.
                 */
                if (timer.seconds() > 0.75) {
                    stopPickupAction();

                    follower.followPath(scoreCycle);
                    setPathState(PathState.SCORE_CYCLE);
                }
                break;

            case SCORE_CYCLE:
                /*
                 * Wait until robot returns to scoring position.
                 */
                if (!follower.isBusy()) {
                    scoreCycleAction();

                    follower.followPath(park);
                    setPathState(PathState.PARK);
                }
                break;

            case PARK:
                /*
                 * End auto after parking.
                 */
                if (!follower.isBusy()) {
                    setPathState(PathState.IDLE);
                }
                break;

            case IDLE:
                /*
                 * Do nothing.
                 */
                break;
        }
    }

    private void setPathState(PathState newState) {
        pathState = newState;
        timer.reset();
    }

    /*
     * Replace these methods with your real subsystem code.
     * For example:
     * lift.setTargetPosition(...)
     * claw.open()
     * intake.setPower(...)
     */

    private void scorePreloadAction() {
        // TODO: open claw / outtake preload / shoot / drop specimen
    }

    private void startPickupAction() {
        // TODO: turn on intake or close claw
    }

    private void stopPickupAction() {
        // TODO: stop intake or secure object
    }

    private void scoreCycleAction() {
        // TODO: score second object
    }
}
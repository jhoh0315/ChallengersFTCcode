package org.firstinspires.ftc.teamcode.sampledata.AutoMode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.checkerframework.checker.units.qual.Speed;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name="RedFront", group="Pushbot", preselectTeleOp="main_control")
public class RedFront extends LinearOpMode {
    static final int aud_speed = 3;
    static final int al_speed = 10;
    static final int al_min = -2500;
    static final int al_max = 0;

    static final int isBlue = 1; //  Blue -> -1  ,  Red -> 1
    static final Pose2d startpose = new Pose2d(15, -64 * isBlue, Math.toRadians(-90 * isBlue));





    static double agtgt_tick = 0.0395;
    static int aud_tick = 0;
    static int al_tick = 0;
    static boolean afldo = true;
    static boolean afrdo = true;


    static DcMotor aud_motor1 = null;
    static DcMotor aud_motor2 = null;
    static DcMotor rightFront = null;
    static DcMotor rightRear = null;
    static DcMotor leftFront = null;
    static DcMotor leftRear = null;
    static DcMotor al_motor = null;

    static Servo agtgt_servo1 = null;
    static Servo agtgt_servo2 = null;
    static Servo afl_servo = null;
    static Servo afr_servo = null;
    static Servo fly_servo = null;

    private static final String TFOD_MODEL_ASSET = "AI.tflite";
    private static final String[] LABELS = {
            "BlUE_TES","RED_TES"
    };
    private TfodProcessor tfod;
    VisionPortal visionPortal;
    SampleMecanumDrive drive = null;
    static int findID = 0;
    static Trajectory Trajec = null;


    @Override
    public void runOpMode() {

        tfod = new TfodProcessor.Builder()
                .setModelAssetName(TFOD_MODEL_ASSET)
                .setModelLabels(LABELS)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(tfod)
                .build();

        aud_motor1 = hardwareMap.get(DcMotor.class, "motor1");
        aud_motor2 = hardwareMap.get(DcMotor.class, "motor2");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        al_motor = hardwareMap.get(DcMotor.class, "len_motor");

        agtgt_servo1 = hardwareMap.get(Servo.class, "agtgt_servo1");
        agtgt_servo2 = hardwareMap.get(Servo.class, "agtgt_servo2");
        afl_servo = hardwareMap.get(Servo.class, "afl_servo");
        afr_servo = hardwareMap.get(Servo.class, "afr_servo");
        fly_servo = hardwareMap.get(Servo.class, "fly_servo");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        aud_motor1.setDirection(DcMotor.Direction.REVERSE);
        aud_motor2.setDirection(DcMotor.Direction.FORWARD);
        al_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        agtgt_servo1.setDirection(Servo.Direction.FORWARD);
        agtgt_servo2.setDirection(Servo.Direction.REVERSE);
        afl_servo.setDirection(Servo.Direction.FORWARD);
        afr_servo.setDirection(Servo.Direction.REVERSE);

        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aud_motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aud_motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        al_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        aud_motor1.setTargetPosition(aud_motor1.getCurrentPosition());
        aud_motor2.setTargetPosition(aud_motor2.getCurrentPosition());
        al_motor.setTargetPosition(al_motor.getCurrentPosition());
        rightFront.setTargetPosition(rightFront.getCurrentPosition());
        rightRear.setTargetPosition(rightRear.getCurrentPosition());
        leftFront.setTargetPosition(leftFront.getCurrentPosition());
        leftRear.setTargetPosition(leftRear.getCurrentPosition());

        aud_motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        aud_motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        al_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        aud_motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        aud_motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        al_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(startpose);
        Trajec = drive.trajectoryBuilder(new Pose2d())
                .lineToSplineHeading(startpose)
                .build();

        fly_servo.setPosition(0.55);


        afldo = true;
        afrdo = true;
        armmove(0,0,0.0395);
        RobotRun();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        resetRuntime();

        if(!opModeIsActive()) return;

        //////////////////////////////////////////////////
        // Start Hear
        //    |
        //    \/
        //////////////////////////////////////////////////


        //ID check
        findID = ObjectCheck();
        visionPortal.setProcessorEnabled(tfod, false);
        armmove(100,0,0.0395);
        armmove(100,0,0.6);
        sleep(100);

        gotoPosition(24,-49,-90);

        //goto TargetPostition
        if (findID ==  1)      gotoPosition(18  ,-33,0);
        else if (findID ==  2) gotoPosition(12,-36.5,-90);
        else                   gotoPosition(40,-33,0);

        //pixel drop
        afldo = false;
        RobotRun();
        sleep(100);
        armmove(100,0,0.0395);

        RobotRun();

        //goto backboard
        if (findID == 1) gotoPosition(55,-24,0);
        else if (findID ==  2)    gotoPosition(55,-30,0);
        else                      gotoPosition(55,-36,0);

        sleep(100);
        //backdrop pixel
        armmove(1250,0,0);
        sleep(100);
        afrdo = false;
        RobotRun();
        RobotRun();
        RobotRun();
        sleep(1000);
        armmove(100,0,0.0395);

        //parking
        gotoPosition(50,-62,0);
        armmove(0,0,0.0395);
    }

    public static void RobotRun() {
        if (!afldo) afl_servo.setPosition(0);
        else afl_servo.setPosition(0.12);
        if (!afrdo) afr_servo.setPosition(0);
        else afr_servo.setPosition(0.12);
        if (al_tick > al_max) al_tick=al_max;
        if (al_tick < al_min) al_tick=al_min;
        if (aud_tick < 0) aud_tick = 0;
        if (aud_tick > 1250) aud_tick = 1250;
        if (agtgt_tick > 1) agtgt_tick = 1;
        if (agtgt_tick < 0.0395) agtgt_tick = 0.0395;
        aud_motor1.setTargetPosition(aud_tick);
        aud_motor2.setTargetPosition(aud_tick);
        al_motor.setTargetPosition(al_tick);
        aud_motor1.setPower(1);
        aud_motor2.setPower(1);
        al_motor.setPower(1);
        agtgt_servo1.setPosition(agtgt_tick);
        agtgt_servo2.setPosition(agtgt_tick-0.0395);
    }


    public int ObjectCheck() {
        while(true){
            List<Recognition> currentRecognitions = tfod.getRecognitions();
            sleep(100);
            for (Recognition recognition : currentRecognitions) {
                double x = (recognition.getLeft() + recognition.getRight()) / 2;
                if (((recognition.getLabel().equals("RED_TES") && isBlue == 1)
                        || (recognition.getLabel().equals("BlUE_TES") && isBlue == -1))
                        && recognition.getConfidence() >= 0.8) {
                    if (x <= 150) {
                        return 2 - isBlue;
                    } else if (150 < x && x < 450) {
                        return 2;
                    } else if (450 <= x) {
                        return 2 + isBlue;
                    }
                }
            }
        }
    }


    public void armmove(int aud, int al, double agtgt){
        agtgt_tick = agtgt;
        boolean auto = true;
        while(auto){
            if (aud_tick > aud) aud_tick -= aud_speed;
            else if (aud_tick < aud) aud_tick += aud_speed;
            if (al_tick > al) al_tick -= al_speed;
            else if (al_tick < al) al_tick += al_speed;
            RobotRun();
            if((aud+6 >= aud_tick && aud_tick >= aud-6) &&
                    (al-20 <= al_tick && al_tick <= al+20)) auto = false;
        }
    }

    public void gotoPosition(double x, double y, double angle){
        Trajec  = drive.trajectoryBuilder(Trajec.end())
                .lineToSplineHeading(new Pose2d(x, y * isBlue, Math.toRadians(angle * isBlue)))
                .build();

        drive.followTrajectory(Trajec);
    }

}

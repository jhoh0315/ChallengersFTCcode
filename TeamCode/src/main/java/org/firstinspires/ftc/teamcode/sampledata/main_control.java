package org.firstinspires.ftc.teamcode.sampledata;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp

public class main_control extends LinearOpMode {

    final double agtgt_speed = 0.006;
    final int aud_speed = 4;
    final int al_speed = 20;
    final int al_min = -1500;
    final int al_max = 0;


    @Override
    public void runOpMode() {
        DcMotor aud_motor1 = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor aud_motor2 = hardwareMap.get(DcMotor.class, "motor2");
        DcMotor rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        DcMotor leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        DcMotor al_motor = hardwareMap.get(DcMotor.class, "len_motor");

        Servo agtgt_servo1 = hardwareMap.get(Servo.class, "agtgt_servo1");
        Servo agtgt_servo2 = hardwareMap.get(Servo.class, "agtgt_servo2");
        Servo afl_servo = hardwareMap.get(Servo.class, "afl_servo");
        Servo afr_servo = hardwareMap.get(Servo.class, "afr_servo");
        Servo fly_servo = hardwareMap.get(Servo.class, "fly_servo");


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

        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        aud_motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aud_motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        al_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        aud_motor1.setTargetPosition(aud_motor1.getCurrentPosition());
        aud_motor2.setTargetPosition(aud_motor2.getCurrentPosition());
        al_motor.setTargetPosition(al_motor.getCurrentPosition());

        aud_motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        aud_motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        al_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        aud_motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        aud_motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        al_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        fly_servo.setPosition(0.55);

        double agtgt_tick = 0.0395;
        int aud_tick = aud_motor1.getCurrentPosition();
        int al_tick = al_motor.getCurrentPosition();
        int aud_TargeTtick = 0;
        int al_TargetTick = 0;
        boolean autodo = false;
        boolean aflOnOff = false;
        boolean afrOnOff = false;
        boolean afldo = false;
        boolean afrdo = false;


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
            /*
            double x1_1 = gamepad1.right_stick_x;
            double y1_1 = -gamepad1.left_stick_y;
            double x1_2 = gamepad1.left_stick_x;
            */

            double x1_1 = gamepad1.right_stick_x;
            double y1_1 = -gamepad1.left_stick_y;
            double x1_2 = gamepad1.left_stick_x;

            rightFront.setPower(y1_1-x1_1-(x1_2*0.5));
            rightRear.setPower(y1_1+x1_1-(x1_2*0.5));
            leftFront.setPower(y1_1+x1_1+(x1_2*0.5));
            leftRear.setPower(y1_1-x1_1+(x1_2*0.5));

            if(gamepad1.a){
                fly_servo.setPosition(0.18);
            }
            if(gamepad1.b){
                fly_servo.setPosition(0.55);
            }
            
            if(gamepad2.x){
                aud_TargeTtick = 100;
                al_TargetTick = 0;
                agtgt_tick = 0.6;
                autodo = true;
            }

            if(gamepad2.y){
                aud_TargeTtick = 1250;
                al_TargetTick = -441;
                agtgt_tick = 0;
                autodo = true;
            }

            if(gamepad2.a){
                aud_TargeTtick = 916;
                al_TargetTick = -936;
                autodo = true;
            }


            if(gamepad2.left_stick_y > 0){
                aud_tick-=aud_speed;
            } else if (gamepad2.left_stick_y < 0) {
                aud_tick+=aud_speed;
            }

            if(gamepad2.right_stick_y > 0){
                al_tick+= Math.round(al_speed*gamepad2.right_stick_y);
            } else if (gamepad2.right_stick_y < 0) {
                al_tick-= Math.round(-al_speed*gamepad2.right_stick_y);
            }

            if(gamepad2.dpad_up){
                agtgt_tick -= agtgt_speed;
            } else if (gamepad2.dpad_down) {
                agtgt_tick += agtgt_speed;
            }

            if (gamepad2.right_bumper && !aflOnOff) {
                afldo = !afldo;
                aflOnOff = true;
            }
            if (!gamepad2.right_bumper) aflOnOff = false;
            if (gamepad2.left_bumper && !afrOnOff){
                afrdo = !afrdo;
                afrOnOff=true;
            }
            if (!gamepad2.left_bumper) afrOnOff = false;

            if (autodo) {
                if (aud_tick > aud_TargeTtick) aud_tick -= aud_speed;
                else if (aud_tick < aud_TargeTtick) aud_tick += aud_speed;
                if (al_tick > al_TargetTick) al_tick -= al_speed;
                else if (al_tick < al_TargetTick) al_tick += al_speed;
                if((aud_TargeTtick+6 >= aud_tick && aud_tick >= aud_TargeTtick-6) &&
                        (al_TargetTick-20 <= al_tick && al_tick <= al_TargetTick+20)) autodo = false;
            }

            if (gamepad1.x) {
                rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

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
//            telemetry.addData("encoder",al_motor.getCurrentPosition());

            telemetry.addData("r1_tick",rightFront.getCurrentPosition());
            telemetry.addData("r2_tick",rightRear.getCurrentPosition());
            telemetry.addData("l1_tick",leftFront.getCurrentPosition());
            telemetry.addData("l2_tick",leftRear.getCurrentPosition());
            telemetry.addData("agtgt_tick",agtgt_tick);
            telemetry.addData("al_tick",al_tick);
            telemetry.addData("aud_tick",aud_tick);
            telemetry.addData("fly_tick",fly_servo.getPosition());

            telemetry.update();
        }
    }
}

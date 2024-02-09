package org.firstinspires.ftc.teamcode.sampledata;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp

public class main_control extends LinearOpMode {

    final double agtgt_speed = 0.003;
    final int aud_speed = 3;
    final int al_speed = 10;
    final int al_min = -2500;
    final int al_max = 0;


    @Override
    public void runOpMode() {
        DcMotor aud_motor1 = hardwareMap.get(DcMotor.class, "motor1");
        DcMotor aud_motor2 = hardwareMap.get(DcMotor.class, "motor2");
        DcMotor r1_motor = hardwareMap.get(DcMotor.class, "r1_motor");
        DcMotor r2_motor = hardwareMap.get(DcMotor.class, "r2_motor");
        DcMotor l1_motor = hardwareMap.get(DcMotor.class, "l1_motor");
        DcMotor l2_motor = hardwareMap.get(DcMotor.class, "l2_motor");
        DcMotor al_motor = hardwareMap.get(DcMotor.class, "len_motor");

        Servo agtgt_servo1 = hardwareMap.get(Servo.class, "agtgt_servo1");
        Servo agtgt_servo2 = hardwareMap.get(Servo.class, "agtgt_servo2");
        Servo afl_servo = hardwareMap.get(Servo.class, "afl_servo");
        Servo afr_servo = hardwareMap.get(Servo.class, "afr_servo");
        Servo fly_servo = hardwareMap.get(Servo.class, "fly_servo");


        r1_motor.setDirection(DcMotor.Direction.REVERSE);
        r2_motor.setDirection(DcMotor.Direction.REVERSE);
        l1_motor.setDirection(DcMotor.Direction.FORWARD);
        l2_motor.setDirection(DcMotor.Direction.FORWARD);
        aud_motor1.setDirection(DcMotor.Direction.REVERSE);
        aud_motor2.setDirection(DcMotor.Direction.FORWARD);
        al_motor.setDirection(DcMotorSimple.Direction.FORWARD);
        agtgt_servo1.setDirection(Servo.Direction.FORWARD);
        agtgt_servo2.setDirection(Servo.Direction.REVERSE);
        afl_servo.setDirection(Servo.Direction.FORWARD);
        afr_servo.setDirection(Servo.Direction.REVERSE);

        r1_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        r2_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        l1_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        l2_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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


        fly_servo.setPosition(1);

        double agtgt_tick = 0.45;
        int aud_tick = aud_motor1.getCurrentPosition();
        int al_tick = al_motor.getCurrentPosition();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while (opModeIsActive()) {
//            double x1_1 = gamepad1.right_stick_x; //x1_1
//            double y1_1 = -gamepad1.left_stick_y;  //y1_1
            double x1_2 = gamepad1.left_stick_x;   //yx1_2


            double x1_1 = -gamepad1.left_stick_y; //x1_1
            double y1_1 = ;  //y1_1

            r1_motor.setPower(0.5*(y1_1-x1_1-x1_2));
            r2_motor.setPower(0.5*(y1_1+x1_1-x1_2));
            l1_motor.setPower(0.5*(y1_1+x1_1+x1_2));
            l2_motor.setPower(0.5*(y1_1-x1_1+x1_2));

            if(gamepad1.a){
                fly_servo.setPosition(0);
            }



            if(gamepad2.left_stick_y > 0){
                aud_tick+=aud_speed;
            } else if (gamepad2.left_stick_y < 0) {
                aud_tick-=aud_speed;
            }

            if(gamepad2.right_stick_y > 0){
                al_tick+= Math.round(al_speed*gamepad2.right_stick_y);
            } else if (gamepad2.right_stick_y < 0) {
                al_tick-= Math.round(-al_speed*gamepad2.right_stick_y);
            }

            if(al_tick > al_max){
                al_tick=al_max;
            } else if (al_tick < al_min) {
                al_tick=al_min;
            }
            aud_motor1.setTargetPosition(aud_tick);
            aud_motor2.setTargetPosition(aud_tick);
            al_motor.setTargetPosition(al_tick);
            aud_motor1.setPower(1);
            aud_motor2.setPower(1);
            al_motor.setPower(1);


            if(gamepad2.dpad_up){
                agtgt_tick += agtgt_speed;
                if (agtgt_tick > 1) agtgt_tick = 1;
            } else if (gamepad2.dpad_down) {
                agtgt_tick -= agtgt_speed;
                if (agtgt_tick < 0.0395) agtgt_tick = 0;
            }

            if (gamepad2.left_bumper){
                afl_servo.setPosition(0.12);
            } else if (gamepad2.left_trigger > 0) {
                afl_servo.setPosition(0);
            }

            if (gamepad2.right_bumper){
                afr_servo.setPosition(0.12);
            } else if (gamepad2.right_trigger > 0) {
                afr_servo.setPosition(0);
            }

            if (gamepad2.a){
                al_motor.setTargetPosition(0);
                while(al_motor.getCurrentPosition()!=0){
                    al_motor.setPower(1);
                }

            }

            agtgt_servo1.setPosition(agtgt_tick);
            agtgt_servo2.setPosition(agtgt_tick-0.0395);
//            telemetry.addData("encoder",al_motor.getCurrentPosition());
//            telemetry.addData("al_tick",al_tick);
//            telemetry.addData("aud_tick",aud_tick);

            telemetry.update();
        }
    }
}

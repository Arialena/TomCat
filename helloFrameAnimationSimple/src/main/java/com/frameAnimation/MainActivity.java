package com.frameAnimation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import kr.pe.burt.android.lib.faimageview.FAImageView;


public class MainActivity extends Activity {

    private static MediaPlayer mMediaPlayer;
    private static boolean isPause = false;

    private static final String LOG_TAG = "" ;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private File fpath;

    private int BASE = 600;
    private int SPACE = 2000;// 间隔取样时间
    private int db = 0;

    private Thread dbThread;


    private Context context;
    private FAImageView faImageView;
    private AnimationDrawable anim;
    private MediaPlayer mediaPlayer;
    private int what;


    private final Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
             what=msg.what;
            //根据mHandler发送what的大小决定话筒的图片是哪一张
            //说话声音越大,发送过来what值越大
            Log.d("===>", "db = " + db);
            if(what < 30 && db > 0){
                playSound(audioFile.getAbsolutePath());

            }
        };
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main_debug);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        context = this;
        faImageView = (FAImageView) findViewById( R.id.imageView);
        mediaPlayer = new MediaPlayer();

        fpath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/data/files/");

        // fpath.mkdirs();// 创建文件夹
        try {
// 创建临时文件,注意这里的格式为.pcm
            audioFile = File.createTempFile("recording", ".pcm", fpath);
        } catch (IOException e) {
//         TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true){
            if(mediaRecorder == null) {

                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setOutputFile(String.valueOf(audioFile));
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                try {
                    mediaRecorder.prepare();
                    if (what > 30 && db > 0){
                        if(faImageView.isAnimating()){

                            mediaRecorder.start();
                            faImageView.stopAnimation();
                        }
                        faImageView.clearAnimation();
                        faImageView.reset();
                        faImageView.setInterval(200);//设置间隔时间
                        faImageView.setLoop(false);
                        faImageView.addImageFrame(R.mipmap.cat_listen0000);
                        faImageView.addImageFrame(R.mipmap.cat_listen0001);
                        faImageView.addImageFrame(R.mipmap.cat_listen0002);
                        faImageView.addImageFrame(R.mipmap.cat_listen0003);
                        faImageView.addImageFrame(R.mipmap.cat_listen0004);
                        faImageView.addImageFrame(R.mipmap.cat_listen0005);
                        faImageView.addImageFrame(R.mipmap.cat_listen0006);
                        faImageView.addImageFrame(R.mipmap.cat_listen0007);
                        faImageView.addImageFrame(R.mipmap.cat_listen0008);
                        faImageView.addImageFrame(R.mipmap.cat_listen0009);
                        faImageView.addImageFrame(R.mipmap.cat_listen0010);
                        faImageView.addImageFrame(R.mipmap.cat_listen0011);

                        faImageView.startAnimation();
                    }

                    Log.d("===>", "开始录音");
                    dbThread = new Thread() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    sleep(SPACE);
                                } catch (InterruptedException e) {

                                }
                                updateMicStatus();
                            }
                        }
                    };
                    dbThread.start();

                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }}
    }

    public void stopRecord() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    //播放
    public  void playSound(String filePath) {

        Log.d("===>", "停止录音，开始播放");
        stopRecord();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateMicStatus() {
        if(mediaRecorder != null){
            int ratio = mediaRecorder.getMaxAmplitude() / 100; //BASE;
            Log.d("===>", "ratio：" + ratio);
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            //我对着手机说话声音最大的时候，db达到了35左右，
            Log.d("===>", "取出分贝值：" + db);
            Message message = new Message();
            message.what = db;
            mHandler.sendMessage(message);
        }
    }

    /*头晕*/
    public void playangryAnimationTou(View v){
        getPlay(R.raw.fall);
    }

    /*左脸*/
    public void playangryAnimationLiftLian(View v){
        getPlay(R.raw.fall);
    }

    /*右脸*/
    public void playangryAnimationRightLian(View v){
        getPlay(R.raw.fall);
    }

    /*头晕动作*/
    public void getPlay(int musicID){
        if(faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(200);//设置间隔时间
        faImageView.setLoop(false);
        faImageView.addImageFrame(R.mipmap.cat_knockout0000);
        faImageView.addImageFrame(R.mipmap.cat_knockout0002);
        faImageView.addImageFrame(R.mipmap.cat_knockout0004);
        faImageView.addImageFrame(R.mipmap.cat_knockout0006);
        faImageView.addImageFrame(R.mipmap.cat_knockout0008);
        faImageView.addImageFrame(R.mipmap.cat_knockout0010);
        faImageView.addImageFrame(R.mipmap.cat_knockout0012);
        faImageView.addImageFrame(R.mipmap.cat_knockout0014);
        faImageView.addImageFrame(R.mipmap.cat_knockout0016);
        faImageView.addImageFrame(R.mipmap.cat_knockout0018);
        faImageView.addImageFrame(R.mipmap.cat_knockout0020);
        faImageView.addImageFrame(R.mipmap.cat_knockout0022);
        faImageView.addImageFrame(R.mipmap.cat_knockout0024);
        faImageView.addImageFrame(R.mipmap.cat_knockout0026);
        faImageView.addImageFrame(R.mipmap.cat_knockout0028);
        faImageView.addImageFrame(R.mipmap.cat_knockout0030);
        faImageView.addImageFrame(R.mipmap.cat_knockout0032);
        faImageView.addImageFrame(R.mipmap.cat_knockout0034);
        faImageView.addImageFrame(R.mipmap.cat_knockout0036);
        faImageView.addImageFrame(R.mipmap.cat_knockout0038);
        faImageView.addImageFrame(R.mipmap.cat_knockout0040);
        faImageView.addImageFrame(R.mipmap.cat_knockout0042);
        faImageView.addImageFrame(R.mipmap.cat_knockout0044);
        faImageView.addImageFrame(R.mipmap.cat_knockout0046);
        faImageView.addImageFrame(R.mipmap.cat_knockout0048);
        faImageView.addImageFrame(R.mipmap.cat_knockout0050);
        faImageView.addImageFrame(R.mipmap.cat_knockout0052);
        faImageView.addImageFrame(R.mipmap.cat_knockout0054);
        faImageView.addImageFrame(R.mipmap.cat_knockout0056);
        faImageView.addImageFrame(R.mipmap.cat_knockout0058);
        faImageView.addImageFrame(R.mipmap.cat_knockout0060);
        faImageView.addImageFrame(R.mipmap.cat_knockout0062);
        faImageView.addImageFrame(R.mipmap.cat_knockout0064);
        faImageView.addImageFrame(R.mipmap.cat_knockout0066);
        faImageView.addImageFrame(R.mipmap.cat_knockout0068);
        faImageView.addImageFrame(R.mipmap.cat_knockout0070);
        faImageView.addImageFrame(R.mipmap.cat_knockout0072);
        faImageView.addImageFrame(R.mipmap.cat_knockout0074);
        faImageView.addImageFrame(R.mipmap.cat_knockout0076);
        faImageView.addImageFrame(R.mipmap.cat_knockout0078);
        faImageView.addImageFrame(R.mipmap.cat_knockout0080);

        faImageView.startAnimation();


//        mediaPlayer.stop();
//        mediaPlayer.reset();
//        mediaPlayer.setDataSource();
//        try {
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        }catch (IOException e){
//
//        }

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.slap5);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.p_stars2s);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        },2000);
    }

    /*打喷嚏*/
    public void playangryAnimationBiZi(View v){
        if(faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(120);//设置间隔时间
        faImageView.setLoop(false);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0000);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0001);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0002);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0003);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0004);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0005);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0006);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0007);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0008);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0009);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0010);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0011);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0012);
        faImageView.addImageFrame(R.mipmap.cat_sneeze0013);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this, R.raw.p_sneeze);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*嘴巴*/
    public void playangryAnimationZuiBa(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(100);
        faImageView.setLoop(false);
        faImageView.addImageFrame(R.mipmap.cat_zeh0000);
        faImageView.addImageFrame(R.mipmap.cat_zeh0001);
        faImageView.addImageFrame(R.mipmap.cat_zeh0002);
        faImageView.addImageFrame(R.mipmap.cat_zeh0003);
        faImageView.addImageFrame(R.mipmap.cat_zeh0004);
        faImageView.addImageFrame(R.mipmap.cat_zeh0005);
        faImageView.addImageFrame(R.mipmap.cat_zeh0006);
        faImageView.addImageFrame(R.mipmap.cat_zeh0007);
        faImageView.addImageFrame(R.mipmap.cat_zeh0008);
        faImageView.addImageFrame(R.mipmap.cat_zeh0009);
        faImageView.addImageFrame(R.mipmap.cat_zeh0010);
        faImageView.addImageFrame(R.mipmap.cat_zeh0011);
        faImageView.addImageFrame(R.mipmap.cat_zeh0012);
        faImageView.addImageFrame(R.mipmap.cat_zeh0013);
        faImageView.addImageFrame(R.mipmap.cat_zeh0014);
        faImageView.addImageFrame(R.mipmap.cat_zeh0015);
        faImageView.addImageFrame(R.mipmap.cat_zeh0016);
        faImageView.addImageFrame(R.mipmap.cat_zeh0017);
        faImageView.addImageFrame(R.mipmap.cat_zeh0018);
        faImageView.addImageFrame(R.mipmap.cat_zeh0019);
        faImageView.addImageFrame(R.mipmap.cat_zeh0020);
        faImageView.addImageFrame(R.mipmap.cat_zeh0021);
        faImageView.addImageFrame(R.mipmap.cat_zeh0022);
        faImageView.addImageFrame(R.mipmap.cat_zeh0023);
        faImageView.addImageFrame(R.mipmap.cat_zeh0024);
        faImageView.addImageFrame(R.mipmap.cat_zeh0025);
        faImageView.addImageFrame(R.mipmap.cat_zeh0026);
        faImageView.addImageFrame(R.mipmap.cat_zeh0027);
        faImageView.addImageFrame(R.mipmap.cat_zeh0028);
        faImageView.addImageFrame(R.mipmap.cat_zeh0029);
        faImageView.addImageFrame(R.mipmap.cat_zeh0030);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this, R.raw.p_yawn2_11a);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*肚子疼*/
    public void playangryAnimationDuZi(View v){
        if(faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(180);//设置间隔时间
        faImageView.setLoop(false);
        faImageView.addImageFrame(R.mipmap.cat_stomach0000);
        faImageView.addImageFrame(R.mipmap.cat_stomach0001);
        faImageView.addImageFrame(R.mipmap.cat_stomach0002);
        faImageView.addImageFrame(R.mipmap.cat_stomach0003);
        faImageView.addImageFrame(R.mipmap.cat_stomach0004);
        faImageView.addImageFrame(R.mipmap.cat_stomach0005);
        faImageView.addImageFrame(R.mipmap.cat_stomach0006);
        faImageView.addImageFrame(R.mipmap.cat_stomach0007);
        faImageView.addImageFrame(R.mipmap.cat_stomach0008);
        faImageView.addImageFrame(R.mipmap.cat_stomach0009);
        faImageView.addImageFrame(R.mipmap.cat_stomach0010);
        faImageView.addImageFrame(R.mipmap.cat_stomach0011);
        faImageView.addImageFrame(R.mipmap.cat_stomach0012);
        faImageView.addImageFrame(R.mipmap.cat_stomach0013);
        faImageView.addImageFrame(R.mipmap.cat_stomach0014);
        faImageView.addImageFrame(R.mipmap.cat_stomach0015);
        faImageView.addImageFrame(R.mipmap.cat_stomach0016);
        faImageView.addImageFrame(R.mipmap.cat_stomach0017);
        faImageView.addImageFrame(R.mipmap.cat_stomach0018);
        faImageView.addImageFrame(R.mipmap.cat_stomach0019);
        faImageView.addImageFrame(R.mipmap.cat_stomach0020);
        faImageView.addImageFrame(R.mipmap.cat_stomach0021);
        faImageView.addImageFrame(R.mipmap.cat_stomach0022);
        faImageView.addImageFrame(R.mipmap.cat_stomach0023);
        faImageView.addImageFrame(R.mipmap.cat_stomach0024);
        faImageView.addImageFrame(R.mipmap.cat_stomach0025);
        faImageView.addImageFrame(R.mipmap.cat_stomach0026);
        faImageView.addImageFrame(R.mipmap.cat_stomach0027);
        faImageView.addImageFrame(R.mipmap.cat_stomach0028);
        faImageView.addImageFrame(R.mipmap.cat_stomach0029);
        faImageView.addImageFrame(R.mipmap.cat_stomach0030);
        faImageView.addImageFrame(R.mipmap.cat_stomach0031);
        faImageView.addImageFrame(R.mipmap.cat_stomach0032);
        faImageView.addImageFrame(R.mipmap.cat_stomach0033);

        faImageView.startAnimation();
        mediaPlayer = MediaPlayer.create(this, R.raw.p_noo);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

    }


    /*左脚动作*/
    public void playangryAnimationFootRight(View v) {
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(120);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame( R.mipmap.cat_foot_left0000);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0001);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0004);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0005);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0006);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0008);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0009);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0010);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0012);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0013);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0014);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0015);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0016);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0018);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0020);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0021);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0022);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0024);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0025);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0026);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0028);
        faImageView.addImageFrame( R.mipmap.cat_foot_left0029);
        //start animation
        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.p_foot3);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }


    /*右脚动作*/
    public void playangryAnimationFootLeft(View v) {
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(120);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame( R.mipmap.cat_foot_right0000);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0001);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0004);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0005);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0006);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0008);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0009);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0010);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0012);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0013);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0014);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0015);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0016);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0018);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0020);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0021);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0022);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0024);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0025);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0026);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0028);
        faImageView.addImageFrame( R.mipmap.cat_foot_right0029);
        //start animation
        faImageView.startAnimation();
        mediaPlayer = MediaPlayer.create(this,R.raw.p_foot4);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*尾巴*/
    public void playangryAnimationdrinWeiBa(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(180);
        faImageView.setLoop(false);
        faImageView.addImageFrame(R.mipmap.cat_angry0000);
        faImageView.addImageFrame(R.mipmap.cat_angry0001);
        faImageView.addImageFrame(R.mipmap.cat_angry0002);
        faImageView.addImageFrame(R.mipmap.cat_angry0003);
        faImageView.addImageFrame(R.mipmap.cat_angry0004);
        faImageView.addImageFrame(R.mipmap.cat_angry0005);
        faImageView.addImageFrame(R.mipmap.cat_angry0006);
        faImageView.addImageFrame(R.mipmap.cat_angry0007);
        faImageView.addImageFrame(R.mipmap.cat_angry0008);
        faImageView.addImageFrame(R.mipmap.cat_angry0009);
        faImageView.addImageFrame(R.mipmap.cat_angry0010);
        faImageView.addImageFrame(R.mipmap.cat_angry0011);
        faImageView.addImageFrame(R.mipmap.cat_angry0012);
        faImageView.addImageFrame(R.mipmap.cat_angry0013);
        faImageView.addImageFrame(R.mipmap.cat_angry0014);
        faImageView.addImageFrame(R.mipmap.cat_angry0015);
        faImageView.addImageFrame(R.mipmap.cat_angry0016);
        faImageView.addImageFrame(R.mipmap.cat_angry0017);
        faImageView.addImageFrame(R.mipmap.cat_angry0018);
        faImageView.addImageFrame(R.mipmap.cat_angry0019);
        faImageView.addImageFrame(R.mipmap.cat_angry0020);
        faImageView.addImageFrame(R.mipmap.cat_angry0021);
        faImageView.addImageFrame(R.mipmap.cat_angry0022);
        faImageView.addImageFrame(R.mipmap.cat_angry0023);
        faImageView.addImageFrame(R.mipmap.cat_angry0024);
        faImageView.addImageFrame(R.mipmap.cat_angry0025);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.angry);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*喝牛奶动作*/
    public void playangryAnimationdrinkmilk(View v) {
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(200);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.cat_drink0000);
        faImageView.addImageFrame(R.mipmap.cat_drink0002);
        faImageView.addImageFrame(R.mipmap.cat_drink0004);
        faImageView.addImageFrame(R.mipmap.cat_drink0006);
        faImageView.addImageFrame(R.mipmap.cat_drink0008);
        faImageView.addImageFrame(R.mipmap.cat_drink0010);
        faImageView.addImageFrame(R.mipmap.cat_drink0012);
        faImageView.addImageFrame(R.mipmap.cat_drink0014);
        faImageView.addImageFrame(R.mipmap.cat_drink0016);
        faImageView.addImageFrame(R.mipmap.cat_drink0018);
        faImageView.addImageFrame(R.mipmap.cat_drink0020);
        faImageView.addImageFrame(R.mipmap.cat_drink0022);
        faImageView.addImageFrame(R.mipmap.cat_drink0024);
        faImageView.addImageFrame(R.mipmap.cat_drink0026);
        faImageView.addImageFrame(R.mipmap.cat_drink0028);
        faImageView.addImageFrame(R.mipmap.cat_drink0030);
        faImageView.addImageFrame(R.mipmap.cat_drink0032);
        faImageView.addImageFrame(R.mipmap.cat_drink0034);
        faImageView.addImageFrame(R.mipmap.cat_drink0036);
        faImageView.addImageFrame(R.mipmap.cat_drink0038);
        faImageView.addImageFrame(R.mipmap.cat_drink0040);
        faImageView.addImageFrame(R.mipmap.cat_drink0042);
        faImageView.addImageFrame(R.mipmap.cat_drink0044);
        faImageView.addImageFrame(R.mipmap.cat_drink0046);
        faImageView.addImageFrame(R.mipmap.cat_drink0048);
        faImageView.addImageFrame(R.mipmap.cat_drink0050);
        faImageView.addImageFrame(R.mipmap.cat_drink0052);
        faImageView.addImageFrame(R.mipmap.cat_drink0054);
        faImageView.addImageFrame(R.mipmap.cat_drink0056);
        faImageView.addImageFrame(R.mipmap.cat_drink0058);
        faImageView.addImageFrame(R.mipmap.cat_drink0060);
        faImageView.addImageFrame(R.mipmap.cat_drink0062);
        faImageView.addImageFrame(R.mipmap.cat_drink0064);
        faImageView.addImageFrame(R.mipmap.cat_drink0066);
        faImageView.addImageFrame(R.mipmap.cat_drink0068);
        faImageView.addImageFrame(R.mipmap.cat_drink0070);
        faImageView.addImageFrame(R.mipmap.cat_drink0072);
        faImageView.addImageFrame(R.mipmap.cat_drink0074);
        faImageView.addImageFrame(R.mipmap.cat_drink0076);
        faImageView.addImageFrame(R.mipmap.cat_drink0078);
        faImageView.addImageFrame(R.mipmap.cat_drink0080);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.p_drink_milk);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*放屁*/
    public void playangryAnimationdFangPin(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(160);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.cat_fart0000);
        faImageView.addImageFrame(R.mipmap.cat_fart0001);
        faImageView.addImageFrame(R.mipmap.cat_fart0002);
        faImageView.addImageFrame(R.mipmap.cat_fart0003);
        faImageView.addImageFrame(R.mipmap.cat_fart0004);
        faImageView.addImageFrame(R.mipmap.cat_fart0005);
        faImageView.addImageFrame(R.mipmap.cat_fart0006);
        faImageView.addImageFrame(R.mipmap.cat_fart0007);
        faImageView.addImageFrame(R.mipmap.cat_fart0008);
        faImageView.addImageFrame(R.mipmap.cat_fart0009);
        faImageView.addImageFrame(R.mipmap.cat_fart0010);
        faImageView.addImageFrame(R.mipmap.cat_fart0011);
        faImageView.addImageFrame(R.mipmap.cat_fart0012);
        faImageView.addImageFrame(R.mipmap.cat_fart0013);
        faImageView.addImageFrame(R.mipmap.cat_fart0014);
        faImageView.addImageFrame(R.mipmap.cat_fart0015);
        faImageView.addImageFrame(R.mipmap.cat_fart0016);
        faImageView.addImageFrame(R.mipmap.cat_fart0017);
        faImageView.addImageFrame(R.mipmap.cat_fart0018);
        faImageView.addImageFrame(R.mipmap.cat_fart0019);
        faImageView.addImageFrame(R.mipmap.cat_fart0020);
        faImageView.addImageFrame(R.mipmap.cat_fart0021);
        faImageView.addImageFrame(R.mipmap.cat_fart0022);
        faImageView.addImageFrame(R.mipmap.cat_fart0023);
        faImageView.addImageFrame(R.mipmap.cat_fart0024);
        faImageView.addImageFrame(R.mipmap.cat_fart0025);
        faImageView.addImageFrame(R.mipmap.cat_fart0026);
        faImageView.addImageFrame(R.mipmap.cat_fart0027);

        faImageView.startAnimation();
        mediaPlayer = MediaPlayer.create(this, R.raw.fart001);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

    }

    /*扔东西*/

    public void playangryAnimationdRengDongXi(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(140);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.pie0000);
        faImageView.addImageFrame(R.mipmap.pie0001);
        faImageView.addImageFrame(R.mipmap.pie0002);
        faImageView.addImageFrame(R.mipmap.pie0003);
        faImageView.addImageFrame(R.mipmap.pie0004);
        faImageView.addImageFrame(R.mipmap.pie0005);
        faImageView.addImageFrame(R.mipmap.pie0006);
        faImageView.addImageFrame(R.mipmap.pie0007);
        faImageView.addImageFrame(R.mipmap.pie0008);
        faImageView.addImageFrame(R.mipmap.pie0009);
        faImageView.addImageFrame(R.mipmap.pie0010);
        faImageView.addImageFrame(R.mipmap.pie0011);
        faImageView.addImageFrame(R.mipmap.pie0012);
        faImageView.addImageFrame(R.mipmap.pie0013);
        faImageView.addImageFrame(R.mipmap.pie0014);
        faImageView.addImageFrame(R.mipmap.pie0015);
        faImageView.addImageFrame(R.mipmap.pie0016);
        faImageView.addImageFrame(R.mipmap.pie0017);
        faImageView.addImageFrame(R.mipmap.pie0018);
        faImageView.addImageFrame(R.mipmap.pie0019);
        faImageView.addImageFrame(R.mipmap.pie0020);
        faImageView.addImageFrame(R.mipmap.pie0021);
        faImageView.addImageFrame(R.mipmap.pie0022);

        faImageView.startAnimation();
        mediaPlayer = MediaPlayer.create(this, R.raw.fall);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

    }

    /*打锣*/
    public void playangryAnimationdDaLuo(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(90);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.cat_cymbal0000);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0001);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0002);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0003);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0004);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0005);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0006);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0007);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0008);
        faImageView.addImageFrame(R.mipmap.cat_cymbal0009);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.cymbal);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*吃东西*/
    public void playangryAnimationdEat(View v){
        if (faImageView.isAnimating()){
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(200);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.cat_eat0000);
        faImageView.addImageFrame(R.mipmap.cat_eat0001);
        faImageView.addImageFrame(R.mipmap.cat_eat0002);
        faImageView.addImageFrame(R.mipmap.cat_eat0005);
        faImageView.addImageFrame(R.mipmap.cat_eat0006);
        faImageView.addImageFrame(R.mipmap.cat_eat0007);
        faImageView.addImageFrame(R.mipmap.cat_eat0009);
        faImageView.addImageFrame(R.mipmap.cat_eat0010);
        faImageView.addImageFrame(R.mipmap.cat_eat0011);
        faImageView.addImageFrame(R.mipmap.cat_eat0014);
        faImageView.addImageFrame(R.mipmap.cat_eat0015);
        faImageView.addImageFrame(R.mipmap.cat_eat0016);
        faImageView.addImageFrame(R.mipmap.cat_eat0019);
        faImageView.addImageFrame(R.mipmap.cat_eat0020);
        faImageView.addImageFrame(R.mipmap.cat_eat0021);
        faImageView.addImageFrame(R.mipmap.cat_eat0024);
        faImageView.addImageFrame(R.mipmap.cat_eat0025);
        faImageView.addImageFrame(R.mipmap.cat_eat0026);
        faImageView.addImageFrame(R.mipmap.cat_eat0029);
        faImageView.addImageFrame(R.mipmap.cat_eat0030);
        faImageView.addImageFrame(R.mipmap.cat_eat0031);
        faImageView.addImageFrame(R.mipmap.cat_eat0034);
        faImageView.addImageFrame(R.mipmap.cat_eat0035);
        faImageView.addImageFrame(R.mipmap.cat_eat0036);
        faImageView.addImageFrame(R.mipmap.cat_eat0038);
        faImageView.addImageFrame(R.mipmap.cat_eat0039);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.p_eat);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /*划屏幕*/
    public void playangryAnimationdHua(View v) {
        if (faImageView.isAnimating()) {
            faImageView.stopAnimation();
        }
        faImageView.clearAnimation();
        faImageView.reset();
        faImageView.setInterval(200);//设置间隔时间
        faImageView.setLoop(false);//设置是否循环播放动画
        faImageView.addImageFrame(R.mipmap.cat_scratch0000);
        faImageView.addImageFrame(R.mipmap.cat_scratch0002);
        faImageView.addImageFrame(R.mipmap.cat_scratch0004);
        faImageView.addImageFrame(R.mipmap.cat_scratch0006);
        faImageView.addImageFrame(R.mipmap.cat_scratch0008);
        faImageView.addImageFrame(R.mipmap.cat_scratch0010);
        faImageView.addImageFrame(R.mipmap.cat_scratch0012);
        faImageView.addImageFrame(R.mipmap.cat_scratch0014);
        faImageView.addImageFrame(R.mipmap.cat_scratch0016);
        faImageView.addImageFrame(R.mipmap.cat_scratch0018);
        faImageView.addImageFrame(R.mipmap.cat_scratch0020);
        faImageView.addImageFrame(R.mipmap.cat_scratch0022);
        faImageView.addImageFrame(R.mipmap.cat_scratch0024);
        faImageView.addImageFrame(R.mipmap.cat_scratch0026);
        faImageView.addImageFrame(R.mipmap.cat_scratch0028);
        faImageView.addImageFrame(R.mipmap.cat_scratch0030);
        faImageView.addImageFrame(R.mipmap.cat_scratch0032);
        faImageView.addImageFrame(R.mipmap.cat_scratch0034);
        faImageView.addImageFrame(R.mipmap.cat_scratch0036);
        faImageView.addImageFrame(R.mipmap.cat_scratch0038);
        faImageView.addImageFrame(R.mipmap.cat_scratch0040);
        faImageView.addImageFrame(R.mipmap.cat_scratch0042);
        faImageView.addImageFrame(R.mipmap.cat_scratch0044);
        faImageView.addImageFrame(R.mipmap.cat_scratch0046);
        faImageView.addImageFrame(R.mipmap.cat_scratch0048);
        faImageView.addImageFrame(R.mipmap.cat_scratch0050);
        faImageView.addImageFrame(R.mipmap.cat_scratch0052);
        faImageView.addImageFrame(R.mipmap.cat_scratch0054);
        faImageView.addImageFrame(R.mipmap.cat_scratch0055);

        faImageView.startAnimation();

        mediaPlayer = MediaPlayer.create(this,R.raw.scratch_kratzen);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        faImageView.clearAnimation();
        faImageView.stopAnimation();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        faImageView.clearAnimation();
        faImageView.stopAnimation();
    }

}
package app.ocrlib.com.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.View;

import app.ocrlib.com.R;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/11/20-17:00
 */
public class AnimUtils {
    //人脸的检测
    private static AnimationDrawable faceDetectionAnim;
    //眼睛的检测
    private static AnimationDrawable eyeDetectionAnim;

    /**
     * 人脸检测
     *
     * @param context
     * @param imageView
     */
    public static void startFaceDetection(Context context, View imageView) {

//        imageView.setBackgroundDrawable(faceDetectionAnim);
        if (null != faceDetectionAnim && !faceDetectionAnim.isRunning()) {
            faceDetectionAnim.start();
        }
    }

    public static void stopFaceDetection() {
        if (null != faceDetectionAnim && faceDetectionAnim.isRunning()) {
            faceDetectionAnim.stop();
        }
    }


    /**
     * 眼睛检测
     *
     * @param context
     * @param
     */

    public static void startEyeDetection(Context context, View eyeimageView, View faceimageView) {
        eyeDetectionAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.wbcf_eye_detect_gif);
        faceDetectionAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.wbcf_face_detect_gif);
        eyeimageView.setBackgroundDrawable(eyeDetectionAnim);
        faceimageView.setBackgroundDrawable(faceDetectionAnim);
        faceimageView.setVisibility(View.INVISIBLE);

        if (null != eyeDetectionAnim && !eyeDetectionAnim.isRunning()) {
            eyeDetectionAnim.start();
        }
        eyeMoveAnimation(context, eyeimageView, faceimageView);

    }

    public static void stopEyeDetection() {
        if (null != eyeDetectionAnim && eyeDetectionAnim.isRunning()) {
            eyeDetectionAnim.stop();
        }
    }

    /**
     * 需要为眼睛添加动画 随机动画
     */
    public static void eyeMoveAnimation(final Context context, final View imageView, final View facepicetur_detection_iv) {
        ObjectAnimator objectXAnimator = ObjectAnimator.ofFloat(imageView, "translationX", -50f, 0f, 10f, 50f, 20f, 80f, 70f, 50f, 40f, 20f, 60f, 10f, 20f);
        ObjectAnimator objectYAnimator = ObjectAnimator.ofFloat(imageView, "translationY", -50f, 0f, 10f, -50f, 20f, -80f, -60f, 20f, 40f, 30f, -10f, -20f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectXAnimator).with(objectYAnimator);
        animatorSet.setDuration(1 * 500);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stopEyeDetection();
                imageView.setVisibility(View.GONE);
                facepicetur_detection_iv.setVisibility(View.VISIBLE);
                startFaceDetection(context, facepicetur_detection_iv);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    /**
     * 身份证的扫描
     */
    public static void IdentityCard(float top,float bottom,View scanview) {
        ObjectAnimator objectAnimator=  ObjectAnimator.ofFloat(scanview,"translationY",top,bottom);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setDuration(1*1000);
        objectAnimator.start();
    }

    /**
     * 眼睛坐标随机点
     */
//    public static float eyePointRandom(float point) {
//        float min = 1f;
//        float max = 10f;
//        Random random=new Random();
//
//        float randomFloat =random.nextFloat()  ;
//        float generatedFloat = min + randomFloat * (max - min);
//    }
    public static void playNotece(Context context) {
        try {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.keep_face_in);//重新设置要播放的音频
            mp.start();//开始播放

        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }


    }
}

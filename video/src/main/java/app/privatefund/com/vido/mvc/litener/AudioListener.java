package app.privatefund.com.vido.mvc.litener;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * desc  ${DESC}
 * author wangyongkui  wangyongkui@simuyun.com
 * 日期 2017/5/12-19:25
 */
public class AudioListener {

    private static final String TAG = AudioListener.class.getSimpleName();
    private ICallBack callback;
    private AudioManager mAm;
    private MyOnAudioFocusChangeListener mListener;

    public void setCallback(Context context, ICallBack callback){
        this.callback = callback;

        mAm = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mListener = new MyOnAudioFocusChangeListener();
        int result = mAm.requestAudioFocus(mListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.i(TAG, "requestAudioFocus successfully.");
        } else {
            Log.e(TAG, "requestAudioFocus failed.");
        }
    }

    public void release(){
        if(null != mAm) {
            mAm.abandonAudioFocus(mListener);
            mAm = null;
        }
        if(null != mListener){
            mListener = null;
        }
    }

    private class MyOnAudioFocusChangeListener implements
            AudioManager.OnAudioFocusChangeListener
    {
        public void onAudioFocusChange(int focusChange)
        {
            Log.i(TAG, "focusChange=" + focusChange);
            callback.callback();
        }
    }
    public interface ICallBack {
        void callback();
    }

}

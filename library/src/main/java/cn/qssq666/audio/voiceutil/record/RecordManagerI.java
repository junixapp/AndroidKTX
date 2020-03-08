package cn.qssq666.audio.voiceutil.record;

import java.io.File;
import java.io.IOException;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/22.
 */

public interface RecordManagerI {
    public void startRecord(int stopTime) throws IOException;

    public boolean stopRecord();

    public void setOnTimeSecondChanage(OnTimeSecondChanage onTimeSecondChanage);

    public void setSoundAmplitudeListenr(SoundAmplitudeListenr soundAmplitudeListenr);

    public void setOnTimeOutStopListener(OnTimeOutStopListener onTimeOutStopListener);

    /**
     * 获取内部管理器 通常返回的是 {@link android.media.AudioRecord }或 {@link android.media.MediaRecorder}
     *
     * @return
     */
    public Object getInternAudioRecord();

    public boolean isRecordIng();

    public int getCurrenttime();

    public File getFile();

    public interface SoundAmplitudeListenr {
        public void volume(int volume);
    }

    public interface OnTimeOutStopListener {
        void onStop();
    }

    /**
     * 当前流逝的时间
     */
    public interface OnTimeSecondChanage {
        void onSecondChnage(int duration);
    }

}

package cn.qssq666.audio.voiceutil.record;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/22.
 */

public class RecordFactory {
    public static RecordManagerI getAmrRocrdInstance() {
        return new AmrRecorderManager();
    }

    public static RecordManagerI getAAcRocrdInstance() {
        return new AACMediaRecorderManager();
    }

    public static RecordManagerI getWavRecordInstance() {
        return new WavRecordManager();
    }

    public static RecordManagerI getMp3RecordInstance() {
        return new MP3RecordManager();
    }

    public static RecordManagerI getWavRecordMp3OutInstance() {
        return new Mp3RecordFromWavManager();
    }

    public static void release(RecordManagerI recordManager) {
        if (recordManager != null) {
            if (recordManager.isRecordIng()) {
                recordManager.stopRecord();
                recordManager.setOnTimeOutStopListener(null);
            }
        }

    }
}

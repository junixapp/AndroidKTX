package cn.qssq666.audio.voiceutil.record;

/**
 * Created by luozheng on 2016/7/11.  qssq.space
 */
public interface RecordSate {
     int RECORD_OVER_STOP=3;//录制完成了
     int RECORD_PLAYING=4;//正在播放
     int START=2;//等待就绪
     int REACORDING=1;//录音中
}

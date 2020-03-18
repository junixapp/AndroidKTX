package cn.qssq666.audio.voiceutil.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import java.io.File;
import java.io.IOException;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.qssq666.audio.voiceutil.record.mp3.DataEncodeThread;
import cn.qssq666.audio.voiceutil.record.mp3.PCMFormat;
import cn.qssq666.audio.voiceutil.utils.MediaDirectoryUtils;
import cn.qssq666.audio.voiceutil.utils.VolumeUtil;
import cn.qssq666.audio.AudioManager;

public class MP3RecordManager implements RecordManagerI {
    //=======================AudioRecord Default Settings=======================
    private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    private static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    /**
     * 下面是对此的封装
     * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
     */
    private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings=====================
    private static final int DEFAULT_LAME_MP3_QUALITY = 7;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    private static final int DEFAULT_LAME_IN_CHANNEL = 1;
    /**
     * Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
    private static final int DEFAULT_LAME_MP3_BIT_RATE = 32;

    //==================================================================

    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    private static final int FRAME_COUNT = 160;

    public AudioRecord getInternAudioRecord() {
        return mAudioRecord;
    }

    private AudioRecord mAudioRecord = null;
    private int mBufferSize;
    private short[] mPCMBuffer;
    private DataEncodeThread mEncodeThread;
    private boolean mIsRecording = false;
    private File mRecordFile;

    /**
     * Default constructor. Setup recorder with default sampling rate 1 channel,
     * 16 bits pcm
     */
    public MP3RecordManager() {
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException initAudioRecorder throws
     */
    public void start() {
        if (mIsRecording) {
            return;
        }
        mIsRecording = true; // 提早，防止init或startRecording被多次调用
        initAudioRecorder();
        if(mAudioRecord==null)return;
        mAudioRecord.startRecording();
        new Thread() {
            @Override
            public void run() {
                //设置线程权限
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                while (mIsRecording && mAudioRecord!=null) {
                    int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
                    if (readSize > 0) {
                        mEncodeThread.addTask(mPCMBuffer, readSize);
                        calculateRealVolume(mPCMBuffer, readSize);
                    }
                }
                // release and finalize audioRecord
                if(mAudioRecord!=null){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                }
                // stop the encoding thread and try to wait
                // until the thread finishes its job
                mEncodeThread.sendStopMessage();
            }

            /**
             * 此计算方法来自samsung开发范例
             *
             * @param buffer buffer
             * @param readSize readSize
             */
            private void calculateRealVolume(short[] buffer, int readSize) {
                double sum = 0;
                for (int i = 0; i < readSize; i++) {
                    // 这里没有做运算的优化，为了更加清晰的展示代码
                    sum += buffer[i] * buffer[i];
                }
                if (readSize > 0) {
                    double amplitude = sum / readSize;
                    mVolume = (int) Math.sqrt(amplitude);
                }
            }
        }.start();
    }

    private int mVolume;

    /**
     * 获取真实的音量。 [算法来自三星]
     *
     * @return 真实音量
     */
    public int getRealVolume() {
        return mVolume;
    }

    /**
     * 获取相对音量。 超过最大值时取最大值。
     *
     * @return 音量
     */
    public int getVolume() {
        if (mVolume >= MAX_VOLUME) {
            return MAX_VOLUME;
        }
        return mVolume;
    }

    private static final int MAX_VOLUME = 2000;

    /**
     * 根据资料假定的最大值。 实测时有时超过此值。
     *
     * @return 最大音量值。
     */
    public int getMaxVolume() {
        return MAX_VOLUME;
    }

    public void stop() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsRecording = false;
            }
        },300);
    }

    /**
     * Initialize audio recorder
     */
    private void initAudioRecorder() {
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());

        int bytesPerFrame = DEFAULT_AUDIO_FORMAT.getBytesPerFrame();
        /* Get number of samples. Calculate the buffer size
         * (round up to the factor of given frame size)
         * 使能被整除，方便下面的周期性通知
         * */
        int frameSize = mBufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            mBufferSize = frameSize * bytesPerFrame;
        }

        /* Setup audio recorder */
        mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
                DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(),
                mBufferSize);

        mPCMBuffer = new short[mBufferSize];
        /*
         * Initialize lame buffer
         * mp3 sampling rate is the same as the recorded pcm sampling rate
         * The bit rate is 32kbps
         *
         */

        // Create and run thread used to encode data
        // The thread will
        AudioManager audioManager = new AudioManager();
        audioManager.init(DEFAULT_SAMPLING_RATE, DEFAULT_LAME_IN_CHANNEL, DEFAULT_SAMPLING_RATE, DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
        mEncodeThread = new DataEncodeThread(mRecordFile, mBufferSize, audioManager);
        mEncodeThread.start();
        if(mAudioRecord!=null){
            mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
            mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
        }
    }

    private final Handler mHandler = new Handler();
    private Runnable mAddTimeRunnnable = new Runnable() {
        @Override
        public void run() {
            if (currenttime < maxRecordTime) {
                currenttime++;
                mHandler.postDelayed(this, 1000);//1秒一次
                if (onTimeSecondChanage != null) {
                    onTimeSecondChanage.onSecondChnage(1000 * currenttime);
                }
            } else {
                stopRecord();
            }
        }
    };
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        /**
         * 分贝的计算公式K=20lg(Vo/Vi) Vo当前的振幅值,Vi基准值为600
         */
        private int postDelayed = 200;

        @Override
        public void run() {
            if (soundAmplitudeListenr != null) {
                soundAmplitudeListenr.volume(VolumeUtil.unify(true, getVolume()));
                mHandler.postDelayed(mUpdateMicStatusTimer, postDelayed);
            }
        }
    };

    private int maxRecordTime;
    private int currenttime;

    @Override
    public void startRecord(int stopTime) throws IOException {
        PermissionUtils.permission(PermissionConstants.MICROPHONE, PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
//        ActionEngine.requestAudioFocus();
                        maxRecordTime = stopTime;
                        currenttime = 0;//从新清空
                        mRecordFile = MediaDirectoryUtils.getTempMp3FileName();
                        start();
                        mHandler.post(mUpdateMicStatusTimer);
                        mHandler.postDelayed(mAddTimeRunnnable, 1000);

                    }
                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用录音功能");
                    }
                }).request();

    }


    @Override
    public boolean stopRecord() {
//        ActionEngine.abandonAudioFocus();
        stop();
        if (onTimeOutStopListener != null) {
            onTimeOutStopListener.onStop();
        }
        mHandler.removeCallbacks(mUpdateMicStatusTimer);
        mHandler.removeCallbacks(mAddTimeRunnnable);
        return false;
    }

    @Override
    public void setOnTimeSecondChanage(OnTimeSecondChanage onTimeSecondChanage) {
        this.onTimeSecondChanage = onTimeSecondChanage;

    }

    OnTimeSecondChanage onTimeSecondChanage;
    OnTimeOutStopListener onTimeOutStopListener;
    SoundAmplitudeListenr soundAmplitudeListenr;

    @Override
    public void setSoundAmplitudeListenr(SoundAmplitudeListenr soundAmplitudeListenr) {
        this.soundAmplitudeListenr = soundAmplitudeListenr;
    }

    @Override
    public void setOnTimeOutStopListener(OnTimeOutStopListener onTimeOutStopListener) {
        this.onTimeOutStopListener = onTimeOutStopListener;
    }

    @Override
    public boolean isRecordIng() {
        return mIsRecording;
    }

    @Override
    public int getCurrenttime() {
        return currenttime;
    }

    @Override
    public File getFile() {
        return mRecordFile;
    }
}
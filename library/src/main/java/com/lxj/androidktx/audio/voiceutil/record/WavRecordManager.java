package com.lxj.androidktx.audio.voiceutil.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lxj.androidktx.audio.voiceutil.utils.MediaDirectoryUtils;

/**
 * Created by 情随事迁(qssq666@foxmail.com) on 2017/3/22.
 */

public class WavRecordManager implements RecordManagerI {
    public WavRecordManager() {
//        recorder = new AudioRecord(audioSource, audioRate, audioChannel, audioFormat, bufferSize);
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
    private int maxRecordTime;
    private int currenttime;

    @Override
    public void startRecord(int stopTime) throws IOException {

        PermissionUtils.permission(PermissionConstants.MICROPHONE, PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        createFile();//创建文件
                        maxRecordTime = stopTime;
                        currenttime = 0;//从新清空
                        if (recorder != null) {
                            recorder.stop();
                            recorder.release();
                        }
                        recorder = new AudioRecord(audioSource, audioRate, audioChannel, audioFormat, bufferSize);
//        mHandler.post(mUpdateMicStatusTimer);
                        startRecord();

                        // 开启音频文件写入线程
                        recordData();
                        record_state = AmrRecorderManager.RECORD_STATE.RECORDING;
                        mHandler.postDelayed(mAddTimeRunnnable, 1000);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("权限获取失败，无法使用录音功能");
                    }
                }).request();
    }

    //开始录音
    public void startRecord() {
        isRecording = true;
//        ActionEngine.requestAudioFocus();
        recorder.startRecording();
    }

    //记录数据
    public void recordData() {

        new Thread(new WriteThread()).start();
    }

    int record_state = AmrRecorderManager.RECORD_STATE.STOP;

    @Override
    public boolean stopRecord() {
//        ActionEngine.abandonAudioFocus();
        record_state = AmrRecorderManager.RECORD_STATE.STOP;


        isRecording = false;
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        if (onTimeOutStopListener != null) {
            onTimeOutStopListener.onStop();
        }
        mHandler.removeCallbacks(mAddTimeRunnnable);
        return false;
    }

    @Override
    public void setOnTimeSecondChanage(OnTimeSecondChanage onTimeSecondChanage) {
        this.onTimeSecondChanage = onTimeSecondChanage;

    }

    OnTimeSecondChanage onTimeSecondChanage;
    OnTimeOutStopListener onTimeOutStopListener;

    @Override
    public void setSoundAmplitudeListenr(SoundAmplitudeListenr soundAmplitudeListenr) {

    }


    @Override
    public void setOnTimeOutStopListener(OnTimeOutStopListener onTimeOutStopListener) {
        this.onTimeOutStopListener = onTimeOutStopListener;
    }

    @Override
    public Object getInternAudioRecord() {
        return recorder;
    }


    @Override
    public boolean isRecordIng() {

        return record_state == AmrRecorderManager.RECORD_STATE.RECORDING;
    }

    @Override
    public int getCurrenttime() {
        return currenttime;
    }

    @Override
    public File getFile() {
        return wavFile;
    }

    public void setFile(File file) {
        wavFile = file;
    }


    private AudioRecord recorder;
    //录音源
    private static int audioSource = MediaRecorder.AudioSource.MIC;
    //录音的采样频率
    private static int audioRate = 44100;
    //录音的声道，单声道
    private static int audioChannel = AudioFormat.CHANNEL_IN_MONO;
    //量化的深度
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    //缓存的大小
    private static int bufferSize = AudioRecord.getMinBufferSize(audioRate, audioChannel, audioFormat);
    //记录播放状态
    private boolean isRecording = false;
    //数字信号数组
    private byte[] noteArray;
    //PCM文件
    private File pcmFile;
    //WAV文件
    private File wavFile;
    //文件输出流
    private OutputStream os;

    //文件根目录
 /*   private String basePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/yinfu/";
    //wav文件目录
    private String outFileName = basePath+"/yinfu.wav";
    //pcm文件目录
    private String inFileName = basePath+"/yinfu.pcm";
*/
    //读取录音数字数据线程
    class WriteThread implements Runnable {
        public void run() {
            writeData();
            convertWaveFile();
        }
    }


    //将数据写入文件夹,文件的写入没有做优化
    public void writeData() {
        noteArray = new byte[bufferSize];
        //建立文件输出流
        try {
            os = new BufferedOutputStream(new FileOutputStream(pcmFile));
        } catch (IOException e) {

        }
        while (isRecording == true) {
            int recordSize = recorder.read(noteArray, 0, bufferSize);
            if (recordSize > 0) {
                try {
                    os.write(noteArray);
                } catch (IOException e) {

                }
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {

            }
        }
    }

    // 这里得到可播放的音频文件
    public void convertWaveFile() {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = audioRate;
        int channels = 1;
        long byteRate = 16 * audioRate * channels / 8;
        byte[] data = new byte[bufferSize];
        try {
            in = new FileInputStream(pcmFile);
            out = new FileOutputStream(wavFile);
            totalAudioLen = in.getChannel().size();
            //由于不包括RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            writeWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 任何一种文件在头部添加相应的头文件才能够确定的表示这种文件的格式，wave是RIFF文件结构，每一部分为一个chunk，其中有RIFF WAVE chunk， FMT Chunk，Fact chunk,Data chunk,其中Fact chunk是可以选择的， */
    private void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                     int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        header[0] = 'R'; // RIFF
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);//数据大小
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //FMT Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';//过渡字节
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        header[34] = 16;
        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

    //创建文件夹,首先创建目录，然后创建对应的文件
    public File createFile() {
        File tempCachePcmFileName = MediaDirectoryUtils.getTempCachePcmFileName();
        if (!tempCachePcmFileName.getParentFile().exists()) {
            tempCachePcmFileName.getParentFile().mkdirs();//
        }
        pcmFile = tempCachePcmFileName;
        wavFile = MediaDirectoryUtils.getTempCacheWavFileName();
        if (pcmFile.exists()) {
            pcmFile.delete();
        }
        if (wavFile.exists()) {
            wavFile.delete();
        }
        try {
            pcmFile.createNewFile();
            wavFile.createNewFile();
        } catch (IOException e) {

        }
        return wavFile;
    }

}

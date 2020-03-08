package com.lxj.androidktx.audio;

import android.util.Log;


public class AudioManager {
    public static final String TAG = "AudioManager";

    public static boolean isErrorLoadSo() {
        return mErrorLoadSo;
    }

    static boolean mErrorLoadSo;

    static {
        try {
            System.loadLibrary("mp3lame");
        } catch (UnsatisfiedLinkError e) {
            mErrorLoadSo = true;

        }
    }

    /**
     * Initialize LAME.
     *
     * @param inSamplerate  input sample rate in Hz.
     * @param inChannel     number of channels in input stream.
     * @param outSamplerate output sample rate in Hz.
     * @param outBitrate    brate compression ratio in KHz.
     * @param quality       <p>quality=0..9. 0=best (very slow). 9=worst.</p>
     *                      <p>recommended:</p>
     *                      <p>2 near-best quality, not too slow</p>
     *                      <p>5 good quality, fast</p>
     *                      7 ok quality, really fast
     */
    public native void init(int inSamplerate, int inChannel,
                            int outSamplerate, int outBitrate, int quality);

    /**
     * Encode buffer to mp3.
     *
     * @param bufferLeft  PCM data for left channel.
     * @param bufferRight PCM data for right channel.
     * @param samples     number of samples per channel.
     * @param mp3buf      result encoded MP3 stream. You must specified
     *                    "7200 + (1.25 * buffer_l.length)" length array.
     * @return <p>number of bytes output in mp3buf. Can be 0.</p>
     * <p>-1: mp3buf was too small</p>
     * <p>-2: malloc() problem</p>
     * <p>-3: lame_init_params() not called</p>
     * -4: psycho acoustic problems
     */
    public native int encode(short[] bufferLeft, short[] bufferRight,
                             int samples, byte[] mp3buf);

    /**
     * Flush LAME buffer.
     * <p>
     * REQUIRED:
     * lame_encode_flush will flush the intenal PCM buffers, padding with
     * 0's to make sure the final frame is complete, and then flush
     * the internal MP3 buffers, and thus may return a
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long
     * to hold all possible emitted data.
     * <p>
     * will also write id3v1 tags (if any) into the bitstream
     * <p>
     * return code = number of bytes output to mp3buf. Can be 0
     *
     * @param mp3buf result encoded MP3 stream. You must specified at least 7200
     *               bytes.
     * @return number of bytes output to mp3buf. Can be 0.
     */
    public native int flush(byte[] mp3buf);

    /**
     */
    public native void close();


    /**
     * @param wav
     * @param mp3
     */
    public native void convertmp3(String wav, String mp3);

    /**
     * @return
     */
    public native String getLameVersion();

    /**
     * @param progress
     */
    public void setConvertProgress(int progress) {
        Log.d(TAG, "setConvertProgress:" + progress);
    }
}

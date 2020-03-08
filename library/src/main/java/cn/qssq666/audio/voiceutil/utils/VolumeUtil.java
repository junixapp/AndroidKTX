package cn.qssq666.audio.voiceutil.utils;

import android.animation.IntEvaluator;

public class VolumeUtil {
    /**
     * 统一计算音量，按最大值100计算
     */
    public static int unify(boolean isMp3, int volume){
        IntEvaluator evaluator = new IntEvaluator();
        int max = isMp3? 2000 : 32768;
        float fraction = volume*1f / max;
        return evaluator.evaluate(fraction, 0, 100);
    }
}

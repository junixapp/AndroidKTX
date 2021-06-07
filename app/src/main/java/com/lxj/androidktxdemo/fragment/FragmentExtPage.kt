package com.lxj.androidktxdemo.fragment

import com.blankj.utilcode.util.FileUtils
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.replace
import com.lxj.androidktxdemo.R
import com.lxj.androidktxdemo.entity.User
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import kotlinx.android.synthetic.main.fragment_fragment_ext.*

/**
 * Description:
 * Create by dance, at 2018/12/5
 */
class FragmentExtPage: BaseFragment(){
    override fun getLayoutId() = R.layout.fragment_fragment_ext

    override fun initView() {
        replace(R.id.frame1, TempFragment(), arrayOf(
                TempFragment.Key1 to "我是第一个Fragment",
                TempFragment.Key2 to User(name = "lxj", age = 1)
        ))

        replace(R.id.frame2, TempFragment(), arrayOf(
                TempFragment.Key1 to "我是第二个Fragment",
                TempFragment.Key2 to "疑是地上霜"
        ))
        RecordManager.getInstance().setRecordSoundSizeListener {
            tvVolumeSize.text = "音量大小： $it"
        }
        RecordManager.getInstance().setRecordResultListener {
            tvResult.text = "文件路径：${it.absolutePath} 文件大小：${FileUtils.getSize(it)}"
        }
        RecordManager.getInstance().setRecordStateListener(object : RecordStateListener{
            override fun onStateChange(state: RecordHelper.RecordState) {
                tvState.text = "当前状态: $state"
            }
            override fun onError(error: String?) {
                tvState.text = "当前状态: $error"
            }
        })
        btnStart.click {
            RecordManager.getInstance().start()
        }
        btnPause.click {
            if(RecordManager.getInstance().state==RecordHelper.RecordState.PAUSE){
                RecordManager.getInstance().resume()
            }else{
                RecordManager.getInstance().pause()
            }

        }
        btnEnd.click {
            RecordManager.getInstance().stop()
        }
    }

}


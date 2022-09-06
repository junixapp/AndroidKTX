package com.lxj.androidktx.picker

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.*
import com.lxj.androidktx.R
import com.lxj.androidktx.base.AdaptActivity
import com.lxj.androidktx.core.*
import com.lxj.androidktx.util.DirManager
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.android.synthetic.main._ktx_activity_camera.*
import java.io.File

/**
 * 视频录制
 */
class KTXCameraActivity : AdaptActivity() {

    override fun getLayoutId() = R.layout._ktx_activity_camera

    var inConfirm = false
    var file: File? = null
    override fun initView() {
        BarUtils.setStatusBarVisibility(this, false)
        val ratio = (ScreenUtils.getScreenWidth()*1f / ScreenUtils.getScreenHeight()) * 1.35
        _ktxCameraContainer.height((ScreenUtils.getScreenWidth()/ratio ).toInt())
        _ktxCameraView.setLifecycleOwner(this)
        _ktxCameraView.useDeviceOrientation = true
        _ktxCameraView.addCameraListener(object : CameraListener(){
            override fun onCameraOpened(options: CameraOptions) {}
            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                toast("camera error")
                finishDelay(500)
            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                _ktxPreview.visible()
                if(file?.exists()==true) FileUtils.delete(file)
                file = File(DirManager.cacheDir, "_ktx_capture_${System.currentTimeMillis()}.jpeg")
                result.toFile(file!!) { f ->
                    inConfirm = true
                    _ktxPreview.load(f)
                    _ktxBtnFlip.setImageResource(R.drawable._ktx_check)
                    _ktxSlFlip.showContent()
                }
            }
        })

        _ktxBtnExit.click {
            if (_ktxCameraView.isTakingPicture) return@click
            if(inConfirm){
                inConfirm = false
                _ktxBtnFlip.setImageResource(R.drawable._ktx_camera_flip)
                _ktxPreview.setImageBitmap(null)
                _ktxPreview.gone()
                _ktxBtnTake.animateVisible(move = false)
            }else{
                finish()
            }
        }

        _ktxBtnTake.click {
            if (_ktxCameraView.isTakingPicture) return@click
            _ktxBtnTake.animateInvisible()
            _ktxSlFlip.showLoading()
//            _ktxCameraView.takePicture()
            _ktxCameraView.takePictureSnapshot()
        }

        _ktxBtnFlip.click {
            if (_ktxCameraView.isTakingPicture) return@click
            if(inConfirm){
                if(file!=null){
                    val intent = Intent()
                    intent.data = UriUtils.file2Uri(file!!)
                    intent.putExtra("result_path", file!!.absolutePath)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }else{
                _ktxCameraView.facing = if(_ktxCameraView.facing==Facing.BACK) Facing.FRONT else Facing.BACK
            }
        }
    }
    override fun initData() {
    }


}
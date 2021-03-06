package com.lxj.androidktx.qrcode

import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ReflectUtils
import com.king.zxing.CaptureHelper
import com.king.zxing.OnCaptureCallback
import com.king.zxing.ViewfinderView
import com.lxj.androidktx.R
import com.lxj.androidktx.core.click
import com.lxj.androidktx.core.color
import kotlinx.android.synthetic.main._ktx_activity_custom_capture.*

/**
 * @author [Jenly](mailto:jenly1314@gmail.com)
 */
class CustomCaptureActivity : AppCompatActivity(), OnCaptureCallback {
    private var surfaceView: SurfaceView? = null
    private var viewfinderView: ViewfinderView? = null
    private var ivTorch: View? = null

    var captureHelper: CaptureHelper? = null

    val primaryColor : Int by lazy { intent.getIntExtra("color", color(R.color.colorPrimary)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._ktx_activity_custom_capture)
        initUI()
        captureHelper!!.onCreate()
    }

    /**
     * 初始化
     */
    fun initUI() {
        surfaceView = findViewById(R.id.surfaceView)
        viewfinderView = findViewById(R.id.viewfinderView)
        ivTorch = findViewById(R.id.ivTorch)

        ReflectUtils.reflect(viewfinderView).field("cornerColor", primaryColor)
        ReflectUtils.reflect(viewfinderView).field("laserColor", primaryColor)
        ReflectUtils.reflect(viewfinderView).field("frameColor", primaryColor)
        ivClose.click { finish() }
//        ivTorch?.setVisibility(View.INVISIBLE)
        initCaptureHelper()
    }

    fun initCaptureHelper() {
        captureHelper = CaptureHelper(this, surfaceView, viewfinderView, ivTorch)
        captureHelper!!.setOnCaptureCallback(this)
    }

    public override fun onResume() {
        super.onResume()
        captureHelper!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        captureHelper!!.onPause()
    }

    public override fun onDestroy() {
        super.onDestroy()
        captureHelper!!.onDestroy()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        captureHelper!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    /**
     * 接收扫码结果回调
     * @param result 扫码结果
     * @return 返回true表示拦截，将不自动执行后续逻辑，为false表示不拦截，默认不拦截
     */
    override fun onResultCallback(result: String): Boolean {
        return false
    }
}
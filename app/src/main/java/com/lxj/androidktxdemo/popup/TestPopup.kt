package com.lxj.androidktxdemo.popup

import android.content.Context
import com.lxj.ext.click
import com.lxj.androidktx.ext.toast
import com.lxj.androidktxdemo.R
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.tt.view.*

class TestPopup (context: Context): CenterPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.tt
    }

    override fun onCreate() {
        super.onCreate()
        tvCancel.click {
            dismiss()
        }
        tvConfirm.click {
            dismiss()
        }
        etBirthday.click {
            toast("click")
        }
    }
}
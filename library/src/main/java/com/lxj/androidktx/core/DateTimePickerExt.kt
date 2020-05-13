package com.lxj.androidktx.core

import android.content.Context
import android.graphics.Color
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import java.util.*


fun Context.showDatePicker(onSelect: (date: Date) -> Unit, types: BooleanArray? = booleanArrayOf(true, true, true, false, false, false),
                           selectedDate: Calendar? = null, dateRange: List<Calendar>? = null) {
    val picker = TimePickerBuilder(this, OnTimeSelectListener { d, v -> //选中事件回调
        onSelect(d)
    })
            .setType(types) // 默认全部显示
            .setCancelText("取消") //取消按钮文字
            .setSubmitText("确定") //确认按钮文字
            .setTitleSize(20) //标题文字大小
            .setTitleText("选择日期") //标题文字
            .setOutSideCancelable(true) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(true) //是否循环滚动
            .setTitleColor(Color.BLACK) //标题文字颜色
            .setSubmitColor(Color.BLACK) //确定按钮文字颜色
            .setCancelColor(Color.BLACK) //取消按钮文字颜色
            .setTitleBgColor(Color.WHITE) //标题背景颜色 Night mode
            .setBgColor(Color.WHITE) //滚轮背景颜色 Night mode
            .setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true) //是否显示为对话框样式
    if (selectedDate != null) picker.setDate(selectedDate) // 如果不设置的话，默认是系统时间*/
    if (dateRange != null && dateRange.size == 2) picker.setRangDate(dateRange[0], dateRange[1]) //起始终止年月日设定

    picker.build().show()
}
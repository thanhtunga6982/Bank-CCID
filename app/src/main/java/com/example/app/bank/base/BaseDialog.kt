package com.example.app.bank.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Window
import android.view.WindowManager

abstract class BaseDialog : DialogFragment() {

    companion object {
        private const val dimValue = 0.5f
    }

    var onBackPressCallback: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(context) {
            override fun onBackPressed() {
                //disable onBack dismiss com.example.app.bank.dialog
                onBackPressCallback()
            }

        }.apply {
            window.requestFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.setDimAmount(dimValue)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentDialog(this)
            initListeners(this)
        }
    }

    abstract fun setContentDialog(dialog: Dialog)

    abstract fun initListeners(dialog: Dialog)
}

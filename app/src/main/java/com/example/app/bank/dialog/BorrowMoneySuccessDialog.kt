package com.example.app.bank.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.support.v4.app.FragmentManager
import com.example.app.bank.R
import com.example.app.bank.base.BaseDialog

class BorrowMoneySuccessDialog : BaseDialog() {

    companion object {
        @Volatile
        private var isShowing: Boolean = false
    }

    override fun setContentDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_borrow_money_success)
    }

    override fun initListeners(dialog: Dialog) {

    }

    override fun show(manager: FragmentManager?, tag: String?) {
        if (!isShowing) {
            isShowing = true
            super.show(manager, tag)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        if (isShowing) {
            isShowing = false
            super.onDismiss(dialog)
        }
    }
}

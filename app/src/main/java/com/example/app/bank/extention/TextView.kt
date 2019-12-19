package com.example.app.bank.extention

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView


/**
 * TextView extension afterTextChange
 */
fun TextView.afterTextChanged(afterTextChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(edt: Editable?) {
            afterTextChange.invoke(edt.toString())
        }

        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
        }
    })
}

fun TextView.afterTextChangedOfMoney(afterTextChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(edt: Editable?) {

            afterTextChange.invoke(edt.toString())
        }

        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
            removeTextChangedListener(this)
        }
    })
}



fun TextView.textChanged(textChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(edt: Editable?) {
            //no-opt
        }

        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            textChange.invoke(char.toString())
        }
    })
}


/**
 * TextView extension beforeTextChange
 */
fun TextView.beforeTextChanged(beforeTextChange: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            beforeTextChange.invoke()
        }

        override fun afterTextChanged(edt: Editable?) {
            //no-opt
        }

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //no-opt
        }
    })
}

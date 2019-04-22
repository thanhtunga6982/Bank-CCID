package com.example.app.bank.extention

import android.support.v4.app.FragmentActivity

fun FragmentActivity.popBackStack(s: String) {
    supportFragmentManager.popBackStack()
}
package com.example.app.bank.base

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.app.bank.R

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(){

    internal fun replaceFragment(fragment: Fragment, isAddtoBackStack : Boolean){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flMainContainer,fragment)
            if (isAddtoBackStack) {
                addToBackStack(null)
            }
            commit()
        }
    }
    open fun getCurrentFragment(): Fragment? = supportFragmentManager.findFragmentById(R.id.flBaseContainer)

}

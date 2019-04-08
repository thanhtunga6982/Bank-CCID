package com.example.app.bank.base

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.app.bank.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val subscription: CompositeDisposable = CompositeDisposable()

    internal fun replaceFragment(fragment: Fragment, isAddtoBackStack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flMainContainer, fragment)
            if (isAddtoBackStack) {
                addToBackStack(null)
            }
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    open fun getCurrentFragment(): Fragment? = supportFragmentManager.findFragmentById(R.id.flBaseContainer)

}

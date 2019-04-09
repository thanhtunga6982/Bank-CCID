package com.example.app.bank.base

import android.support.v4.app.Fragment
import com.example.app.bank.extention.popBackStack
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment() : Fragment() {
    private val subscription: CompositeDisposable = CompositeDisposable()

    fun replaceFragment(fragment: Fragment, isAddBackStack: Boolean, tagNameBackStack: String? = null) {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.replaceFragment(fragment, isAddBackStack, tagNameBackStack)
            }
        }
    }

    fun popBackStack() {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.childFragmentManager.popBackStack()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onBindViewModel()
    }

    override fun onPause() {
        super.onPause()
        subscription.clear()
    }

    abstract fun onBindViewModel()

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

}

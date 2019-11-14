package com.example.app.bank.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
    fun removeFragment(fragment: Fragment) {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.removeFragment(fragment)
            }
        }
    }

    fun addFragment(fragment: Fragment, isAddBackStack: Boolean, tagNameBackStack: String? = null) {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.addFragment(fragment, isAddBackStack, tagNameBackStack)
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

    fun popBackStackPDF(isback: Boolean) {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.childFragmentManager.popBackStack()
            }
        }
    }

    fun popBackStackTagName(tagNameBackStack: String? = null) {
        parentFragment?.let {
            if (it is BaseFragmentContainer) {
                it.childFragmentManager.popBackStackImmediate(
                    tagNameBackStack,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
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

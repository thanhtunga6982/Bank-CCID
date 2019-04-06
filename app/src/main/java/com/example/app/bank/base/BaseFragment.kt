package com.example.app.bank.base

import android.support.v4.app.Fragment
import com.example.app.bank.extention.popBackStack

abstract class BaseFragment() : Fragment() {
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
}

package com.example.app.bank.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R

open class BaseFragmentContainer() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_container, container, false)
    }

    fun replaceFragment(fragment: Fragment, isAddBackStack: Boolean, tagNameBackStack: String? = null) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flBaseContainer, fragment, fragment.javaClass.simpleName)
            if (isAddBackStack) {
                addToBackStack(tagNameBackStack)
            }
        }.commitAllowingStateLoss()
    }

    fun addFragment(fragment: Fragment, isAddBackStack: Boolean, tagNameBackStack: String? = null) {
        childFragmentManager.beginTransaction().apply {
            add(R.id.flBaseContainer, fragment, fragment.javaClass.simpleName)
            if (isAddBackStack) {
                addToBackStack(tagNameBackStack)
            }
        }.commit()
    }

    internal fun getChildFragment(): Fragment? = childFragmentManager.findFragmentById(R.id.flBaseContainer)
}
package com.example.app.bank.main.login

import android.os.Bundle
import android.view.View
import com.example.app.bank.AppConstant
import com.example.app.bank.base.BaseFragmentContainer

class MainBankContainer : BaseFragmentContainer() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(LoginFragment(),true)
    }
}

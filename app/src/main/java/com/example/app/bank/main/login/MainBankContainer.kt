package com.example.app.bank.main.login

import android.os.Bundle
import android.view.View
import com.example.app.bank.AppConstant
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.maindtu.login.LoginDTUFragment

class MainBankContainer : BaseFragmentContainer() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(LoginDTUFragment(),true)
    }
}

package com.example.app.bank.main.login

import android.os.Bundle
import android.view.View
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.maindtu.home.HomeFragment
import com.example.app.bank.maindtu.login.LoginDTUFragment


class MainBankContainer : BaseFragmentContainer() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        if (isUserLoggedOut()) {
            replaceFragment(LoginDTUFragment(), false)
        } else {
            replaceFragment(HomeFragment.newInstance(LocalRepository(context).getUserLocal()), false)
        }
    }

    private fun isUserLoggedOut(): Boolean {
        val token = LocalRepository(context).getToken()
        return token.isEmpty()
    }
}

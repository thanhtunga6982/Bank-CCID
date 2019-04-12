package com.example.app.bank

import android.os.Bundle
import com.example.app.bank.base.BaseActivity
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.main.condition.ConditionBorrowMoney
import com.example.app.bank.main.borrowmoney.list.LendingMoneyFragment
import com.example.app.bank.main.login.MainBankContainer

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(MainBankContainer(), true)

    }

    override fun onBackPressed() {
        supportFragmentManager.findFragmentById(R.id.flMainContainer).also {
            if (it is MainBankContainer) {
                val fragment = it.getChildFragment()
                when (fragment) {
                    is ConditionBorrowMoney -> {
                        return

                    }
                    is LendingMoneyFragment -> {
                        fragment.popBackStackTagName(AppConstant.TAG_NAME_BORROW_MONEY)
                    }

                    else -> {
                        (fragment as BaseFragment).popBackStack()
                    }
                }
            }
        }
    }
}

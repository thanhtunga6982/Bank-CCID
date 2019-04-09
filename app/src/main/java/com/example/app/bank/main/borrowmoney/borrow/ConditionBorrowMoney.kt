package com.example.app.bank.main.borrowmoney.borrow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User
import com.example.app.bank.main.borrowmoney.list.LendingMoneyFragment
import kotlinx.android.synthetic.main.item_layout_function.*

class ConditionBorrowMoney : BaseFragment() {

    companion object {
        private const val USER_CURRENT = "user_current"

        fun newInstance(user: User): ConditionBorrowMoney {
            return ConditionBorrowMoney().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT, user)
                }
            }
        }
    }

    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_CURRENT)
        }
        return inflater.inflate(R.layout.fragment_condition_borrow_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVaytien.setOnClickListener {
            replaceFragment(BorrowMoneyFragment.newInstance(user.name), true)
        }
        btnChoVaytien.setOnClickListener {
            replaceFragment(LendingMoneyFragment(), true)
        }
    }
}
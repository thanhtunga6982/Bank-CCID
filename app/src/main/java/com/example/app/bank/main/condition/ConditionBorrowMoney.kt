package com.example.app.bank.main.condition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.AppConstant
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.loadUrl
import com.example.app.bank.extention.visible
import com.example.app.bank.main.borrowmoney.borrow.BorrowMoneyFragment
import com.example.app.bank.main.borrowmoney.list.LendingMoneyFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_condition_borrow_money.*
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

    private lateinit var viewModel: ConditionBorrowViewModel

    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ConditionBorrowViewModel()
        arguments?.let {
            user = it.getParcelable(USER_CURRENT)
        }
        return inflater.inflate(R.layout.fragment_condition_borrow_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVaytien.setOnClickListener {
            replaceFragment(BorrowMoneyFragment.newInstance(user.name), true, AppConstant.TAG_NAME_BORROW_MONEY)
        }
        btnChoVaytien.setOnClickListener {
            replaceFragment(LendingMoneyFragment(), true, AppConstant.TAG_NAME_BORROW_MONEY)
        }
        btnSignOut.setOnClickListener {
            viewModel.timerDelayProgressbar
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        progressBar.gone()
                        this@ConditionBorrowMoney.popBackStackTagName(AppConstant.TAG_NAME_LOGIN)
                    } else {
                        progressBar.visible()
                    }
                }) {}
        }
        imgCircleAvatar.loadUrl(user.avatar)
        tvNameHeader.text = user.name
        tvTotalAsset.text = user.totalasset
    }

    override fun onBindViewModel() {
    }

}
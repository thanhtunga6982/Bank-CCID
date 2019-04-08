package com.example.app.bank.main.borrowmoney.borrow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.extention.afterTextChanged
import com.example.app.bank.extention.moneyFormat
import com.example.app.bank.extention.parseToInt
import com.example.app.bank.extention.showKeyboard
import com.example.app.bank.main.borrowmoney.list.LendingMoneyFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_borrow_money.*

class BorrowMoneyFragment : BaseFragment() {


    companion object {
        private const val USER_CURRENT = "user_current"

        fun newInstance(name: String): BorrowMoneyFragment {
            return BorrowMoneyFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_CURRENT, name)
                }
            }
        }
    }

    private lateinit var viewModel: BorrowMoneyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = BorrowMoneyViewModel(LocalRepository())
        arguments?.let {
            viewModel.name = it.getString(USER_CURRENT)
        }
        return inflater.inflate(com.example.app.bank.R.layout.fragment_borrow_money, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserList()
        initListener()
        handleClick()
    }

    override fun onBindViewModel() {

    }

    private fun handleClick() {
        btnOK.setOnClickListener { view ->
            viewModel.listUserSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.handleUpdateUser(it)
                    replaceFragment(LendingMoneyFragment(), true)
                }, {})
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    @SuppressLint("CheckResult")
    private fun initView() {
        viewModel.stateButtonSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                btnOK.isEnabled = it
            }, {})
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        edtMoneyBorrow.afterTextChanged {
            it.parseToInt().moneyFormat()?.also { value ->
                viewModel.validateMoneyBorrow(value)
                tvMoney.text = value
            }
        }

        tvMoney.setOnClickListener {
            edtMoneyBorrow.apply {
                requestFocus()
                showKeyboard()
            }
        }

        edtCollateral.afterTextChanged {
            viewModel.validateMoneyAssetTax(it)
        }
        edtPlan.afterTextChanged {
            viewModel.validateMoneyDebtpaymentplan(it)
        }
    }
}

package com.example.app.bank.main.borrowmoney.borrow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.*
import com.example.app.bank.main.borrowmoney.list.LendingMoneyFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_borrow_money.*
import kotlinx.android.synthetic.main.layout_header_app.*

class BorrowMoneyFragment : BaseFragment() {


    companion object {
        private const val USER_CURRENT = "user_current"

        fun newInstance(user: User): BorrowMoneyFragment {
            return BorrowMoneyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT, user)
                }
            }
        }
    }

    private lateinit var viewModel: BorrowMoneyViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = BorrowMoneyViewModel(LocalRepository())
        arguments?.let {
            viewModel.userbank = it.getParcelable(USER_CURRENT)
        }
        return inflater.inflate(R.layout.fragment_borrow_money, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserList()
        initListener()
        handleClick()
    }

    override fun onBindViewModel() {
        //TODO
    }

    private fun handleClick() {
        btnOK.setOnClickListener {
            viewModel.handleUpdateUser(viewModel.listUser)
            replaceFragment(LendingMoneyFragment(), true)
        }
        imgClose.setOnClickListener {
            parentFragment?.let {
                if (it is BaseFragmentContainer) {
                    popBackStack()
                }
            }
        }
        tvTitleHeader.text = "Thong tin vay tien"
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
                if (value == "0") {
                    tvMoney.text = ""
                } else {
                    tvMoney.text = value
                }
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


    override fun onDestroyView() {
        super.onDestroyView()
        edtMoneyBorrow.hideKeyboard()
    }
}

package com.example.app.bank.maindtu.borrow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.afterTextChanged
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.textChanged
import com.example.app.bank.extention.visible
import com.example.app.bank.maindtu.home.HomeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_borrow_money_dtu.*
import kotlinx.android.synthetic.main.layout_header_app.*

class BorrowMoneyDTUFragment : BaseFragment() {

    companion object {
        private const val USER_CURRENT_DTU_BORROW = "user_current_dtu_borrow"

        fun newInstance(user: User): BorrowMoneyDTUFragment {
            return BorrowMoneyDTUFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT_DTU_BORROW, user)
                }
            }
        }
    }

    var listUser = mutableListOf<User>()

    private lateinit var viewModel: BorrowMoneyDTUViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.let {
            viewModel = BorrowMoneyDTUViewModel(LocalRepository(it))
        }
        arguments?.let {
            viewModel.user = it.getParcelable(USER_CURRENT_DTU_BORROW)
        }
        listUser.add(viewModel.user)
        return inflater.inflate(R.layout.fragment_borrow_money_dtu, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserList()
        initListener()

    }

    override fun onBindViewModel() {
        addDisposables(viewModel.stateButtonSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                btnConfirm.isEnabled = it

            }
        )
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    @SuppressLint("CheckResult")
    private fun initView() {
    }


    @SuppressLint("CheckResult")
    private fun initListener() {
        tvTitleHeader.text = "Yêu cầu vay "
        imgClose.setOnClickListener {
            popBackStack()
        }

        spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                viewModel.handleDataSpinnerArea(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        spinnerAssetTax.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)
                viewModel.handleDataSpinnerAssetTax(item.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        edtMoney.textChanged {

            viewModel.validateMoneyBorrow(it)
            when {
                it.length > 7 -> tvCheckCondition.gone()
                it.isEmpty() -> {
                    tvCheckCondition.gone()
                }
                else -> {
                    tvCheckCondition.visible()
                    tvCheckCondition.text = "Số tiền vay phải ít nhất 1.000.000 "
                }
            }

        }
        edtInterest.afterTextChanged {
            viewModel.validateInterest(it)

        }
        edtTime.afterTextChanged {
            viewModel.validateTimeBorrow(it)
        }

        edtPlan.afterTextChanged {
            viewModel.validatePlan(it)
        }

        btnConfirm.setOnClickListener {
            viewModel.handleUpdateUser(listUser)
            replaceFragment(HomeFragment.newInstance(user = viewModel.user), true)
        }
    }
}
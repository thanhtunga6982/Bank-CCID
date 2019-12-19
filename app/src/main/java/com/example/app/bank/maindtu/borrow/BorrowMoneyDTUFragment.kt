package com.example.app.bank.maindtu.borrow

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.afterTextChanged
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.maindtu.home.HomeFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_borrow_money_dtu.*
import kotlinx.android.synthetic.main.layout_header_app.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            viewModel = BorrowMoneyDTUViewModel(LocalRepository(it))
        }
        arguments?.let {
            viewModel.user = it.getParcelable(USER_CURRENT_DTU_BORROW)
        }
        listUser.add(viewModel.user)
        return inflater.inflate(
            com.example.app.bank.R.layout.fragment_borrow_money_dtu,
            container,
            false
        )
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

        edtMoney.afterTextChanged {

        }

        edtMoney.addTextChangedListener(onTextChangedListener(edtMoney))

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

    private fun onTextChangedListener(edtMoney: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                edtMoney.removeTextChangedListener(this)

                try {
                    var originalString = s.toString()
                    val longval: Long?
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = java.lang.Long.parseLong(originalString)

                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)
                    //setting text after format to EditText
                    edtMoney.setText(formattedString)
                    edtMoney.setSelection(edtMoney.text.length)
                    viewModel.validateMoneyBorrow(formattedString)
                    when {
                        originalString.length >= 7 -> tvCheckCondition.gone()
                        originalString.isEmpty() -> {
                            tvCheckCondition.gone()
                        }
                        else -> {
                            tvCheckCondition.visible()
                            tvCheckCondition.text = "Số tiền vay phải ít nhất 1.000.000 "
                        }
                    }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                edtMoney.addTextChangedListener(this)

            }
        }
    }
}

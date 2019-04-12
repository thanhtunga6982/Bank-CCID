package com.example.app.bank.main.detailUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.loadUrl
import kotlinx.android.synthetic.main.fragment_detail_user.*
import kotlinx.android.synthetic.main.item_lending_money.*
import kotlinx.android.synthetic.main.layout_detail_user.*

class DetailUserFragment() : BaseFragment() {
    companion object {
        private const val USER_DETAIL = "user_detail"
        fun newInstance(user: User) = DetailUserFragment().apply {
            arguments = Bundle().apply {
                putParcelable(USER_DETAIL, user)
            }
        }
    }
    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_DETAIL)
        }
        return inflater.inflate(R.layout.fragment_detail_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        with(user){
            tvNameDetail.text = name
            tvEmailDetail.text = email
            tvMoneyDetail.text = moneyBorrow
            tvAssetTaxDetail.text = assettax
            tvDebtpaymentplanDetail.text = debtpaymentplan
            tvTotalAssetDetail.text = totalasset
        }
    }

    override fun onBindViewModel() {

    }
}
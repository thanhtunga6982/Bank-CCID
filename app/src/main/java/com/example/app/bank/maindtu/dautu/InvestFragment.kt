package com.example.app.bank.maindtu.dautu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User
import com.example.app.bank.maindtu.dautu.link.InvestLinkFragment
import com.example.app.bank.maindtu.dautu.sangiaodich.SGDFragment
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.layout_invest.*
import kotlinx.android.synthetic.main.layout_invest.view.*

class InvestFragment : BaseFragment() {

    companion object {
        private const val USER_CURRENT_DTU_INVEST = "user_current_dtu_invest"
        fun newInstance(user: User): InvestFragment {
            return InvestFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT_DTU_INVEST, user)
                }
            }
        }
    }

    private var user = User()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU_INVEST)
        }
        return inflater.inflate(R.layout.layout_invest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitleHeader.text = "Đầu tư"


        tvMoneyAsset.text = user.money
        imgClose.setOnClickListener {
            popBackStack()
        }
        tvTitleLink.setOnClickListener {
            replaceFragment(InvestLinkFragment.newInstance(user),true)
        }
        btnTransaction.setOnClickListener {
            replaceFragment(SGDFragment(), true)
        }
    }

    override fun onBindViewModel() {
        //TODO
    }
}

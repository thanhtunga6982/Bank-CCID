package com.example.app.bank.maindtu.dautu.link

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.layout_invest_link.*

class InvestLinkFragment() : BaseFragment() {
    private lateinit var adapter: InvestLinkAdapter

    companion object {
        private const val USER_CURRENT_DTU_INVEST = "user_current_dtu_invest_link"
        fun newInstance(user: User): InvestLinkFragment {
            return InvestLinkFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT_DTU_INVEST, user)
                }
            }
        }
    }

    private var user = User()
    private var linearLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU_INVEST)
        }

        return inflater.inflate(R.layout.layout_invest_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user.linkBank?.let {
            adapter = InvestLinkAdapter(it)
        }
        adapter.userClickListeners = {
            user.money = it.money
            popBackStack()

        }
        recyclerViewLink.run {
            layoutManager = LinearLayoutManager(context).apply { linearLayoutManager = this }
            adapter = this@InvestLinkFragment.adapter
            this@InvestLinkFragment.adapter.notifyDataSetChanged()
        }
        imgClose.setOnClickListener {
            popBackStack()
        }
        tvTitleHeader.text = "Chọn ngân hàng liên kết "
    }

    override fun onBindViewModel() {
        //TODO
    }


}
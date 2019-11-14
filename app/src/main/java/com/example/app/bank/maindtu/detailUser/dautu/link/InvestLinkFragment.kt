package com.example.app.bank.maindtu.detailUser.dautu.link

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private lateinit var viewModel: InvestLinkViewModel
    private var user = User()
    private var linearLayoutManager = LinearLayoutManager(context)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = InvestLinkViewModel(LocalRepository(context))
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU_INVEST)
        }

        return inflater.inflate(R.layout.layout_invest_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = InvestLinkAdapter(viewModel.listBank)

        adapter.userClickListeners = {
            user.bank = it.name
            user.money = it.money
            user.isCheckMoney = true
            popBackStack()

        }
        recyclerViewLink.run {
            layoutManager = LinearLayoutManager(context).apply { linearLayoutManager = this }
            adapter = this@InvestLinkFragment.adapter
        }
        imgClose.setOnClickListener {
            popBackStack()
        }
        tvTitleHeader.text = "Chọn ngân hàng liên kết "
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.getBank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    user.forEach {
                        if (it.id == this.user.id) {
                            it.linkBank?.let { bank ->
                                viewModel.listBank.addAll(bank)
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }, {})
        )

    }

}
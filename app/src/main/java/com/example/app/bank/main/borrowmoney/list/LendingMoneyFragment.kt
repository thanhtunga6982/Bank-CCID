package com.example.app.bank.main.borrowmoney.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.main.detailUser.DetailUserFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.lending_money_fragment.*

class LendingMoneyFragment() : BaseFragment() {

    private lateinit var adapter: LendingMoneyAdapter
    private lateinit var viewModel: LendingMoneyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = LendingMoneyViewModel(LocalRepository())
        return inflater.inflate(R.layout.lending_money_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClick()
    }

    private fun initAdapter() {
        adapter = LendingMoneyAdapter(viewModel.listUser)
        recyclerViewLIst.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@LendingMoneyFragment.adapter
            itemAnimator = null
            this@LendingMoneyFragment.adapter.notifyDataSetChanged()
        }
    }

    private fun initClick() {
        adapter.userClickListeners = {
            replaceFragment(DetailUserFragment.newInstance(it,false), true)
        }
        imgClose.setOnClickListener {
            parentFragment?.let {
                if (it is BaseFragmentContainer) {
                    popBackStack()
                }
            }
        }
        tvTitleHeader.text = context?.getString(R.string.tv_list_need_borrow_money)
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        containerProgressbar.visible()
                    } else {
                        containerProgressbar.gone()

                    }
                }, {}),
            viewModel.getUserLending()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.notifyDataSetChanged()
                }, {})

        )
    }

}

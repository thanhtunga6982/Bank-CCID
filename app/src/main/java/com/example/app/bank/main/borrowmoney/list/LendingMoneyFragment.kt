package com.example.app.bank.main.borrowmoney.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
            println("TTTTT$it")
        //TODO Detail
        }
    }

    override fun onBindViewModel() {
        initList()
    }

    @SuppressLint("CheckResult")
    private fun initList() {
        viewModel.getUserLending()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.notifyDataSetChanged()
            }, {})
    }
}

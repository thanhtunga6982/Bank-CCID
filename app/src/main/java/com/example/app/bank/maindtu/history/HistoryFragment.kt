package com.example.app.bank.maindtu.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_history_connect.*
import kotlinx.android.synthetic.main.layout_header_app.*

class HistoryFragment : BaseFragment() {

    private var linearLayoutManager = LinearLayoutManager(context)
    private lateinit var adapter: HistoryAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.let {
            viewModel = HistoryViewModel(LocalRepository(it))
        }
        return inflater.inflate(R.layout.fragment_history_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitleHeader.text = "Lịch sử giao dịch thành công"
        imgClose.setOnClickListener {
            popBackStack()
        }

        adapter = HistoryAdapter(viewModel.listHistory)
        recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(context).apply {
                linearLayoutManager = this
            }
            adapter = this@HistoryFragment.adapter
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
            layoutAnimation = controller
            scheduleLayoutAnimation()
            this@HistoryFragment.adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.getUserHistory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.notifyDataSetChanged()
                    adapter.listHistory.reverse()
                }, {}),
            viewModel.loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        containerProgressbarHistory.visible()
                    } else {
                        containerProgressbarHistory.gone()
                    }
                }, {})
        )
    }
}
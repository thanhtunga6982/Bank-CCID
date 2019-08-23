package com.example.app.bank.maindtu.dautu.sangiaodich

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.main.detailUser.DetailUserFragment
import com.example.app.bank.maindtu.home.HomeAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.layout_sgd_fagment.*

class SGDFragment : BaseFragment() {
    private var linearLayoutManager = LinearLayoutManager(context)

    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: SGDViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = SGDViewModel(LocalRepository())
        return inflater.inflate(R.layout.layout_sgd_fagment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter(viewModel.listUser)
        recyclerViewSGD.apply {
            layoutManager = LinearLayoutManager(context).apply { linearLayoutManager = this }
            adapter = this@SGDFragment.adapter
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
            layoutAnimation = controller
            scheduleLayoutAnimation()
            this@SGDFragment.adapter.notifyDataSetChanged()
        }
        initClick()
    }

    private fun initClick() {
        imgClose.setOnClickListener {
            popBackStack()
        }
        tvTitleHeader.text = "Sàn giao dịch"
        adapter.userClickListeners = {
            replaceFragment(DetailUserFragment.newInstance(it, true), true)
        }
    }

    override fun onBindViewModel() {
        addDisposables(
            viewModel.getUserLending()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.notifyDataSetChanged()

                }, {})
        )
    }
}
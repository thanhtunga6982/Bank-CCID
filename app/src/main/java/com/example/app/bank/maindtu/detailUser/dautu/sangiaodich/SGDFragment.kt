package com.example.app.bank.maindtu.detailUser.dautu.sangiaodich

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.maindtu.detailUser.DetailUserFragment
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.layout_sgd_fagment.*


class SGDFragment : BaseFragment() {
    private var linearLayoutManager = LinearLayoutManager(context)

    private lateinit var adapter: SGDAdapter
    private lateinit var viewModel: SGDViewModel

    companion object {
        private const val USER_CURRENT_DTU_INVEST = "user_current_dtu_invest_sgd"
        fun newInstance(user: User): SGDFragment {
            return SGDFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT_DTU_INVEST, user)
                }
            }
        }
    }

    private var user = User()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.let {
            viewModel = SGDViewModel(LocalRepository(it))
        }
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU_INVEST)
        }
        return inflater.inflate(com.example.app.bank.R.layout.layout_sgd_fagment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SGDAdapter(viewModel.listUser)
        recyclerViewSGD.apply {
            layoutManager = LinearLayoutManager(context).apply { linearLayoutManager = this }
            adapter = this@SGDFragment.adapter
            val controller = AnimationUtils.loadLayoutAnimation(context, com.example.app.bank.R.anim.layout_fall_down)
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

            //update money user
            it.money = user.money

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
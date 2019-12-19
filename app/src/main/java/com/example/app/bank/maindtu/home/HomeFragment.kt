package com.example.app.bank.maindtu.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.gone
import com.example.app.bank.extention.visible
import com.example.app.bank.maindtu.borrow.BorrowMoneyDTUFragment
import com.example.app.bank.maindtu.detailUser.DetailUserFragment
import com.example.app.bank.maindtu.detailUser.dautu.InvestFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.lending_money_fragment.*
import java.util.concurrent.TimeUnit


class HomeFragment : BaseFragment() {
    companion object {
        private const val USER_CURRENT_DTU = "user_current_dtu"

        fun newInstance(user: User): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_CURRENT_DTU, user)
                }
            }
        }
    }


    private var user = User()
    private lateinit var adapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel
    private var linearLayoutManager = LinearLayoutManager(context)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.let {
            viewModel = HomeViewModel(LocalRepository(it))
        }
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU)
        }
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HomeAdapter(viewModel.listUser)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                linearLayoutManager = this
            }
            adapter = this@HomeFragment.adapter
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
            layoutAnimation = controller
            scheduleLayoutAnimation()
            this@HomeFragment.adapter.notifyDataSetChanged()
        }
        initClick()

        initListener()
    }

    private fun initListener() {

    }

    private fun initClick() {
        adapter.userClickListeners = {
            replaceFragment(DetailUserFragment.newInstance(it, false), true)
        }
        btnDautu.setOnClickListener {
            replaceFragment(InvestFragment.newInstance(user), true)
        }
        btnChovay.setOnClickListener {
            replaceFragment(BorrowMoneyDTUFragment.newInstance(user), true)
        }
    }

    override fun onBindViewModel() {
        addDisposables(
            handleStartInterval()
            ,
            viewModel.getUserLending()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.notifyDataSetChanged()

                }, {}),
            viewModel.loadingSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        containerProgressbar.visible()
                    } else {
                        containerProgressbar.gone()
                    }
                }, {})
        )
    }


    @SuppressLint("CheckResult")
    private fun handleStartInterval() =
        Observable.interval(2000L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (viewModel.listUser.size > 4) {
                    val userFirst = viewModel.listUser[0]
                    viewModel.listUser.removeAt(0)
                    adapter.notifyItemRemoved(0)
                    viewModel.listUser.add(viewModel.listUser.size, userFirst)
                    adapter.notifyItemInserted(viewModel.listUser.size)
                }
            }, {})

}

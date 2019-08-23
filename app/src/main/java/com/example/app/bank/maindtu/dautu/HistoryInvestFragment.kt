package com.example.app.bank.maindtu.dautu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.R
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.data.model.User

class HistoryInvestFragment : BaseFragment() {

    companion object {
        private const val USER_CURRENT_DTU_INVEST = "user_current_dtu_invest"
        fun newInstance(user: User): HistoryInvestFragment {
            return HistoryInvestFragment().apply {
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
        println("PPPP" + user)
        return inflater.inflate(R.layout.layout_invest_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onBindViewModel() {
        //TODO
    }
}

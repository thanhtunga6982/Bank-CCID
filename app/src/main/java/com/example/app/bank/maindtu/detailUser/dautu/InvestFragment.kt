package com.example.app.bank.maindtu.detailUser.dautu

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.bank.base.BaseFragment
import com.example.app.bank.base.BaseFragmentContainer
import com.example.app.bank.data.model.User
import com.example.app.bank.extention.moneyFormat
import com.example.app.bank.maindtu.detailUser.dautu.link.InvestLinkFragment
import com.example.app.bank.maindtu.detailUser.dautu.sangiaodich.SGDFragment
import com.example.app.bank.maindtu.history.HistoryFragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_header_app.*
import kotlinx.android.synthetic.main.layout_invest.*


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

    private var firebase = FirebaseDatabase.getInstance().reference

    private var user = User()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            user = it.getParcelable(USER_CURRENT_DTU_INVEST)
        }
        return inflater.inflate(com.example.app.bank.R.layout.layout_invest, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTitleHeader.text = "Đầu tư"

        handleMoney()
        handleStateButton()
        imgClose.setOnClickListener {
            popBackStack()
        }
        tvTitleLink.setOnClickListener {
            replaceFragment(InvestLinkFragment.newInstance(user), true)
        }
        btnTransaction.setOnClickListener {
            replaceFragment(SGDFragment.newInstance(user), true, "apply_invest")
        }
        btnHistory.setOnClickListener {
            replaceFragment(HistoryFragment(), true)

        }

    }

    private fun handleStateButton() {
        btnTransaction.isEnabled = user.money != ""
    }


    private fun handleMoney() {
        parentFragment.let {
            if (it is BaseFragmentContainer) {
                println("xxxxx" + user.isCheckMoney)
                if (user.isCheckMoney) {
                    tvMoneyAsset.text = user.money.toInt().moneyFormat()
                } else {
                    println("xxxxx" + it.getMoney())
                    if (user.money != "" && it.getMoney() != "") {
                        val result = user.money.toInt() - it.getMoney().toInt()
                        val userUpdates = HashMap<String, String>()
                        val nameBank: String = if (user.bank == "Vietcombank") "vcb" else "bidv"

                        //update child money on firebase
                        val usersRef = firebase.child("listUser").child(user.key).child("linkbank").child(nameBank)
                        userUpdates["money"] = result.toString()
                        usersRef.updateChildren(userUpdates as Map<String, Any>)

                        tvMoneyAsset.text = result.moneyFormat()

                        //update user
                        user.apply {
                            result.toString().let { result ->
                                linkBank?.map { bank ->
                                    bank.money = result
                                }
                                money = result
                            }
                        }

                        //set Money in activity default
                        it.setMoney("")
                    } else {
                        if (user.money != "") tvMoneyAsset.text = user.money.toInt().moneyFormat()

                    }
                }
            }
        }
    }


    override fun onPause() {
        user.isCheckMoney = false
        super.onPause()
    }

    override fun onBindViewModel() {

    }
}

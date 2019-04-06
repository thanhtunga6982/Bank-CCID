package com.example.app.bank.main.borrowmoney.borrow

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class BorrowMoneyViewModel(var localRepository: LocalRepository) {
    var listUser = mutableListOf<User>()
    var name = ""
    var user = User()
    internal val listUserSubject = BehaviorSubject.create<MutableList<User>>()
    internal val stateButtonSubject = BehaviorSubject.create<Boolean>()
    private var firebase = FirebaseDatabase.getInstance().reference

    init {
        stateButtonSubject.onNext(false)
    }

    @SuppressLint("CheckResult")
    fun getUserList() {
        localRepository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listUser.clear()
                listUser.addAll(it)
                listUserSubject.onNext(listUser)
            }, {})
    }

    fun validateMoneyBorrow(text: String) {
        user.moneyBorrow = text
        validateForm()
    }

    fun validateMoneyAssetTax(text: String) {
        user.assettax = text
        validateForm()
    }

    fun validateMoneyDebtpaymentplan(text: String) {
        user.debtpaymentplan = text
        validateForm()
    }

    private fun validateForm() {
        stateButtonSubject.onNext(user.moneyBorrow.isNotBlank() && user.assettax.isNotBlank() && user.debtpaymentplan.isNotBlank())
    }

    internal fun handleUpdateUser(list: MutableList<User>) {
        for (i in 0 until list.size) {
            if (list[i].name == name) {
                val userUpdates = HashMap<String, String>()
                val usersRef = firebase.child("listUser")
                //getUser position
                list[i].run {
                    val user = User(
                        moneyBorrow = user.moneyBorrow,
                        debtpaymentplan = user.debtpaymentplan,
                        assettax = user.assettax
                    )
                    userUpdates["${list[i].key}/moneyborrow"] = user.moneyBorrow
                    userUpdates["${list[i].key}/debtpaymentplan"] = user.debtpaymentplan
                    userUpdates["${list[i].key}/assettax"] = user.assettax
                }
                usersRef.updateChildren(userUpdates as Map<String, Any>)
            }
        }
    }

}

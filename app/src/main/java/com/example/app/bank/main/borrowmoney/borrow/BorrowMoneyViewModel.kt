package com.example.app.bank.main.borrowmoney.borrow

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class BorrowMoneyViewModel(var localRepository: LocalRepository) {
    var name = ""
    var userbank = User()
    var listUser = mutableListOf(userbank)
    internal val stateButtonSubject = BehaviorSubject.create<Boolean>()
    private var firebase = FirebaseDatabase.getInstance().reference.child("listLending")

    init {
        stateButtonSubject.onNext(false)
    }

    @SuppressLint("CheckResult")
    fun getUserList() {
        localRepository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // map user to add
                it.mapIndexed { index, users ->
                    if (it[index].name == name) {
                        userbank.name = users.name
                        userbank.email = users.email
                        userbank.id = users.id
                    }
                }
            }, {})
    }

    fun validateMoneyBorrow(text: String) {
        userbank.moneyBorrow = text
        validateForm()
    }

    fun validateMoneyAssetTax(text: String) {
        userbank.assettax = text
        validateForm()
    }

    fun validateMoneyDebtpaymentplan(text: String) {
        userbank.debtpaymentplan = text
        validateForm()
    }

    private fun validateForm() {
        stateButtonSubject.onNext(userbank.moneyBorrow.isNotBlank() && userbank.assettax.isNotBlank() && userbank.debtpaymentplan.isNotBlank())
    }

    internal fun handleUpdateUser(list: MutableList<User>) {
        for (i in 0 until list.size) {
            if (list[i].name == name) {
                val id = firebase.push().key
                //getUser position
                list[i].run {
                    val user = User(
                        key = userbank.key,
                        id = userbank.id,
                        name = userbank.name,
                        email = userbank.email,
                        moneyBorrow = userbank.moneyBorrow,
                        debtpaymentplan = userbank.debtpaymentplan,
                        assettax = userbank.assettax,
                        totalasset = userbank.totalasset
                    )
                    firebase.child(id.toString()).setValue(user)
                }
            }
        }

    }
}

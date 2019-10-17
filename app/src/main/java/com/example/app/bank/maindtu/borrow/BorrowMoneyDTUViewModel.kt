package com.example.app.bank.maindtu.borrow

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class BorrowMoneyDTUViewModel(private var localRepository: LocalRepository) {

    var email = ""
    // object this to checkButton
    var userbank = User()
    //object this to get user info  to app
    var user = User()

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
                    if (it[index].email == user.email) {
                        user.name = users.name
                        user.email = users.email
                        user.id = users.id
                        user.totalasset = users.totalasset
                        email = user.email

                    }
                }
            }, {})
    }

    private fun checkButtonState() {
        stateButtonSubject.onNext(userbank.moneyBorrow.isNotEmpty() && userbank.interest.isNotEmpty() && userbank.timeBorrow.isNotEmpty() && userbank.debtpaymentplan.isNotEmpty())
    }

    fun validateMoneyBorrow(edtMoneyBorrow: String) {
        if (checkMoneyMillion(edtMoneyBorrow)) {
            userbank.moneyBorrow = edtMoneyBorrow
            checkButtonState()
        } else {
            userbank.moneyBorrow = ""
            checkButtonState()
        }
    }

    fun checkMoneyMillion(edtMoneyBorrow: String): Boolean {

        return edtMoneyBorrow.length > 7
    }

    fun validateInterest(edtInterest: String) {
        userbank.interest = edtInterest
        checkButtonState()
    }

    fun validateTimeBorrow(edtTime: String) {
        userbank.timeBorrow = edtTime
        checkButtonState()
    }

    fun handleDataSpinnerArea(textSelect: String) {
        user.address = textSelect
    }

    fun handleDataSpinnerAssetTax(textSelect: String) {
        user.assettax = textSelect
    }

    fun validatePlan(edtPlan: String) {
        userbank.debtpaymentplan = edtPlan
        checkButtonState()
    }

    internal fun handleUpdateUser(list: MutableList<User>) {
        for (i in 0 until list.size) {
            val id = firebase.push().key
            //getUser position
            if (list[i].email == email) {
                list[i].run {
                    val user = User(
                        key = user.key,
                        id = user.id,
                        name = user.name,
                        email = user.email,
                        moneyBorrow = userbank.moneyBorrow,
                        debtpaymentplan = userbank.debtpaymentplan,
                        assettax = user.assettax,
                        totalasset = user.totalasset,
                        timeBorrow = userbank.timeBorrow,
                        interest = userbank.interest,
                        address = user.address
                    )
                    firebase.child(id.toString()).setValue(user)
                }
            }
        }
    }
}

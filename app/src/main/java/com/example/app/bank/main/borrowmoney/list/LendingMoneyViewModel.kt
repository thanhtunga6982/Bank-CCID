package com.example.app.bank.main.borrowmoney.list

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class LendingMoneyViewModel(var localRepository: LocalRepository) {
    var listUser = mutableListOf<User>()
    var user = User()
    internal val listUserSubject = BehaviorSubject.create<MutableList<User>>()
    internal val stateButtonSubject = BehaviorSubject.create<Boolean>()
    private var firebase = FirebaseDatabase.getInstance().reference


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
}
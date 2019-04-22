package com.example.app.bank.main.borrowmoney.list

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class LendingMoneyViewModel(var localRepository: LocalRepository) {
    var listUser = mutableListOf<User>()
    internal var loadingSubject = BehaviorSubject.create<Boolean>()

    @SuppressLint("CheckResult")
    fun getUserLending(): Single<MutableList<User>> =
        localRepository.getUserLending()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)
            }
            .doOnSuccess {
                listUser.addAll(it)
            }
            .doFinally {
                loadingSubject.onNext(false)
            }
}

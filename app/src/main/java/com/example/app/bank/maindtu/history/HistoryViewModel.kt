package com.example.app.bank.maindtu.history

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class HistoryViewModel(var localRepository: LocalRepository) {

    internal var listHistory = mutableListOf<User>()
    internal var loadingSubject = BehaviorSubject.create<Boolean>()

    @SuppressLint("CheckResult")
    fun getUserHistory(): Single<MutableList<User>> =
        localRepository.getUserHistory()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)
            }
            .doOnSuccess {
                listHistory.addAll(it)
            }
            .doFinally {
                loadingSubject.onNext(false)
            }
}

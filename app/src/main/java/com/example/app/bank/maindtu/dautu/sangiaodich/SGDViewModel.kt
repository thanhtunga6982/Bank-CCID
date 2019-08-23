package com.example.app.bank.maindtu.dautu.sangiaodich

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class SGDViewModel(var localRepository : LocalRepository){

    internal var listUser = mutableListOf<User>()
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
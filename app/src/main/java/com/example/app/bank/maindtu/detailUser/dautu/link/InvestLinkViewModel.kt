package com.example.app.bank.maindtu.detailUser.dautu.link

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.Bank
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class InvestLinkViewModel(var localRepository: LocalRepository) {

    internal var listBank = mutableListOf<Bank>()
    internal var listUser = mutableListOf<User>()
    internal var loadingSubject = BehaviorSubject.create<Boolean>()
    @SuppressLint("CheckResult")
    fun getBank(): Single<MutableList<User>> =
        localRepository.getBank()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                loadingSubject.onNext(true)

            }
            .doOnError{
                //handle it
            }
            .doFinally {
                loadingSubject.onNext(false)
            }

}

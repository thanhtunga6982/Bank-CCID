package com.example.app.bank.main.profile.edit_profile

import android.annotation.SuppressLint
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class EditProfileBankViewModel(var localRepository: LocalRepository){
    private var listUser = mutableListOf<User>()

    internal var loadingSubject = BehaviorSubject.create<Boolean>()

    @SuppressLint("CheckResult")
    fun getUserList(): Single<MutableList<User>> = localRepository.getUser()
        .subscribeOn(Schedulers.io())
        .doOnSuccess {
            listUser.clear()
            listUser.addAll(it)
        }
        .doOnSubscribe {
            loadingSubject.onNext(true)
        }
        .doFinally {
            loadingSubject.onNext(false)
        }


}
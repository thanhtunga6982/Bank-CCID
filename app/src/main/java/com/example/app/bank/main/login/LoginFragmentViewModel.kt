package com.example.app.bank.main.login

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class LoginFragmentViewModel(var localRepository: LocalRepository) {
    private var listUser = mutableListOf<User>()
    internal val listUserSubject = BehaviorSubject.create<MutableList<User>>()
    var user = User()
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


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


}

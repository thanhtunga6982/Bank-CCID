package com.example.app.bank.main.login

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class LoginFragmentViewModel(var localRepository: LocalRepository) {
    private var listUser = mutableListOf<User>()
    internal val listUserSubject = BehaviorSubject.create<MutableList<User>>()
    var user = User()


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

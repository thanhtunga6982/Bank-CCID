package com.example.app.bank.maindtu.detailUser.dautu.sangiaodich

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.example.app.bank.data.LocalRepository
import com.example.app.bank.data.model.User
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File


class SGDViewModel(var localRepository: LocalRepository) {

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

    fun handleOpenPDF(fileName: String) {
        val file = File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName)
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(Uri.fromFile(file), "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
    }
}
package com.example.app.bank.main.detailUser

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class DetailUserViewModel() {
    internal val timerDelayProgressbar = BehaviorSubject.create<Boolean>()
    internal val timerDelayProgressbar2 = BehaviorSubject.create<Boolean>()

    init {
        timerDelayProgressbar.onNext(false)
        Observable.timer(5000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                timerDelayProgressbar.onNext(true)
            }, {})

    }

    internal fun autoDismissDialog() = Observable.timer(3000L, TimeUnit.MILLISECONDS)

    internal fun autoDismissDialog2() = Observable.timer(3000L, TimeUnit.MILLISECONDS)
    fun getTimer(){
        timerDelayProgressbar2.onNext(false)

        Observable.timer(5000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                timerDelayProgressbar2.onNext(true)
            }, {})
    }
}
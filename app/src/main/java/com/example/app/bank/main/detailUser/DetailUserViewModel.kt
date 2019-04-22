package com.example.app.bank.main.detailUser

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class DetailUserViewModel(){
    internal val timerDelayProgressbar = BehaviorSubject.create<Boolean>()

    init {
        timerDelayProgressbar.onNext(false)
        Observable.timer(5000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                timerDelayProgressbar.onNext(true)
            }, {})


    }
    internal fun autoDismissDialog() = Observable.interval(3000L, TimeUnit.MILLISECONDS)


}
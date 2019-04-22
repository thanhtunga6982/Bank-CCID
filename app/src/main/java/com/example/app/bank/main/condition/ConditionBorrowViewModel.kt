package com.example.app.bank.main.condition

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class ConditionBorrowViewModel() {
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
}

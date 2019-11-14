package com.example.app.bank.data.source

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 *
 * @author at-hoavo.
 */
object RxBus {
    private val publisher = PublishSubject.create<Any>()

    /**
     * Emit item to listen
     */
    fun publish(event: Any) {
        publisher.onNext(event)
    }

    /**
     * Listen should return an Observable and not the publisher
     * Using ofType we filter only events that match that class type
     */
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}

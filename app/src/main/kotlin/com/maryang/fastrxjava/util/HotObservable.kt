package com.maryang.fastrxjava.util

import android.util.Log
import com.maryang.fastrxjava.base.BaseApplication.Companion.TAG
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject


object HotObservable {

    fun example() {
        // Cold Ovservable
        Observable.just(true)
            .subscribe(object: DisposableObserver<Boolean>() {
                override fun onComplete() {

                }

                override fun onNext(t: Boolean) {

                }

                override fun onError(e: Throwable) {

                }
            })

        // Hot Observable
        // subject는 내부에 list<observer>가 있다
        // subscribe를 하면 list.add(observer)
        // onNExt를 하면 list.foreach{ observer.onNext() }
        val subject = PublishSubject.create<Int>()
        subject.subscribe() // 이벤트트 시작 여부와 관련이없다
        subject.onNext(1)
    }

    fun logConnectableObservable() {
        var count = 0
        val observable = Observable
            .range(0, 3)
            .timestamp()
            .map { timestamped ->
                Log.d(
                    TAG,
                    "_____________연산__________"
                )
                String.format("[%d] %d", timestamped.value(), timestamped.time())
            }
            .doOnNext { value -> count++ }
            .publish()
            //.refCount() -> 일반적인 Observable로 동작하게끔 해줌
            //.replay() 등 여러 함수가 있음

        observable.subscribe { value ->
            try {
                Thread.sleep(700)
                Log.d(TAG, "subscriber1 : $value")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        observable.subscribe { value ->
            try {
                Thread.sleep(10)
                Log.d(TAG, "subscriber2 : $value")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        observable.connect()

        Thread.sleep(100)

        observable.subscribe { value ->
            try {
                Thread.sleep(10)
                Log.d(TAG, "subscriber3 : $value")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun logAsyncSubject() {
        val subject = AsyncSubject.create<Int>()
        subject.subscribe {
            Log.d(TAG, "AsyncSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.subscribe {
            Log.d(TAG, "AsyncSubject subscriber2 value $it")
        }
        subject.onNext(2)
        subject.onNext(3)
        subject.onComplete()
    }

    fun logPublishSubject() {
        val subject = PublishSubject.create<Int>()
        subject.subscribe {
            Log.d(TAG, "PublishSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.subscribe {
            Log.d(TAG, "PublishSubject subscriber2 value $it")
        }
        Thread.sleep(100)
        subject.onNext(2)
        subject.onNext(3)
    }

    fun logBehaviorSubject() {
        val subject = BehaviorSubject.create<Int>()
        subject.subscribe {
            Log.d(TAG, "BehaviorSubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.onNext(2)
        subject.subscribe {
            Log.d(TAG, "BehaviorSubject subscriber2 value $it")
        }
        subject.onNext(3)
        subject.onNext(4)
    }

    fun logReplaySubject() {
        val subject = ReplaySubject.create<Int>()
        subject.subscribe {
            Log.d(TAG, "ReplaySubject subscriber1 value $it")
        }
        subject.onNext(1)
        subject.onNext(2)
        subject.subscribe {
            Log.d(TAG, "ReplaySubject subscriber2 value $it")
        }
        subject.onNext(3)
        subject.onNext(4)
    }
}

package com.example.nasaobjects

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NasaObjectUnitTest {
    private var disposable: CompositeDisposable = CompositeDisposable()

    @Test
    fun addition_isCorrect() {
        this.disposable.add(
                NasaService.getNasaObjects().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .doOnError {
                            fail(it.message)
                        }
                        .subscribe {
                            assertNotEquals(it.count(), 0)
                        })
    }
}
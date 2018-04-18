package proglife.com.ua.intellektiks.ui.base

import android.content.Context
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.MaybeTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import proglife.com.ua.intellektiks.App
import proglife.com.ua.intellektiks.di.activity.ActivityComponent

/**
 * Created by Evhenyi Shcherbyna on 22.03.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class BasePresenter<V: BaseView>: MvpPresenter<V>() {

    private var cd: CompositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        cd.clear()
        super.onDestroy()
    }

    private fun unSubscribeOnDestroy(disposable: Disposable) {
        cd.add(disposable)
    }

    fun <T> sAsync(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { this.unSubscribeOnDestroy(it)}
        }
    }

    fun <T> oAsync(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), true)
                    .doOnSubscribe { this.unSubscribeOnDestroy(it) }
        }
    }

    fun <T> mAsync(): MaybeTransformer<T, T> {
        return MaybeTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { this.unSubscribeOnDestroy(it) }
        }
    }

    protected fun injector(): ActivityComponent {
        return App.graph.activityComponent()
    }

}
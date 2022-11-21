package com.device.deviceinfosdk.rxjava;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author Nevio
 * on 2022/2/7
 */
public abstract class OnRxMainListener<T> implements Observer<T> {


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }


}

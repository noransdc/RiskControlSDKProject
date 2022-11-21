package com.device.deviceinfosdk.rxjava;


import androidx.annotation.NonNull;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * rxJava切换子线程进行耗时操作，完成后再回调主线程
 *
 * @author Nevio
 * on 2019/4/13
 **/
public class RxScheduler {

    private RxScheduler() {
    }


    public static <T> void execute(OnRxSubListener<T> subListener, OnRxMainListener<T> mainListener, Scheduler scheduler) {
        Scheduler mainScheduler;
        if (scheduler == null){
            mainScheduler = Schedulers.from(ExecutorManager.getInstance().getExecutor());
        } else {
            mainScheduler = scheduler;
        }

        Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Throwable {
                if (subListener != null) {
                    emitter.onNext(subListener.onSubThread());
//                    subListener.onSubComplete();
                }
                emitter.onComplete();
            }
        }).subscribeOn(mainScheduler)
                .observeOn(AndroidSchedulers.mainThread());
        if (mainListener != null) {
            observable.subscribe(mainListener);
        } else {
            observable.subscribe();
        }
    }

//    public static <T> void execute(OnRxSubComListener subListener, OnRxMainComListener mainListener, Scheduler scheduler) {
//        Scheduler mainScheduler;
//        if (scheduler == null){
//            mainScheduler = Schedulers.from(ExecutorManager.getInstance().getExecutor());
//        } else {
//            mainScheduler = scheduler;
//        }
//        Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Throwable {
//                if (subListener != null) {
////                    emitter.onNext(subListener.onSubNext());
//                    subListener.onSubComplete();
//                }
//                emitter.onComplete();
//            }
//        }).subscribeOn(mainScheduler)
//                .observeOn(AndroidSchedulers.mainThread());
//
//        if (mainListener != null) {
//            observable.subscribe(mainListener);
//        } else {
//            observable.subscribe();
//        }
//    }

    public static <T> void execute(OnRxSubListener<T> subListener) {
        execute(subListener, null, null);
    }

    public static <T> void execute(OnRxSubListener<T> subListener, OnRxMainListener<T> mainListener) {
        execute(subListener, mainListener, null);
    }

//    public static <T> void execute(OnRxSubComListener subListener) {
//        execute(subListener, null, null);
//    }
//
//    public static <T> void execute(OnRxSubComListener subListener, OnRxMainComListener mainListener) {
//        execute(subListener, mainListener, null);
//    }

    public static <T> void executeSingle(OnRxSubListener<T> subListener, OnRxMainListener<T> mainListener) {
        execute(subListener, mainListener, Schedulers.single());
    }

    public static <T> void executeSingle(OnRxSubListener<T> subListener) {
        executeSingle(subListener, null);
    }

    public static <T> void executeIo(OnRxSubListener<T> subListener, OnRxMainListener<T> mainListener) {
        execute(subListener, mainListener, Schedulers.io());
    }

    public static <T> void executeIo(OnRxSubListener<T> subListener) {
        executeIo(subListener, null);
    }

    public static <T> void executeComputation(OnRxSubListener<T> subListener, OnRxMainListener<T> mainListener) {
        execute(subListener, mainListener, Schedulers.computation());
    }

    public static <T> void executeComputation(OnRxSubListener<T> subListener) {
        executeComputation(subListener, null);
    }




}

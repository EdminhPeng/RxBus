package com.ming.rx;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * RxBus事件对象
 *
 * @author PengZhiming
 * @version 1.0
 * @since 2019/1/23
 */
public class RxBusEvent<T> {

    private Observable<T> observable;

    private Lifecycle lifecycle;

    RxBusEvent(Observable<T> observable) {
        this.observable = observable;
    }

    /**
     * 指定观察者线程
     *
     * @param scheduler 线程对象
     */
    public RxBusEvent<T> observeOn(Scheduler scheduler) {
        this.observable = this.observable.observeOn(scheduler);
        return this;
    }

    /**
     * 绑定Activity的生命周期，将会在{@link Activity#onDestroy()}取消监听
     *
     * @param activity Activity
     */
    public RxBusEvent<T> bindLifecycle(AppCompatActivity activity) {
        this.lifecycle = activity.getLifecycle();
        return this;
    }

    /**
     * 绑定Fragment的生命周期，将会在{@link Fragment#onDestroy()}取消监听
     *
     * @param fragment Fragment
     */
    public RxBusEvent<T> bindLifecycle(Fragment fragment) {
        this.lifecycle = fragment.getLifecycle();
        return this;
    }


    /**
     * 绑定生命周期
     *
     * @param lifecycle 寿命周期
     */
    public RxBusEvent<T> bindLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        return this;
    }

    /**
     * 注册消费者
     *
     * @param consumer 可自行注销的消费者
     * @return rx注销器
     */
    public Disposable register(final DisposableConsumer<T> consumer) {
        Disposable disposable = observable.subscribeWith(new DisposableObserver<T>() {
            @Override
            public void onNext(T t) {
                if (consumer != null) {
                    try {
                        consumer.accept(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onStart() {
                if (consumer != null) {
                    consumer.onSubscribe(this);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
        if (lifecycle != null) {
            RxLifecycleCompositor.getLifecycleObserver(lifecycle)
                    .addDisposable(disposable);
        }
        return disposable;
    }
}

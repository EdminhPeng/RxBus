package com.ming.rx;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 基于RxBus可取消的Consumer
 *
 * @author PengZhiming
 * @version 1.0
 * @since 2019/3/15
 */
public abstract class DisposableConsumer<T> implements Consumer<T> {

    private Disposable mDisposable;

    public final void onSubscribe(Disposable disposable) {
        this.mDisposable = disposable;
    }

    public final void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}

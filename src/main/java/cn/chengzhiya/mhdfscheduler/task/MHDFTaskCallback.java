package cn.chengzhiya.mhdfscheduler.task;

import java.util.concurrent.CompletableFuture;

public final class MHDFTaskCallback<T> implements CallBack<T> {
    private final CompletableFuture<Boolean> lock = new CompletableFuture<>();
    private T callback;

    @Override
    public T getCallBack() {
        this.lock.join();
        return this.callback;
    }

    @Override
    public void setCallBack(T callback) {
        this.lock.complete(true);
        this.callback = callback;
    }
}

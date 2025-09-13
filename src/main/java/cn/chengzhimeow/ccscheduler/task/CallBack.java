package cn.chengzhimeow.ccscheduler.task;

public interface CallBack<T> {
    T getCallBack();

    void setCallBack(T callback);
}

package cn.chengzhiya.mhdfscheduler.task;

public interface CallBack<T> {
    T getCallBack();

    void setCallBack(T callback);
}

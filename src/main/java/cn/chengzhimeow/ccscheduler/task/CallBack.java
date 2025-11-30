package cn.chengzhimeow.ccscheduler.task;

@SuppressWarnings("unused")
public interface CallBack<T> {
    T getCallBack();

    void setCallBack(T callback);
}

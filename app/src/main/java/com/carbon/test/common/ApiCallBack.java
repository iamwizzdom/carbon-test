package com.carbon.test.common;

public abstract class ApiCallBack {
    public abstract void onComplete();
    public abstract void onFailure(Throwable t);
}

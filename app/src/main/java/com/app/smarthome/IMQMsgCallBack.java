package com.app.smarthome;

/**
 * 作者:胡涛
 * 日期:2021-5-17
 * 时间:22:16
 * 功能:
 */
public interface IMQMsgCallBack {

    void connectIng();

    void connected();

    void connectFailed();

    void setMessage(String message);

    void onError(Throwable e);
}

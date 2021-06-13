package com.app.smarthome;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private MQTTService mqttService;
    private IMQMsgCallBack IMQMsgCallBack;

    public MyServiceConnection(com.app.smarthome.IMQMsgCallBack IMQMsgCallBack) {
        this.IMQMsgCallBack = IMQMsgCallBack;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder)iBinder).getService();
        mqttService.setImqMsgCallBack(IMQMsgCallBack);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mqttService.onDestroy();
    }

    public MQTTService getMqttService(){
        return mqttService;
    }

    public void setImqMsgCallBack(IMQMsgCallBack IMQMsgCallBack){
        this.IMQMsgCallBack = IMQMsgCallBack;
    }
}
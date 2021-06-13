package com.app.smarthome;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity implements IMQMsgCallBack {

    private static final String TAG = "MainActivity";
    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private SwitchCompat s1;
    private SwitchCompat s2;
    private AppCompatTextView tv1;
    private AppCompatTextView tv2;
    private AppCompatTextView tv3;
    private AppCompatTextView tv4;
    private TextView title;
    private Button btnShaChe;
    private MyServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //启动服务
        startMqttServer();
    }

    private void startMqttServer() {
        serviceConnection = new MyServiceConnection(this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ProgressDialog loadingDialog;

    public void handEvent(View view) {
        switch (view.getId()) {
            case R.id.up:
                serviceConnection.getMqttService().publish("1");
                break;
            case R.id.down:
                serviceConnection.getMqttService().publish("2");
                break;
            case R.id.left:
                serviceConnection.getMqttService().publish("3");
                break;
            case R.id.right:
                serviceConnection.getMqttService().publish("4");
                break;
            case R.id.s1:
                String status = s1.isChecked() ? "12" : "11";
                serviceConnection.getMqttService().publish(status);
                break;
            case R.id.s2:
                String status2 = s2.isChecked() ? "13" : "14";
                serviceConnection.getMqttService().publish(status2);
                break;
            case R.id.tv1:
                serviceConnection.getMqttService().publish("8");
                break;
            case R.id.tv2:
                serviceConnection.getMqttService().publish("7");
                break;
            case R.id.tv3:
                serviceConnection.getMqttService().publish("9"); 
                break;
            case R.id.tv4:
                serviceConnection.getMqttService().publish("10");
                break;
            case R.id.btnShaChe:
                serviceConnection.getMqttService().publish("-1");
                break;
            default:
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        title = findViewById(R.id.title);
        btnShaChe = findViewById(R.id.btnShaChe);
        up.setOnTouchListener(touchListener);
        down.setOnTouchListener(touchListener);
        left.setOnTouchListener(touchListener);
        right.setOnTouchListener(touchListener);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.i(TAG, "event =======> " + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (v.getId() == R.id.up) {
                        serviceConnection.getMqttService().publish("1");
                    } else if (v.getId() == R.id.down) {
                        serviceConnection.getMqttService().publish("2");
                    } else if (v.getId() == R.id.left) {
                        serviceConnection.getMqttService().publish("3");
                    } else if (v.getId() == R.id.right) {
                        serviceConnection.getMqttService().publish("4");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    serviceConnection.getMqttService().publish("-1");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    serviceConnection.getMqttService().publish("-1");
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public void connectIng() {
        loadingDialog = ProgressDialog.show(MainActivity.this, "状态更新", "正在连接服务器...");
        loadingDialog.show();
    }

    @Override
    public void connected() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void connectFailed() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void setMessage(String message) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        switch (message) {
            case "0":
                break;
            case "1":
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        serviceConnection.getMqttService().connect();
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }
}

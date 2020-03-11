package com.yw.eventbussimple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yw.eventbuslib.EventBus;
import com.yw.eventbuslib.Subscribe;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 注册观察者对象
         */
        EventBus.getDefault().register(this);
        btn = findViewById(R.id.main_btn);
        tv = findViewById(R.id.main_tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPost();
            }
        });

    }

    /**
     * 用于接收post发送过来的事件
     *
     * @param event 具体的事件主体
     */
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        switch (event.getArg1()) {
            case 0:
                tv.setText("接收小消息为：" + event.getMsg());
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 取消订阅
         */
        EventBus.getDefault().unRegister(this);
    }

    /**
     * 发送消息
     */
    public void toPost() {
        MessageEvent event = new MessageEvent();
        event.setArg1(0);
        event.setMsg("杨洛峋小宝宝来了");
        EventBus.getDefault().post(event);
    }

}

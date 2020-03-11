# myeventbuslib
仿eventbus，简化eventbus流程，让框架结构更加清晰

博客地址：https://www.cnblogs.com/tony-yang-flutter/p/12463490.html

用法：
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

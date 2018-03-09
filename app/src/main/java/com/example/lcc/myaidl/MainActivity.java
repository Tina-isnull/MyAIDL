package com.example.lcc.myaidl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mBindService;
    private Button mstartService;
    private IBookManagerUpdate mBookManager;
    private MyBook myBook = new MyBook();
    private List<MyBook> lists = new ArrayList<>();
    private NotificationManager manager;
    private Notification myNotification;
    private static final int NOTIFICATION_ID_1 = 1;
    private Bitmap largeIcon;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myBook = (MyBook) msg.obj;
            sendNotification(myBook);
            Log.d("Tina===>", "来新书了");
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBookManager = IBookManagerUpdate.Stub.asInterface(iBinder);
            try {
                lists.addAll(mBookManager.getBookList());
                Log.d("Tina===>", "书库中的书：" + lists.toString());
                MyBook book3 = new MyBook("撒哈拉的沙漠", "3");
                MyBook book4 = new MyBook("雨季不再来", "4");
                mBookManager.addBook(book3);
                mBookManager.addBook(book4);
                lists.addAll(mBookManager.getBookList());
                Log.d("Tina===>", "书库中的书：" + lists.toString());
                //注册通知事件
                mBookManager.registerListener((NotificationNewBook) mBinder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    //通知事件
    private IBinder mBinder = new NotificationNewBook.Stub() {
        @Override
        public void showNewBook(MyBook book) throws RemoteException {
            Message message = new Message();
            message.obj = book;
            mHandler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBindService = findViewById(R.id.bind_service);
        mstartService = findViewById(R.id.start_service);

        mBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent();
                serviceIntent.setAction("com.example.remote.BookService");
                serviceIntent.setPackage(MainActivity.this.getPackageName());
                bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
        });
        mstartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
    }
    private void sendNotification() {
        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle("最简单的Notification")
                //设置通知内容
                .setContentText("只有小图标、标题、内容");
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1
        notifyManager.notify(1, builder.build());
    }
    private void sendNotification(MyBook book) {
        largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.d_025)).getBitmap();
        //3.定义一个PendingIntent，点击Notification后启动一个Activity
        PendingIntent pi = PendingIntent.getActivity(
                this,
                100,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        //2.通过Notification.Builder来创建通知
        NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(MainActivity.this);
        myBuilder.setContentTitle("亲，有新书了")
                .setContentText("《" + book.getName() + "》点击开始阅读")
                .setTicker("您收到新的消息")
                .setLargeIcon(largeIcon)
                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置默认声音和震动
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)//点击后取消
                .setWhen(System.currentTimeMillis())//设置通知时间
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setContentIntent(pi);  //3.关联PendingIntent
        myNotification = myBuilder.build();
        //4.通过通知管理器来发起通知，ID区分通知
        manager.notify(NOTIFICATION_ID_1, myNotification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}

package com.example.lcc.myaidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc on 2018/3/7.
 */

public class BookService extends Service {
    private List<MyBook> books = new ArrayList<>();
    private List<NotificationNewBook> notificationNewBooks = new ArrayList<>();

    @Override
    public void onCreate() {
        books.clear();
        books.add(new MyBook("钢铁是怎么样炼成的", "1"));
        books.add(new MyBook("等风人", "2"));
        new Thread(mRunnable).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Tina===>", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private IBinder mBinder = new IBookManagerUpdate.Stub() {

        @Override
        public List<MyBook> getBookList() throws RemoteException {
            return books;
        }

        @Override
        public void addBook(MyBook book) throws RemoteException {
            books.add(book);

        }

        @Override
        public void registerListener(NotificationNewBook newBook) throws RemoteException {
            notificationNewBooks.add(newBook);
        }

        @Override
        public void unRegisterListener(NotificationNewBook newBook) throws RemoteException {
            if (notificationNewBooks.contains(newBook)) {
                notificationNewBooks.remove(newBook);
            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    int i = books.size();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000);
                    MyBook book = new MyBook("新书一本", i + "");
                    i++;
                    showNewBook(book);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void showNewBook(MyBook book) throws RemoteException {
        for (int i = 0; i < notificationNewBooks.size(); i++) {
            notificationNewBooks.get(i).showNewBook(book);
        }


    }
}

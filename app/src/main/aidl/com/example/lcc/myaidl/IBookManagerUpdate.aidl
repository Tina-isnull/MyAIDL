// IBookManagerUpdate.aidl
package com.example.lcc.myaidl;
import com.example.lcc.myaidl.MyBook;
import com.example.lcc.myaidl.NotificationNewBook;

interface IBookManagerUpdate {
List<MyBook> getBookList();
void addBook(in MyBook book);
void registerListener(in NotificationNewBook newBook);
void unRegisterListener(NotificationNewBook newBook);
}

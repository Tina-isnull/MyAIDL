// IBookManager.aidl
package com.example.lcc.myaidl;
import com.example.lcc.myaidl.MyBook;
//初级版本

interface IBookManager {
List<MyBook> getBookList();
void addBook(in MyBook book);
}

package com.example.lcc.myaidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lcc on 2018/3/2.
 */

public class MyBook implements Parcelable {
    private String name;
    private String index;

    public MyBook() {
    }

    public MyBook(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    protected MyBook(Parcel in) {
        name = in.readString();
        index = in.readString();

    }

    public static final Creator<MyBook> CREATOR = new Creator<MyBook>() {
        @Override
        public MyBook createFromParcel(Parcel in) {
            return new MyBook(in);
        }

        @Override
        public MyBook[] newArray(int size) {
            return new MyBook[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(index);
    }

    @Override
    public String toString() {
        return "MyBook{" +
                "name='" + name + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}

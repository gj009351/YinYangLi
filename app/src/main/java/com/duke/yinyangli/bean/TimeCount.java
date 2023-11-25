package com.duke.yinyangli.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

public class TimeCount implements Parcelable {

    private int year;
    private int month;
    private int day;
    private int count;

    public TimeCount(int year, int month, int day, int count) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.count = count;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    protected TimeCount(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeCount> CREATOR = new Creator<TimeCount>() {
        @Override
        public TimeCount createFromParcel(Parcel in) {
            return new TimeCount(in);
        }

        @Override
        public TimeCount[] newArray(int size) {
            return new TimeCount[size];
        }
    };
}

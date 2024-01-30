package com.jd.other.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
class Book : Parcelable {

    var name: String? = null
    var price: Int = 0

    constructor() {}

    constructor(`in`: Parcel) {
        name = `in`.readString()
        price = `in`.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(price)
    }

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * @param dest
     */
    fun readFromParcel(dest: Parcel) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        name = dest.readString()
        price = dest.readInt()
    }

    //方便打印数据
    override fun toString(): String {
        return "name : $name , price : $price"
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}

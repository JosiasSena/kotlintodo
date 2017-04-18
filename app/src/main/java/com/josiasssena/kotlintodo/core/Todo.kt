package com.josiasssena.kotlintodo.core

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * File created by josiassena on 4/11/17.
 */
@RealmClass
open class Todo(var title: String, var body: String) : RealmObject(), Parcelable {

    constructor() : this("", "")

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Todo> = object : Parcelable.Creator<Todo> {
            override fun createFromParcel(source: Parcel): Todo = Todo(source)
            override fun newArray(size: Int): Array<Todo?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(title)
        dest?.writeString(body)
    }
}
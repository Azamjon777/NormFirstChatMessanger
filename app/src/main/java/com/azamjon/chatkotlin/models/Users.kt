package com.azamjon.chatkotlin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Users(val name: String, val message: String): Parcelable {
    constructor() : this(name = "", message = "")
}
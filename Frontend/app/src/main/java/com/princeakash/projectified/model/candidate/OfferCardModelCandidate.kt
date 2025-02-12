package com.princeakash.projectified.model.candidate

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class OfferCardModelCandidate(val offer_id: String,
                                   val offer_name: String,
                                   val skills: String,
                                   val float_date: Date,
                                   val collegeName: String
                                   ) /*: Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(offer_id)
        parcel.writeString(offer_name)
        parcel.writeString(skills)
        parcel.writeString(float_date)
        parcel.writeString(collegeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfferCardModelCandidate> {
        override fun createFromParcel(parcel: Parcel): OfferCardModelCandidate {
            return OfferCardModelCandidate(parcel)
        }

        override fun newArray(size: Int): Array<OfferCardModelCandidate?> {
            return arrayOfNulls(size)
        }
    }
}*/
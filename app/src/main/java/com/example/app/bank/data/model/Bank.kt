package com.example.app.bank.data.model

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bank(
    @SerializedName("name") var name: String = "",
    @SerializedName("money") var money: String = "",
    @SerializedName("logo") var logo: String = ""

) : Parcelable {

    fun fromDataSnapshot(dataSnapshot: DataSnapshot): Bank? {
        val bank = dataSnapshot.getValue(Bank::class.java)
        return bank
    }

    fun fromDataSnapshottoList(snapshot: DataSnapshot): MutableList<Bank> {
        val listBank = mutableListOf<Bank>()
        snapshot.children.forEach {
            fromDataSnapshot(it)?.run {

                listBank.add(this)
            }
        }
        return listBank
    }


}
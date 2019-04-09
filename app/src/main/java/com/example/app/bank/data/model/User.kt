package com.example.app.bank.data.model

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @Exclude
    var key: String = "",
    @SerializedName("id")  var id: String = "",
    @SerializedName("name")  var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("moneyBorrow") var moneyBorrow: String = "",
    @SerializedName("debtpaymentplan") var debtpaymentplan: String = "",
    @SerializedName("assettax") var assettax: String = "",
    @SerializedName("totalasset") var totalasset: String = ""

) : Parcelable {
    fun fromDataSnapshot(dataSnapshot: DataSnapshot): User? {
        val user = dataSnapshot.getValue(User::class.java)
        user?.key = dataSnapshot.key.toString()
        return user
    }

    fun fromDataSnapshottoList(snapshot: DataSnapshot): MutableList<User> {
        val listUser = mutableListOf<User>()
        snapshot.children.forEach {
            fromDataSnapshot(it)?.run {
                listUser.add(this)
            }
        }
        return listUser
    }

}

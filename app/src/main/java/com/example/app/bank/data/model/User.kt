package com.example.app.bank.data.model

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    var key: String = "",
    var money: String = "",
    @SerializedName("id") var id: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("avatar") var avatar: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("moneyBorrow") var moneyBorrow: String = "",
    @SerializedName("debtpaymentplan") var debtpaymentplan: String = "",
    @SerializedName("assettax") var assettax: String = "",
    @SerializedName("totalasset") var totalasset: String = "",
    @SerializedName("sex") var sex: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("cmnd") var cmnd: String = "",
    @SerializedName("interest") var interest: String = "",
    @SerializedName("timeBorrow") var timeBorrow: String = "",
    @SerializedName("linkbank")var linkBank: MutableList<Bank>? = mutableListOf()

) : Parcelable {
    fun fromDataSnapshot(dataSnapshot: DataSnapshot): User? {
        val user = dataSnapshot.getValue(User::class.java)
        user?.linkBank = Bank().fromDataSnapshottoList(dataSnapshot.child("linkbank"))
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

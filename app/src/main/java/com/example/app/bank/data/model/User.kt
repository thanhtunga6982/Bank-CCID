package com.example.app.bank.data.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable

data class User(
    @Exclude
    var key: String = "",
    @set:PropertyName("id")
    @get:PropertyName("id")
    var id: String? = null,
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String = "",
    @set:PropertyName("email")
    @get:PropertyName("email")
    var email: String = "",
    @set:PropertyName("moneyBorrow")
    @get:PropertyName("moneyBorrow")
    var moneyBorrow: String = "",
    @set:PropertyName("password")
    @get:PropertyName("password")
    var password: String? = null,
    @set:PropertyName("debtpaymentplan")
    @get:PropertyName("debtpaymentplan")
    var debtpaymentplan: String = "",
    @set:PropertyName("assettax")
    @get:PropertyName("assettax")
    var assettax: String = "",
    @set:PropertyName("totalasset")
    @get:PropertyName("totalasset")
    var totalasset: String = ""

) : Serializable {
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

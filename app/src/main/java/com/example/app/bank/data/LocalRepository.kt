package com.example.app.bank.data

import android.annotation.SuppressLint
import android.content.Context
import com.example.app.bank.data.model.User
import com.example.app.bank.data.source.RemoteDataSouce
import com.example.app.bank.extention.ShareReferences
import com.example.app.bank.extention.set
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import io.reactivex.Single


class LocalRepository(var context: Context?) : RemoteDataSouce {

    private val sharePref = ShareReferences.defaultPrefs(context)

    override fun getUser(): Single<MutableList<User>> {
        return Single.create<MutableList<User>> {
            FirebaseDatabase.getInstance().reference
                .child("listUser")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        it.onError(error.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val newsList = User().fromDataSnapshottoList(snapshot)
                        it.onSuccess(newsList)
                    }
                })
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun setToken(token: String) {
        sharePref["token"] = token
    }

    fun saveUser(user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        sharePref["user"] = json
    }

    fun getUserLocal(): User {
        val gson = Gson()
        val json = sharePref.getString("user", "")
        val user: User = gson.fromJson(json, User::class.java)
        return user
    }

    fun getToken(): String {
        return sharePref.getString("token", "") ?: ""
    }

    override fun getUserLending(): Single<MutableList<User>> {
        return Single.create<MutableList<User>> {
            FirebaseDatabase.getInstance().reference
                .child("listLending")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        it.onError(error.toException())
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val newsList = User().fromDataSnapshottoList(snapshot)
                        it.onSuccess(newsList)
                    }
                })
        }
    }
}

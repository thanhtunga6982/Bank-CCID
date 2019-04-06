package com.example.app.bank.data

import com.example.app.bank.data.model.User
import com.example.app.bank.data.source.RemoteDataSouce
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

class LocalRepository() : RemoteDataSouce {
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
                        println("TTTTTnewList$newsList")
                        it.onSuccess(newsList)
                    }
                })
        }
    }
}

package com.example.app.bank.data.source

import com.example.app.bank.data.model.User
import io.reactivex.Single

interface RemoteDataSouce {

    fun getUser(): Single<MutableList<User>>

}
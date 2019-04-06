package com.example.app.bank.extention

import java.util.*

fun Int.moneyFormat(): String? = String.format(Locale.ENGLISH, "%,d", this)

package com.example.app.bank.extention

fun String.parseToInt() :Int {
    return try {
        this.toInt()
    } catch (e: NumberFormatException) {
        0
    }
}
package com.example.app.bank.extention

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


object ShareReferences {

    fun defaultPrefs(context: Context?): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }
}

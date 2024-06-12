package com.example.muvi_app.ui.customView

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyPasswordText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString().length < 8) {
            setError("Password tidak boleh kurang dari 8 karakter", null)
        } else {
            error = null
        }
    }
}
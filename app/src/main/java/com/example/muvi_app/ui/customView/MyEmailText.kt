package com.example.muvi_app.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class MyEmailText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            setError("Email tidak valid", null)
        } else {
            error = null
        }
    }
}
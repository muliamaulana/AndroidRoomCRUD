package com.muliamaulana.tangutang.utils

import android.text.Editable
import android.text.TextWatcher
import java.text.NumberFormat
import java.util.*

class CurrencyTextWatcher : TextWatcher {
    private var mEditing = false

    override fun afterTextChanged(s: Editable) {
        if (!mEditing) {
            mEditing = true

            val locale = Locale("id", "ID")
            val formatter = NumberFormat.getCurrencyInstance(locale)
            val currencySymbol = formatter.currency?.symbol
            val replaceable = String.format("[Rp,.\\s]", currencySymbol)
            val cleanString = s.toString().replace(replaceable.toRegex(), "")

            val parsed: Double = try {
                cleanString.toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }

            formatter.maximumFractionDigits = 0
            formatter.isParseIntegerOnly = true
            val formatted = formatter.format(parsed)

            s.replace(0, s.length, formatted)

            mEditing = false
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}
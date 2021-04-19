@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.muliamaulana.tangutang.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.muliamaulana.tangutang.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private var datePickerDialog: DatePickerDialog? = null
    private var timePickerDialog: TimePickerDialog? = null
    private val locale = Locale("id", "ID")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
    private val timeFormat = SimpleDateFormat("HH:mm", locale)

    @SuppressLint("ConstantLocale")
    val timestampFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US)

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return dateFormat.format(calendar.time)
    }

    fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return timeFormat.format(calendar.time)
    }

    fun displayDate(timestamp: String?): String {
        val date = timestampFormat.parse(timestamp)
        return dateFormat.format(date)
    }

    fun displayTime(timestamp: String?): String {
        val time = timestampFormat.parse(timestamp)
        return timeFormat.format(time)
    }

    fun getTimeStamp(date: String, time: String?): String {
        val dateSplit = date.split("-".toRegex()).toTypedArray()
        val year = dateSplit[0].toInt()
        val month = dateSplit[1].toInt() - 1
        val day = dateSplit[2].toInt()

        val calendar = Calendar.getInstance()

        if (time != null) {
            val timeSplit = time.split(":".toRegex()).toTypedArray()
            val hour = timeSplit[0].toInt()
            val minute = timeSplit[1].toInt()
            calendar.set(year, month, day, hour, minute, 0)
        } else {
            calendar.set(year, month, day, 0, 0, 0)
        }

        return calendar.time.toString()
    }

    fun showDatePicker(context: Context, input: TextInputEditText) {

        val c = Calendar.getInstance()

        if (!input.text.isNullOrEmpty()) {
            c.time = dateFormat.parse(input.text.toString())
        }

        datePickerDialog = datePickerAction(context, c, input)
    }

    fun showTimePicker(context: Context, input: TextInputEditText) {
        val c = Calendar.getInstance()
        if (!input.text.isNullOrEmpty()) {
            c.time = timeFormat.parse(input.text.toString())
        }

        timePickerDialog = timePickerAction(context, c, input)
    }

    private fun timePickerAction(
        context: Context,
        calendar: Calendar,
        textInput: TextInputEditText
    ): TimePickerDialog {
        val timePickerDialog = TimePickerDialog(
            context, { _, hour, minute ->
                // set day of month , month and year value in the edit text
                val hourPicked = if (hour < 10) "0${hour}" else hour
                val minutePicked = if (minute < 10) "0${minute}" else minute
                val timePicked = "$hourPicked:$minutePicked"
                val c = Calendar.getInstance()
                c.time = timeFormat.parse(timePicked)

                textInput.setText(timeFormat.format(c.time))
            },
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
        return timePickerDialog
    }

    private fun datePickerAction(
        context: Context,
        calendar: Calendar,
        textInput: TextInputEditText
    ): DatePickerDialog {

        val datePickerDialog = DatePickerDialog(
            context, { _, year, monthOfYear, dayOfMonth ->
                // set day of month , month and year value in the edit text
                val monthPicked =
                    if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else monthOfYear + 1
                val dayPicked = if (dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth
                val datePicked = "$year-$monthPicked-$dayPicked"
                val c = Calendar.getInstance()
                c.time = dateFormat.parse(datePicked)

                textInput.setText(dateFormat.format(c.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
        return datePickerDialog
    }

    fun currencyValue(string: String?): Long {
        return if (string != null) {
            val removeSymbol = string.replace("Rp ", "").replace("Rp", "")
            val removeSeparator = removeSymbol.replace(".", "")
            removeSeparator.toLong()
        } else 0
    }

    fun currencyDisplay(amount: Long?): String? {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }

    fun showConfirmDialog(
        context: Context,
        title: String?,
        message: String?,
        clickListener: DialogInterface.OnClickListener? = null) {
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.getString(R.string.yes_label), clickListener)
        builder.setNegativeButton(context.getString(R.string.no_label), clickListener)

        val dialog = builder.create()
        if (clickListener != null) {
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }

        dialog.show()
    }
}
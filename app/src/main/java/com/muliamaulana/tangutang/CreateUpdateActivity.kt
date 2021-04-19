package com.muliamaulana.tangutang

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muliamaulana.tangutang.db.DebtDao
import com.muliamaulana.tangutang.db.DebtRoomDatabase
import com.muliamaulana.tangutang.model.Debt
import com.muliamaulana.tangutang.utils.CurrencyTextWatcher
import com.muliamaulana.tangutang.utils.Utils
import kotlinx.android.synthetic.main.activity_create_update.*
import java.util.*

class CreateUpdateActivity : AppCompatActivity() {

    private lateinit var database: DebtRoomDatabase
    private lateinit var dao: DebtDao
    private lateinit var debt: Debt
    private var isEdit: Boolean = false
    private var debtId: Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val itemType = listOf(getString(R.string.borrow_label), getString(R.string.lent_label))
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemType)

        database = DebtRoomDatabase.getDatabase(this)
        dao = database.debtDao()

        setupListener()

        button_delete.setOnClickListener {
            Utils.showConfirmDialog(this, getString(R.string.delete_label), getString(R.string.delete_message)) { dialog, event ->
                when (event) {
                    /*Positive Button*/
                    -1 -> {
                        dialog.dismiss()
                        dao.delete(debt)
                        Toast.makeText(this, getString(R.string.item_deleted_message), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    /*Negative Button*/
                    else -> dialog.dismiss()
                }
            }
        }

        isEdit = intent.getBooleanExtra("isEdit", false)
        debtId = intent.getIntExtra("id", 0)

        if (isEdit) {
            supportActionBar?.title = getString(R.string.edit_item_label)

            layout_update.visibility = View.VISIBLE

            debt = dao.getById(debtId)
            if (debt.type == 0) {
                input_spinner_type.setText(itemType[0])
            } else if (debt.type == 1) {
                input_spinner_type.setText(itemType[1])
            }

            input_spinner_type.setAdapter(adapter)
            input_name.setText(debt.name)
            input_amount.setText(debt.amount.toString())
            input_date.setText(Utils.displayDate(debt.date))
            input_time.setText(Utils.displayTime(debt.date))
            input_due_date.setText(Utils.displayDate(debt.dueDate))
            input_note.setText(debt.note)

            if (debt.isPaid) {
                checkbox_paid_off.text = "${getString(R.string.paid_off_label)} ${Utils.displayDate(debt.paidDate)} ${Utils.displayTime(debt.paidDate)}"
                checkbox_paid_off.isChecked = true
            } else {
                checkbox_paid_off.text = getString(R.string.paid_off_label)
                checkbox_paid_off.isChecked = false
            }

            checkbox_paid_off.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && debt.isPaid) {
                    checkbox_paid_off.text = "${getString(R.string.paid_off_label)} ${Utils.displayDate(debt.paidDate)} ${Utils.displayTime(debt.paidDate)}"
                } else checkbox_paid_off.text = getString(R.string.paid_off_label)
            }

        } else {
            supportActionBar?.title = getString(R.string.add_new_label)
            layout_update.visibility = View.GONE
            input_spinner_type.setText(itemType[0]) // set default value
            input_spinner_type.setAdapter(adapter)
        }

    }

    private fun setupListener() {
        input_date.setText(Utils.getCurrentDate())
        input_date.setOnClickListener {
            Utils.showDatePicker(this, input_date)
        }

        input_time.setText(Utils.getCurrentTime())
        input_time.setOnClickListener {
            Utils.showTimePicker(this, input_time)
        }

        input_due_date.setText(Utils.getCurrentDate())
        input_due_date.setOnClickListener {
            Utils.showDatePicker(this, input_due_date)
        }

        input_amount.addTextChangedListener(CurrencyTextWatcher())
        input_amount.setRawInputType(InputType.TYPE_CLASS_NUMBER)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.create_update_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_save -> {
                saveRecord()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveRecord() {
        if (isInputValid()) {

            val typeString = input_spinner_type.text.toString()
            val typeValue = if (typeString == getString(R.string.borrow_label)) 0 else 1 // 0 is borrow, 1 is lent
            val dateString = input_date.text.toString()
            val timeString = input_time.text.toString()
            val timestamp = Utils.getTimeStamp(dateString, timeString)
            val name = input_name.text.toString()
            val amount = Utils.currencyValue(input_amount.text.toString())
            val dueDate = Utils.getTimeStamp(input_due_date.text.toString(), null)
            val note = input_note.text.toString()
            val isPaid = checkbox_paid_off.isChecked

            val calendar = Calendar.getInstance()
            val paidDate = if (isPaid) {
                if (debt.paidDate != null) {
                    debt.paidDate
                } else calendar.time.toString()
            } else null

            if (isEdit) {
                val debt = Debt(id = debtId, name = name, amount = amount, note = note, date = timestamp, dueDate = dueDate, isPaid = isPaid, paidDate = paidDate, type = typeValue)
                dao.update(debt)
            } else {
                val debt = Debt(name = name, amount = amount, note = note, date = timestamp, dueDate = dueDate, isPaid = isPaid, paidDate = paidDate, type = typeValue)
                dao.insert(debt)
            }

            finish()
        }
    }

    // Input Validation
    private fun isInputValid(): Boolean {
        input_layout_type.isErrorEnabled = false
        input_layout_date.isErrorEnabled = false
        input_layout_time.isErrorEnabled = false
        input_layout_name.isErrorEnabled = false
        input_layout_amount.isErrorEnabled = false
        input_layout_due_date.isErrorEnabled = false
        input_layout_note.isErrorEnabled = false

        return when {
            input_spinner_type.text.isNullOrEmpty() -> {
                input_layout_type.isErrorEnabled = true
                input_layout_type.error = getString(R.string.field_required)
                false
            }
            input_date.text.isNullOrEmpty() -> {
                input_layout_date.isErrorEnabled = true
                input_layout_date.error = getString(R.string.field_required)
                false
            }
            input_time.text.isNullOrEmpty() -> {
                input_layout_time.isErrorEnabled = true
                input_layout_time.error = getString(R.string.field_required)
                false
            }
            input_name.text.isNullOrEmpty() -> {
                input_layout_name.isErrorEnabled = true
                input_layout_name.error = getString(R.string.field_required)
                false
            }
            input_amount.text.isNullOrEmpty() -> {
                input_layout_amount.isErrorEnabled = true
                input_layout_amount.error = getString(R.string.field_required)
                false
            }
            input_due_date.text.isNullOrEmpty() -> {
                input_layout_due_date.isErrorEnabled = true
                input_layout_due_date.error = getString(R.string.field_required)
                false
            }
            input_note.text.isNullOrEmpty() -> {
                input_layout_note.isErrorEnabled = true
                input_layout_note.error = getString(R.string.field_required)
                false
            }
            else -> true
        }
    }
}
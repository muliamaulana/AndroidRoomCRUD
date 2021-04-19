package com.muliamaulana.tangutang

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.muliamaulana.tangutang.model.Debt
import com.muliamaulana.tangutang.utils.Utils
import kotlinx.android.synthetic.main.item_layout.view.*

class DebtAdapter(private val items: MutableList<Debt>) : RecyclerView.Adapter<DebtViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return DebtViewHolder(view)
    }

    override fun onBindViewHolder(holder: DebtViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class DebtViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Debt) {

        if (item.type == 0) {
            itemView.item_type.text = itemView.context.resources.getString(R.string.borrow_label)
            itemView.item_type.setBackgroundResource(R.drawable.rounded_borrow)
        } else {
            itemView.item_type.text = itemView.context.resources.getString(R.string.lent_label)
            itemView.item_type.setBackgroundResource(R.drawable.rounded_lent)
        }

        itemView.item_name.text = item.name
        itemView.item_amount.text = Utils.currencyDisplay(item.amount)
        itemView.item_note.text = item.note
        itemView.item_date.text = "${Utils.displayDate(item.date)} ${Utils.displayTime(item.date)}"

        if (item.isPaid) {
            itemView.item_is_paid.text = itemView.resources.getString(R.string.paid_off_label)
            itemView.item_is_paid.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark))
            itemView.item_due_date.text = "${Utils.displayDate(item.paidDate)} ${Utils.displayTime(item.paidDate)}"
        } else {
            itemView.item_is_paid.text = itemView.context.getString(R.string.unpaid_label)
            itemView.item_is_paid.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
            itemView.item_due_date.text = "${itemView.context.resources.getString(R.string.due_date_label)} ${Utils.displayDate(item.dueDate)}"
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, CreateUpdateActivity::class.java)
            intent.putExtra("id", item.id)
            intent.putExtra("isEdit", true)
            itemView.context.startActivity(intent)
        }
    }
}

package com.muliamaulana.tangutang.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity annotation to specify the table's name
@Entity(tableName = "debt")

data class Debt(
    //PrimaryKey annotation to declare primary key
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "amount") var amount: Long?,
    @ColumnInfo(name = "note") var note: String?,
    @ColumnInfo(name = "type") var type: Int?,
    @ColumnInfo(name = "date") var date: String?,
    @ColumnInfo(name = "due_date") var dueDate: String?,
    @ColumnInfo(name = "is_paid") var isPaid: Boolean = false,
    @ColumnInfo(name = "paid_date") var paidDate: String?,
)
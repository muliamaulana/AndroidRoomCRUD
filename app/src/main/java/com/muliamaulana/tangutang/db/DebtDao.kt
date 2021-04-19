package com.muliamaulana.tangutang.db

import androidx.room.*
import com.muliamaulana.tangutang.model.Debt

@Dao
interface DebtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(debt: Debt)

    @Update
    fun update(debt: Debt)

    @Delete
    fun delete(debt: Debt)

    @Query("SELECT * FROM debt")
    fun getAll() : MutableList<Debt>

    @Query("SELECT * FROM debt WHERE id = :id")
    fun getById(id: Int) : Debt

}
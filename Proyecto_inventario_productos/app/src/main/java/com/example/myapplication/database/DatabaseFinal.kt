package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.DAO.ProductoDAO
import com.example.myapplication.model.Producto

@Database(entities = [Producto::class], version = 1, exportSchema = false)
abstract class DatabaseFinal: RoomDatabase() {
    abstract fun productoDAO(): ProductoDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseFinal? = null

        fun getDatabase(context: Context): DatabaseFinal {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseFinal::class.java,
                    "ProyectoFinalDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
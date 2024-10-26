package com.example.myapplication.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.Producto

@Dao
interface ProductoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: Producto) : Unit

    @Query("SELECT * FROM producto_table")
    suspend fun getAllProductos() : List<Producto>

    @Query("SELECT * FROM producto_table WHERE title LIKE :cadena")
    suspend fun buscarProductos(cadena : String) : List<Producto>

    @Query("SELECT * FROM producto_table WHERE id LIKE :prodId")
    suspend fun buscarProductosPorId(prodId : Int) : Producto

    @Delete
    suspend fun delete(producto: Producto) : Unit

    @Update
    suspend fun update(producto: Producto): Unit
}
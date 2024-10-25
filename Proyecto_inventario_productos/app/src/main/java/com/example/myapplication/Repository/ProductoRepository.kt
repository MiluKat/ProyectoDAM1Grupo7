package com.example.myapplication.Repository

import com.example.myapplication.model.Producto

interface ProductoRepository {
    suspend fun getProductos(): List<Producto>
}
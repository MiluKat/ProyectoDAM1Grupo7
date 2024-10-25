package com.example.myapplication.Repository

import android.util.Log
import com.example.myapplication.Factory.ProductoFactory
import com.example.myapplication.model.Producto
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductoRepositoryImpl: ProductoRepository {
    override suspend fun getProductos(): List<Producto> {
        try {
            val response = ProductoFactory.client.get("https://fakestoreapi.com/products")
            return response.body()
        } catch (e: Exception) {
            Log.e("Error RepositoryImpl", "El error es: ${e.message}")
            return emptyList()
        }
    }
}
package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.DAO.ProductoDAO
import com.example.myapplication.database.DatabaseFinal
import com.example.myapplication.databinding.ActivityAgregarProductoBinding
import com.example.myapplication.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarProducto : AppCompatActivity() {
    private lateinit var productoDAO: ProductoDAO
    private lateinit var database: DatabaseFinal
    private lateinit var binding: ActivityAgregarProductoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = DatabaseFinal.getDatabase(this)
        productoDAO = database.productoDAO()
        val productId: Int = intent.getIntExtra("productoId", -1)

        if (productId == -1) {
            setupAddProductUI()
        } else {
            setupEditProductUI(productId)
        }
    }

    private fun setupAddProductUI() {
        binding.txvTitulo.text = "Agregar Producto"
        binding.btnAgregar.text = "Agregar"

        binding.btnAgregar.setOnClickListener {
            val newProducto = Producto().apply {
                title = binding.etNombreProducto.text.toString().trim()
                description = binding.etDescripcionProducto.text.toString().trim()
                category = binding.etCategorAProducto.text.toString().trim()
                image = binding.etImg.text.toString().trim()
                price = binding.etPrecioProducto.text.toString().toDoubleOrNull() ?: 0.0
            }

            if ((newProducto.title?.isNotEmpty()?:false) && (newProducto.price?:0.0) > 0) {
                createProduct(newProducto) { isCreated ->
                    if (isCreated) {
                        val intent = Intent(this, UserRoom::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al crear producto", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditProductUI(productId: Int) {
        binding.txvTitulo.text = "Editar Producto"
        binding.btnAgregar.text = "Grabar"

        buscarProducto(productId) { producto ->
            if (producto != null) {
                binding.etNombreProducto.setText(producto.title)
                binding.etDescripcionProducto.setText(producto.description)
                binding.etCategorAProducto.setText(producto.category)
                binding.etImg.setText(producto.image)
                binding.etPrecioProducto.setText(producto.price.toString())

                binding.btnAgregar.setOnClickListener {
                    val updatedProducto = producto.apply {
                        title = binding.etNombreProducto.text.toString().trim()
                        description = binding.etDescripcionProducto.text.toString().trim()
                        category = binding.etCategorAProducto.text.toString().trim()
                        image = binding.etImg.text.toString().trim()
                        price = binding.etPrecioProducto.text.toString().toDoubleOrNull() ?: 0.0
                    }

                    if ((updatedProducto.title?.isNotEmpty() ?: false) && (updatedProducto.price?:0.0) > 0.0) {
                        actualizarProducto(updatedProducto) { isUpdated ->
                            if (isUpdated) {
                                val intent = Intent(this, UserRoom::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error: Producto no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createProduct(producto: Producto, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var isCreated = false
            try {
                productoDAO.insertProducto(producto)
                isCreated = true
            } catch (e: Exception) {
                Log.w("Error al crear producto", e.toString())
            }

            withContext(Dispatchers.Main) {
                onResult(isCreated)
            }
        }
    }

    private fun buscarProducto(productoId: Int, onResult: (Producto?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var producto: Producto? = null
            try {
                producto = productoDAO.buscarProductosPorId(productoId)
            } catch (e: Exception) {
                Log.w("Error al buscar producto", e.toString())
            }

            withContext(Dispatchers.Main) {
                onResult(producto)
            }
        }
    }

    private fun actualizarProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var isUpdated = false
            try {
                productoDAO.update(producto)
                isUpdated = true
            } catch (e: Exception) {
                Log.w("Error al actualizar producto", e.toString())
            }

            withContext(Dispatchers.Main) {
                onResult(isUpdated)
            }
        }
    }
}

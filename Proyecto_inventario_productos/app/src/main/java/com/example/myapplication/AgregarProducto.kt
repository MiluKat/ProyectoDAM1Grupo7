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
import com.example.myapplication.databinding.ProductListBinding
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
        var productId: Int = intent.getIntExtra("productoId", -1)
        Log.w("ProdId",productId.toString())
        if(productId == -1) {
            binding.txvTitulo.text = "Agregar Producto"
            binding.btnAgregar.text = "Agregar"
            binding.btnAgregar.setOnClickListener {
                var newProducto: Producto = Producto()
                var title: String = binding.etNombreProducto.text.trim().toString()
                var description: String = binding.etDescripcionProducto.text.trim().toString()
                var category: String = binding.etCategorAProducto.text.trim().toString()
                var image: String = binding.etImg.text.trim().toString()
                var price: Double = binding.etPrecioProducto.text.trim().toString().toDouble()

                if (title.trim().isEmpty() ||
                    description.trim().isEmpty() ||
                    category.trim().isEmpty() ||
                    image.trim().isEmpty() ||
                    price <= 0
                ) {
                    Toast.makeText(this, "Error al ingresar los campos", Toast.LENGTH_SHORT).show()
                } else {
                    newProducto.title = title
                    newProducto.description = description
                    newProducto.category = category
                    newProducto.image = image
                    newProducto.price = price
                    var isCreated: Boolean = createProduct(newProducto)
                    if (isCreated) {
                        val intent = Intent(this, UserRoom::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al crear producto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            binding.txvTitulo.text = "Editar Producto"
            binding.btnAgregar.text = "Editar"
            var productoToUpdate: Producto = buscarProducto(productId)
            binding.etNombreProducto.setText(productoToUpdate.title.toString())
            binding.etDescripcionProducto.setText(productoToUpdate.description.toString())
            binding.etCategorAProducto.setText(productoToUpdate.category.toString())
            binding.etImg.setText(productoToUpdate.image.toString())
            binding.etPrecioProducto.setText(productoToUpdate.price.toString())
            binding.btnAgregar.setOnClickListener {
                var title: String = binding.etNombreProducto.text.trim().toString()
                var description: String = binding.etDescripcionProducto.text.trim().toString()
                var category: String = binding.etCategorAProducto.text.trim().toString()
                var image: String = binding.etImg.text.trim().toString()
                var price: Double =0.0
                try {
                    price = binding.etPrecioProducto.text.trim().toString().toDouble()
                }catch (e:Exception){
                    Toast.makeText(this, "Error al ingresar el precio", Toast.LENGTH_SHORT).show()
                }


                if (title.trim().isEmpty() ||
                    description.trim().isEmpty() ||
                    category.trim().isEmpty() ||
                    image.trim().isEmpty() ||
                    price <= 0
                ) {
                    Toast.makeText(this, "Error al ingresar los campos", Toast.LENGTH_SHORT).show()
                } else {
                    productoToUpdate.title = title
                    productoToUpdate.description = description
                    productoToUpdate.category = category
                    productoToUpdate.image = image
                    productoToUpdate.price = price
                    var isUpdated: Boolean = actualizarProducto(productoToUpdate)
                    if (isUpdated) {
                        val intent = Intent(this, UserRoom::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun createProduct(producto: Producto): Boolean{
        var isCreated : Boolean = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                productoDAO.insertProducto(producto)
                isCreated =true
            }catch (e : Exception){
                Log.w("Ocurrio un error al registrar", e.toString())
            }
        }
        return isCreated
    }

    fun buscarProducto(productoId: Int): Producto{
        Log.w("ProdId",productoId.toString())
        var producto : Producto = Producto()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                producto = productoDAO.buscarProductosPorId(productoId)
            }catch (e : Exception){
                Log.w("Register", e.toString())
            }
        }
        return producto
    }

    fun actualizarProducto(producto: Producto): Boolean{
        var isUpdated : Boolean = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                productoDAO.update(producto)
                isUpdated = true
            }catch (e : Exception){
                Log.w("Ocurrio un error al registrar", e.toString())
            }
        }
        return isUpdated
    }
}
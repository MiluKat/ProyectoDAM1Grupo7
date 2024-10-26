package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.DAO.ProductoDAO
import com.example.myapplication.database.DatabaseFinal
import com.example.myapplication.databinding.ActivityVerProductoBinding
import com.example.myapplication.model.Producto
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerProducto : AppCompatActivity() {
    private lateinit var productoDAO:ProductoDAO
    private lateinit var database: DatabaseFinal
    private lateinit var binding: ActivityVerProductoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerProductoBinding.inflate((layoutInflater))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = DatabaseFinal.getDatabase(this)
        productoDAO = database.productoDAO()
        var productId: Int = intent.getIntExtra("productoId", 0)
        var producto : Producto? = findProduct(productId)
        if(producto?.id == null){
            Picasso.get().load(producto?.image).into(binding.imgProd)
            binding.txvNombre.text = producto?.title.toString()
            binding.txvCategoria.text = producto?.category.toString()
            binding.txvPrecio.text = producto?.price.toString()
            binding.txvDescripcion.text = producto?.description.toString()
        }

        binding.btnEditarProducto.setOnClickListener {
            val intent = Intent(this, AgregarProducto::class.java)
            intent.putExtra("productoId", productId)
            startActivity(intent)
        }
    }

    fun findProduct(productId : Int):Producto?{
        var producto : Producto? = Producto()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                producto = productoDAO.buscarProductosPorId(productId)
            }catch (e : Exception){
                Log.w("Error al buscar producto", e.toString())
            }
        }
        return producto
    }

}
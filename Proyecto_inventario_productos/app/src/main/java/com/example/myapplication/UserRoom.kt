package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.DAO.ProductoDAO
import com.example.myapplication.database.DatabaseFinal
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ActivityUserRoomBinding
import com.example.myapplication.databinding.ProductListBinding
import com.example.myapplication.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRoom : AppCompatActivity() {
    private lateinit var productoDAO: ProductoDAO
    private lateinit var database: DatabaseFinal
    private lateinit var binding: ProductListBinding
    private lateinit var adapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        database = DatabaseFinal.getDatabase(this)
        productoDAO = database.productoDAO()

        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = ProductListAdapter(emptyList(), {
            verInfo(it)
        }){
            deleteProducto(it)
        }

        binding.rv.adapter = adapter

        binding.btnCrear.setOnClickListener {
            val intent = Intent(this, AgregarProducto::class.java)
            intent.putExtra("productoId", -1)
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            UtilsSharedPreferences.cerrarSesion(this)
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) )
        }

        updateList()
    }

    fun updateList(){
        CoroutineScope(Dispatchers.IO).launch {
            val lstPersonas = productoDAO.getAllProductos()
            withContext(Dispatchers.Main){
                adapter.updateList(lstPersonas)
            }
        }
    }

    fun verInfo(prodId: Int){
        try {
            val intent = Intent(this, VerProducto::class.java)
            intent.putExtra("productoId", prodId)
            startActivity(intent)
        }catch (e: Exception){
            Log.w("Erroe", e.toString())
        }
    }

    fun deleteProducto(prodId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            var prod = Producto()
            prod.id = prodId

            productoDAO.delete(prod)
            updateList()
        }
    }
}
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
import com.example.myapplication.Repository.ProductoRepositoryImpl
import com.example.myapplication.database.DatabaseFinal
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
        private lateinit var productoDAO: ProductoDAO
        private lateinit var database: DatabaseFinal
        private lateinit var binding: ActivityMainBinding
        val service: ProductoRepositoryImpl = ProductoRepositoryImpl()
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            if(UtilsSharedPreferences.getSesion(this)){
                startActivity(Intent(this,UserRoom ::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }else {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                database = DatabaseFinal.getDatabase(this)
                productoDAO = database.productoDAO()
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                    insets
                }
                var lstProductos : List<Producto> = getList()
                if(lstProductos.isEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        val lstProductos = service.getProductos()
                        for (data in lstProductos){
                            crearProducto(data)
                            Log.w("Producto", data.title.toString())
                        }
                    }
                }
                binding.btnLogin.setOnClickListener {
                    if (binding.etEmail.text.toString().trim() == "lucas" && binding.etPassword.text.toString().trim() == "1234") {
                        UtilsSharedPreferences.createSesion(this)
                        startActivity(Intent(this,UserRoom ::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    } else {
                        Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show()
                    }

                }
       }
    }
    fun crearProducto(producto : Producto){
        CoroutineScope(Dispatchers.IO).launch {
            productoDAO.insertProducto(producto)
        }
    }

    fun getList() : List<Producto> {
        var existDatabase: List<Producto> = emptyList()
        CoroutineScope(Dispatchers.IO).launch {
            existDatabase = productoDAO.getAllProductos()
        }
        return existDatabase
    }
}
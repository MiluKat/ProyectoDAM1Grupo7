package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            if(UtilsSharedPreferences.getSesion(this)){
                startActivity(Intent(this,UserRoom ::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }else {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

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

                binding.btnLogin.setOnClickListener {
                    if (binding.etEmail.text.toString().lowercase().trim() == "lucas" && binding.etPassword.text.toString().trim() == "1234") {
                        UtilsSharedPreferences.createSesion(this)
                        startActivity(Intent(this,UserRoom ::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    } else {
                        Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show()
                    }

                }
       }
    }
}
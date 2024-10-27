package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ProductBinding
import com.example.myapplication.model.Producto
import com.squareup.picasso.Picasso

class ProductListAdapter(
        var lstProductos: List<Producto>,
        private val verInfo: (prodId : Int) -> Unit,
        private val deleteInfo: (prodId : Int) -> Unit):
    RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
    class ProductListViewHolder (val binding: ProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val binding = ProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return lstProductos.size
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val prod = lstProductos.get(position)
        holder.binding.txvTitulo.text = prod.title
        holder.binding.txvPrecio.text = prod.price.toString()
        holder.binding.txvDescripcion.text = prod.description
        holder.binding.txvCategoria.text = prod.category

        Picasso.get().load(prod.image).into(holder.binding.imgProducto)
        holder.binding.btnInfo.setOnClickListener {
            verInfo(prod.id)
        }
        holder.binding.btnEliminar.setOnClickListener {
            deleteInfo(prod.id)
        }
    }

    fun updateList(newList: List<Producto>){
        lstProductos = newList
        notifyDataSetChanged()
    }
}
package com.example.myapplication.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "producto_table")
@Serializable
data class Producto (

    @SerialName("id"          )
    @PrimaryKey
    var id          : Long    = 0,
    @SerialName("title"       ) var title       : String? = null,
    @SerialName("price"       ) var price       : Double? = null,
    @SerialName("description" ) var description : String? = null,
    @SerialName("category"    ) var category    : String? = null,
    @SerialName("image"       ) var image       : String? = null,

)

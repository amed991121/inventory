package com.savent.inventory.data.comom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("companies")
data class Company(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
)
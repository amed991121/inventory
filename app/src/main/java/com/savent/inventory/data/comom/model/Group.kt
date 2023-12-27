package com.savent.inventory.data.comom.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("groups")
data class Group (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String,
)
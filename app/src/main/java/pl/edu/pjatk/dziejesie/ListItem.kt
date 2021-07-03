package pl.edu.pjatk.dziejesie

import java.io.Serializable

data class ListItem(
    val picture: String,
    val name: String,
    val location: String,
    val note: String
) : Serializable
package com.example.uas_mocom.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SportsBasketball
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.SportsTennis
import androidx.compose.material.icons.rounded.SportsVolleyball
import androidx.compose.ui.graphics.vector.ImageVector

enum class SportType(val title: String, val icon: ImageVector) {
    BASKETBALL("Basketball", Icons.Rounded.SportsBasketball),
    VOLLEYBALL("Volleyball", Icons.Rounded.SportsVolleyball),
    SOCCER("Soccer", Icons.Rounded.SportsSoccer),
    BADMINTON("Badminton", Icons.Rounded.SportsTennis)
}
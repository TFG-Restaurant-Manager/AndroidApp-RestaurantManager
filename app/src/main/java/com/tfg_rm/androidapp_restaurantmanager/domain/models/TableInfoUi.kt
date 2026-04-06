package com.tfg_rm.androidapp_restaurantmanager.domain.models

import androidx.compose.ui.graphics.Color

/**
 * UI-specific data model used to represent summary information for restaurant tables.
 *
 * This class is designed to be consumed by Jetpack Compose components to display
 * aggregated table statistics (such as the number of available or occupied tables)
 * with associated visual styling.
 *
 * @property title The string resource ID (R.string...) for the label of the information category.
 * @property count The numerical value representing the number of tables in this category.
 * @property color The [Color] used to represent this status or category in the user interface.
 */
data class TableInfoUi(
    val title: Int,
    val count: Int,
    val color: Color
)
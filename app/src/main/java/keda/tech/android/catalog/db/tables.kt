package keda.tech.android.catalog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "is_favorite") var isFavorite: Int
)
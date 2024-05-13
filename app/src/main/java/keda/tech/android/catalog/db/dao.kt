package keda.tech.android.catalog.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product): Long

    @Update
    fun updateProduct(product: Product): Int

    @Query("UPDATE product SET is_favorite=:isFavorite WHERE id = :id")
    fun updateProductFavorite(isFavorite: Int, id: String): Int

    @Query("SELECT COUNT(id) FROM product")
    fun getCountAllProduct(): Int

    @Query("SELECT id, name, description, price, is_favorite FROM product")
    fun getAllProduct(): LiveData<List<Product>>

    @Query("SELECT id, name, description, price, is_favorite FROM product WHERE is_favorite = 1")
    fun getAllProductFavorite(): LiveData<List<Product>>

    @Query("SELECT id, name, description, price, is_favorite FROM product WHERE name LIKE :search")
    fun getAllProductFilter(search: String): LiveData<List<Product>>

    @Query("SELECT id, name, description, price, is_favorite FROM product WHERE name LIKE :search AND is_favorite = 1")
    fun getAllProductFavoriteFilter(search: String): LiveData<List<Product>>
}
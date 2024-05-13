package keda.tech.android.catalog.view.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import keda.tech.android.catalog.db.AppDatabase
import keda.tech.android.catalog.db.Product

class ProductViewModel(application: Application) : ViewModel() {
    private val db: AppDatabase = AppDatabase.getAppDataBase(application)
    internal val allProduct: LiveData<List<Product>> = db.dataDao().getAllProduct()
    internal val allProductFavorite: LiveData<List<Product>> = db.dataDao().getAllProductFavorite()

    fun insertProduct(product: Product) {
        db.dataDao().insertProduct(product)
    }

    fun updateProductFavorite(isFavorite: Int, id: String) {
        db.dataDao().updateProductFavorite(isFavorite, id)
    }

    fun productFilter(search: String): LiveData<List<Product>> {
        if (search.isNotEmpty()) {
            val searchParam = "%$search%"
            return db.dataDao().getAllProductFilter(searchParam)
        } else {
            return db.dataDao().getAllProduct()
        }
    }

    fun productFavoriteFilter(search: String): LiveData<List<Product>> {
        if (search.isNotEmpty()) {
            val searchParam = "%$search%"
            return db.dataDao().getAllProductFavoriteFilter(searchParam)
        } else {
            return db.dataDao().getAllProductFavorite()
        }
    }
}
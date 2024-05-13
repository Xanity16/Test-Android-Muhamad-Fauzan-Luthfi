package keda.tech.android.catalog.view.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import keda.tech.android.catalog.view.model.ProductViewModel

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(private var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
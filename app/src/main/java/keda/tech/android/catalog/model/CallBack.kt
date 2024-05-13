package keda.tech.android.catalog.model

import keda.tech.android.catalog.db.Product

interface OnChangeProductFavoriteListener {
    fun onChangeProductFavorite(isFavorite: Int, product: Product, adapterPosition: Int)
}
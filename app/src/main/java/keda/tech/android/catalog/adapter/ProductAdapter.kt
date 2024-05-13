package keda.tech.android.catalog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import keda.tech.android.catalog.R
import keda.tech.android.catalog.db.Product
import keda.tech.android.catalog.ext.formatCurrency
import keda.tech.android.catalog.model.OnChangeProductFavoriteListener

class ProductAdapter(
    private var ctx: Context,
    private var productList: List<Product>,
    private var listener: OnChangeProductFavoriteListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_product, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvId.text = productList[position].id
        holder.tvName.text = productList[position].name
        holder.tvPrice.text = productList[position].price.formatCurrency()
        if (productList[position].isFavorite == 1) {
            holder.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.ic_favorite
                )
            )
            holder.mbFavorite.setText(R.string.lbl_un_favorite)
        } else {
            holder.ivFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.ic_un_favorite
                )
            )
            holder.mbFavorite.setText(R.string.lbl_add_favorite)
        }
        holder.ivFavorite.setOnClickListener {
            if (productList[position].isFavorite == 1) {
                listener.onChangeProductFavorite(
                    0,
                    productList[position],
                    holder.absoluteAdapterPosition
                )
            } else {
                listener.onChangeProductFavorite(
                    1,
                    productList[position],
                    holder.absoluteAdapterPosition
                )
            }
        }
        holder.mbFavorite.setOnClickListener {
            if (productList[position].isFavorite == 1) {
                listener.onChangeProductFavorite(
                    0,
                    productList[position],
                    holder.absoluteAdapterPosition
                )
            } else {
                listener.onChangeProductFavorite(
                    1,
                    productList[position],
                    holder.absoluteAdapterPosition
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvId: TextView = itemView.findViewById(R.id.tvId)
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
        var mbFavorite: MaterialButton = itemView.findViewById(R.id.mbFavorite)
    }
}
package keda.tech.android.catalog.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import keda.tech.android.catalog.App
import keda.tech.android.catalog.R
import keda.tech.android.catalog.adapter.ProductAdapter
import keda.tech.android.catalog.databinding.ActivityFavoriteBinding
import keda.tech.android.catalog.db.DataDao
import keda.tech.android.catalog.db.Product
import keda.tech.android.catalog.dialog.progress.normal.DialogProgress
import keda.tech.android.catalog.model.OnChangeProductFavoriteListener
import keda.tech.android.catalog.utils.executeAsyncTask
import keda.tech.android.catalog.view.factory.ProductViewModelFactory
import keda.tech.android.catalog.view.model.ProductViewModel

class FavoriteActivity : AppCompatActivity(), OnChangeProductFavoriteListener {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var dao: DataDao
    private var app: App? = null
    private var positionScroll = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = applicationContext as App
        dao = app!!.getDb().dataDao()

        initView()
        initEvent()
    }

    private fun initView() {
        val productViewModelFactory = ProductViewModelFactory(application)
        productViewModel = ViewModelProvider(
            this,
            productViewModelFactory
        )[ProductViewModel::class.java]
        val linearLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.rvProduct.layoutManager = linearLayoutManager
        productViewModel.allProductFavorite.observe(this) { products ->
            if (products.isNotEmpty()) {
                binding.rvProduct.visibility = View.VISIBLE
                binding.tvDataEmpty.visibility = View.GONE
            } else {
                binding.rvProduct.visibility = View.GONE
                binding.tvDataEmpty.visibility = View.VISIBLE
            }
            binding.rvProduct.scrollToPosition(positionScroll)
            binding.rvProduct.adapter = ProductAdapter(this, products, this)
        }
    }

    private fun initEvent() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tieSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                productViewModel.productFavoriteFilter(s.toString())
                    .observe(this@FavoriteActivity) { products ->
                        if (products.isNotEmpty()) {
                            binding.rvProduct.visibility = View.VISIBLE
                            binding.tvDataEmpty.visibility = View.GONE
                        } else {
                            binding.rvProduct.visibility = View.GONE
                            binding.tvDataEmpty.visibility = View.VISIBLE
                        }
                        binding.rvProduct.scrollToPosition(positionScroll)
                        binding.rvProduct.adapter = ProductAdapter(
                            this@FavoriteActivity,
                            products,
                            this@FavoriteActivity
                        )
                    }
            }
        })
    }

    override fun onChangeProductFavorite(isFavorite: Int, product: Product, adapterPosition: Int) {
        val pDialog = DialogProgress(getString(R.string.lbl_please_wait_a_moment))
        pDialog.show(supportFragmentManager, "DialogProgress")
        positionScroll = adapterPosition
        lifecycleScope.executeAsyncTask(onPreExecute = {}, doInBackground = {
            productViewModel.updateProductFavorite(isFavorite, product.id)
            if (isFavorite == 1) {
                "Add product favorite ${product.name} is success"
            } else {
                "Remove product favorite ${product.name} is success"
            }
        }, onPostExecute = {
            Log.i("MainActivity", it)
            binding.tieSearch.setText("")
            pDialog.dismissAllowingStateLoss()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}
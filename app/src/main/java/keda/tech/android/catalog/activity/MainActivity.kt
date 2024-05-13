package keda.tech.android.catalog.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import keda.tech.android.catalog.App
import keda.tech.android.catalog.R
import keda.tech.android.catalog.adapter.ProductAdapter
import keda.tech.android.catalog.databinding.ActivityMainBinding
import keda.tech.android.catalog.db.DataDao
import keda.tech.android.catalog.db.Product
import keda.tech.android.catalog.dialog.progress.normal.DialogProgress
import keda.tech.android.catalog.model.OnChangeProductFavoriteListener
import keda.tech.android.catalog.utils.executeAsyncTask
import keda.tech.android.catalog.view.factory.ProductViewModelFactory
import keda.tech.android.catalog.view.model.ProductViewModel

class MainActivity : AppCompatActivity(), OnChangeProductFavoriteListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var dao: DataDao
    private var app: App? = null
    private var positionScroll = 0
    private var selectedFilterFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
        productViewModel.allProduct.observe(this) { products ->
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

    @SuppressLint("InflateParams")
    private fun initEvent() {
        binding.tvFilter.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_filter, null)
            val btnClose = view.findViewById<ImageView>(R.id.ivClose)
            val mbFilterBottomSheet = view.findViewById<MaterialButton>(R.id.mbFilterBottomSheet)
            val cbFavorite = view.findViewById<MaterialCheckBox>(R.id.cbFavorite)
            cbFavorite.isChecked = selectedFilterFavorite
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            mbFilterBottomSheet.setOnClickListener {
                selectedFilterFavorite = cbFavorite.isChecked
                positionScroll = 0
                if (cbFavorite.isChecked) {
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
                } else {
                    productViewModel.allProduct.observe(this) { products ->
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
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.tvFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        binding.tieSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                positionScroll = 0
                productViewModel.productFilter(s.toString())
                    .observe(this@MainActivity) { products ->
                        if (products.isNotEmpty()) {
                            binding.rvProduct.visibility = View.VISIBLE
                            binding.tvDataEmpty.visibility = View.GONE
                        } else {
                            binding.rvProduct.visibility = View.GONE
                            binding.tvDataEmpty.visibility = View.VISIBLE
                        }
                        binding.rvProduct.scrollToPosition(positionScroll)
                        binding.rvProduct.adapter = ProductAdapter(
                            this@MainActivity,
                            products,
                            this@MainActivity
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
            pDialog.dismissAllowingStateLoss()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}
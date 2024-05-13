package keda.tech.android.catalog.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import keda.tech.android.catalog.App
import keda.tech.android.catalog.R
import keda.tech.android.catalog.databinding.ActivitySplashScreenBinding
import keda.tech.android.catalog.db.DataDao
import keda.tech.android.catalog.db.Product
import keda.tech.android.catalog.dialog.progress.normal.DialogProgress
import keda.tech.android.catalog.utils.executeAsyncTask

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var dao: DataDao
    private var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = applicationContext as App
        dao = app!!.getDb().dataDao()

        initView()
    }

    private fun initView() {
        val pDialog = DialogProgress(getString(R.string.lbl_please_wait_a_moment))
        lifecycleScope.executeAsyncTask(onPreExecute = {
            pDialog.show(supportFragmentManager, "DialogProgress")
        }, doInBackground = {
            val countProduct = dao.getCountAllProduct()
            val productList = arrayListOf<Product>()
            if (countProduct == 0) {
                for (i in 0 until 1000) {
                    val product = Product(
                        "P${i + 1}",
                        "Product Name ${i + 1}",
                        "Product Description ${i + 1}",
                        "${(i + 1) * 100}",
                        0
                    )
                    dao.insertProduct(product)
                    productList.add(product)
                }
            }
            productList
        }, onPostExecute = {
            if (it.isNotEmpty()) {
                Log.i("SplashScreenActivity", "Success insert product ${Gson().toJson(it)}")
            } else {
                Log.i("SplashScreenActivity", "List is already")
            }
            pDialog.dismissAllowingStateLoss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
    }
}
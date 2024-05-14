package keda.tech.android.catalog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import keda.tech.android.catalog.db.AppDatabase
import keda.tech.android.catalog.db.DataDao
import keda.tech.android.catalog.db.Product
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class UnitTestApp {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var dao: DataDao? = null

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true
        dao = AppDatabase.getAppDataBase(InstrumentationRegistry.getInstrumentation().targetContext)
            .dataDao()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testData() {
        // Insert 1000 data for first install application
        for (i in 0 until 1000) {
            val product = Product(
                "P${i + 1}",
                "Product Name ${i + 1}",
                "Product Description ${i + 1}",
                "${(i + 1) * 100}",
                0
            )
            dao?.insertProduct(product)
        }

        // Get all product
        Assert.assertEquals(dao?.getCountAllProduct(), 1000)
        // Get all product favorite
        Assert.assertEquals(dao?.getCountAllProductFavorite(), 0)

        // Add product to favorite
        dao?.updateProductFavorite(1, "P1")
        val addProductFavorite = getOrAwaitValue(dao?.getProduct("P1")!!)!!
        Assert.assertEquals(addProductFavorite.isFavorite, 1)

        // Remove product to favorite
        dao?.updateProductFavorite(0, "P2")
        val removeProductFavorite = getOrAwaitValue(dao?.getProduct("P2")!!)!!
        Assert.assertEquals(removeProductFavorite.isFavorite, 0)

        // Search product
        val listFilterProduct = getOrAwaitValue(dao?.getAllProductFilter("Product Name 2")!!)!!
        Assert.assertEquals(listFilterProduct.size, 1)

        // Search product favorite
        val listFilterProductFavorite = getOrAwaitValue(dao?.getAllProductFavoriteFilter("Product Name 1")!!)!!
        Assert.assertEquals(listFilterProductFavorite.size, 1)
    }

    @Throws(InterruptedException::class)
    fun <T> getOrAwaitValue(liveData: LiveData<T>): T? {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer: Observer<T> = object : Observer<T> {
            override fun onChanged(value: T) {
                data[0] = value
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw RuntimeException("LiveData value was never set.")
        }
        return data[0] as T?
    }
}
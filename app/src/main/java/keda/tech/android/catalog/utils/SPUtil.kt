package keda.tech.android.catalog.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Collections
import java.util.Date

class SPUtils private constructor(context: Context) {
    private val sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    /**
     * SP中写入String

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    /**
     * SP中读取String

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getString(key: String, defaultValue: String = ""): String? {
        return sp.getString(key, defaultValue)
    }

    /**
     * SP中写入int

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    /**
     * SP中读取int

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getInt(key: String, defaultValue: Int = -1): Int {
        return sp.getInt(key, defaultValue)
    }

    /**
     * SP中写入long

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    /**
     * SP中读取long

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getLong(key: String, defaultValue: Long = -1L): Long {
        return sp.getLong(key, defaultValue)
    }

    /**
     * SP中写入float

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    /**
     * SP中读取float

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getFloat(key: String, defaultValue: Float = -1f): Float {
        return sp.getFloat(key, defaultValue)
    }

    /**
     * SP中写入boolean

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    /**
     * SP中读取boolean

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            sp.getBoolean(key, defaultValue)
        } catch (e: Exception) {
            if (contains(key)) {
                val data = getString(key, if (defaultValue) "1" else "0")
                remove(key)
                put(key, data == "1")
                data == "1"
            } else {
                put(key, defaultValue)
                defaultValue
            }
        }
    }

    /**
     * SP中写入String集合

     * @param key    键
     * *
     * @param values 值
     */
    fun put(key: String, values: Set<String>) {
        sp.edit().putStringSet(key, values).apply()
    }

    /**
     * SP中读取StringSet

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getStringSet(
        key: String,
        defaultValue: Set<String> = Collections.emptySet()
    ): MutableSet<String>? {
        return sp.getStringSet(key, defaultValue)
    }

    fun put(key: String, value: String, delimiters: String) {
        val list = value.split(delimiters).toList()
        val result: ArrayList<String> = arrayListOf()
        for (item in list) {
            if (item.isNotEmpty()) result.add(item)
        }

        sp.edit().putString(key, Gson().toJson(result)).apply()
    }

    fun getArrayString(key: String): ArrayList<String> {
        val source = sp.getString(key, "[]")
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(source, listType)
    }

    /**
     * SP中写入date

     * @param key   键
     * *
     * @param value 值
     */
    fun put(key: String, value: Date) {
        sp.edit().putLong(key, value.time).apply()
    }

    /**
     * SP中读取date

     * @param key          键
     * *
     * @param defaultValue 默认值
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads
    fun getDate(key: String, defaultValue: Long = 1423526400000): Date {
        val date = try {
            sp.getLong(key, defaultValue)
        } catch (e: Exception) {
            Log.d("SPUtil", "getDate key = $key ,Exception = ${e.localizedMessage}")
            defaultValue
        }
        return Date(date)
    }

    /**
     * SP中读取date string

     * @param key          键
     * *
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    fun getDateString(key: String): String {
        return Helper.getCurrentTimeStamp(getDate(key))!!
    }

    /**
     * SP中获取所有键值对

     * @return Map对象
     */
    val all: Map<String, *>
        get() = sp.all

    /**
     * SP中移除该key

     * @param key 键
     */
    fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    /**
     * SP中是否存在该key

     * @param key 键
     * *
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    operator fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    /**
     * SP中清除所有数据
     */
    fun clear() {
        sp.edit().clear().apply()
    }

    companion object {

        private val sSPMap = HashMap<String, SPUtils>()

        /**
         * 获取SP实例

         * @param context Context
         * *
         * @return [SPUtils]
         */
        fun getInstance(context: Context): SPUtils {
            var spName = context.packageName
            if (isSpace(spName)) spName = "spUtils"
            var sp: SPUtils? = sSPMap[spName]
            if (sp == null) {
                sp = SPUtils(context)
                sSPMap[spName] = sp
            }
            return sp
        }

        private fun isSpace(s: String?): Boolean {
            if (s == null) return true
            var i = 0
            val len = s.length
            while (i < len) {
                if (!Character.isWhitespace(s[i])) {
                    return false
                }
                ++i
            }
            return true
        }
    }
}
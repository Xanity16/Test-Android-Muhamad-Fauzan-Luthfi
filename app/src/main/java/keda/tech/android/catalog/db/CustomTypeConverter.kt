package keda.tech.android.catalog.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class CustomTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): ArrayList<String?>? {
        return Gson().fromJson(value, object : TypeToken<ArrayList<String?>?>() {}.type)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>?): String? {
        return Gson().toJson(list)
    }
}
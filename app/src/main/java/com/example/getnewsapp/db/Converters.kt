package com.example.getnewsapp.db

import androidx.room.TypeConverter
import com.example.getnewsapp.dataclass.Source


class Converters {


    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String): Source{
        return Source(name, name)
    }
}
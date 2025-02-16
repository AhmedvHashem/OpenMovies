//package com.hashem.openmovies.feature.framework.database
//
//import androidx.room.TypeConverter
//import kotlinx.serialization.json.Json
//
//class Converter {
//
//    @TypeConverter
//    fun fromPhoneticCacheToString(value: PhoneticCache): String {
//        return Json.encodeToString(value)
//    }
//
//    @TypeConverter
//    fun fromStringToPhoneticCache(value: String): PhoneticCache {
//        return Json.decodeFromString(value)
//    }
//
//    @TypeConverter
//    fun fromStringToMeaningCache(value: String): Map<String, MeaningCache> {
//        return Json.decodeFromString(value)
//    }
//
//    @TypeConverter
//    fun fromMeaningCacheToString(value: Map<String, MeaningCache>): String {
//        return Json.encodeToString(value)
//    }
//}
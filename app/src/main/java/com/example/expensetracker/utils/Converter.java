package com.example.expensetracker.utils;

import androidx.room.TypeConverter;

public class Converter {
    @TypeConverter
    public static String fromCategory(Categories category) {
        return category == null? null : category.name();
    }

    @TypeConverter
    public static Categories toCategory(String category) {
        return category == null? null : Categories.valueOf(category);
    }
}

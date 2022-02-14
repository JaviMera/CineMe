package com.merajavier.cineme.common

import java.text.SimpleDateFormat
import java.util.*

fun Double.toPercentAverage() : Double {

    return (this * 10)
}

fun String.toDateFormat() : String{

    val pattern = "MMM d, yyyy"
    val calendar = Calendar.getInstance()
    val values = this.split("-")

    return if(values.isNotEmpty()){
        calendar.set(Calendar.YEAR, values[0].toInt())
        calendar.set(Calendar.MONTH, values[1].toInt())
        calendar.set(Calendar.DAY_OF_MONTH, values[2].toInt())
        SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
    }else{
        ""
    }
}

fun String.toActorYears() : String{

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val values = this.split("-")

    return if(values.isNotEmpty()){
        return " (${currentYear - values[0].toInt()} years)"
    }else{
        ""
    }
}
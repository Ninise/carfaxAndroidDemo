package com.nikitinnikita.cfdemo.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


object Utils {

    fun string(context: Context, @StringRes res: Int) : String {
        return context.getString(res)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    fun makeCall(context: Context, number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun formatMoney(amount: Int) : String {
        val df = DecimalFormat("$ ###,###.#")
        return df.format(amount)
    }

    fun formatMiles(amount: Int) : String {
        val suffix = charArrayOf(' ', 'k', 'M')
        val value = floor(log10(amount.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                amount / 10.0.pow((base * 3).toDouble())
            ).replace(".0", "") + suffix[base]
        } else {
            DecimalFormat("#,##0").format(amount)
        }

    }


}
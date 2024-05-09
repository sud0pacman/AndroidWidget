package com.sudo_pacman.currencyusd

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.sudo_pacman.currencyusd.currency.Currency
import com.sudo_pacman.currencyusd.currency.CurrencyThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CurrencyWidgetConfigureActivity]
 */
class CurrencyWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val views = RemoteViews(context.packageName, R.layout.currency_widget)

    Log.d("TTT", "updateAppWidget")

    CoroutineScope(Dispatchers.Default).launch {
        val responseData = fetchDataFromNetwork()
        // Use the responseData here after the job is done
        Log.d("TTT", "${responseData.size} mashincha data keldi")

        val usd = responseData.filter { it.Ccy == "USD" }[0]

        // Set the text for different TextViews
        views.setTextViewText(R.id.tv_currency, usd.Rate)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}


private suspend fun fetchDataFromNetwork(): List<Currency> {
    return withContext(Dispatchers.IO) {
        val url = "http://cbu.uz/uzc/arkhiv-kursov-valyut/json/"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()
            responseData?.let {
                val gson = Gson()
                val currencies = gson.fromJson(it, Array<Currency>::class.java)
                currencies.toList()
            } ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}


//fun getCurrentCurrency(): String {
//    val service = Service()
//
//    val url = URL("http://cbu.uz/uzc/arkhiv-kursov-valyut/json/")
//    val connection = url.openConnection() as HttpURLConnection
//    connection.connect()
//
//    val stringBuilder = StringBuilder()
//    connection.inputStream.bufferedReader().readLines().forEach {
//        stringBuilder.append(it)
//    }
//
//    service.writeText(stringBuilder.toString())
//
//    val list = service.getCurrencyList()
//
//    val  usd = list.filter { it.Ccy == "USD"  }
//
//    return usd[0].Rate
//}

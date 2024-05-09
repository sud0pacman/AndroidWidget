package com.sudo_pacman.currencyusd

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.RemoteViews
import com.sudo_pacman.currencyusd.currency.CurrencyThread
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import thread.Service
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

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

    CurrencyThread { list ->
        // Use the currency list here
        val usd = list.filter { it.Ccy == "USD" }
        Log.d("TTT", "updateAppWidget $usd")
        views.setTextViewText(R.id.tv_currency, usd[0].Rate)
    }



    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
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

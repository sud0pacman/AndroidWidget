package com.sudo_pacman.currencyusd.currency

import com.google.gson.Gson
import thread.Service
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class CurrencyThread(private val onListReady: (ArrayList<Currency>) -> Unit) : Runnable {
    private val thread = Thread(this)

    override fun run() {
        val gson = Gson()
        val service = Service()
        val url = URL("http://cbu.uz/uzc/arkhiv-kursov-valyut/json/")
        val connection = url.openConnection() as HttpURLConnection
        connection.connect()

        val stringBuilder = StringBuilder()
        connection.inputStream.bufferedReader().readLines().forEach {
            stringBuilder.append(it)
        }

        service.writeText(stringBuilder.toString())

        val currencyList = service.getCurrencyList()

        currencyList.forEach {
            println(it)
        }

        // Invoke the callback with the currency list once it's ready
        onListReady(currencyList)
    }

    init {
        thread.start()
    }
}

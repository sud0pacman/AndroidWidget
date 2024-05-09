package thread

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sudo_pacman.currencyusd.currency.Currency
import java.io.File

class Service {
    val file = File("currency.txt")
    private val gson = Gson()

    fun getCurrencyList(): ArrayList<Currency> {
        val jsonString = file.readText()
        val list: ArrayList<Currency>
        if (jsonString.isEmpty()) {
            list = ArrayList()
        }
        else {
            val type = object : TypeToken<List<Currency>>() {}.type
            list = gson.fromJson(jsonString, type)
        }

        return list
    }

    fun writeText(jsonString: String) {
        file.writeBytes(jsonString.toByteArray())
    }
}
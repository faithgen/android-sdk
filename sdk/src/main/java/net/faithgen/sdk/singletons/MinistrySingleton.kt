package net.faithgen.sdk.singletons

import net.faithgen.sdk.SDK
import net.faithgen.sdk.models.Config
import net.faithgen.sdk.models.Ministry
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MinistrySingleton private constructor() {
    val ministry: Ministry
    val config: Config

    companion object {
        val instance = MinistrySingleton()
    }

    init {
        var reader: BufferedReader? = null
        val text = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(SDK.getInputStream()))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                text.append(line)
                text.append('\n')
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) { //log the exception
                }
            }
        }
        ministry = GSONSingleton.instance.gson.fromJson(text.toString(), Ministry::class.java)
        config = GSONSingleton.instance.gson.fromJson(text.toString(), Config::class.java)
    }
}
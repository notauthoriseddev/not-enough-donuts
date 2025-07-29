package jmd.notenoughdonuts.modules

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.nio.file.Paths

data class ModuleState(
    val name: String,
    var enabled: Boolean
)

object ModuleStateManager {
    private val gson = Gson()
    private val configFile = Paths.get(System.getProperty("user.home"), "not-enough-donuts", "module-states.json").toFile()
    private var states = mutableMapOf<String, Boolean>()

    init {
        loadStates()
    }

    fun saveStates() {
        configFile.parentFile?.mkdirs()
        configFile.writeText(gson.toJson(states))
    }

    private fun loadStates() {
        if (configFile.exists()) {
            try {
                val type = object : TypeToken<Map<String, Boolean>>() {}.type
                states = gson.fromJson(configFile.readText(), type)
            } catch (e: Exception) {
                println("Failed to load module states: ${e.message}")
                states = mutableMapOf()
            }
        }
    }

    fun getState(moduleName: String): Boolean {
        return states[moduleName] ?: false
    }

    fun setState(moduleName: String, enabled: Boolean) {
        states[moduleName] = enabled
        saveStates()
    }
}

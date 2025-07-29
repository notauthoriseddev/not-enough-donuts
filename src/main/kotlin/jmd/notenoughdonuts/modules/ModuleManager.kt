package jmd.notenoughdonuts.modules

import jmd.notenoughdonuts.gui.Category

object ModuleManager {
    private val modules = mutableListOf<Module>()
    
    init {
        // Register modules
        registerModule(PriceChecker())
    }
    
    fun registerModule(module: Module) {
        modules.add(module)
    }
    
    fun getModules(): List<Module> = modules
    
    fun getModulesByCategory(category: Category): List<Module> {
        return modules.filter { it.category == category }
    }
    
    fun toggleModule(module: Module) {
        module.enabled = !module.enabled
        if (module.enabled) {
            module.onEnable()
        } else {
            module.onDisable()
        }
    }
}

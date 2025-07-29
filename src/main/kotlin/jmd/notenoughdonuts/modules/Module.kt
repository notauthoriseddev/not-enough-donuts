package jmd.notenoughdonuts.modules

import jmd.notenoughdonuts.gui.Category

interface Module {
    val name: String
    val description: String
    val category: Category
    var enabled: Boolean
    
    fun onEnable()
    fun onDisable()
}

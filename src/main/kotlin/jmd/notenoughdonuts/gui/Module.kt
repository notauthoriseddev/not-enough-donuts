package jmd.notenoughdonuts.gui

data class Module(
    val name: String,
    var enabled: Boolean = false,
    val description: String = "",
    val category: Category? = null
) {
    fun toggle() {
        enabled = !enabled
        onToggle(enabled)
    }
    
    open fun onToggle(state: Boolean) {
        // Override this in specific module implementations
    }
}

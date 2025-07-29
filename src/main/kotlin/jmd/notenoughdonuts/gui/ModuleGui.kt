package jmd.notenoughdonuts.gui

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import java.awt.Color
import jmd.notenoughdonuts.gui.Category
import jmd.notenoughdonuts.modules.ModuleManager

class ModuleGui : Screen(Text.literal("Not Enough Donuts")) {
    private val categories: List<Category> = Category.values().toList()
    private var selectedCategory: Category = Category.AH
    private val sidebarWidth: Int = 130
    private val categoryHeight: Int = 20
    private var closeButtonHovered: Boolean = false
    private var searchText: String = ""
    private var isSearching: Boolean = false

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        
        // Draw solid dark background
        context.fill(0, 0, width, height, Color(17, 17, 17, 255).rgb)

        // Draw sidebar background
        context.fill(0, 0, sidebarWidth, height, Color(24, 24, 24, 255).rgb)
        
        // Draw close button
        closeButtonHovered = isMouseOverCloseButton(mouseX, mouseY)
        val closeButtonColor = if (closeButtonHovered) Color(255, 50, 50) else Color(170, 170, 170)
        
        val x = width - 20
        val y = 5
        
        context.drawTextWithShadow(
            textRenderer,
            Text.literal("✕"),
            x,
            y,
            closeButtonColor.rgb
        )

        // Draw mod title
        context.drawTextWithShadow(
            textRenderer,
            Text.literal("Not Enough Donuts"),
            10,
            10,
            Color.WHITE.rgb
        )

        // Draw search bar
        val searchBarY = 35
        val searchBoxWidth = sidebarWidth - 20
        
        context.fill(
            10,
            searchBarY,
            10 + searchBoxWidth,
            searchBarY + 15,
            Color(32, 32, 32, 255).rgb
        )

        context.drawTextWithShadow(
            textRenderer,
            Text.literal("⚲"),
            12,
            searchBarY + 3,
            Color(170, 170, 170).rgb
        )

        if (searchText.isEmpty() && !isSearching) {
            context.drawTextWithShadow(
                textRenderer,
                Text.literal("Search..."),
                25,
                searchBarY + 3,
                Color(90, 90, 90).rgb
            )
        } else {
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(searchText),
                25,
                searchBarY + 3,
                Color.WHITE.rgb
            )
        }

        // Draw categories in the middle
        var categoryY = height / 2 - (categories.size * categoryHeight) / 2
        categories.forEach { category ->
            val selected = category == selectedCategory
            
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(category.displayName),
                10,
                categoryY,
                if (selected) Color.WHITE.rgb else Color(170, 170, 170).rgb
            )
            
            categoryY += categoryHeight
        }

        // Draw selected category title in content area
        context.drawTextWithShadow(
            textRenderer,
            Text.literal(selectedCategory.displayName),
            sidebarWidth + 10,
            20,
            Color.WHITE.rgb
        )

        // Draw modules for selected category
        var moduleY = 50
        val moduleX = sidebarWidth + 10
        val contentWidth = width - sidebarWidth - 20 // Total available width
        val moduleWidth = (contentWidth * 0.8).toInt() // 80% of available width
        val moduleHeight = 40 // Increased height for description
        val switchSize = 16 // Size of the switch
        val padding = 10 // Padding around elements
        
        ModuleManager.getModulesByCategory(selectedCategory).forEach { module ->
            // Draw module background
            val moduleColor = if (module.enabled) Color(46, 46, 46, 255) else Color(32, 32, 32, 255)
            context.fill(
                moduleX,
                moduleY,
                moduleX + moduleWidth,
                moduleY + moduleHeight,
                moduleColor.rgb
            )
            
            // Draw module name
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(module.name),
                moduleX + padding,
                moduleY + padding,
                if (module.enabled) Color.WHITE.rgb else Color(170, 170, 170).rgb
            )

            // Draw module description
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(module.description),
                moduleX + padding,
                moduleY + padding + 12,
                Color(170, 170, 170).rgb
            )
            
            // Draw switch background
            val switchX = moduleX + moduleWidth - switchSize - padding
            val switchY = moduleY + (moduleHeight - switchSize) / 2
            val switchBgColor = if (module.enabled) Color(78, 201, 176) else Color(58, 58, 58)
            
            // Draw switch background (rounded rectangle using multiple fills)
            context.fill(
                switchX + 2,
                switchY,
                switchX + switchSize - 2,
                switchY + switchSize,
                switchBgColor.rgb
            )
            context.fill(
                switchX,
                switchY + 2,
                switchX + switchSize,
                switchY + switchSize - 2,
                switchBgColor.rgb
            )
            
            // Draw switch knob
            val knobSize = switchSize - 6
            val knobX = if (module.enabled) switchX + switchSize - knobSize - 3 else switchX + 3
            val knobColor = if (module.enabled) Color.WHITE else Color(170, 170, 170)
            
            context.fill(
                knobX + 1,
                switchY + 3,
                knobX + knobSize - 1,
                switchY + knobSize + 3,
                knobColor.rgb
            )
            context.fill(
                knobX,
                switchY + 4,
                knobX + knobSize,
                switchY + knobSize + 2,
                knobColor.rgb
            )
            
            moduleY += moduleHeight + padding
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        when {
            isMouseOverCloseButton(mouseX.toInt(), mouseY.toInt()) -> {
                close()
                return true
            }
            mouseX >= 10 && mouseX < sidebarWidth - 10 && mouseY >= 35 && mouseY <= 50 -> {
                isSearching = true
                return true
            }
            isSearching -> {
                isSearching = false
            }
        }
        
        if (mouseX < sidebarWidth && mouseX >= 10) {
            val middleY = height / 2 - (categories.size * categoryHeight) / 2
            val relativeY = mouseY - middleY
            val categoryIndex = (relativeY / categoryHeight).toInt()
            
            if (categoryIndex in categories.indices) {
                selectedCategory = categories[categoryIndex]
                return true
            }
        }

        // Handle module clicks
        if (mouseX >= sidebarWidth + 10) {
            val contentWidth = width - sidebarWidth - 20
            val moduleWidth = (contentWidth * 0.8).toInt()
            val moduleHeight = 40
            val padding = 10
            val switchSize = 16
            val moduleX = sidebarWidth + 10
            
            // Calculate which module was clicked
            val moduleY = ((mouseY - 50) / (moduleHeight + padding)).toInt()
            val modules = ModuleManager.getModulesByCategory(selectedCategory)
            
            if (moduleY in modules.indices) {
                // Check if click was on the switch
                val switchX = moduleX + moduleWidth - switchSize - padding
                val switchY = 50 + moduleY * (moduleHeight + padding) + (moduleHeight - switchSize) / 2
                
                if (mouseX >= switchX && mouseX <= switchX + switchSize &&
                    mouseY >= switchY && mouseY <= switchY + switchSize) {
                    ModuleManager.toggleModule(modules[moduleY])
                    return true
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == 256) { // ESC key
            if (isSearching) {
                isSearching = false
                return true
            }
            close()
            return true
        }
        
        if (isSearching) {
            if (keyCode == 259) { // Backspace
                if (searchText.isNotEmpty()) {
                    searchText = searchText.substring(0, searchText.length - 1)
                }
                return true
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }
    
    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        if (isSearching && (chr.isLetterOrDigit() || chr.isWhitespace())) {
            searchText += chr
            return true
        }
        return super.charTyped(chr, modifiers)
    }

    private fun isMouseOverCloseButton(mouseX: Int, mouseY: Int): Boolean {
        val x = width - 20
        val y = 5
        return mouseX >= x && mouseX <= x + 10 && mouseY >= y && mouseY <= y + 10
    }
}

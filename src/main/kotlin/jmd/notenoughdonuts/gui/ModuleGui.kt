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
    
    // Layout constants
    private val screenPadding: Int = 10
    private val titleHeight: Int = 40
    private var searchBarY: Int = 0

    init {
        // Initialize dependent values after Screen is constructed
        this.searchBarY = screenPadding + (titleHeight - 9) / 2  // 9 is default font height
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)
        
        // Draw solid dark background
        context.fill(0, 0, width, height, Color(17, 17, 17, 255).rgb)

        // Draw title bar container
        val titleBarColor = Color(24, 24, 24, 255)
        val borderColor = Color(48, 48, 48, 255)
        
        // Title bar background
        context.fill(screenPadding, screenPadding, width - screenPadding, screenPadding + titleHeight, titleBarColor.rgb)
        // Title bar border
        drawBorder(context, screenPadding, screenPadding, width - screenPadding, screenPadding + titleHeight, borderColor.rgb)

        if (!isSearching) {
            // Draw mod title
            context.drawTextWithShadow(
                textRenderer,
                Text.literal("Not Enough Donuts"),
                screenPadding + 10,
                screenPadding + (titleHeight - textRenderer.fontHeight) / 2,
                Color.WHITE.rgb
            )

            // Draw search icon
            val searchIconX = width - screenPadding - 90
            val searchIconY = screenPadding + (titleHeight - 16) / 2
            
            // Draw magnifying glass
            val iconSize = 16
            val centerX = searchIconX + iconSize / 2
            val centerY = searchIconY + iconSize / 2
            val radius = 5
            
            // Draw circle outline (thicker)
            for (i in 0..1) {
                drawCircle(context, centerX, centerY, radius + i, Color(170, 170, 170))
            }
            
            // Draw handle
            val handleAngle = Math.PI * 0.75 // Angled at 45 degrees
            val handleLength = 6
            val startX = centerX + (radius * Math.cos(handleAngle)).toInt()
            val startY = centerY + (radius * Math.sin(handleAngle)).toInt()
            val endX = startX + (handleLength * Math.cos(handleAngle)).toInt()
            val endY = startY + (handleLength * Math.sin(handleAngle)).toInt()
            
            // Draw thick handle
            for (i in 0..1) {
                context.fill(
                    startX + i,
                    startY + i,
                    endX + i + 1,
                    endY + i + 1,
                    Color(170, 170, 170).rgb
                )
            }
        } else {
            // Draw expanded search bar
            val searchBoxColor = Color(32, 32, 32, 255)
            context.fill(
                screenPadding + 5,
                screenPadding + 5,
                width - screenPadding - 5,
                screenPadding + titleHeight - 5,
                searchBoxColor.rgb
            )
            
            // Draw search text
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(if (searchText.isEmpty()) "Search..." else searchText),
                screenPadding + 15,
                screenPadding + (titleHeight - textRenderer.fontHeight) / 2,
                if (searchText.isEmpty()) Color(90, 90, 90).rgb else Color.WHITE.rgb
            )
        }

        // Draw sidebar background with border
        context.fill(screenPadding, screenPadding + titleHeight + screenPadding, 
            screenPadding + sidebarWidth, height - screenPadding, Color(24, 24, 24, 255).rgb)
        drawBorder(context, screenPadding, screenPadding + titleHeight + screenPadding, 
            screenPadding + sidebarWidth, height - screenPadding, borderColor.rgb)

        // Draw categories in the middle
        var categoryY = height / 2 - (categories.size * categoryHeight) / 2
        categories.forEach { category ->
            val selected = category == selectedCategory
            
            // Draw category with proper padding
            context.drawTextWithShadow(
                textRenderer,
                Text.literal(category.displayName),
                screenPadding + 10,
                categoryY,
                if (selected) Color.WHITE.rgb else Color(170, 170, 170).rgb
            )
            
            categoryY += categoryHeight
        }

        // Draw modules area background
        val modulesAreaX = screenPadding + sidebarWidth + screenPadding
        val modulesAreaY = screenPadding + titleHeight + screenPadding
        val modulesAreaWidth = width - modulesAreaX - screenPadding
        val modulesAreaHeight = height - modulesAreaY - screenPadding
        
        // Draw background and border for modules area
        context.fill(
            modulesAreaX,
            modulesAreaY,
            modulesAreaX + modulesAreaWidth,
            modulesAreaY + modulesAreaHeight,
            Color(24, 24, 24, 255).rgb
        )
        drawBorder(
            context,
            modulesAreaX,
            modulesAreaY,
            modulesAreaX + modulesAreaWidth,
            modulesAreaY + modulesAreaHeight,
            Color(48, 48, 48, 255).rgb
        )

        // Draw selected category title in content area
        context.drawTextWithShadow(
            textRenderer,
            Text.literal(selectedCategory.displayName),
            modulesAreaX + screenPadding,
            modulesAreaY + screenPadding,
            Color.WHITE.rgb
        )

        // Draw modules for selected category
        var moduleY = screenPadding + titleHeight + screenPadding
        val moduleX = screenPadding + sidebarWidth + screenPadding
        val contentWidth = width - moduleX - screenPadding // Total available width
        val moduleWidth = (contentWidth * 0.95).toInt() // 95% of available width
        val moduleHeight = 40 // Increased height for description
        val switchSize = 16 // Size of the switch
        val padding = 10 // Padding around elements
        
        ModuleManager.getModulesByCategory(selectedCategory).forEach { module ->
            // Draw module background with layers for depth
            val moduleColor = if (module.enabled) Color(46, 46, 46, 255) else Color(32, 32, 32, 255)
            val borderColor = if (module.enabled) Color(78, 201, 176, 100) else Color(48, 48, 48, 255)
            
            // Shadow layer
            context.fill(
                moduleX + 2,
                moduleY + 2,
                moduleX + moduleWidth + 2,
                moduleY + moduleHeight + 2,
                Color(0, 0, 0, 50).rgb
            )
            
            // Main background
            context.fill(
                moduleX,
                moduleY,
                moduleX + moduleWidth,
                moduleY + moduleHeight,
                moduleColor.rgb
            )
            
            // Draw border
            drawBorder(context, moduleX, moduleY, moduleX + moduleWidth, moduleY + moduleHeight, borderColor.rgb)
            
            // Draw subtle highlight line at top
            context.fill(
                moduleX + 1,
                moduleY + 1,
                moduleX + moduleWidth - 1,
                moduleY + 2,
                Color(255, 255, 255, 15).rgb
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
            
            // Draw switch (realistic toggle style)
            val switchX = moduleX + moduleWidth - switchSize * 2 - padding
            val switchY = moduleY + (moduleHeight - switchSize) / 2
            
            // Draw switch track
            val trackColor = if (module.enabled) Color(78, 201, 176, 180) else Color(58, 58, 58, 180)
            val trackWidth = switchSize * 2
            val trackHeight = switchSize - 4
            
            // Track background
            context.fill(
                switchX,
                switchY + 2,
                switchX + trackWidth,
                switchY + trackHeight + 2,
                Color(20, 20, 20).rgb
            )
            
            // Track inner
            context.fill(
                switchX + 1,
                switchY + 3,
                switchX + trackWidth - 1,
                switchY + trackHeight + 1,
                trackColor.rgb
            )
            
            // Draw knob with 3D effect
            val knobSize = switchSize - 2
            val knobX = if (module.enabled) switchX + trackWidth - knobSize - 1 else switchX + 1
            
            // Knob shadow
            context.fill(
                knobX + 1,
                switchY + 1,
                knobX + knobSize + 1,
                switchY + knobSize + 1,
                Color(0, 0, 0, 100).rgb
            )
            
            // Knob main body
            context.fill(
                knobX,
                switchY,
                knobX + knobSize,
                switchY + knobSize,
                Color.WHITE.rgb
            )
            
            // Knob highlight
            context.fill(
                knobX + 2,
                switchY + 2,
                knobX + knobSize - 2,
                switchY + 4,
                Color(255, 255, 255, 180).rgb
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
            mouseX >= width - screenPadding - 90 && 
            mouseX <= width - screenPadding - 70 &&
            mouseY >= screenPadding && 
            mouseY <= screenPadding + titleHeight -> {
                isSearching = true
                return true
            }
            isSearching && mouseX >= screenPadding && 
            mouseX <= width - screenPadding &&
            mouseY >= screenPadding && 
            mouseY <= screenPadding + titleHeight -> {
                // Keep searching active when clicking in search area
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
        val x = width - screenPadding - 15
        val y = screenPadding + (titleHeight - textRenderer.fontHeight) / 2
        return mouseX >= x && mouseX <= x + 10 && mouseY >= y && mouseY <= y + 10
    }

    private fun drawBorder(context: DrawContext, x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        // Draw border lines
        context.fill(x1, y1, x2, y1 + 1, color) // Top
        context.fill(x1, y2 - 1, x2, y2, color) // Bottom
        context.fill(x1, y1, x1 + 1, y2, color) // Left
        context.fill(x2 - 1, y1, x2, y2, color) // Right
    }

    private fun drawCircle(context: DrawContext, x: Int, y: Int, radius: Int, color: Color) {
        // Draw a simple circle using multiple horizontal lines
        for (dy in -radius..radius) {
            val dx = Math.sqrt((radius * radius - dy * dy).toDouble()).toInt()
            context.fill(x - dx, y + dy, x + dx, y + dy + 1, color.rgb)
        }
    }
}

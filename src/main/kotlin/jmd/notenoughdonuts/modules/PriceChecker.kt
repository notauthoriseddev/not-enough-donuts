package jmd.notenoughdonuts.modules

import jmd.notenoughdonuts.gui.Category
import net.minecraft.text.Text
import net.minecraft.client.MinecraftClient

class PriceChecker : Module {
    override val name = "Price Checker"
    override val description = "Checks and displays current item prices"
    override val category = Category.AH
    override var enabled = false
    
    override fun onEnable() {
        MinecraftClient.getInstance().player?.sendMessage(Text.literal("Price Checker enabled"), true)
    }
    
    override fun onDisable() {
        MinecraftClient.getInstance().player?.sendMessage(Text.literal("Price Checker disabled"), true)
    }
}

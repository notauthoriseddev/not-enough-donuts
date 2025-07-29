package jmd.notenoughdonuts

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import jmd.notenoughdonuts.gui.ModuleGui

object NotEnoughDonutsClient : ClientModInitializer {
    override fun onInitializeClient() {
        registerCommands()
    }

    private fun registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            // Register /ned command
            dispatcher.register(
                ClientCommandManager.literal("ned")
                    .executes { context ->
                        MinecraftClient.getInstance().let { client ->
                            client.send { 
                                client.setScreen(ModuleGui())
                            }
                        }
                        1
                    }
            )

            // Register /notenoughdonuts command
            dispatcher.register(
                ClientCommandManager.literal("notenoughdonuts")
                    .executes { context ->
                        MinecraftClient.getInstance().let { client ->
                            client.send { 
                                client.setScreen(ModuleGui())
                            }
                        }
                        1
                    }
            )
        }
    }
}
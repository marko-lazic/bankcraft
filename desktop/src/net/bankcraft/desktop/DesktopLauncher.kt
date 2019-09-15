package net.bankcraft.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import net.bankcraft.BankCraftGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            width = 1080
            height = 720
        }
        LwjglApplication(BankCraftGame(), config)
    }
}

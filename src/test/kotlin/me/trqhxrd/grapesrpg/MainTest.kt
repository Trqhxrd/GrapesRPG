package me.trqhxrd.grapesrpg

import org.bukkit.Bukkit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MainTest() : me.trqhxrd.grapesrpg.Test() {

    @Test
    fun onEnable() {
        assertNotNull(Bukkit.getPluginManager().getPlugin("GrapesRPG"))
    }
}

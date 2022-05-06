package me.trqhxrd.grapesrpg.api.world

import org.bukkit.Location

interface Chunk {
    val id: Pair<Int, Int>
    val world: World
    val location: Location
    val blocks: MutableMap<Triple<Int, Int, Int>, Block>

    fun getBlock(id: Triple<Int, Int, Int>): Block
    fun getBlock(loc: Location): Block

    fun addBlock(id: Triple<Int, Int, Int>): Block
    fun addBlock(loc: Location): Block

    fun exists(id: Triple<Int, Int, Int>): Boolean
    fun exists(loc: Location): Boolean

    fun save()
    fun load()
}

package me.trqhxrd.grapesrpg.api.world

import org.bukkit.Location
import org.bukkit.World as BukkitWorld

interface World {
    val bukkitWorld: BukkitWorld
    val name: String
    val chunks: MutableMap<Pair<Int, Int>, Chunk>

    fun getChunk(id: Pair<Int, Int>): Chunk
    fun getChunk(loc: Location): Chunk
    fun addChunk(id: Pair<Int, Int>): Chunk
    fun addChunk(loc: Location): Chunk

    fun chunkExists(id: Pair<Int, Int>): Boolean
    fun chunkExists(loc: Location): Boolean

    fun getBlock(id: Triple<Int, Int, Int>): Block
    fun getBlock(loc: Location): Block
    fun addBlock(id: Triple<Int, Int, Int>): Block
    fun addBlock(loc: Location): Block

    fun save()
    fun load()
}

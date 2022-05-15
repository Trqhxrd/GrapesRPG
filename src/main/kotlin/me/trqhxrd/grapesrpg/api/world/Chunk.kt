package me.trqhxrd.grapesrpg.api.world

import kotlinx.coroutines.sync.Mutex
import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkTable
import me.trqhxrd.grapesrpg.util.coords.ChunkID
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import org.bukkit.Location
import org.bukkit.Chunk as BukkitChunk

interface Chunk {
    val id: ChunkID
    val world: World
    val bukkitChunk: BukkitChunk
    val blocks: MutableMap<Coordinate, Block>
    val table: ChunkTable
    val lock: Mutex

    fun getBlock(id: Coordinate): Block
    fun getBlock(loc: Location): Block

    fun exists(id: Coordinate): Boolean
    fun exists(loc: Location): Boolean

    fun corner(): Coordinate

    fun save()
    fun load()
}

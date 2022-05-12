package me.trqhxrd.grapesrpg.util.coords

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World

data class ChunkID(val x: Int, val z: Int) : Cloneable {
    constructor(id: ChunkID) : this(id.x, id.z)
    constructor(loc: Location) : this(loc.chunk.x, loc.chunk.z)
    constructor(chunk: Chunk) : this(chunk.x, chunk.z)

    fun toLocation(world: World?) = Location(world, (this.x * 16).toDouble(), 0.toDouble(), (this.z * 16).toDouble())
    fun toLocation() = this.toLocation(null)

    fun bukkitChunk(world: World) = this.toLocation(world).chunk

    override fun clone() = ChunkID(this)
}

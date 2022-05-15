package me.trqhxrd.grapesrpg.util.coords

import me.trqhxrd.grapesrpg.GrapesRPG
import org.bukkit.Location
import org.bukkit.World
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

data class Coordinate(val x: Int, val y: Int, val z: Int) : Cloneable, Comparable<Coordinate> {

    constructor() : this(0, 0, 0)
    constructor(loc: Location) : this(loc.blockX, loc.blockY, loc.blockZ)
    constructor(coords: Coordinate) : this(coords.x, coords.y, coords.z)

    companion object {
        val DEFAULT = Coordinate(0, 0, 0)
    }

    fun toLocation(world: World?) = Location(world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
    fun toLocation() = this.toLocation(null)
    fun toLocation(world: WorldAPI) = this.toLocation(world.bukkitWorld)

    fun toVector() = this.toLocation().toVector()

    fun block(world: World) = this.toLocation(world).block

    fun toChunkID() = ChunkID(this.x / 16, this.z / 16)
    fun inChunkCoords(): Coordinate {
        var x = this.x % 16
        var z = this.z % 16
        if (x < 0) x += 16
        if (z < 0) z += 16
        return Coordinate(x, this.y, z)
    }

    fun toJson(): String = GrapesRPG.gson.toJson(this)

    override fun clone() = Coordinate(this)

    override fun compareTo(other: Coordinate) =
        if (x.compareTo(other.x) != 0) x.compareTo(other.x)
        else if (y.compareTo(other.y) != 0) y.compareTo(other.y)
        else z.compareTo(other.z)
}

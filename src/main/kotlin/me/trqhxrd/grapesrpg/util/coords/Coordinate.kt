package me.trqhxrd.grapesrpg.util.coords

import com.google.gson.Gson
import org.bukkit.Location
import org.bukkit.World
import me.trqhxrd.grapesrpg.api.world.World as WorldAPI

data class Coordinate(val x: Int, val y: Int, val z: Int) : Cloneable, Comparable<Coordinate> {

    constructor() : this(0, 0, 0)
    constructor(loc: Location) : this(loc.blockX, loc.blockY, loc.blockZ)
    constructor(coords: Coordinate) : this(coords.x, coords.y, coords.z)
    constructor(json: String) : this(Gson().fromJson(json, Coordinate::class.java))

    companion object {
        val DEFAULT = Coordinate(0, 0, 0)
    }

    fun toLocation(world: World?) = Location(world, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
    fun toLocation() = this.toLocation(null)
    fun toLocation(world: WorldAPI) = this.toLocation(world.bukkitWorld)

    fun toVector() = this.toLocation().toVector()

    fun toChunkID() = ChunkID((this.x - this.x % 16) / 16, (this.z - this.z % 16) / 16)
    fun toChunkCoords() = Coordinate(this.x % 16, this.y, this.z % 16)

    fun block(world: World) = this.toLocation(world).block

    fun toJson(): String {
        return Gson().toJson(this)
    }

    override fun clone() = Coordinate(this)

    override fun compareTo(other: Coordinate) =
        if (x.compareTo(other.x) != 0) x.compareTo(other.x)
        else if (y.compareTo(other.y) != 0) y.compareTo(other.y)
        else z.compareTo(other.z)
}

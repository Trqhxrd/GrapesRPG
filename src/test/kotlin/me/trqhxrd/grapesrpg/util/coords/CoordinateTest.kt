package me.trqhxrd.grapesrpg.util.coords

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CoordinateTest {

    private val c1 = Coordinate(12, 15, 27)
    private val c2 = Coordinate(100, 14, -127)

    @Test
    fun chunkIDOffsetConstructor() {
        val id1 = ChunkID(12, 14)
        val o1 = Coordinate(12, 127, 4)
        val coords1 = Coordinate(id1, o1)

        assertEquals(204, coords1.x)
        assertEquals(127, coords1.y)
        assertEquals(228, coords1.z)

        val id2 = ChunkID(-3, 7)
        val o2 = Coordinate(12, 127, 4)
        val coords2 = Coordinate(id2, o2)

        assertEquals(-36, coords2.x)
        assertEquals(127, coords2.y)
        assertEquals(116, coords2.z)
    }

    @Test
    fun toChunkID() {
        assertEquals(ChunkID(0, 1), c1.toChunkID())
        assertEquals(ChunkID(6, -8), c2.toChunkID())
    }

    @Test
    fun inChunkCoords() {
        assertEquals(12, c1.inChunkCoords().x)
        assertEquals(15, c1.inChunkCoords().y)
        assertEquals(11, c1.inChunkCoords().z)

        assertEquals(4, c2.inChunkCoords().x)
        assertEquals(14, c2.inChunkCoords().y)
        assertEquals(1, c2.inChunkCoords().z)
    }

    @Test
    fun toJson() {
        assertEquals("{\"x\":12,\"y\":15,\"z\":27}", c1.toJson())
        assertEquals("{\"x\":100,\"y\":14,\"z\":-127}", c2.toJson())
    }
}

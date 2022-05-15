package me.trqhxrd.grapesrpg.impl.world.loading

import com.google.common.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkHandler
import me.trqhxrd.grapesrpg.impl.world.Block
import me.trqhxrd.grapesrpg.impl.world.blockdata.BlockData
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ChunkLoader(
    override val database: Database,
    private val dbLock: ReadWriteMutex,
    private val chunks: MutableSet<Chunk> = mutableSetOf(),
    private val chunkLock: Mutex = Mutex(false),
) : ChunkHandler {

    private var job: Job

    companion object {
        const val MAX_TIME_SINCE_LAST_COMMIT = 50
        const val DELAY = 100L
        const val THRESHOLD = 10
    }

    init {
        var timeSinceLastCommit = 0
        job = WorldScope.launch {
            while (this.isActive) {
                if (timeSinceLastCommit > MAX_TIME_SINCE_LAST_COMMIT || this@ChunkLoader.chunks.size > THRESHOLD) {
                    this@ChunkLoader.commit()
                    timeSinceLastCommit = 0
                } else {
                    timeSinceLastCommit++
                    delay(DELAY)
                }
            }
        }
    }

    override suspend fun commit() {
        val copy = this.chunkLock.withLock { return@withLock chunks.toMutableSet() }
        this.dbLock.read {
            transaction(this.database) {
                for (chunk in copy) {
                    val table = chunk.table
                    if (table.exists()) {
                        table.selectAll().map { it[table.id] }
                            .map { Block(GrapesRPG.gson.fromJson(it.value, Coordinate::class.java), chunk) }
                            .forEach { block ->
                                val row = table.select { table.id eq block.location.toJson() }.first()
                                block.blockData = GrapesRPG.gson.fromJson(row[table.data], object : TypeToken<BlockData<*>>() {}.type)
                            }
                    }
                }
            }
            delay(50)
        }
        this.chunkLock.withLock { this.chunks.removeAll(copy) }
    }

    override fun add(chunk: Chunk) {
        WorldScope.launch {
            this@ChunkLoader.chunkLock.withLock {
                this@ChunkLoader.chunks.add(chunk)
            }
        }
    }

    override fun shutdownGracefully() {
        runBlocking {
            this@ChunkLoader.commit()
            job.cancelAndJoin()
        }
    }
}

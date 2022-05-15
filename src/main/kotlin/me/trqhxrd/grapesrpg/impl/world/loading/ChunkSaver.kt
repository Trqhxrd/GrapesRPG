package me.trqhxrd.grapesrpg.impl.world.loading

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.api.world.Block
import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkHandler
import me.trqhxrd.grapesrpg.impl.world.blockdata.Void
import me.trqhxrd.grapesrpg.util.coords.Coordinate
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ChunkSaver(
    override val database: Database,
    private val dbLock: ReadWriteMutex,
    private val chunks: MutableSet<Chunk> = mutableSetOf(),
    private val chunkLock: Mutex = Mutex(false)
) : ChunkHandler {

    private var job: Job
    private val savingJobs: MutableSet<Job> = mutableSetOf()

    companion object {
        var max_delay = 60L
        var delay = 5L
        var threashold = 10L
    }

    init {
        var timeSinceLastCommit = 0
        job = WorldScope.launch {
            while (this.isActive) {
                if (timeSinceLastCommit > max_delay || this@ChunkSaver.chunks.size > threashold) {
                    this@ChunkSaver.commit()
                    timeSinceLastCommit = 0
                } else {
                    timeSinceLastCommit++
                    delay(delay)
                }
            }
        }
    }

    override suspend fun commit() {
        this.savingJobs.joinAll()
        this.chunkLock.withLock {
            val copy = chunks.toMutableSet().filter { it.lock.withLock { return@filter it.blocks.isNotEmpty() } }
            for (chunk in copy) {
                val blockCopy: MutableMap<Coordinate, Block>
                chunk.lock.withLock {
                    blockCopy = chunk.blocks.toMutableMap()
                }
                for (next in blockCopy)
                    if (next.value.blockData is Void)
                        chunk.blocks.remove(next.key)
            }
            this.dbLock.write {
                transaction(this.database) {
                    for (chunk in copy) {
                        val table = chunk.table
                        var b = false
                        for (block in chunk.blocks.filter { it.value.blockData !is Void }) {
                            if (!b) {
                                SchemaUtils.create(table)
                                b = true
                            }
                            table.insert {
                                it[table.id] = block.key.inChunkCoords().toJson()
                                it[table.dataType] = block.value.blockData.id.toJson()
                                it[table.data] = GrapesRPG.gson.toJson(block.value.blockData)
                            }
                        }
                    }
                }
                delay(50)
            }
            this.chunks.removeAll(copy.toSet())
        }
    }

    override fun add(chunk: Chunk) {
        savingJobs.add(WorldScope.launch {
            this@ChunkSaver.chunkLock.withLock {
                this@ChunkSaver.chunks.add(chunk)
            }
        })
    }

    override fun shutdownGracefully() {
        runBlocking {
            this@ChunkSaver.savingJobs.joinAll()
            this@ChunkSaver.commit()
            job.cancelAndJoin()
        }
    }
}

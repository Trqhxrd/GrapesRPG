package me.trqhxrd.grapesrpg.impl.world.loading

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.trqhxrd.grapesrpg.api.world.Chunk
import me.trqhxrd.grapesrpg.api.world.jdbc.ChunkHandler
import me.trqhxrd.grapesrpg.util.sync.ReadWriteMutex
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class ChunkSaver(
    override val database: Database,
    private val dbLock: ReadWriteMutex,
    private val chunks: MutableSet<Chunk> = mutableSetOf(),
    private val chunkLock: Mutex = Mutex(false)
) : ChunkHandler {

    private var job: Job

    companion object {
        const val MAX_TIME_SINCE_LAST_COMMIT = 60
        const val DELAY = 1000L
        const val THRESHOLD = 10
    }

    init {
        var timeSinceLastCommit = 0
        job = WorldScope.launch {
            while (this.isActive) {
                if (timeSinceLastCommit > MAX_TIME_SINCE_LAST_COMMIT || this@ChunkSaver.chunks.size > THRESHOLD) {
                    this@ChunkSaver.commit()
                    timeSinceLastCommit = 0
                } else {
                    timeSinceLastCommit++
                    delay(DELAY)
                }
                if (!this.isActive) this@ChunkSaver.commit()
            }
        }
    }

    override suspend fun commit() {
        this.chunkLock.withLock {
            val copy = chunks.toMutableSet()
            this.dbLock.write {
                transaction {
                    for (chunk in copy) {
                        val table = chunk.table
                        if (!table.exists()) SchemaUtils.create(table)
                        for (block in chunk.blocks.filter { it.value.data !is me.trqhxrd.grapesrpg.impl.world.blockdata.Void }) {
                            table.insert {
                                it[table.id] = block.key.toJson()
                                it[table.dataType] = block.value.data.id.toJson()
                                it[table.data] = block.value.data.save()
                            }
                        }
                    }
                }
                delay(50)
            }
            this.chunks.removeAll(copy)
        }
    }

    override fun add(chunk: Chunk) {
        WorldScope.launch {
            this@ChunkSaver.chunkLock.withLock {
                this@ChunkSaver.chunks.add(chunk)
            }
        }
    }

    override fun shutdownGracefully() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}

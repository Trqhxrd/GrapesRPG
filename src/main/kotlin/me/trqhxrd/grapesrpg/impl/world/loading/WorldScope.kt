package me.trqhxrd.grapesrpg.impl.world.loading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object WorldScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
}

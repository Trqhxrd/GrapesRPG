package me.trqhxrd.grapesrpg.impl.world

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object WorldScope : CoroutineScope {
    override val coroutineContext = Dispatchers.IO
}

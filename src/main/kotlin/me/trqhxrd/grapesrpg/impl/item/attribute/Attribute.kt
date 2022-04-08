package me.trqhxrd.grapesrpg.impl.item.attribute

import me.trqhxrd.grapesrpg.util.ModuleKey

abstract class Attribute(override val moduleKey: ModuleKey) : me.trqhxrd.grapesrpg.api.item.attribute.Attribute {

    constructor(module: String, key: String) : this(ModuleKey(module, key))
}

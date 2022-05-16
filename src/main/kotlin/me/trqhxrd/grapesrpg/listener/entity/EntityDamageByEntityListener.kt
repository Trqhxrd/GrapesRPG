package me.trqhxrd.grapesrpg.listener.entity

import me.trqhxrd.grapesrpg.GrapesRPG
import me.trqhxrd.grapesrpg.game.item.attribute.Damaging
import me.trqhxrd.grapesrpg.impl.item.Item
import me.trqhxrd.grapesrpg.util.AbstractListener
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityDamageByEntityListener : AbstractListener(GrapesRPG.plugin) {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is LivingEntity) return
        val damager = event.damager as LivingEntity
        val damagerEquipment = damager.equipment
        if (damager.equipment == null) return
        val weaponIs = damagerEquipment?.itemInMainHand ?: return
        if (!Item.isGrapesItem(weaponIs)) return
        val weapon = Item.fromItemStack(weaponIs)
        val attr = weapon.getAttribute(Damaging::class)
        if (attr != null) event.damage = attr.damage.toDouble()
    }
}
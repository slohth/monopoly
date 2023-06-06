package dev.slohth.monopoly.utils.region.event

import dev.slohth.monopoly.utils.region.Region
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class RegionEnterEvent(val region: Region, val player: Player) : Event() {

    override fun getHandlers(): HandlerList { return h }

    companion object {
        @JvmStatic val h = HandlerList()
        @JvmStatic fun getHandlerList(): HandlerList { return h }
    }

}
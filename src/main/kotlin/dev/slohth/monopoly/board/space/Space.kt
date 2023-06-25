package dev.slohth.monopoly.board.space

import dev.slohth.monopoly.Monopoly
import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.behaviour.Purchasable
import dev.slohth.monopoly.board.space.types.property.Property
import dev.slohth.monopoly.board.space.types.special.Railway
import dev.slohth.monopoly.board.space.types.special.Utility
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.CC.color
import dev.slohth.monopoly.utils.region.Region
import dev.slohth.monopoly.utils.region.event.RegionEnterEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

abstract class Space(open val board: Board, open val name: String, open val type: SpaceType, open val region: Region) : Listener {

    private val plugin = Monopoly.INSTANCE!!

    abstract fun interact(profile: Profile)

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onRegionEnter(event: RegionEnterEvent) {
        if (event.region != region) return

        val msg = "&7${board.spaceIndex(this)} &fEntered ${
            if (this is Property) "&bProperty"
            else if (this is Utility) "&aUtility"
            else if (this is Railway) "&cRailway"
            else "&fSpace"
        }&e $name ${
            if (this is Purchasable) "&ffor &2$&a$cost" else ""
        }".color()

        event.player.sendMessage(msg)

        plugin.profileManager.get(event.player.uniqueId)?.let {
            if (!it.isInGame || it.game!! != board.game) return
            interact(it)
        }
    }

}
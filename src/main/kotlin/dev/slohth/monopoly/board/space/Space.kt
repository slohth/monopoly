package dev.slohth.monopoly.board.space

import dev.slohth.monopoly.Monopoly
import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.types.property.Property
import dev.slohth.monopoly.board.space.types.property.railway.Railway
import dev.slohth.monopoly.board.space.types.property.utility.Utility
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region
import dev.slohth.monopoly.utils.region.event.RegionEnterEvent
import org.bukkit.Bukkit
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

        event.player.sendMessage("Entered ${
            if (this is Utility) "utility"
            else if (this is Railway) "railway"
            else if (this is Property) "property"
            else "null"
        }: $name")

        plugin.profileManager.get(event.player.uniqueId)?.let {
            if (!it.isInGame || it.game!! != board.game) return
            interact(it)
        }
    }

}
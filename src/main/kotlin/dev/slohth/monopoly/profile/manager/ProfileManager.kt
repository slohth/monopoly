package dev.slohth.monopoly.profile.manager

import dev.slohth.monopoly.Monopoly
import dev.slohth.monopoly.profile.Profile
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import kotlin.collections.HashMap

class ProfileManager(private val plugin: Monopoly) : Listener {

    private val profiles: MutableMap<UUID, Profile> = HashMap()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    private fun register(player: Player) {
        val profile = Profile(player.uniqueId)
        profiles[player.uniqueId] = profile

        plugin.scoreboard.getTeam(if (player.name == "Slohth") "dev" else "player")?.addEntry(player.name)
        player.scoreboard = plugin.scoreboard
    }

    private fun unregister(player: Player) {
        get(player.uniqueId)?.let {
            // save data or something idk
        }
        profiles.remove(player.uniqueId)
    }

    fun get(id: UUID): Profile? = profiles[id]

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        register(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        unregister(event.player)
    }

}
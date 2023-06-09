package dev.slohth.monopoly

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.game.Game
import dev.slohth.monopoly.profile.manager.ProfileManager
import dev.slohth.monopoly.utils.CC.color
import dev.slohth.monopoly.utils.command.Framework
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Scoreboard

class Monopoly : JavaPlugin() {

    lateinit var framework: Framework
    lateinit var scoreboard: Scoreboard

    lateinit var profileManager: ProfileManager

    override fun onEnable() {
        INSTANCE = this
        framework = Framework(this)
        scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard

        profileManager = ProfileManager(this)

        val playerTeam = scoreboard.registerNewTeam("player")
        playerTeam.color = ChatColor.GRAY

        val devTeam = scoreboard.registerNewTeam("dev")
        devTeam.color = ChatColor.GRAY
        devTeam.prefix = "&6☕ ".color()

        for (command in listOf(TestCommand())) framework.registerCommands(command)

        Board(Game())
    }

    companion object {
        var INSTANCE: Monopoly? = null
    }

}
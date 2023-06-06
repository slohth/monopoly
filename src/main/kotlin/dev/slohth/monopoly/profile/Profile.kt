package dev.slohth.monopoly.profile

import dev.slohth.monopoly.game.Game
import dev.slohth.monopoly.utils.CC
import dev.slohth.monopoly.utils.CC.color
import org.bukkit.Bukkit
import java.util.UUID

data class Profile(val id: UUID) {

    val player get() = Bukkit.getPlayer(id)!!

    var game: Game? = null
    val isInGame get() = game != null

    fun msg(vararg msg: String) {
        player.sendMessage(CC.join(listOf(*msg), "\n").color())
    }

}
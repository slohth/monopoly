package dev.slohth.monopoly.utils.region

import dev.slohth.monopoly.Monopoly
import dev.slohth.monopoly.utils.region.event.RegionEnterEvent
import dev.slohth.monopoly.utils.region.event.RegionExitEvent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.BoundingBox
import java.util.*
import kotlin.collections.HashSet

class Region {

    private val inRegion: MutableSet<UUID> = HashSet()
    private var region: BoundingBox = BoundingBox()
    val id = UUID.randomUUID()

    private val task: BukkitTask = object: BukkitRunnable() {
        override fun run() {

            for (player in Bukkit.getOnlinePlayers()) {

                if (contains(player.location)) {
                    if (!inRegion.contains(player.uniqueId)) {
                        inRegion.add(player.uniqueId)
                        Bukkit.getPluginManager().callEvent(RegionEnterEvent(this@Region, player))
                    }
                } else {
                    if (inRegion.contains(player.uniqueId)) {
                        inRegion.remove(player.uniqueId)
                        Bukkit.getPluginManager().callEvent(RegionExitEvent(this@Region, player))
                    }
                }

            }

        }
    }.runTaskTimer(Monopoly.INSTANCE!!, 2, 2)

    fun contains(location: Location): Boolean {
        if (location.x < region.minX || location.x > region.maxX) return false
        if (location.y < region.minY || location.y > region.maxY) return false
        return !(location.z < region.minZ || location.z > region.maxZ)
    }

    fun setCorners(location: Location, location2: Location) {
        region = BoundingBox.of(location, location2)
    }

    fun setCorners(block: Block, block2: Block) {
        region = BoundingBox.of(block, block2)
    }

}
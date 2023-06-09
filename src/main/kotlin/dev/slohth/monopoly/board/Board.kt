package dev.slohth.monopoly.board

import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceDirection
import dev.slohth.monopoly.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*

class Board(val game: Game) {

    val spaces: MutableList<Space> = LinkedList()

    init {
        // FOR TESTING

        // -30 89 20
        // 135 89 -145

        /**
         *
         * FACING EAST (23, 11)
         * [17:15:26 INFO]: RED | 0.0,0.0,-140.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-127.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-114.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-101.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-88.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-75.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-62.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-49.0
         * [17:15:26 INFO]: RED | 0.0,0.0,-36.0
         *
         * FACING SOUTH (-11, 23)
         * [17:15:26 INFO]: ORANGE | 36.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 49.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 62.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 75.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 88.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 101.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 114.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 127.0,0.0,-165.0
         * [17:15:26 INFO]: ORANGE | 140.0,0.0,-165.0
         *
         * FACING WEST (-23, -11)
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-129.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-116.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-103.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-90.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-77.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-64.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-51.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-38.0
         * [17:15:26 INFO]: YELLOW | 165.0,0.0,-25.0
         *
         * FACING NORTH (11, -23)
         * [17:15:26 INFO]: GREEN | 25.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 38.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 51.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 64.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 77.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 90.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 103.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 116.0,0.0,0.0
         * [17:15:26 INFO]: GREEN | 129.0,0.0,0.0
         */

//        val bottomRight = Location(Bukkit.getWorld("world")!!, -30.0, 89.0, 20.0)
//
//        val red: MutableList<String> = ArrayList()
//        val orange: MutableList<String> = ArrayList()
//        val yellow: MutableList<String> = ArrayList()
//        val green: MutableList<String> = ArrayList()
//
//        for (x in -30..135) {
//            for (z in -145..20) {
//                val loc = Location(Bukkit.getWorld("world")!!, x.toDouble(), 89.0, z.toDouble())
//
//                when (loc.block.type) {
//                    Material.RED_WOOL -> { red }
//                    Material.ORANGE_WOOL -> { orange }
//                    Material.YELLOW_WOOL -> { yellow }
//                    Material.GREEN_WOOL -> { green }
//                    else -> { null }
//                }?.add("${loc.x - bottomRight.x},${loc.y - bottomRight.y},${loc.z - bottomRight.z}")
//            }
//        }
//
//        for (item in red) Bukkit.getLogger().info("RED | $item")
//        for (item in orange) Bukkit.getLogger().info("ORANGE | $item")
//        for (item in yellow) Bukkit.getLogger().info("YELLOW | $item")
//        for (item in green) Bukkit.getLogger().info("GREEN | $item")
    }

    fun genSpace(start: Location, facing: SpaceDirection) {
        val relative = when (facing) {
            SpaceDirection.NORTH -> Pair(11, -23)
            SpaceDirection.EAST -> Pair(23, 11)
            SpaceDirection.SOUTH -> Pair(-11, 23)
            SpaceDirection.WEST -> Pair(-23, -11)
        }

        val xRange = if (relative.first >= 0) 0..relative.first else 0 downTo relative.first
        val zRange = if (relative.second >= 0) 0..relative.second else 0 downTo relative.second

        for (x in xRange) {
            for (z in zRange) {
                start.block.getRelative(x, 0 , z).type = Material.WHITE_CONCRETE
            }
        }
    }

}
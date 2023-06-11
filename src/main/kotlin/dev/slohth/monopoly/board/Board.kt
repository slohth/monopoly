package dev.slohth.monopoly.board

import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceColor
import dev.slohth.monopoly.board.space.SpaceDirection
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.game.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*
import kotlin.collections.LinkedHashSet

class Board(val game: Game) {

    private val spaces: MutableList<Space> = LinkedList()

    companion object {
        /*
        For each property, we store the bottom left corner of each tile
        They are also grouped into their colours/type and stored in order of appearance on the board
         */
        private val RELATIVE_POSITIONS_PROPERTIES: Map<SpaceColor, LinkedHashSet<Pair<Int, Int>>> = hashMapOf(
                SpaceColor.BROWN to linkedSetOf(0 to -36, 0 to -62),
                SpaceColor.LIGHT_BLUE to linkedSetOf(0 to -101, 0 to -127, 0 to -140),
                SpaceColor.PINK to linkedSetOf(36 to -165, 62 to -165, 74 to -165),
                SpaceColor.ORANGE to linkedSetOf(101 to -165, 127 to -165, 140 to -165),
                SpaceColor.RED to linkedSetOf(165 to -129, 165 to -103, 165 to -90),
                SpaceColor.YELLOW to linkedSetOf(165 to -64, 165 to -51, 165 to -25),
                SpaceColor.GREEN to linkedSetOf(129 to 0, 116 to 0, 90 to 0),
                SpaceColor.BLUE to linkedSetOf(51 to 0, 25 to 0)
        )

        private val RELATIVE_POSITIONS_PROPERTIES_SPECIAL: Map<SpaceType, LinkedHashSet<Pair<Int, Int>>> = hashMapOf(
                SpaceType.RAILWAY to linkedSetOf(0 to -88, 88 to -65, 165 to -77, 77 to 0),
                SpaceType.UTILITY to linkedSetOf(49 to -165, 165 to -38)
        )

        private val RELATIVE_POSITIONS_ACTION: Map<SpaceType, LinkedHashSet<Pair<Int, Int>>> = hashMapOf(
                SpaceType.CHEST to linkedSetOf(0 to -49, 114 to -165, 103 to 0),
                SpaceType.CHANCE to linkedSetOf(0 to -114, 165 to -116, 64 to 0),
                SpaceType.TAX to linkedSetOf(0 to -75, 38 to 0)
        )

        // Storing the two corners of each region on the board
        private val RELATIVE_POSITIONS_CORNERS: Map<SpaceType, Pair<Pair<Int, Int>, Pair<Int, Int>>> = hashMapOf(
                SpaceType.GO to ((0 to -23) to (23 to 0)),
                SpaceType.VISITING_JAIL to ((0 to -165) to (23 to -142)),
                SpaceType.JAIL to ((23 to -142) to (11 to -154)),
                SpaceType.GO_TO_JAIL to ((142 to -23) to (165 to 0)),
                SpaceType.FREE_PARKING to ((142 to -165) to (165 to -142))
        )

        fun LinkedHashSet<Any>.indexOf(item: Any): Int {
            return ArrayList(this).indexOf(item)
        }
    }

    init {
    }

    private fun genSpace(start: Location, facing: SpaceDirection) {
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
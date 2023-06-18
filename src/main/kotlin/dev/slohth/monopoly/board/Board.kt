package dev.slohth.monopoly.board

import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceColor
import dev.slohth.monopoly.board.space.SpaceDirection
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.types.property.Property
import dev.slohth.monopoly.board.space.types.property.railway.Railway
import dev.slohth.monopoly.board.space.types.property.utility.Utility
import dev.slohth.monopoly.game.Game
import dev.slohth.monopoly.utils.Config
import dev.slohth.monopoly.utils.region.Region
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import java.util.*
import kotlin.collections.LinkedHashSet

class Board(val game: Game, val location: Location) {

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
        genProperties()
        genSpecialProperties()
    }

    private fun genProperties() {
        for (entry in RELATIVE_POSITIONS_PROPERTIES.entries) {
            for (property in entry.value) {
                val path = "${entry.key.toString().lowercase()}.${entry.value.indexOf(property)}"

                val name = Config.PROPERTIES.getString("$path.name") ?: "null"
                val cost = Config.PROPERTIES.getInteger("$path.cost")

                val propertyBL = location.block.getRelative(property.first, 0, property.second)
                val corners = calcOtherCorner(entry.key)
                val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)

                spaces.add(Property(this, name, SpaceType.PROPERTY, Region(
                        propertyBL,
                        propertyTR,
                ), cost))
            }
        }
    }

    private fun genSpecialProperties() {
        for (railway in RELATIVE_POSITIONS_PROPERTIES_SPECIAL[SpaceType.RAILWAY]!!.withIndex()) {
            val name = Config.PROPERTIES.getString("railway.${railway.index}") ?: "null"

            val propertyBL = location.block.getRelative(railway.value.first, 0, railway.value.second)
            val corners = getBLtoTR(when (railway.index) {
                0 -> SpaceDirection.EAST
                1 -> SpaceDirection.SOUTH
                2 -> SpaceDirection.WEST
                3 -> SpaceDirection.NORTH
                else -> continue
            })
            val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)

            spaces.add(Railway(this, name, Region(
                    propertyBL,propertyTR
            )))
        }

        for (utility in RELATIVE_POSITIONS_PROPERTIES_SPECIAL[SpaceType.UTILITY]!!.withIndex()) {
            val name = Config.PROPERTIES.getString("utility.${utility.index}") ?: "null"

            val propertyBL = location.block.getRelative(utility.value.first, 0, utility.value.second)
            val corners = getBLtoTR(when (utility.index) {
                0 -> SpaceDirection.SOUTH
                1 -> SpaceDirection.WEST
                else -> continue
            })
            val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)

            spaces.add(Utility(this, name, Region(
                    propertyBL, propertyTR
            )))
        }
    }

    private fun calcOtherCorner(color: SpaceColor): Pair<Int, Int> {
        return getBLtoTR(when (color) {
            SpaceColor.BROWN, SpaceColor.LIGHT_BLUE -> SpaceDirection.EAST
            SpaceColor.PINK, SpaceColor.ORANGE -> SpaceDirection.SOUTH
            SpaceColor.RED, SpaceColor.YELLOW -> SpaceDirection.WEST
            SpaceColor.GREEN, SpaceColor.BLUE -> SpaceDirection.NORTH
        })
    }

    private fun getBLtoTR(direction: SpaceDirection): Pair<Int, Int> {
        return when (direction) {
            SpaceDirection.NORTH -> Pair(11, -23)
            SpaceDirection.EAST -> Pair(23, 11)
            SpaceDirection.SOUTH -> Pair(-11, 23)
            SpaceDirection.WEST -> Pair(-23, -11)
        }
    }

}
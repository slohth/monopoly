package dev.slohth.monopoly.board

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceColor
import dev.slohth.monopoly.board.space.SpaceDirection
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.types.action.Chance
import dev.slohth.monopoly.board.space.types.action.Chest
import dev.slohth.monopoly.board.space.types.action.Tax
import dev.slohth.monopoly.board.space.types.corner.*
import dev.slohth.monopoly.board.space.types.property.Property
import dev.slohth.monopoly.board.space.types.special.Railway
import dev.slohth.monopoly.board.space.types.special.Utility
import dev.slohth.monopoly.game.Game
import dev.slohth.monopoly.utils.Config
import dev.slohth.monopoly.utils.region.Region
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

class Board(val game: Game, val location: Location) {

    private val spaces: BiMap<String, Space> = HashBiMap.create()

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
                SpaceType.RAILWAY to linkedSetOf(0 to -88, 88 to -165, 165 to -77, 77 to 0),
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

        private val BOARD_ORDER: LinkedList<String> = LinkedList()

        init {
            BOARD_ORDER.addAll(listOf(
                    "GO", "BROWN-0", "CHEST-0", "BROWN-1", "TAX-0", "RAILWAY-0", "LIGHT_BLUE-0", "CHANCE-0", "LIGHT_BLUE-1",
                    "LIGHT_BLUE-2", "VISITING_JAIL", "PINK-0", "UTILITY-0", "PINK-1", "PINK-2", "RAILWAY-1", "ORANGE-0",
                    "CHEST-1", "ORANGE-1", "ORANGE-2", "FREE_PARKING", "RED-0", "CHANCE-1", "RED-1", "RED-2", "RAILWAY-2",
                    "YELLOW-0", "YELLOW-1", "UTILITY-1", "YELLOW-2", "GO_TO_JAIL", "GREEN-0", "GREEN-1", "CHEST-2", "GREEN-2",
                    "RAILWAY-3", "CHANCE-2", "BLUE-0", "TAX-1", "BLUE-1"
            ))
        }

        fun LinkedHashSet<Any>.indexOf(item: Any): Int {
            return ArrayList(this).indexOf(item)
        }
    }

    init {
        genProperties()
        genSpecialProperties()
        genActionProperties()
        genCorners()
    }

    private fun genProperties() {
        for (entry in RELATIVE_POSITIONS_PROPERTIES.entries) {
            for (property in entry.value) {
                val index = entry.value.indexOf(property)
                val path = "${entry.key.toString().lowercase()}.$index"

                val name = Config.PROPERTIES.getString("$path.name") ?: "null"
                val cost = Config.PROPERTIES.getInteger("$path.cost")

                val propertyBL = location.block.getRelative(property.first, 0, property.second)
                val corners = calcOtherCorner(entry.key)
                val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)

                spaces["${entry.key}-$index"] = Property(this, name, entry.key, Region(propertyBL, propertyTR), cost)
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

            spaces["RAILWAY-${railway.index}"] = Railway(this, name, Region(propertyBL, propertyTR))
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

            spaces["UTILITY-${utility.index}"] = Utility(this, name, Region(propertyBL, propertyTR))
        }
    }

    private fun genActionProperties() {
        for (chest in RELATIVE_POSITIONS_ACTION[SpaceType.CHEST]!!.withIndex()) {
            val propertyBL = location.block.getRelative(chest.value.first, 0, chest.value.second)
            val corners = getBLtoTR(when (chest.index) {
                0 -> SpaceDirection.EAST
                1 -> SpaceDirection.SOUTH
                2 -> SpaceDirection.NORTH
                else -> continue
            })
            val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)
            spaces["CHEST-${chest.index}"] = Chest(this, Region(propertyBL, propertyTR))
        }

        for (chance in RELATIVE_POSITIONS_ACTION[SpaceType.CHANCE]!!.withIndex()) {
            val propertyBL = location.block.getRelative(chance.value.first, 0, chance.value.second)
            val corners = getBLtoTR(when (chance.index) {
                0 -> SpaceDirection.EAST
                1 -> SpaceDirection.WEST
                2 -> SpaceDirection.NORTH
                else -> continue
            })
            val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)
            spaces["CHANCE-${chance.index}"] = Chance(this, Region(propertyBL, propertyTR))
        }

        for (tax in RELATIVE_POSITIONS_ACTION[SpaceType.TAX]!!.withIndex()) {
            val propertyBL = location.block.getRelative(tax.value.first, 0, tax.value.second)
            val corners = getBLtoTR(when (tax.index) {
                0 -> SpaceDirection.EAST
                1 -> SpaceDirection.NORTH
                else -> continue
            })
            val propertyTR = propertyBL.getRelative(corners.first, 10, corners.second)
            spaces["TAX-${tax.index}"] = Tax(this, Region(propertyBL, propertyTR))
        }
    }

    private fun genCorners() {
        for (entry in RELATIVE_POSITIONS_CORNERS) {
            val regionBL = location.block.getRelative(entry.value.first.first, 0, entry.value.first.second)
            val regionTR = location.block.getRelative(entry.value.second.first, 10, entry.value.second.second)
            val region = Region(regionBL, regionTR)

            Bukkit.getLogger().info("${entry.key} : ${regionBL.location.blockX}, " +
                    "${regionBL.location.blockY}, ${regionBL.location.blockZ} to " +
                    "${regionTR.location.blockX}, ${regionTR.location.blockY}, ${regionTR.location.blockZ}")

            spaces[entry.key.toString()] = when (entry.key) {
                SpaceType.GO -> Go(this, region)
                SpaceType.VISITING_JAIL -> Visiting(this, region)
                SpaceType.JAIL -> Jail(this, region)
                SpaceType.FREE_PARKING -> FreeParking(this, region)
                SpaceType.GO_TO_JAIL -> GoToJail(this, region)
                else -> continue
            }
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

    fun spaceIndex(space: Space): Int {
        return BOARD_ORDER.indexOf(spaces.inverse()[space])
    }

}
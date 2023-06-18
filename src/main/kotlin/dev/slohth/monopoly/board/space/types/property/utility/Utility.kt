package dev.slohth.monopoly.board.space.types.property.utility

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.types.property.Property
import dev.slohth.monopoly.utils.region.Region

class Utility(
        override val board: Board, override val name: String, override val region: Region
) : Property(board, name, SpaceType.UTILITY, region, 150) {
}
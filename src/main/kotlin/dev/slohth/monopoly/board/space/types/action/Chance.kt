package dev.slohth.monopoly.board.space.types.action

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.behaviour.Actionable
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region

class Chance(
        override val board: Board, override val region: Region
) : Space(board, "Chance", SpaceType.CHANCE, region), Actionable {

    override fun interact(profile: Profile) {

    }

}
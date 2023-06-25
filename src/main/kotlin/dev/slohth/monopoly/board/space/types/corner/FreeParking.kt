package dev.slohth.monopoly.board.space.types.corner

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region

class FreeParking(
        override val board: Board, override val region: Region
) : Space(board, "Free Parking", SpaceType.GO, region) {

    override fun interact(profile: Profile) {

    }

}
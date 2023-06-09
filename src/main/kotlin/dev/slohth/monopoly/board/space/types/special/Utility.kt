package dev.slohth.monopoly.board.space.types.special

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.behaviour.Purchasable
import dev.slohth.monopoly.board.space.behaviour.PurchaseState
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region

class Utility(
        override val board: Board, override val name: String, override val region: Region
) : Space(board, name, SpaceType.UTILITY, region), Purchasable {

    override var owner: Profile? = null
    override var state: PurchaseState = PurchaseState.UNCLAIMED

    override val cost get() = 200

    override fun interact(profile: Profile) {

    }

}
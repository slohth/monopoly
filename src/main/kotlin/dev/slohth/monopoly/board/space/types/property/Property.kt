package dev.slohth.monopoly.board.space.types.property

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.board.space.behaviour.Purchasable
import dev.slohth.monopoly.board.space.behaviour.PurchaseState
import dev.slohth.monopoly.board.space.behaviour.Upgradable
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region

open class Property(
        override val board: Board, override val name: String, override val region: Region
) : Space(board, name, SpaceType.PROPERTY, region), Purchasable, Upgradable {

    override var owner: Profile? = null
    override var state: PurchaseState = PurchaseState.UNCLAIMED
    override var houses: Int = 0

    override val cost get() = 150

    override fun interact(profile: Profile) {
        when (state) {
            PurchaseState.OWNED -> {
                if (profile == owner) {
                    profile.msg(" ", "&aYou may rest easy... you own this property!", " ")
                    return
                } else {
                    // force payment
                }
            }
            PurchaseState.MORTGAGED -> { return }
            PurchaseState.UNCLAIMED -> { return }
        }
    }

}
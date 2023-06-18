package dev.slohth.monopoly.board.space.types.property

import dev.slohth.monopoly.Monopoly
import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.Space
import dev.slohth.monopoly.board.space.SpaceType
import dev.slohth.monopoly.profile.Profile
import dev.slohth.monopoly.utils.region.Region

open class Property(
        override val board: Board, override val name: String, override val type: SpaceType, override val region: Region, open val cost: Int
) : Space(board, name, type, region) {

    var state = PropertyState.UNCLAIMED
    val mortgageCost = cost * 0.5

    var owner: Profile? = null
    var houses = 0

    override fun interact(profile: Profile) {
        when (state) {
            PropertyState.OWNED -> {
                if (profile == owner) {
                    profile.msg(" ", "&aYou may rest easy... you own this property!", " ")
                    return
                } else {
                    // force payment
                }
            }
            PropertyState.MORTGAGED -> { return }
            PropertyState.UNCLAIMED -> { return }
        }
    }

}
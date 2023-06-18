package dev.slohth.monopoly.board.space.behaviour

import dev.slohth.monopoly.profile.Profile

interface Purchasable {

    var state: PurchaseState
    var owner: Profile?

    val cost: Int
    val mortgageCost get() = cost * 0.5

    fun purchase(purchaser: Profile) {

    }

    fun mortgage() {

    }

}
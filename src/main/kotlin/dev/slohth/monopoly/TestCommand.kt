package dev.slohth.monopoly

import dev.slohth.monopoly.board.Board
import dev.slohth.monopoly.board.space.SpaceDirection
import dev.slohth.monopoly.game.Game
import dev.slohth.monopoly.utils.command.Command
import dev.slohth.monopoly.utils.command.CommandArgs
import dev.slohth.monopoly.utils.command.Completer
import dev.slohth.monopoly.utils.command.ICommand

class TestCommand : ICommand {

    @Command(name = "ping")
    fun onPingCommand(args: CommandArgs) {
        args.sender.sendMessage("Pong!")
    }

    @Command(name = "space", inGameOnly = true)
    fun onSpaceCommand(args: CommandArgs) {
        val direction = SpaceDirection.valueOf(args.getArgs(0)!!.uppercase())
        Board(Game()).genSpace(args.getPlayer()!!.location, direction)
    }

    @Completer(name = "space")
    fun onSpaceComplete(args: CommandArgs): List<String> {
        if (args.args.size == 1) return listOf("NORTH", "EAST", "SOUTH", "WEST")
        return emptyList()
    }

}
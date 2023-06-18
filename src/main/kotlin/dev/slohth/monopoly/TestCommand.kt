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

    @Command(name = "board", inGameOnly = true)
    fun onSpaceCommand(args: CommandArgs) {
        //Board(Game()).genSpaces(args.getPlayer()!!.location)
    }

}
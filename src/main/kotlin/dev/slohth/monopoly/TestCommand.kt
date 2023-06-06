package dev.slohth.monopoly

import dev.slohth.monopoly.utils.command.Command
import dev.slohth.monopoly.utils.command.CommandArgs
import dev.slohth.monopoly.utils.command.ICommand

class TestCommand : ICommand {

    @Command(name = "ping")
    fun onPingCommand(args: CommandArgs) {
        args.sender.sendMessage("Pong!")
    }

}
package dev.slohth.monopoly.utils.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Command Framework - CommandArgs <br></br>
 * This class is passed to the command methods and contains various utilities as
 * well as the command info.
 *
 * @author minnymin3
 */
class CommandArgs(
    sender: CommandSender, command: Command, label: String?, args: Array<String>,
    subCommand: Int
) {
    /**
     * Gets the command sender
     *
     * @return
     */
    val sender: CommandSender

    /**
     * Gets the original command object
     *
     * @return
     */
    val command: Command

    /**
     * Gets the label including sub command labels of this command
     *
     * @return Something like 'test.subcommand'
     */
    val label: String

    /**
     * Gets all the arguments after the command's label. ie. if the command
     * label was test.subcommand and the arguments were subcommand foo foo, it
     * would only return 'foo foo' because 'subcommand' is part of the command
     *
     * @return
     */
    val args: Array<String?>

    init {
        val modArgs = arrayOfNulls<String>(args.size - subCommand)
        for (i in 0 until args.size - subCommand) {
            modArgs[i] = args[i + subCommand]
        }
        val buffer = StringBuffer()
        buffer.append(label)
        for (x in 0 until subCommand) {
            buffer.append("." + args[x])
        }
        val cmdLabel = buffer.toString()
        this.sender = sender
        this.command = command
        this.label = cmdLabel
        this.args = modArgs
    }

    /**
     * Gets the argument at the specified index
     *
     * @param index The index to get
     * @return The string at the specified index
     */
    fun getArgs(index: Int): String? {
        return if (index < args.size) args[index] else null
    }

    /**
     * Returns the length of the command arguments
     *
     * @return int length of args
     */
    fun length(): Int {
        return args.size
    }

    val isPlayer: Boolean
        get() = sender is Player

    fun getPlayer(): Player? {
        return if (isPlayer) sender as Player else null
    }
}
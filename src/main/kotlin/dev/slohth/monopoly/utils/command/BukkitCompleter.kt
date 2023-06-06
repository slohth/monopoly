package dev.slohth.monopoly.utils.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import java.util.AbstractMap.SimpleEntry

/**
 * Command Framework - BukkitCompleter <br></br>
 * An implementation of the TabCompleter class allowing for multiple tab
 * completers per command
 *
 * @author minnymin3
 */
class BukkitCompleter : TabCompleter {
    private val completers: MutableMap<String, Map.Entry<Method, Any>> = HashMap()
    fun addCompleter(label: String, m: Method, obj: Any) {
        completers[label] = SimpleEntry(m, obj)
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        for (i in args.size downTo 0) {
            val buffer = StringBuffer()
            buffer.append(label.lowercase(Locale.getDefault()))
            for (x in 0 until i) {
                if (args[x] != "" && args[x] != " ") {
                    buffer.append("." + args[x].lowercase(Locale.getDefault()))
                }
            }
            val cmdLabel = buffer.toString()
            if (completers.containsKey(cmdLabel)) {
                val (key, value) = completers[cmdLabel]!!
                try {
                    return key.invoke(value,
                        CommandArgs(
                            sender,
                            command,
                            label,
                            args,
                            cmdLabel.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray().size - 1)) as List<String>
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}
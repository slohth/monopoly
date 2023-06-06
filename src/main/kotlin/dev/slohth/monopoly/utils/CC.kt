package dev.slohth.monopoly.utils

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

object CC {

    private val pattern = Pattern.compile("#[a-fA-F0-9]{6}")

    fun String.color(): String {
        var str = this

        var matcher = pattern.matcher(str)
        while (matcher.find()) {
            val color = str.substring(matcher.start(), matcher.end())
            val newStr = StringBuilder("&x")
            for (i in 1 until color.length) newStr.append("&").append(color[i])
            str = str.replace(color, newStr.toString())
            matcher = pattern.matcher(str)
        }

        return ChatColor.translateAlternateColorCodes('&', str)
    }

    fun String.stripColor(): String {
        return ChatColor.stripColor(this)!!
    }

    fun Collection<String?>.color(): List<String> {
        val toReturn: MutableList<String> = ArrayList()
        for (line in this) if (line != null) toReturn.add(line.color())
        return toReturn
    }

    fun Array<String?>.color(): List<String> {
        val toReturn: MutableList<String> = ArrayList()
        for (line in this) if (line != null) toReturn.add(line.color())
        return toReturn
    }

    @JvmName("colorString")
    fun Array<out String>.color(): List<String> {
        val toReturn: MutableList<String> = ArrayList()
        for (line in this) toReturn.add(line.color())
        return toReturn
    }

    fun String.getLastColor(): org.bukkit.ChatColor {
        var color: org.bukkit.ChatColor = org.bukkit.ChatColor.WHITE
        if (this.isEmpty()) return color

        var next = false
        for (char in this.color().toCharArray()) {
            if (next) { color = org.bukkit.ChatColor.getByChar(char)!!; next = false }
            if (char == ChatColor.COLOR_CHAR) next = true
        }
        return color
    }

    fun join(list: List<String>?, separator: String): String {
        if (list == null) return ""
        val sb = StringBuilder()
        for (i in 0 until list.size - 1) {
            sb.append("${list[i]}${separator}")
        }
        sb.append(list[list.size - 1])
        return sb.toString()
    }
}
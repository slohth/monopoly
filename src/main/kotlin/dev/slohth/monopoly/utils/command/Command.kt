package dev.slohth.monopoly.utils.command

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Command Framework - Command <br></br>
 * The command annotation used to designate methods as commands. All methods
 * should have a single CommandArgs argument
 *
 * @author minnymin3
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    RetentionPolicy.RUNTIME
)
annotation class Command(
    /**
     * The name of the command. If it is a sub command then its values would be
     * separated by periods. ie. a command that would be a subcommand of test
     * would be 'test.subcommandname'
     *
     * @return
     */
    val name: String,
    /**
     * Gets the required permission of the command
     *
     * @return
     */
    val permission: String = "",
    /**
     * The message sent to the player when they do not have permission to
     * execute it
     *
     * @return
     */
    val noPerm: String = "&cYou do not have permission to do this.",
    /**
     * A list of alternate names that the command is executed under. See
     * name() for details on how names work
     *
     * @return
     */
    val aliases: Array<String> = [],
    /**
     * The description that will appear in /help of the command
     *
     * @return
     */
    val description: String = "",
    /**
     * The usage that will appear in /help (commandname)
     *
     * @return
     */
    val usage: String = "",
    /**
     * Whether or not the command is available to players only
     *
     * @return
     */
    val inGameOnly: Boolean = false
)
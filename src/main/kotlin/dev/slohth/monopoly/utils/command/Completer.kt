package dev.slohth.monopoly.utils.command

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Command Framework - Completer <br></br>
 * The completer annotation used to designate methods as command completers. All
 * methods should have a single CommandArgs argument and return a String List
 * object
 *
 * @author minnymin3
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    RetentionPolicy.RUNTIME
)
annotation class Completer(
    /**
     * The command that this completer completes. If it is a sub command then
     * its values would be separated by periods. ie. a command that would be a
     * subcommand of test would be 'test.subcommandname'
     *
     * @return
     */
    val name: String,
    /**
     * A list of alternate names that the completer is executed under. See
     * name() for details on how names work
     *
     * @return
     */
    val aliases: Array<String> = []
)
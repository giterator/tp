package seedu.duke.commands;

import java.util.logging.Level;

/** Represents the command to print the help menu to the user. */
public class HelpCommand extends Command {

    private static final String HELP_MESSAGE = "Here are the list of commands:\n"
            + "1. cap MC_AND_GRADES  - Calculates the total cap for the semester\n"
            + "2. view MODULE_CODE - View the module details\n"
            + "3. bus - Check for a NUS bus route from stop to another\n"
            + "4. store_module MODULE_CODE - Add a module to your module list"
            + "5. delete_module MODULE_CODE - Delete a module from your module list"
            + "6. planner add DESCRIPTION/DATE/START_TIME/END_TIME - Add an event to your schedule\n"
            + "7. planner list DATE - Lists events on a certain date\n"
            + "8. help - View this menu again\n"
            + "9. bye - Exit Kolinux";

    @Override
    public CommandResult executeCommand() {
        logger.log(Level.INFO, "User needed help");
        return new CommandResult(HELP_MESSAGE);
    }
}
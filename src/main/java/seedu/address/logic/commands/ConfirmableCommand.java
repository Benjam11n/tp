package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

//Solution below inspired by:
//https://github.com/AY2425S2-CS2103T-W12-4/tp/blob/master/src/main/java/tassist/address/logic/commands/

/**
 * Represents a command that requires a confirmation step before execution.
 * Classes implementing this interface should define the specific logic for confirmation and execution.
 */
public interface ConfirmableCommand {

    public CommandResult executeConfirmed(Model model) throws CommandException;

    public CommandResult executeAborted();
}

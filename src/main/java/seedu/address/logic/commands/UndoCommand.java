package seedu.address.logic.commands;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandTracker;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * This class handles the "undoing" of commands entered by the CLI user.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undoes the most recent command entered by the user.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_UNDO_SUCCESS = "Undo successful! Reverted Command: ";

    public static final String MESSAGE_NO_UNDO_FAILURE = "Nothing to undo!";

    private final Logger logger = LogsCenter.getLogger(DeleteCommand.class);

    @Override
    public CommandResult execute(Model model) throws CommandException {
        CommandTracker tracker = CommandTracker.getInstance();
        if (!tracker.canUndo()) {
            logger.info("Cannot undo, empty undo stack");
            throw new CommandException(MESSAGE_NO_UNDO_FAILURE);
        }

        UndoableCommand lastCommand = (UndoableCommand) tracker.popUndo();
        if (lastCommand == null) {
            logger.info("Cannot undo, lastCommand is null");
            throw new CommandException(MESSAGE_NO_UNDO_FAILURE);
        }

        lastCommand.undo(model);
        String undoneCommand = lastCommand.getCommandString();
        return new CommandResult(MESSAGE_UNDO_SUCCESS + undoneCommand);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UndoCommand;
    }

    @Override
    public String toString() {
        return UndoCommand.class.getCanonicalName();
    }
}

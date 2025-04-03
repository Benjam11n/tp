package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_CONFIRMATION = "WARNING: This command CANNOT be undone. "
            + "The entries in the address book will be lost permanently. "
            + "\n\nAre you sure you want to clear the address book? (y/n)";

    public static final String ABORTION_SUCCESS = "Clear aborted";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_CONFIRMATION, this);

    }


    /**
     * Executes the confirmed version of the clear command by clearing the address book.
     *
     * @param model The model to be modified. Must not be null.
     * @return A {@code CommandResult} indicating the result of the operation with a success message.
     */
    public CommandResult executeConfirmed(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    public CommandResult executeAborted() {
        return new CommandResult(ABORTION_SUCCESS);
    }
}

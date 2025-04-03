package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */

public class DeleteCommand extends UndoableCommand implements ConfirmableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index number(s) used in the displayed person list.\n"
            + "Parameters: INDEX/INDICES (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_CONFIRM_DELETE = "Confirm Deleting Person: %1$s ? (y/n)";

    public static final String MESSAGE_ABORTED = "Aborted deletion!";

    private final Logger logger = LogsCenter.getLogger(DeleteCommand.class);

    private final List<Index> targetIndices;

    private List<Person> personsToDelete;

    private List<Integer> originalIndices;

    public DeleteCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        logger.info("Get confirmation to delete: " + targetIndices.stream()
                .map(Index::getOneBased).toList());
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        personsToDelete = targetIndices.stream()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toList();

        originalIndices = targetIndices.stream()
                .map(Index::getZeroBased)
                .toList();

        return new CommandResult(String.format(MESSAGE_CONFIRM_DELETE,
                personsToDelete.stream()
                        .map(person -> person.getName().toString())
                        .collect(Collectors.joining(", "))), this);
    }

    @Override
    public void undo(Model model) {
        requireNonNull(model);
        if (personsToDelete != null) {
            for (int i = 0; i < personsToDelete.size(); i++) {
                Person person = personsToDelete.get(i);
                int originalIndex = originalIndices.get(i);
                if (!model.hasPerson(person)) {
                    model.addPersonAt(person, originalIndex);
                }
            }
        }
    }

    @Override
    public void redo(Model model) {
        requireNonNull(model);
        if (personsToDelete != null) {
            for (Person person : personsToDelete) {
                model.deletePerson(person);
            }
        }
    }

    @Override
    public String getCommandString() {
        if (personsToDelete == null || personsToDelete.isEmpty()) {
            return COMMAND_WORD + " <unknown person(s)>";
        }

        String summary = personsToDelete.stream()
                .map(Person::toCommandSummary)
                .collect(Collectors.joining(", "));
        return COMMAND_WORD + " " + summary;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }

    /**
     * Executes the delete command after confirmation and removes the specified persons
     * from the model.
     */
    public CommandResult executeConfirmed(Model model) throws CommandException {
        logger.info("Execute Confirmed Deletion for: " + targetIndices.stream()
                .map(Index::getOneBased).toList());

        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index targetIndex : targetIndices) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        List<Person> personsToDelete = targetIndices.stream()
                .map(targetIndex -> lastShownList.get(targetIndex.getZeroBased()))
                .toList();

        assert !personsToDelete.isEmpty();

        for (Person personToDelete : personsToDelete) {
            model.deletePerson(personToDelete);
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                personsToDelete.stream()
                        .map(person -> person.getName().toString())
                        .collect(Collectors.joining(","))));
    }

    /**
     * Handles the process of aborting a delete operation and returns a feedback message to the user.
     */
    public CommandResult executeAborted() {
        logger.info("Execute Aborted Deletion");
        return new CommandResult(MESSAGE_ABORTED);
    }
}

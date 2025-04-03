package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());

    @Test
    public void execute_validIndexUnfilteredList_success() throws CommandException {
        List<Index> indicesToDelete = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        List<Person> personsToDelete = indicesToDelete.stream()
                .map(index -> model.getFilteredPersonList().get(index.getZeroBased()))
                .toList();

        DeleteCommand deleteCommand = new DeleteCommand(indicesToDelete);
        String actualMessage = deleteCommand.execute(model).getFeedbackToUser();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                personsToDelete.stream()
                        .map(person -> person.getName().toString())
                        .collect(Collectors.joining(", ")));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                model.getCommandHistory());
        personsToDelete.forEach(expectedModel::deletePerson);

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void executeConfirmed_validIndices_success() throws CommandException {
        List<Index> indicesToDelete = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        List<Person> personsToDelete = indicesToDelete.stream()
                .map(index -> model.getFilteredPersonList().get(index.getZeroBased()))
                .toList();

        DeleteCommand deleteCommand = new DeleteCommand(indicesToDelete);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personsToDelete.stream()
                        .map(person -> person.getName().toString())
                        .collect(Collectors.joining(",")));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        personsToDelete.forEach(expectedModel::deletePerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_requestConfirmation() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        List<Index> indicesToDelete = List.of(INDEX_FIRST_PERSON);
        List<Person> personsToDelete = indicesToDelete.stream()
                .map(index -> model.getFilteredPersonList().get(index.getZeroBased()))
                .toList();

        DeleteCommand deleteCommand = new DeleteCommand(indicesToDelete);
        String actualMessage = deleteCommand.execute(model).getFeedbackToUser();

        String expectedMessage = String.format(DeleteCommand.MESSAGE_CONFIRM_DELETE,
                personsToDelete.stream()
                        .map(person -> person.getName().toString())
                        .collect(Collectors.joining(",")));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        personsToDelete.forEach(expectedModel::deletePerson);
        showNoPerson(expectedModel);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Model emptyModel = new ModelManager();
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));

        assertCommandFailure(deleteCommand, emptyModel, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void undoRedo_validDeleteCommand_success() throws CommandException {
        // Initiate lists
        List<Index> indicesToDelete = List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        List<Person> personsToDelete = indicesToDelete.stream()
                .map(index -> model.getFilteredPersonList().get(index.getZeroBased()))
                .toList();

        DeleteCommand deleteCommand = new DeleteCommand(indicesToDelete);

        deleteCommand.execute(model);
        deleteCommand.executeConfirmed(model);

        Model modelAfterDelete = new ModelManager(model.getAddressBook(), new UserPrefs(), new CommandHistory());

        deleteCommand.undo(model);
        for (Person person : personsToDelete) {
            assertTrue(model.getFilteredPersonList().contains(person));
        }

        deleteCommand.redo(model);
        for (Person person : personsToDelete) {
            assertTrue(!model.getFilteredPersonList().contains(person));
        }

        assertEquals(modelAfterDelete, model);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        DeleteCommand deleteSecondCommand = new DeleteCommand(List.of(INDEX_SECOND_PERSON));
        DeleteCommand deleteMultipleCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertEquals(deleteFirstCommand, deleteFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, deleteFirstCommand);

        // null -> returns false
        assertNotEquals(null, deleteFirstCommand);

        // different indices -> returns false
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);

        // multiple vs single -> returns false
        assertNotEquals(deleteFirstCommand, deleteMultipleCommand);
    }

    @Test
    public void getCommandStringMethod_invalidPersons_returnsUnsuccessfulSummary() {
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        String expected = String.format("delete <unknown person(s)>");
        assertEquals(expected, deleteCommand.getCommandString());
    }

    @Test
    public void getCommandString_nonEmptyPersons_returnsSuccessfulSummary() throws CommandException {
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        deleteCommand.execute(model);
        String expected = String.format("delete Alice Pauline");
        assertEquals(expected, deleteCommand.getCommandString());
    }

    @Test
    public void toStringMethod() {
        List<Index> targetIndices = List.of(Index.fromOneBased(1), Index.fromOneBased(2));
        DeleteCommand deleteCommand = new DeleteCommand(targetIndices);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndices=" + targetIndices + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}

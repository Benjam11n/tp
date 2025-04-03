package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NICKNAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTES_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NICKNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandTracker;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.AddressBook;
import seedu.address.model.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.namepredicate.NameContainsKeywordsPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());
    @BeforeEach
    public void resetCommandTracker() {
        CommandTracker.getInstance().clear();
    }
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        ArrayList<Prefix> toRemoveFields = new ArrayList<>();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, toRemoveFields);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_allOptionalFieldsEmpty_success() {
        Person editedPerson = new PersonBuilder(true).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        ArrayList<Prefix> toRemoveFields = new ArrayList<>();
        toRemoveFields.add(PREFIX_BIRTHDAY);
        toRemoveFields.add(PREFIX_NICKNAME);
        toRemoveFields.add(PREFIX_RELATIONSHIP);
        toRemoveFields.add(PREFIX_NOTES);
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor, toRemoveFields);
        Person editedPersonExpected = new PersonBuilder().build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPersonExpected));
        System.out.println(expectedMessage);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPersonExpected);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        ArrayList<Prefix> toRemoveFields = new ArrayList<>();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor, toRemoveFields);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editNicknameUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withNickname(VALID_NICKNAME_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withNickname(VALID_NICKNAME_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor, new ArrayList<>());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editNotesUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withNotes(VALID_NOTES_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withNotes(VALID_NOTES_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor, new ArrayList<>());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor(), new ArrayList<>());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), new ArrayList<>());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new CommandHistory(model.getCommandHistory()));
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);
        List<String> keywords = List.of(personInFilteredList.getName().toString());
        expectedModel.updateFilteredPersonList(new NameContainsKeywordsPredicate(keywords));
        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor, new ArrayList<>());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build(), new ArrayList<>());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor, new ArrayList<>());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build(), new ArrayList<>());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validEditCommand_successfulUndoRedo() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());

        // Edits the first person's name
        Person originalPerson = model.getFilteredPersonList().get(0);
        Name newName = new Name("Edited Name");
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setName(newName);
        EditCommand editCommand = new EditCommand(Index.fromZeroBased(0), descriptor, new ArrayList<>());

        editCommand.execute(model);

        // Check the initial name change
        Person editedPerson = model.getFilteredPersonList().get(0);
        assertEquals(newName, editedPerson.getName());

        // Undo should bring back the original person
        UndoableCommand lastCommand = (UndoableCommand) CommandTracker.getInstance().popUndo();
        lastCommand.undo(model);

        Person undonePerson = model.getFilteredPersonList().get(0);
        assertEquals(originalPerson, undonePerson);

        // Checks redo functionality
        UndoableCommand redoCommand = (UndoableCommand) CommandTracker.getInstance().popRedo();
        redoCommand.redo(model);

        Person redonePerson = model.getFilteredPersonList().get(0);
        assertEquals(newName, redonePerson.getName());
    }

    @Test
    public void multipleEditCommands_undoOrderIsCorrect() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());

        Person firstPersonOriginal = model.getFilteredPersonList().get(0);
        Person secondPersonOriginal = model.getFilteredPersonList().get(1);

        EditCommand.EditPersonDescriptor descriptor1 = new EditCommand.EditPersonDescriptor();
        descriptor1.setName(new Name("Alice Edited"));
        EditCommand edit1 = new EditCommand(Index.fromZeroBased(0), descriptor1, new ArrayList<>());
        edit1.execute(model);

        EditCommand.EditPersonDescriptor descriptor2 = new EditCommand.EditPersonDescriptor();
        descriptor2.setPhone(Optional.of(new Phone("99999999")));
        EditCommand edit2 = new EditCommand(Index.fromZeroBased(1), descriptor2, new ArrayList<>());
        edit2.execute(model);

        // Undo edit2
        UndoableCommand cmd2 = (UndoableCommand) CommandTracker.getInstance().popUndo();
        cmd2.undo(model);

        // Verify second person is reverted
        assertEquals(secondPersonOriginal, model.getFilteredPersonList().get(1));

        // Undo edit1
        UndoableCommand cmd1 = (UndoableCommand) CommandTracker.getInstance().popUndo();
        cmd1.undo(model);

        // Verify first person is reverted
        assertEquals(firstPersonOriginal, model.getFilteredPersonList().get(0));
    }

    @Test
    public void undoMoreThanExecutedCommands_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());

        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setPhone(Optional.of(new Phone("88888888")));
        EditCommand editCommand = new EditCommand(Index.fromZeroBased(0), descriptor, new ArrayList<>());
        assertDoesNotThrow(() -> editCommand.execute(model));

        UndoCommand undoCommand = new UndoCommand();
        assertDoesNotThrow(() -> undoCommand.execute(model));

        UndoCommand secondUndo = new UndoCommand();
        assertThrows(CommandException.class, () -> secondUndo.execute(model));
    }


    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY, new ArrayList<>());

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor, new ArrayList<>());
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY, new ArrayList<>())));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB, new ArrayList<>())));
    }

    @Test
    public void getCommandString_returnsCorrectUndoMessage() throws Exception {
        // Arrange: Original and edited person
        Person original = new PersonBuilder()
                .withName("Alice Pauline")
                .withEmail("alice@example.com")
                .build();

        Person edited = new PersonBuilder(original)
                .withName("Bob Choo")
                .withEmail("bob@example.com")
                .build();

        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setName(edited.getName());
        descriptor.setEmail(edited.getEmail());

        EditCommand editCommand = new EditCommand(Index.fromZeroBased(0), descriptor, new ArrayList<>());

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new CommandHistory());
        model.setPerson(model.getFilteredPersonList().get(0), original); // overwrite with known person

        editCommand.execute(model);

        String expected = String.format("edit 1: %s -> %s", ALICE.getName(), BOB.getName());
        assertEquals(expected, editCommand.getCommandString());
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor, new ArrayList<>());
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }


}

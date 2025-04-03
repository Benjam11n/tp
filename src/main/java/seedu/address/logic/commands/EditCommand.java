package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NICKNAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.CommandTracker;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.ImagePath;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nickname;
import seedu.address.model.person.Notes;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_BIRTHDAY + "BIRTHDAY] "
            + "[" + PREFIX_RELATIONSHIP + "RELATIONSHIP] "
            + "[" + PREFIX_NICKNAME + "NICKNAME] "
            + "[" + PREFIX_NOTES + "NOTES] "
            + "[" + PREFIX_IMG + "IMAGE_PATH] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private ArrayList<Prefix> toRemoveFields;
    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor, ArrayList<Prefix> toRemoveFields) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
        this.toRemoveFields = toRemoveFields;
    }

    public ArrayList<Prefix> getToRemoveFields() {
        if (toRemoveFields == null) {
            throw new AssertionError("toRemoveField is null");
        }
        return toRemoveFields;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor, toRemoveFields);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        CommandTracker.getInstance().push(this); //push this EditCommand into the stack
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public void undo(Model model) {
        model.setPerson(editedPerson, personToEdit);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void redo(Model model) {
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public String getCommandString() {
        return String.format("%s %d: %s -> %s",
                COMMAND_WORD,
                index.getOneBased(),
                personToEdit.toCommandSummary(),
                editedPerson.toCommandSummary()
        );
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor,
                                             ArrayList<Prefix> toRemoveFields) {
        assert personToEdit != null;
        assert editPersonDescriptor != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());

        assert updatedName != null : "Name cannot be null after editing";

        Optional<Phone> updatedPhone = processOptionalField(
                editPersonDescriptor.getPhone(),
                personToEdit.getPhone(),
                toRemoveFields.contains(PREFIX_PHONE));

        Optional<Email> updatedEmail = processOptionalField(
                editPersonDescriptor.getEmail(),
                personToEdit.getEmail(),
                toRemoveFields.contains(PREFIX_EMAIL));

        Optional<Address> updatedAddress = processOptionalField(
                editPersonDescriptor.getAddress(),
                personToEdit.getAddress(),
                toRemoveFields.contains(PREFIX_ADDRESS));

        Optional<Birthday> updatedBirthday = processOptionalField(
                editPersonDescriptor.getBirthday(),
                personToEdit.getBirthday(),
                toRemoveFields.contains(PREFIX_BIRTHDAY));

        Optional<Relationship> updatedRelationship = processOptionalField(
                editPersonDescriptor.getRelationship(),
                personToEdit.getRelationship(),
                toRemoveFields.contains(PREFIX_RELATIONSHIP));

        Optional<Nickname> updatedNickname = processOptionalField(
                editPersonDescriptor.getNickname(),
                personToEdit.getNickname(),
                toRemoveFields.contains(PREFIX_NICKNAME));

        Optional<Notes> updatedNotes = processOptionalField(
                editPersonDescriptor.getNotes(),
                personToEdit.getNotes(),
                toRemoveFields.contains(PREFIX_NOTES));

        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        Optional<ImagePath> updatedImagePath = editPersonDescriptor.getImagePath().isPresent()
                ? editPersonDescriptor.getImagePath()
                : Optional.of(personToEdit.getImagePath());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedBirthday,
                updatedRelationship, updatedNickname, updatedNotes, updatedImagePath, updatedTags);
    }

    private static <T> Optional<T> processOptionalField(
            Optional<T> editDescriptorValue,
            Optional<T> existingValue,
            boolean shouldRemove) {

        if (editDescriptorValue.isPresent()) {
            return editDescriptorValue;
        }

        if (shouldRemove) {
            return Optional.empty();
        }

        return existingValue;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Optional<Phone> phone;
        private Optional<Email> email;
        private Optional<Address> address;
        private Optional<Birthday> birthday;
        private Optional<Relationship> relationship;
        private Optional<Nickname> nickname;
        private Optional<Notes> notes;
        private Optional<ImagePath> imagePath;
        private Set<Tag> tags;

        public EditPersonDescriptor() {
            this.imagePath = Optional.empty();
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setBirthday(toCopy.birthday);
            setRelationship(toCopy.relationship);
            setNickname(toCopy.nickname);
            setNotes(toCopy.notes);
            setImagePath(toCopy.imagePath);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, birthday, relationship,
                    nickname, notes, tags)
                    || (imagePath != null && imagePath.isPresent());
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Optional<Phone> phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone).flatMap(p -> p);
        }

        public void setEmail(Optional<Email> email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email).flatMap(e -> e);
        }
        public void setAddress(Optional<Address> address) {
            this.address = address;
        }
        public Optional<Address> getAddress() {
            return Optional.ofNullable(address).flatMap(a -> a);
        }
        public void setBirthday(Optional<Birthday> birthday) {
            this.birthday = birthday;
        }
        public Optional<Birthday> getBirthday() {
            return Optional.ofNullable(birthday).flatMap(b -> b);
        }
        public void setRelationship(Optional<Relationship> relationship) {
            this.relationship = relationship;
        }
        public Optional<Relationship> getRelationship() {
            return Optional.ofNullable(relationship).flatMap(b -> b);
        }
        public void setNickname(Optional<Nickname> nickname) {
            this.nickname = nickname;
        }
        public Optional<Nickname> getNickname() {
            return Optional.ofNullable(nickname).flatMap(b -> b);
        }
        public void setNotes(Optional<Notes> notes) {
            this.notes = notes;
        }
        public Optional<Notes> getNotes() {
            return Optional.ofNullable(notes).flatMap(b -> b);
        }
        public void setImagePath(Optional<ImagePath> imagePath) {
            this.imagePath = imagePath != null ? imagePath : Optional.empty();
        }
        public Optional<ImagePath> getImagePath() {
            return imagePath;
        }
        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(birthday, otherEditPersonDescriptor.birthday)
                    && Objects.equals(relationship, otherEditPersonDescriptor.relationship)
                    && Objects.equals(nickname, otherEditPersonDescriptor.nickname)
                    && Objects.equals(notes, otherEditPersonDescriptor.notes)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(imagePath, otherEditPersonDescriptor.imagePath);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("birthday", birthday)
                    .add("relationship", relationship)
                    .add("nickname", nickname)
                    .add("notes", notes)
                    .add("tags", tags)
                    .add("imagePath", imagePath)
                    .toString();
        }
    }
}

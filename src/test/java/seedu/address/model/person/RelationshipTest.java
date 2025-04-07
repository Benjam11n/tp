package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RelationshipTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Relationship(null));
    }

    @Test
    public void isValidRelationship() {
        // null relationship
        assertThrows(NullPointerException.class, () -> Relationship.isValidRelationship(null));

        // invalid relationship
        assertThrows(IllegalArgumentException.class, () -> Relationship.isValidRelationship("^"));
        assertThrows(IllegalArgumentException.class, () -> Relationship.isValidRelationship("mother*"));

        // valid relationship
        assertTrue(Relationship.isValidRelationship("mother")); // alphabets only
        assertTrue(Relationship.isValidRelationship("12345")); // numbers only
        assertTrue(Relationship.isValidRelationship("father 2nd")); // alphanumeric characters
        assertTrue(Relationship.isValidRelationship("Grand Father")); // with capital letters
        assertTrue(Relationship.isValidRelationship("Grand-Father")); // with hyphen
        assertTrue(Relationship.isValidRelationship("Mother's Friend")); // with apostrophe
        assertTrue(Relationship.isValidRelationship("Great Grand Mother")); // long relationships
    }

    @Test
    public void equals() {
        Relationship relationship = new Relationship("Valid Relationship");

        // same values -> returns true
        assertTrue(relationship.equals(new Relationship("Valid Relationship")));

        // same object -> returns true
        assertTrue(relationship.equals(relationship));

        // null -> returns false
        assertFalse(relationship.equals(null));

        // different types -> returns false
        assertFalse(relationship.equals(5.0f));

        // different values -> returns false
        assertFalse(relationship.equals(new Relationship("Other Valid Relationship")));
    }
}

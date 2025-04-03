package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.namepredicate.NameSimilarPredicate;

public class NameSimilarPredicateTest {

    @Test
    public void equals_sameObject_returnsTrue() {
        NameSimilarPredicate predicate = new NameSimilarPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate.equals(predicate));
    }

    @Test
    public void equals_equalKeywords_returnsTrue() {
        NameSimilarPredicate predicate1 = new NameSimilarPredicate(List.of("Alice", "Bob"));
        NameSimilarPredicate predicate2 = new NameSimilarPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate1.equals(predicate2));
    }

    @Test
    public void equals_differentKeywords_returnsFalse() {
        NameSimilarPredicate predicate1 = new NameSimilarPredicate(List.of("Alice"));
        NameSimilarPredicate predicate2 = new NameSimilarPredicate(List.of("Bob"));
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void equals_null_returnsFalse() {
        NameSimilarPredicate predicate = new NameSimilarPredicate(List.of("Alice"));
        assertFalse(predicate.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        NameSimilarPredicate predicate = new NameSimilarPredicate(List.of("Alice"));
        assertFalse(predicate.equals("NotAPredicate"));
    }
}


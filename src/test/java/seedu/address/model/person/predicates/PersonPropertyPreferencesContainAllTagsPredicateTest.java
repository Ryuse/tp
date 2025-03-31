package seedu.address.model.person.predicates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PropertyPreference;
import seedu.address.model.price.Price;
import seedu.address.model.price.PriceRange;
import seedu.address.model.tag.Tag;

public class PersonPropertyPreferencesContainAllTagsPredicateTest {

    private final Person dummyPerson = new Person(
            new Name("Dummy"),
            new Phone("10000000"),
            new Email("dummy@example.com"),
            List.of(),
            List.of()
    );

    private final Person samplePerson = new Person(
            new Name("John Doe"),
            new Phone("98765432"),
            new Email("john@example.com"),
            List.of(new PropertyPreference(
                    new PriceRange(new Price("500000"), new Price("700000")),
                    Set.of(new Tag("Pool", List.of(), List.of()), new Tag("Pet-Friendly", List.of(), List.of())),
                    dummyPerson
            )),
            List.of()
    );

    @Test
    public void test_containsAllTags_true() {
        // Should return true as preference contains both pool and pet-friendly
        PersonPropertyPreferencesContainAllTagsPredicate predicate =
                new PersonPropertyPreferencesContainAllTagsPredicate(Set.of("pool", "pet-friendly"));
        assertTrue(predicate.test(samplePerson));
    }

    @Test
    public void test_partialTagMatch_false() {
        // Should return false since "garden" is not present
        PersonPropertyPreferencesContainAllTagsPredicate predicateMultiple =
                new PersonPropertyPreferencesContainAllTagsPredicate(Set.of("pool", "garden"));
        assertFalse(predicateMultiple.test(samplePerson));
    }

    @Test
    public void test_tagCaseInsensitive_true() {
        PersonPropertyPreferencesContainAllTagsPredicate predicate =
                new PersonPropertyPreferencesContainAllTagsPredicate(Set.of("POOL", "pet-friendly"));
        assertTrue(predicate.test(samplePerson));
    }

    @Test
    public void test_noMatchingTags_false() {
        PersonPropertyPreferencesContainAllTagsPredicate predicate =
                new PersonPropertyPreferencesContainAllTagsPredicate(Set.of("garden", "quiet"));
        assertFalse(predicate.test(samplePerson));
    }

    @Test
    public void test_emptyTagSet_false() {
        PersonPropertyPreferencesContainAllTagsPredicate predicate =
                new PersonPropertyPreferencesContainAllTagsPredicate(Set.of());
        assertFalse(predicate.test(samplePerson));
    }
}

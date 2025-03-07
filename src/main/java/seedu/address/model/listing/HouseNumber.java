package seedu.address.model.listing;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Listing's house number in the real estate system.
 * Guarantees: immutable; is valid as declared in {@link #isValidHouseNumber(String)}
 */
public class HouseNumber {

    public static final String MESSAGE_CONSTRAINTS =
            "House number must be at most 3 characters long, "
                    + "consisting of only letters and numbers. The last character cannot be 'I' or 'O'.";

    /** Validation regex for house numbers */
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9]{1,2}[a-zA-HJ-KL-NP-Z0-9]$";

    public final String houseNumber;

    /**
     * Constructs an {@code HouseNumber}.
     *
     * @param houseNumber A valid house number.
     */
    public HouseNumber(String houseNumber) {
        requireNonNull(houseNumber);
        checkArgument(isValidHouseNumber(houseNumber), MESSAGE_CONSTRAINTS);
        this.houseNumber = houseNumber;
    }

    /**
     * Returns true if a given string is a valid unit number.
     */
    public static boolean isValidHouseNumber(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return houseNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HouseNumber)) {
            return false;
        }

        HouseNumber otherHouseNumber = (HouseNumber) other;
        return houseNumber.equals(otherHouseNumber.houseNumber);
    }

    @Override
    public int hashCode() {
        return houseNumber.hashCode();
    }

}
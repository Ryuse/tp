package seedu.address.model.search.comparators;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Comparator;

import seedu.address.model.listing.Listing;

/**
 * Compares two {@code Listing}s based on default criteria (postal code, unit number, and house number)
 * 1. Postal code (primary)
 * 2. Unit number (secondary, with nulls first)
 * 3. House number (tertiary)
 * If two {@code Listing}s have the same postal code, the {@code Listing} with a houseNumber is ordered before the
 * {@code Listing} with a unit number.
 */
public class DefaultListingComparator implements Comparator<Listing> {
    private static final String NULL_UNIT_NUMBER_PLACEHOLDER = "";

    @Override
    public int compare(Listing listing1, Listing listing2) {
        requireAllNonNull(listing1, listing2);

        // First compare by postal code
        int postalCodeCompare = listing1.getPostalCode().postalCode
                .compareTo(listing2.getPostalCode().postalCode);

        if (postalCodeCompare != 0) {
            return postalCodeCompare;
        }

        // Compare by unit number (with nulls first)
        String unitNumber1 = listing1.getUnitNumber() != null
                ? listing1.getUnitNumber().unitNumber
                : NULL_UNIT_NUMBER_PLACEHOLDER;

        String unitNumber2 = listing2.getUnitNumber() != null
                ? listing2.getUnitNumber().unitNumber
                : NULL_UNIT_NUMBER_PLACEHOLDER;

        int unitNumberCompare = unitNumber1.compareTo(unitNumber2);
        if (unitNumberCompare != 0) {
            return unitNumberCompare;
        }

        // Compare by house number
        return listing1.getHouseNumber().houseNumber
                .compareTo(listing2.getHouseNumber().houseNumber);
    }
}

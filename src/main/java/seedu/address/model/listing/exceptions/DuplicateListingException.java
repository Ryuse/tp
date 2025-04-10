package seedu.address.model.listing.exceptions;

/**
 * Signals that the operation will result in duplicate {@code Listing}s, which are considered duplicates if they have
 * the same identity.
 */
public class DuplicateListingException extends RuntimeException {
    public DuplicateListingException() {
        super("Operation would result in duplicate listings");
    }
}

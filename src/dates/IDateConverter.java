package dates;



/**
 * Converts date strings into NewDate objects
 */
public interface IDateConverter {
    public DateRange convert(String originalDate);
}

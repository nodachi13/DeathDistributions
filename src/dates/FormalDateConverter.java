package dates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formal dates from FamilySearch come in the following format:
 *
 * [Approximate flag] [BC indicator] [Year] - [Month] - [Day]
 * They look like: A+1900-1-22
 *  That example means: "About January 22, 1900"
 *
 * The approximate flag is only set on uncertain dates (an 'A' as the first part of the string)
 * The BC indicator is usually there (+ for AD dates, - for BC), but not guaranteed
 * Month and Day information are optional. Their order, however, is defined.
 *
 * This problem can be solved with regular expressions.
 */
public class FormalDateConverter implements IDateConverter {
    private static final Pattern PATTERN = Pattern.compile("(/)?(A)?([+\\-])?([0-9]+)(-[0-9]+)?(-[0-9]+)?(/((A)?([+\\-])?([0-9]+)(-[0-9]+)?(-[0-9]+)?)?)?");
    private static final int NUM_GROUPS = 14;   // This is the number of groups in the regex, since groupCount() is giving the wrong value
    private static final int APPROXIMATE_INDEX = 2;
    private static final int BC_INDEX = 3;
    private static final int YEAR_INDEX = 4;
    private static final int MONTH_INDEX = 5;
    private static final int DAY_INDEX = 6;
    private static final int SECOND_DATE_INDEX = 8;
    private static final int SECOND_APPROXIMATE_INDEX = 9;
    private static final int SECOND_BC_INDEX = 10;
    private static final int SECOND_YEAR_INDEX = 11;
    private static final int SECOND_MONTH_INDEX = 12;
    private static final int SECOND_DAY_INDEX = 13;
    private static final int APPROXIMATE_BUFFER = 730;  // This is 2 years, in days.

    @Override
    public DateRange convert(String originalDate) {
        Matcher matcher = PATTERN.matcher(originalDate);
        if(!matcher.matches()){
            return null;
        }
        else{
            boolean approximate = matcher.group(APPROXIMATE_INDEX) != null;
            boolean bc = matcher.group(BC_INDEX) != null && matcher.group(BC_INDEX).equals("-");
            String yearString = matcher.group(YEAR_INDEX);
            String monthString = matcher.group(MONTH_INDEX) == null ? null : matcher.group(MONTH_INDEX).substring(1);
            String dayString = matcher.group(DAY_INDEX) == null ? null : matcher.group(DAY_INDEX).substring(1);

            DateRange first = handleDate(yearString, monthString, dayString, approximate, bc);

            if(matcher.group(SECOND_DATE_INDEX) != null){
                boolean secondApproximate = matcher.group(SECOND_APPROXIMATE_INDEX) != null;
                boolean secondBc = matcher.group(SECOND_BC_INDEX) != null && matcher.group(SECOND_BC_INDEX).equals("-");
                String secondYearString = matcher.group(SECOND_YEAR_INDEX);
                String secondMonthString = matcher.group(SECOND_MONTH_INDEX) == null ? null : matcher.group(SECOND_MONTH_INDEX).substring(1);
                String secondDayString = matcher.group(SECOND_DAY_INDEX) == null ? null : matcher.group(SECOND_DAY_INDEX).substring(1);
                DateRange second = handleDate(secondYearString, secondMonthString, secondDayString, secondApproximate, secondBc);
                return new DateRange(first.getFirstDay(), second.getLastDay());
            } else return first;
        }
    }

    private DateRange handleDate(String yearString, String monthString, String dayString, boolean approximate, boolean bc){
        if(yearString == null) return null;     // Without the year, there is nothing to do
        int year = Integer.parseInt(yearString);
        if(monthString == null) return noMonth(year, approximate, bc);
        int month = Integer.parseInt(monthString);
        if(dayString == null) return noDay(year, month, approximate, bc);
        int day = Integer.parseInt(dayString);
        return fullInfo(year, month, day, approximate, bc);
    }

    private DateRange noMonth(int year, boolean approximate, boolean bc){
        DateRange first = fullInfo(year, 1,1, approximate, bc);
        DateRange last = fullInfo(year, 12, 31, approximate, bc);
        return new DateRange(first.getFirstDay(), last.getLastDay());
    }

    private DateRange noDay(int year, int month, boolean approximate, boolean bc){
        DateRange first = fullInfo(year, month, 1, approximate, bc);
        DateRange last = fullInfo(year, month, numDays(month), approximate, bc);
        return new DateRange(first.getFirstDay(), last.getLastDay());
    }

    private DateRange fullInfo(int year, int month, int day, boolean approximate, boolean bc){
        if(bc) year *= -1;
        JulianDayConverter converter = new JulianDayConverter();
        int julianDay = converter.julianDay(year, month, day);
        if(approximate) return new DateRange(julianDay - APPROXIMATE_BUFFER, julianDay + APPROXIMATE_BUFFER);
        else return new DateRange(julianDay);
    }

    public int numDays(int month){
        switch(month){
            case 1: return 31;
            case 2: return 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            default: return 28;
        }
    }
}

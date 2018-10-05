package dates;

/**
 * Transforms Gregorian dates to Julian Days, and vice versa.
 */
public class JulianDayConverter {

    /**
     * Uses the standard formula for calculating julian days to convert a gregorian date into a julian day.
     * @param year Year of the date
     * @param month Month of the date, should be between 1 and 12
     * @param day Day of the date, should be between 1 and 31
     * @return The Julian Day representation of the date
     */
    public int julianDay(int year, int month, int day){
        return (1461 * (year + 4800 + (month - 14)/12))/4 +(367 * (month - 2 - 12 * ((month - 14)/12)))/12 - (3 * ((year + 4900 + (month - 14)/12)/100))/4 + day - 32075;
    }

    /**
     * Uses a standard formula for converting julian days to a Gregorian date.
     * @param julianDay The Julian Day
     * @return A string representation of the gregorian date.
     */
    public String gregorianDate(int julianDay){
        int f = julianDay + 1401 + (((4 * julianDay + 274277) / 146097) * 3) / 4 - 38;
        int e = 4 * f + 3;
        int g = e % 1461 / 4;
        int h = 5 * g + 2;
        int day = (h % 153) / 5 + 1;
        int month = ((h / 153 + 2) % 12) + 1;
        int year = (e / 1461) - 4716 + (14 - month) / 12;

        return month(month) + " " + day + ", " + year;
    }

    /**
     * Gets the English name of a month by its index.
     * @param month The index of the month. Should be between 1 and 12.
     * @return The English name of the month, or null if an invalid number is input.
     */
    public String month(int month){
        switch(month){
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return null;
        }
    }
}

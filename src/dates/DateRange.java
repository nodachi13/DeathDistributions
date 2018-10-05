package dates;

import java.util.Arrays;

/**
 * Represents a range of julian days.
 */
public class DateRange {
    private int firstDay;   // The first day in the range
    private int lastDay;    // The last day in the range

    /**
     * Constructor for when there is more than one day in the range. The last day must
     * be later than the first day.
     * @param firstDay The first day in the range
     * @param lastDay The last day in the range
     */
    public DateRange(int firstDay, int lastDay){
        assert firstDay <= lastDay;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    /**
     * Constructor for when there is only one day in the range.
     * @param day The julian day in the range.
     */
    public DateRange(int day){
        this.firstDay = day;
        this.lastDay = day;
    }

    /**
     * Gets the first day in the range.
     * @return Obvious.
     */
    public int getFirstDay() {
        return firstDay;
    }

    /**
     * Gets the last day in the range.
     * @return Obvious.
     */
    public int getLastDay() {
        return lastDay;
    }

    /**
     * Returns the number of days in the range.
     * @return Obvious.
     */
    public int size(){
        return lastDay - firstDay + 1;
    }

    /**
     * Returns the probability that two dates could refer to the same event. Technically,
     * this is the percentage of the smaller date range that coincides with the larger one.
     * @param other The other date range.
     * @return The percentage of the smaller date range that coincides with the larger one.
     */
    public double sameness(DateRange other){
        // Determine the order of the ranges
        DateRange first = this.firstDay < other.firstDay ? this : other;
        DateRange second = first.equals(this) ? other : this;

        // Determine size of overlap
        int overlap = overlap(first, second);

        // Get overlap as percentage of smaller range
        int size = this.size() < other.size() ? this.size() : other.size();
        return (double)overlap / (double)size;
    }

    /**
     * Determines the amount of overlap between two ranges
     * @param first The range with the lower first day
     * @param second The range with the higher first day
     * @return The number of days contained in both ranges
     */
    private int overlap(DateRange first, DateRange second){
        // Put all points into an array
        RangePoint[] points = new RangePoint[]{
                new RangePoint(first.firstDay, RangePoint.FIRST),
                new RangePoint(first.lastDay, RangePoint.LAST),
                new RangePoint(second.firstDay, RangePoint.FIRST),
                new RangePoint(second.lastDay, RangePoint.LAST)
        };

        // Sort points
        Arrays.sort(points);

        // Determine overlap
        if(points[1].type.equals(RangePoint.LAST)) return 0;    // There's no overlap
        else return points[2].day - points[1].day + 1;
    }

    @Override
    public String toString(){
        if(firstDay == lastDay) return "[ " + firstDay + " ]";
        else return "[ " + firstDay + " - " + lastDay + " ]";
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof DateRange)) return false;
        DateRange other = (DateRange)o;
        return this.firstDay == other.firstDay && this.lastDay == other.lastDay;
    }

    /**
     * This class is used as a convenience class to help with figuring out the size of overlap
     */
    private class RangePoint implements Comparable<RangePoint> {
        private static final String FIRST = "FIRST";
        private static final String LAST = "LAST";

        private int day;        // Corresponds to a day in a DateRange
        private String type;    // Either FIRST or LAST

        public RangePoint(int day, String type){
            this.day = day;
            this.type = type;
        }

        /**
         * The point with the lower day comes first. If there's a tie, the one that is a FIRST point comes first.
         * @param rangePoint The other RangePoint
         * @return -1 if this point comes first, 1 if the other comes first, or 0 if they're the same
         */
        @Override
        public int compareTo(RangePoint rangePoint) {
            if(this.day == rangePoint.day){
                if(this.type.equals(FIRST)) return -1;
                else if(rangePoint.type.equals(FIRST)) return 1;
                else return 0;
            }
            return this.day - rangePoint.day;
        }
    }
}

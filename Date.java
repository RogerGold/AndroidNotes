public class Date {

    //DAYS[1]=31
    private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private final int month;   // month (between 1 and 12)
    private final int day;     // day   (between 1 and DAYS[month]
    private final int year;    // year

    // do bounds-checking to ensure object represents a valid date
    public Date(int m, int d, int y) {
        if (!isValid(m, d, y)) throw new RuntimeException("Invalid date");
        month = m;
        day   = d;
        year  = y;
    }

    // is the given date valid?
    private static boolean isValid(int m, int d, int y) {
        if (m < 1 || m > 12)      return false;
        if (d < 1 || d > DAYS[m]) return false;
        if (m == 2 && d == 29 && !isLeapYear(y)) return false;
        return true;
    }

    // is y a leap year?
    private static boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        return (y % 4 == 0);
    }

    // return the next Date
    public Date next() {
        if (isValid(month, day + 1, year))    return new Date(month, day + 1, year);
        else if (isValid(month + 1, 1, year)) return new Date(month + 1, 1, year);
        else                                  return new Date(1, 1, year + 1);
    }


    // is this Date after b?
    public boolean isAfter(Date b) {
        return compareTo(b) > 0;
    }

    // is this Date a before b?
    public boolean isBefore(Date b) {
        return compareTo(b) < 0;
    }

    // comparison function between two dates
    public int compareTo(Date b) {
        if  (year  != b.year)  return year  - b.year;
        if  (month != b.month) return month - b.month;
        return day - b.day;
    }

    // return a string representation of this date
    public String toString() {
        return month + "/" + day + "/" + year;
    }



    // sample client for testing
    public static void main(String[] args) {
        Date today = new Date(2, 25, 2004);
        StdOut.println(today);
        for (int i = 0; i < 10; i++) {
            today = today.next();
            StdOut.println(today);
        }

        StdOut.println(today.isAfter(today.next()));
        StdOut.println(today.isAfter(today));
        StdOut.println(today.next().isAfter(today));
    }

}

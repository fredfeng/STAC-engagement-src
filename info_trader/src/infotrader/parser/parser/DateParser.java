/*
 * Decompiled with CFR 0_121.
 */
package infotrader.parser.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {
    private static final String FORMAT_RANGE_PERIOD_PREFIX = "(FROM|BEF|BEF\\.|BET|BET\\.|BTW|BTW\\.|AFT|AFT\\.|TO|BETWEEN) ";
    private static final String FORMAT_DATE_MISC = "[A-Za-z0-9. ]*";
    private static final String FORMAT_YEAR = "\\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?";
    private static final String FORMAT_DAY = "(0?[1-9]|[12]\\d|3[01])";
    private static final String FORMAT_MONTH_GREGORIAN_JULIAN = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)";
    private static final String FORMAT_CASE_INSENSITIVE = "(?i)";
    private static final Pattern PATTERN_SINGLE_DATE_FULL_GREGORIAN_JULIAN = Pattern.compile("(?i)(0?[1-9]|[12]\\d|3[01]) (JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC) \\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?");
    private static final Pattern PATTERN_SINGLE_DATE_MONTH_YEAR_GREGORIAN_JULIAN = Pattern.compile("(?i)(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC) \\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?");
    private static final Pattern PATTERN_SINGLE_DATE_YEAR_ONLY = Pattern.compile("(?i)\\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?");
    private static final Pattern PATTERN_ENDS_IN_DOUBLE_ENTRY_YEAR = Pattern.compile("(?i)[A-Za-z0-9. ]*\\d{4}\\/\\d{2}$");
    static final Pattern PATTERN_TWO_DATES = Pattern.compile("(?i)(FROM|BEF|BEF\\.|BET|BET\\.|BTW|BTW\\.|AFT|AFT\\.|TO|BETWEEN) [A-Za-z0-9. ]*\\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)? (AND|TO) [A-Za-z0-9. ]*\\d{1,4}(\\/\\d{2})? ?(BC|B.C.|BCE)?");
    private static final String FORMAT_MONTH_HEBREW = "(TSH|CSH|KSL|TVT|SHV|ADR|ADS|NSN|IYR|SVN|TMZ|AAV|ELL)";
    private static final String FORMAT_MONTH_FRENCH_REPUBLICAN = "(VEND|BRUM|FRIM|NIVO|PLUV|VENT|GERM|FLOR|PRAI|MESS|THER|FRUC|COMP)";
    private static final Pattern PATTERN_SINGLE_HEBREW_DATE = Pattern.compile("(?i)(0?[1-9]|[12]\\d|3[01])? ?(TSH|CSH|KSL|TVT|SHV|ADR|ADS|NSN|IYR|SVN|TMZ|AAV|ELL)? ?\\d{4}");
    private static final Pattern PATTERN_SINGLE_FRENCH_REPUBLICAN_DATE = Pattern.compile("(?i)(0?[1-9]|[12]\\d|3[01])? ?(VEND|BRUM|FRIM|NIVO|PLUV|VENT|GERM|FLOR|PRAI|MESS|THER|FRUC|COMP)? ?\\d{1,4}");

    public Date parse(String dateString) {
        return this.parse(dateString, ImpreciseDatePreference.PRECISE);
    }

    public Date parse(String dateString, ImpreciseDatePreference pref) {
        String ds = dateString.toUpperCase(Locale.US);
        return null;
    }

    String formatBC(String dateString) {
        String d = dateString;
        if (d.endsWith("BC") || d.endsWith("BCE") || d.endsWith("B.C.") || d.endsWith("B.C.E.")) {
            String ds = d.substring(0, d.lastIndexOf(66)).trim();
            String yyyy = null;
            if (ds.lastIndexOf(32) > -1) {
                yyyy = ds.substring(ds.lastIndexOf(32)).trim();
                int i = Integer.parseInt(yyyy);
                int bc = 1 - i;
                String ddMMM = ds.substring(0, ds.lastIndexOf(32));
                d = ddMMM + " " + bc;
            } else {
                yyyy = ds.trim();
                int i = Integer.parseInt(yyyy);
                d = Integer.toString(1 - i);
            }
        }
        return d;
    }

    Date parseFrenchRepublicanSingleDate(String frenchRepublicanDateString, ImpreciseDatePreference pref) {
        String frds = this.removeApproximations(frenchRepublicanDateString.toUpperCase(Locale.US));
        frds = this.removeOpenEndedRangesAndPeriods(frds);
        return null;
    }

    Date parseHebrewSingleDate(String hebrewDateString, ImpreciseDatePreference pref) {
        String hds = this.removeApproximations(hebrewDateString.toUpperCase(Locale.US));
        if (!PATTERN_SINGLE_HEBREW_DATE.matcher(hds = this.removeOpenEndedRangesAndPeriods(hds)).matches()) {
            return null;
        }
        String[] datePieces = hds.split(" ");
        if (datePieces == null) {
            return null;
        }
        return null;
    }

    String removeApproximations(String dateString) {
        String ds = this.removePrefixes(dateString, "ABT", "ABOUT", "APPX", "APPROX", "CAL", "CALC", "EST");
        if (ds.startsWith("INT ") && ds.indexOf(40) > 6) {
            return ds.substring(4, ds.indexOf(40)).trim();
        }
        if (ds.startsWith("INT. ") && ds.indexOf(40) > 7) {
            return ds.substring(4, ds.indexOf(40)).trim();
        }
        return ds;
    }

    /* varargs */ String removePrefixes(String dateString, String ... prefixes) {
        for (String pfx : prefixes) {
            if (dateString.startsWith(pfx + " ") && dateString.length() > pfx.length() + 1) {
                return dateString.substring(pfx.length() + 1).trim();
            }
            if (!dateString.startsWith(pfx + ". ") || dateString.length() <= pfx.length() + 2) continue;
            return dateString.substring(pfx.length() + 2).trim();
        }
        return dateString;
    }

    String resolveEnglishCalendarSwitch(String dateString) {
        if (!PATTERN_ENDS_IN_DOUBLE_ENTRY_YEAR.matcher(dateString).matches()) {
            return dateString;
        }
        int l = dateString.length();
        String oldYYYY = dateString.substring(l - 7, l - 3);
        int yyyy = Integer.parseInt(oldYYYY);
        if (yyyy > 1752 || yyyy < 1582) {
            return dateString;
        }
        String newYY = dateString.substring(l - 2);
        int yy = Integer.parseInt(newYY);
        if (yy == 0 && yyyy % 100 == 99) {
            ++yyyy;
        }
        String upToYYYY = dateString.substring(0, l - 7);
        StringBuilder ds = new StringBuilder(upToYYYY);
        ds.append(yyyy / 100);
        ds.append(newYY);
        return ds.toString();
    }

    String[] splitTwoDateString(String dateString, String splitOn) {
        if (dateString.contains(splitOn)) {
            String[] dateStrings = new String[]{this.removePrefixes(dateString.substring(0, dateString.indexOf(splitOn)).trim(), "BETWEEN", "BET", "BTW", "FROM"), dateString.substring(dateString.indexOf(splitOn) + splitOn.length()).trim()};
            return dateStrings;
        }
        return new String[0];
    }

    private Date getDateWithFormatString(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(dateString);
        }
        catch (ParseException ignored) {
            return null;
        }
    }

    private Date getPreferredDateFromFrenchRepublicanRangeOrPeriod(String frenchRepublicanDateString, ImpreciseDatePreference pref) {
        String[] dateStrings = this.splitTwoDateString(frenchRepublicanDateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = this.splitTwoDateString(frenchRepublicanDateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }
        switch (pref) {
            case FAVOR_EARLIEST: {
                return this.parseFrenchRepublicanSingleDate(dateStrings[0], pref);
            }
            case FAVOR_LATEST: {
                return this.parseFrenchRepublicanSingleDate(dateStrings[1], pref);
            }
            case FAVOR_MIDPOINT: {
                Date d1 = this.parseFrenchRepublicanSingleDate(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = this.parseFrenchRepublicanSingleDate(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(6, (int)daysBetween / 2);
                return c.getTime();
            }
            case PRECISE: {
                return this.parseFrenchRepublicanSingleDate(dateStrings[0], pref);
            }
        }
        throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + (Object)((Object)pref));
    }

    private Date getPreferredDateFromHebrewRangeOrPeriod(String hebrewDateString, ImpreciseDatePreference pref) {
        String[] dateStrings = this.splitTwoDateString(hebrewDateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = this.splitTwoDateString(hebrewDateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }
        switch (pref) {
            case FAVOR_EARLIEST: {
                return this.parseHebrewSingleDate(dateStrings[0], pref);
            }
            case FAVOR_LATEST: {
                return this.parseHebrewSingleDate(dateStrings[1], pref);
            }
            case FAVOR_MIDPOINT: {
                Date d1 = this.parseHebrewSingleDate(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = this.parseHebrewSingleDate(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(6, (int)daysBetween / 2);
                return c.getTime();
            }
            case PRECISE: {
                return this.parseHebrewSingleDate(dateStrings[0], pref);
            }
        }
        throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + (Object)((Object)pref));
    }

    private Date getPreferredDateFromRangeOrPeriod(String dateString, ImpreciseDatePreference pref) {
        String[] dateStrings = this.splitTwoDateString(dateString, " AND ");
        if (dateStrings.length == 0) {
            dateStrings = this.splitTwoDateString(dateString, " TO ");
        }
        if (dateStrings.length == 0) {
            return null;
        }
        switch (pref) {
            case FAVOR_EARLIEST: {
                return this.parse(dateStrings[0], pref);
            }
            case FAVOR_LATEST: {
                return this.parse(dateStrings[1], pref);
            }
            case FAVOR_MIDPOINT: {
                Date d1 = this.parse(dateStrings[0], ImpreciseDatePreference.FAVOR_EARLIEST);
                Date d2 = this.parse(dateStrings[1], ImpreciseDatePreference.FAVOR_LATEST);
                if (d1 == null || d2 == null) {
                    return null;
                }
                long daysBetween = TimeUnit.DAYS.convert(d2.getTime() - d1.getTime(), TimeUnit.MILLISECONDS);
                Calendar c = Calendar.getInstance(Locale.US);
                c.setTimeZone(TimeZone.getTimeZone("UTC"));
                c.setTime(d1);
                c.add(6, (int)daysBetween / 2);
                return c.getTime();
            }
            case PRECISE: {
                return this.parse(dateStrings[0], pref);
            }
        }
        throw new IllegalArgumentException("Unexpected value for imprecise date preference: " + (Object)((Object)pref));
    }

    private Date getYearMonthDay(String dateString) {
        String bc = this.formatBC(dateString);
        String e = this.resolveEnglishCalendarSwitch(bc);
        return this.getDateWithFormatString(e, "dd MMM yyyy");
    }

    private Date getYearMonthNoDay(String dateString, ImpreciseDatePreference pref) {
        String bc = this.formatBC(dateString);
        String e = this.resolveEnglishCalendarSwitch(bc);
        Date d = this.getDateWithFormatString(e, "MMM yyyy");
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(d);
        switch (pref) {
            case FAVOR_EARLIEST: {
                c.set(5, 1);
                return c.getTime();
            }
            case FAVOR_LATEST: {
                c.set(5, 1);
                c.add(2, 1);
                c.add(6, -1);
                return c.getTime();
            }
            case FAVOR_MIDPOINT: {
                c.set(5, 1);
                c.add(2, 1);
                c.add(5, -1);
                int dom = c.get(5) / 2;
                c.set(5, dom);
                return c.getTime();
            }
            case PRECISE: {
                return d;
            }
        }
        throw new IllegalArgumentException("Unknown value for date handling preference: " + (Object)((Object)pref));
    }

    private Date getYearOnly(String dateString, ImpreciseDatePreference pref) {
        String bc = this.formatBC(dateString);
        String e = this.resolveEnglishCalendarSwitch(bc);
        Date d = this.getDateWithFormatString(e, "yyyy");
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(d);
        switch (pref) {
            case FAVOR_EARLIEST: {
                c.set(6, 1);
                return c.getTime();
            }
            case FAVOR_LATEST: {
                c.set(2, 11);
                c.set(5, 31);
                return c.getTime();
            }
            case FAVOR_MIDPOINT: {
                c.set(2, 6);
                c.set(5, 1);
                return c.getTime();
            }
            case PRECISE: {
                return d;
            }
        }
        throw new IllegalArgumentException("Unknown value for date handling preference: " + (Object)((Object)pref));
    }

    private String removeOpenEndedRangesAndPeriods(String dateString) {
        if (!PATTERN_TWO_DATES.matcher(dateString).matches()) {
            return this.removePrefixes(dateString, "FROM", "BEF", "BEFORE", "AFT", "AFTER", "TO");
        }
        return dateString;
    }

    public static enum ImpreciseDatePreference {
        PRECISE,
        FAVOR_EARLIEST,
        FAVOR_LATEST,
        FAVOR_MIDPOINT;
        

        private ImpreciseDatePreference() {
        }
    }

}


package main.java.com.xakers.model;

/**
 * Seminar class with getter methods for key fields. There is probably no
 * good reason why you would want to modify this class for your project.
 *
 * @author CS3114/CS5040 staff
 * @version July 2023, last updated September 2023
 */

public class Seminar {
    private String title; // Semianar title
    private String date; // Seminar date
    private int length; // Seminar length
    private String[] keywords; // Seminar keywords
    private short x; // Seminar x coord
    private short y; // Seminar y coord
    private String desc; // Seminar description
    private int cost; // Seminar cost
    private int id; // Seminar ID

    // ----------------------------------------------------------

    /**
     * Dummy seminar constructor
     */
    public Seminar() {
        // Nothing here
    }


    /**
     * Create a new Seminar object from the field data
     *
     * @param tin    input title
     * @param datein input date
     * @param lin    input length
     * @param kin    input keywords
     * @param xin    input x coord
     * @param yin    input y coord
     * @param descin input description
     * @param cin    input cost
     * @param idin   input ID
     */
    public Seminar(
            int idin,
            String tin,
            String datein,
            int lin,
            short xin,
            short yin,
            int cin,
            String[] kin,
            String descin) {
        id = idin;
        title = tin;
        date = datein;
        length = lin;
        x = xin;
        y = yin;
        cost = cin;
        keywords = kin;
        desc = descin;
    }


    // ----------------------------------------------------------

    /**
     * Returns the seminar ID field
     *
     * @return the ID field for the seminar
     */
    public int id() {
        return id;
    }


    // ----------------------------------------------------------

    /**
     * Return the seminar date
     *
     * @return the date field for the seminar
     */
    public String date() {
        return date;
    }


    // ----------------------------------------------------------

    /**
     * Return the seminar date
     *
     * @return the date field for the seminar
     */
    public int cost() {
        return cost;
    }


    // ----------------------------------------------------------

    /**
     * Return the seminar keywords
     *
     * @return the keywords field for the seminar
     */
    public String[] keywords() {
        return keywords;
    }


    // ----------------------------------------------------------

    /**
     * Return the seminar x coordinate
     *
     * @return the x coordinate field for the seminar
     */
    public int x() {
        return x;
    }


    // ----------------------------------------------------------

    /**
     * Return the seminar y coordinate
     *
     * @return the y coordinate field for the seminar
     */
    public int y() {
        return y;
    }


    /**
     * @return a string representation of the object.
     */
    public String toString() {
        int i;
        String mykeys = "";
        for (i = 0; i < keywords.length; i++) {
            mykeys += keywords[i];
            if (i != keywords.length - 1)
                mykeys += ", ";
        }
        return "ID: " + id + ", Title: " + title + "\nDate: " + date
                + ", Length: " + length + ", X: " + x + ", Y: " + y + ", Cost: "
                + cost + "\nDescription: " + desc + "\nKeywords: " + mykeys;
    }
}
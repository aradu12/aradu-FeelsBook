package com.example.radu.feelsbook301;


import java.util.Comparator;

/** This class represents one emotion
 * 
 * @note I decided to have one class for this, as opposed to an abstract class and several child
 * emotion classes, because the only difference between different emotions is their name
 */
public class Feeling {

    private String feeling;
    private String date;
    private String comment;

    public Feeling(String feeling, String date, String comment) {
        this.feeling = feeling;
        this.date = date;
        this.comment = comment;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Used to compare two feelings based on dates
    // reference: https://stackoverflow.com/questions/8424526/compare-object-by-dates-implements-comparator

    public static Comparator<Feeling> feelingComparator = new Comparator<Feeling>() {

        public int compare(Feeling feeling1, Feeling feeling2) {
            String Date1 = feeling1.getDate();
            String Date2 = feeling2.getDate();

            // a date goes lower down on the list if it was made more recently
            return Date1.compareTo(Date2);
        }};

    public String toString(){
        return this.date+ " | " +this.feeling+" | "+this.comment;
    }
}



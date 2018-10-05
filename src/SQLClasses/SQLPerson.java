package SQLClasses;

import dates.DateRange;
import dates.FormalDateConverter;

import java.util.ArrayList;

public class SQLPerson {
    private String PID;
    private String fullname;
    private String gender;
    private ArrayList<Integer> relations;
    private ArrayList<Integer> facts;
    private DateRange birth;
    private DateRange death;

    public SQLPerson(String pid, String fn, String g, ArrayList<Integer> r, ArrayList<Integer> f, String b, String d)
    {
        PID = pid;
        fullname = fn;
        gender = g;
        relations = r;
        facts = f;
        FormalDateConverter converter = new FormalDateConverter();
        birth = converter.convert(b);
        death = converter.convert(d);
    }

    public int getRelationCount()
    {
        return relations.size();
    }

    public int getFactsCount()
    {
        return facts.size();
    }

    public String getPID() {
        return PID;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public ArrayList<Integer> getRelations() {
        return relations;
    }

    public ArrayList<Integer> getFacts() {
        return facts;
    }

    public DateRange getBirth() {
        return birth;
    }

    public DateRange getDeath() {
        return death;
    }

    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        SQLPerson other = (SQLPerson) obj;
        return ((this.relations == other.relations) && (this.facts == other.facts) && (this.PID.equals(other.PID)) &&
                (this.fullname.equals(other.fullname)) && (this.gender.equals(other.gender)) && (this.birth.equals(other.birth)) &&
                        (this.death.equals(other.death)));
    }
}

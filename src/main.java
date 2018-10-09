import SQLClasses.SQLPerson;
import databaseClasses.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;


public class main
{
    public static void main(String[] args)
    {
        ArrayList<SQLPerson> persons = new ArrayList<>();

        database dbase = new database();
        PreparedStatement personStmt = null;
        int iterations = 0, distSize = 25000;

        ArrayList<Double> ages = new ArrayList<>();
        try
        {
            while (persons.size() < distSize)
            {
                iterations++;
                String personSQL = "SELECT * FROM Person ORDER BY RANDOM() LIMIT 50000";
                personStmt = dbase.prepareStatement(personSQL);
                database.getPersons(personStmt, persons);
            }
//            personStmt.executeBatch();
            int diff = persons.size() - distSize;
            for (int i = 0; i < diff; i++)
                persons.remove(i);
//            int count = 0;
//            for (int i = 0; i < persons.size(); i++)
//            {
//                if (persons.get(i).getFactsCount() >= 2)
//                    count++;
//                else
//                    System.out.println(persons.get(i).getPID());
//            }

            System.out.println("Iterations: " + iterations);
//            System.out.println("People with death dates: " + persons.size());
//            System.out.println("People with at least 2 facts: " + count);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        for (SQLPerson person : persons)
        {
            double birth, death, age;

            birth = (person.getBirth().getLastDay() + person.getBirth().getFirstDay()) / 2;
            death = (person.getDeath().getLastDay() + person.getDeath().getFirstDay()) / 2;

            age = (death - birth) / 365;
//            if (age > 1000)
//                System.out.println(person.getPID());
//            if (person.getRelationCount() > 2)
//                System.out.println(person.getPID());
            ages.add(age);
        }
        double precision = 0.0;

        try {
            Writer wr = new FileWriter("ageDistributions.csv");
            wr.write("YEARS,COUNT,PERCENTAGE\n");

            while (ages.size() > 0)
            {
                int count = 0;

                for (Iterator<Double> itr = ages.iterator(); ((Iterator) itr).hasNext();)
                {
                    double age = itr.next();
                    int temp = (int) (age * 10 + 0.5);
                    age = (double)temp / 10.0;
                    if (age < 0)
                        age = 0.0;
                    if (age == precision)
                    {
                        count++;
                        itr.remove();
                    }
                }

                if (count > 0)
                {
                    double percentage = (double)count / persons.size() * 100;
                    percentage = round(percentage, 4);
                    wr.write(precision + "," + count + "," + percentage + "%\n");

                }
                int temp = (int) (precision * 10 + 0.5);
                precision = (double)(temp + 1)/10.0;
            }
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }

    private static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

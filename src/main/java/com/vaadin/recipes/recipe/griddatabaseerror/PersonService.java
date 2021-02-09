package com.vaadin.recipes.recipe.griddatabaseerror;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * This class simulates a flaky database or backend.
 * 
 */
public class PersonService {

    private static String[] firstName = { "James", "John", "Robert", "Michael",
            "William", "David", "Richard", "Charles", "Joseph", "Christopher",
            "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer",
            "Maria", "Susan", "Margaret", "Dorothy", "Lisa" };

    private static String[] lastName = { "Smith", "Johnson", "Williams",
            "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
            "Andreson", "Thomas", "Jackson", "White" };

    private static List<Person> persons;

    private Random rand = new Random();

    public PersonService() {
        createTestData();        
    }

    public class DatabaseException extends Exception {
        public DatabaseException(String error) {
            super(error);
        }
    }

    public Stream<Person> fetchPersons(int offset, int limit)
            throws DatabaseException {

        final Stream<Person> filtered = persons.stream().skip(offset).limit(limit);
        if (rand.nextDouble() > 0.9) {
            // Simulated data base fault with 10% probability
            throw new DatabaseException("Data base error");
        }
        try {
            // Simulated delay of the back end call
            Thread.sleep(200);
        } catch (Exception e) {

        }
        return filtered;
    }

    private void createTestData() {
        if (persons == null) {

            persons = new ArrayList<Person>();
            for (int i = 0; i < 10000; i++) {
                final Person person = new Person();
                person.setName(firstName[rand.nextInt(firstName.length)] + " "
                        + lastName[rand.nextInt(lastName.length)]);
                person.setAge(rand.nextInt(50) + 18);
                person.setEmail(
                        person.getName().replaceAll(" ", ".") + "@example.com");

                LocalDate date = LocalDate.now().minusYears(person.getAge());
                date = date.withMonth(rand.nextInt(12) + 1);
                date = date.withDayOfMonth(rand.nextInt(28) + 1);
                person.setBirthday(date);

                persons.add(person);
            }
        }
    }


}

package example;

import java.util.*;
import java.lang.*;
import java.io.*;

// for the problem at https://csacademy.com/ieeextreme-practice/task/8761fb7efefcf1d890df1d8d91cae241/
class Main {
    public static void main (String[] args) throws java.lang.Exception {
        Scanner scanner = new Scanner(System.in);
        // The first line contains the number of people we need to sort. Read it in as an int, and go to the next line.
        int numPeople = scanner.nextInt();
        scanner.nextLine();
        // We need to order numPeople people. Create a new Person array that is numPeople large. We will insert now,
        // sort later.
        Person[] people = new Person[numPeople];
        for(int i = 0 ; i < numPeople; ++i) {
            // For each line, split on the space in the string. e.g. "Name 160" -> ["Name", "160"]
            String[] parts = scanner.nextLine().split(" ");
            // Add a new Person with the height converted to an integer
            people[i] = new Person(Integer.parseInt(parts[1]), parts[0]);
        }
        // We need to process people based on their height. Since we implemented Person.compareTo(Person other), this
        // call will sort people based on the results of the compareTo call. Look below for more details.
        Arrays.sort(people);

        //first position
        int pos = 1;
        //heights cannot physically be -1, so use it as a marker.
        int height = -1;
        //the number of people whi are at a given height
        int peopleWithHeight = 0;
        StringBuilder sb = new StringBuilder();

        for (Person person : people) {
            if (person.height != height) { // if the current height changed
                if (height != -1) { //make sure we're not pumping empty, useless, wrong data.
                    //add the starting position and the last position of people
                    sb.append(pos).append(' ').append(pos + peopleWithHeight - 1);
                    //print the line with all the information about people of this height and position
                    System.out.println(sb.toString());
                    //wipe the stringbuilder so we can start a new line
                    sb = new StringBuilder();
                    //move the position forward however many people were in this height
                    pos += peopleWithHeight;
                    //there are no people in this new height category. so set it to 0
                    peopleWithHeight = 0;
                }
                //set the new height we're tracking
                height = person.height;
            }
            //No matter what, add the new persons name and a space
            sb.append(person.name).append(' ');
            //and there is now another person in this height category
            ++peopleWithHeight;
        }
        //When we're through all of the data, there is at least one person in the buffer who has not been written.
        //Add position data and print.
        sb.append(pos).append(' ').append(pos + peopleWithHeight - 1);
        System.out.println(sb.toString());
    }
}

/**
 * This class represents a person that we have to input and process.
 * We extend Comparable<Person> so we can sort against other instances of the Person class.
 */
class Person implements Comparable<Person> {
    //we need a height
    int height;
    //and a name
    String name;

    public Person(int height, String name) {
        this.height = height;
        this.name = name;
    }

    /**
     * This method is required because we implement Comparable. Since we implement Comparable<Person>, we must take a
     * Person other to compare to.
     *
     * In this case, we want to put the lowest heights first. Looking at the JavaDoc for this method, we find that
     * returning a negative number means that this object should come before the other object. This means that since we
     * want the smallest heights first, we can take our height and subtract the other height.
     *
     * Ex.
     * Our height 160, their height 180 - we should be before the other. 160 - 180 gives -20. Since -20 is less than 0,
     * sort algorithms will know our person neds to go first.
     * Our height 180, their height 160 - we are after. 180 - 160 = 20. 20 > 0, we are meant to be after.
     * Ours 200, theirs 200- They're the same! order does not matter. 200 - 200 == 0. sort algorithms will not care
     * about order when we return 0.
     *
     * @param other Person to compare to
     * @return The difference in height compared to the other person
     */
    @Override
    public int compareTo(Person other) {
        return this.height - other.height;
    }

    @Override
    public String toString() {
        return name + ": "+ + height;
    }
}
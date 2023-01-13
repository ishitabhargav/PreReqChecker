package prereqchecker;

import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 *
 * @author Ishita Bhargava
 */
public class Eligible {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        Graph coursesGraph = new Graph();
        StdIn.setFile(args[0]);
        int numCourses = StdIn.readInt();
        for (int i = 0; i < numCourses; i++) {
            String course = StdIn.readString();
            ArrayList<String> prereqs = new ArrayList<String>();
            coursesGraph.addCourse(course, prereqs);
        }
        int numConnections = StdIn.readInt();
        for (int i = 0; i < numConnections; i++) {
            String id = StdIn.readString();
            String prereq = StdIn.readString();
            coursesGraph.addPrereq(id, prereq);
        }
        StdIn.setFile(args[1]);
        int numCoursesTaken = StdIn.readInt();
        ArrayList<String> coursesTaken = new ArrayList<String>();
        for (int i = 0; i < numCoursesTaken; i++) {
            coursesTaken.add(StdIn.readString());
        }
        ArrayList<String> canTake = coursesGraph.eligibleCourses(coursesTaken);
        StdOut.setFile(args[2]);
        for (String course : canTake) {
            StdOut.println(course);
        }
    }
}

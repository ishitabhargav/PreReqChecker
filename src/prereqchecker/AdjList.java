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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 *
 * @author Ishita Bhargava
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
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
        StdOut.setFile(args[1]);
        StdOut.print(coursesGraph.adjacencyList());
    }
}

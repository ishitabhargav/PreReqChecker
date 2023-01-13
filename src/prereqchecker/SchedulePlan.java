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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with space separated course ID's
 *
 * @author Ishita Bhargava
 */
public class SchedulePlan {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
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
        String targetCourse = StdIn.readString();
        int numCoursesTaken = StdIn.readInt();
        ArrayList<String> coursesTaken = new ArrayList<String>();
        for (int i = 0; i < numCoursesTaken; i++) {
            coursesTaken.add(StdIn.readString());
        }
        HashMap<Integer, HashSet<String>> schedule = coursesGraph.buildSchedule(targetCourse, coursesTaken);
        int numSemesters = 0;
        for (int semester : schedule.keySet()) {
            numSemesters++;
        }
        StdOut.setFile(args[2]);
        StdOut.println(numSemesters);
        for (int semester : schedule.keySet()) {
            for (String course : schedule.get(semester)) {
                StdOut.print(course + " ");
            }
            StdOut.println("");
        }
    }
}

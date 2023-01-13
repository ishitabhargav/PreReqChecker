package prereqchecker;

import java.util.*;

/**
 * This class represents a Graph class implemented using a HashMap where each key is a course and
 * the value is an arraylist of prerequisites and another HashMap where each key is a course and
 * the value is a boolean used for marking during the depth-first search algorithm.
 *
 * @author Ishita Bhargava
 */
public class Graph {
    private HashMap<String, ArrayList<String>> coursesAndPreReqs;
	private HashMap<String, Boolean> marked;

    /**
     * Constructor to initialize a Graph object comprised of two HashMaps
     * coursesAndPreReqs HashMap used to store courses and their prerequisites
     * marked HashMap composed of course name and boolean and is primarily used for DFS algorithm
     */
    public Graph() {
        coursesAndPreReqs = new HashMap<String, ArrayList<String>>();
		marked = new HashMap<String, Boolean>();
    }

    /**
     * Adds a course (key) and its prerequisites (value) to the coursesAndPreReqs HashMap
     * and the course (key) and false (value) to the Graph's marked HashMap
     * @param course represents name of the course
     * @param prereqs represents arraylist of prerequisites for course
     */
    public void addCourse(String course, ArrayList<String> prereqs) {
        coursesAndPreReqs.put(course, prereqs);
        marked.put(course, false);
    }

    /**
     * Adds a prerequisite for a course to the coursesAndPreReqs HashMap's arraylist
     * @param course represents name of the course
     * @param prereq represents prerequisite for the course to be added to arraylist
     */
    public void addPrereq(String course, String prereq) {
        coursesAndPreReqs.get(course).add(prereq);
    }

    /**
     * Creates a String representation of an adjacency list for the courses and their prerequisites
     * @return the String adjacency list representation
     */
    public String adjacencyList() {
        String adjListRepresentation = "";
        for (String course : coursesAndPreReqs.keySet()) {
            adjListRepresentation += course;
            for (String prereq : coursesAndPreReqs.get(course)) {
                adjListRepresentation += " " + prereq;
            }
            adjListRepresentation += "\n";
        }
        return adjListRepresentation;
    }

    /**
     * Determines whether all courses would still be possible to take (no cycle would be created)
     * if a course were to become the prerequisite of another course
     * @param course1 the course for which course2 would be considered (but not assigned as) the
     *                prerequisite
     * @param course2 the course that would be considered (but not assigned as) the prerequisite
     *                for course1
     * @return String "YES" or "NO"
     */
    public String canBeValidPrereq(String course1, String course2) {
        HashSet<String> allPrereqs = new HashSet<String>();
        allPrereqs.add(course2);
        DFSAllPrereqs(course2, allPrereqs);
        String yesOrNo = "YES";
        if (allPrereqs.contains(course1)) { yesOrNo = "NO"; }
        return yesOrNo;
    }

    /**
     * Implements depth-first search (DFS) algorithm to fill a HashSet with a course's direct and
     * indirect prerequisites; is helper method for a few methods requiring DFS algorithm
     * @param course the course to find direct and indirect prerequisites for
     * @param allPrereqs HashSet where the direct/indirect prerequisites are added to
     */
    private void DFSAllPrereqs(String course, HashSet<String> allPrereqs) {
        for (String eachCourse : marked.keySet()) {
            marked.put(eachCourse, false);
        }
        for (String prereq : coursesAndPreReqs.get(course)) {
            if (!marked.get(prereq)) {
                allPrereqs.add(prereq);
                DFSAllPrereqs(prereq, allPrereqs);
            }
        }
        marked.put(course, true);
    }

    /**
     * Determines what courses can now be taken based on courses that have been taken
     * @param coursesTaken the arraylist of courses which have been completed (for any course
     *                     completed, any or all of its prerequisites do not have to be listed as
     *                     it is assumed that they must have been taken previously)
     * @return the arraylist of courses which can now be taken
     */
    public ArrayList<String> eligibleCourses(ArrayList<String> coursesTaken) {
        HashSet<String> allCoursesTaken = new HashSet<String>();
        ArrayList<String> canTake = new ArrayList<String>();
        for (String course : coursesTaken) {
            allCoursesTaken.add(course);
            DFSAllPrereqs(course, allCoursesTaken);
        }
        for (String course : coursesAndPreReqs.keySet()) {
            if (canTakeCourse(course, allCoursesTaken)) { canTake.add(course); }
        }
        return canTake;
    }

    /**
     * Determines whether a course can be taken based on all courses already taken; is helper
     * method for eligibleCourses
     * @param course the course for which it is determined if it can be taken
     * @param allCoursesTaken HashSet containing all courses already taken
     * @return true if the course can be taken, false otherwise
     */
    private boolean canTakeCourse(String course, HashSet<String> allCoursesTaken) {
        if (allCoursesTaken.contains(course)) { return false; }
        for (String prereq : coursesAndPreReqs.get(course)) {
            if (!allCoursesTaken.contains(prereq)) { return false; }
        }
        return true;
    }

    /**
     * Determines courses needed to be completed based on courses already taken before a specific
     * course can be taken in the future
     * @param targetCourse the course for which it is calculated what courses must be still taken
     *                     before being able to take this course
     * @param coursesTaken arraylist of courses already taken
     * @return HashSet of courses needed to be taken
     */
    public HashSet<String> coursesToTake(String targetCourse, ArrayList<String> coursesTaken) {
        HashSet<String> allCoursesTaken = new HashSet<String>();
        for (String course : coursesTaken) {
            allCoursesTaken.add(course);
            DFSAllPrereqs(course, allCoursesTaken);
        }
        HashSet<String> needToTake = new HashSet<String>();
        DFSAllPrereqs(targetCourse, needToTake);
        needToTake.removeAll(allCoursesTaken);
        return needToTake;
    }

    /**
     * Based on courses already taken and a target course, a schedule is built to show what courses
     * can be taken in the least number of semesters before being able to take the target course
     * @param targetCourse the course for which the schedule is built in order to take the course
     *                     as soon as possible
     * @param coursesTaken arraylist of courses already taken
     * @return HashMap containing the semester numbers (key) and HashSet of courses to be taken
     * that semester (value)
     */
    public HashMap<Integer, HashSet<String>> buildSchedule(String targetCourse, ArrayList<String> coursesTaken) {
        HashSet<String> needToTake = coursesToTake(targetCourse, coursesTaken);
        HashMap<Integer, HashSet<String>> schedule = new HashMap<Integer, HashSet<String>>();
        int semester = 1;
        HashSet<String> prereqs = new HashSet<String>();
        HashSet<String> semesterCourses = new HashSet<String>();
        while (!needToTake.isEmpty()) {
            for (String course : needToTake) {
                HashSet<String> c = coursesToTake(course, coursesTaken);
                if (c.isEmpty()) { semesterCourses.add(course); }
                prereqs.addAll(c);
            }
            if (!semesterCourses.isEmpty()) {
                HashSet<String> semesterCoursesCopy = new HashSet<String>();
                for (String course : semesterCourses) {
                    semesterCoursesCopy.add(course);
                }
                schedule.put(semester, semesterCoursesCopy);
                semester++;
                needToTake.removeAll(semesterCoursesCopy);
                coursesTaken.addAll(semesterCoursesCopy);
                semesterCourses.clear();
            }
        }
        return schedule;
    }
}

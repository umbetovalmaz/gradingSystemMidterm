import java.util.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Student {

    private String studentID;
    public String name;
    public int age;
    public String major;
    public Map<String, int[]> grades;

    public Student(String id, String name, int age, String major, Map<String, int[]> grades) {
        this.studentID = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.grades = grades;
    }

    public String getStudentID() {
        return studentID;
    }

    public void display() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Student Name: " + name);
        System.out.println("Student Age: " + age);
        System.out.println("Student Major: " + major);
        System.out.println("Grades:");
        for (Map.Entry<String, int[]> entry : grades.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }
        System.out.println();
    }
}

public class Main {
    public static List<Student> students = new ArrayList<>();

    public static void addStudent(String id, String name, int age, String major, Map<String, int[]> grades) {
        Student newStudent = new Student(id, name, age, major, grades);
        students.add(newStudent);
    }

    public static Student removeStudent(String studentID) {
        for (Student student : students) {
            if (student.getStudentID().equals(studentID)) {
                students.remove(student);
                return student;
            }
        }
        return null; 
    }

    public static Student findStudent(String studentID) {
        for (Student student : students) {
            if (student.getStudentID().equals(studentID)) {
                return student;
            }
        }
        return null; 
    }

    public static void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            for (Student student : students) {
                student.display();
            }
        }
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter what you want to do:\n\tadd: add a student\n\tremove: remove a student\n\tdisplay: display all students\n\tsearch: search for a student by their ID\n\tgrade: choose a student and grade them\n\texit: to exit\n");
            String inp = s.nextLine();

            if (inp.equalsIgnoreCase("add")) {
                System.out.println("Please enter the student's ID:");
                String id = s.nextLine();

                boolean idExists = false;
                for (Student student : students) {
                    if (student.getStudentID().equals(id)) {
                        idExists = true;
                        break;
                    }
                }
                if (idExists) {
                    System.out.println("This student ID already exists. Please try again.");
                    continue;
                }

                System.out.println("Please enter the student's name:");
                String name = s.nextLine();

                System.out.println("Please enter the student's age:");
                int age = -1;
                while (age <= 0) {
                    try {
                        age = s.nextInt();
                        if (age <= 0) {
                            throw new InputMismatchException();
                        }
                        s.nextLine(); 
                    } catch (InputMismatchException e) {
                        s.nextLine(); 
                        System.out.println("Invalid age. Please try again.");
                    }
                }

                System.out.println("Please enter the student's major:");
                String major = s.nextLine();

                System.out.println("Please enter the student's subjects (comma-separated):");
                String subjectsInput = s.nextLine();
                String[] subjects = subjectsInput.split(",\\s*");
                Map<String, int[]> grades = new HashMap<>();
                for (String subject : subjects) {
                    grades.put(subject, new int[0]);
                }

                addStudent(id, name, age, major, grades);
                System.out.println("Student added successfully!");

            } else if (inp.equalsIgnoreCase("search")) {
                Student student = searchForStudent(s);
                if (student != null) {
                    System.out.println("Student found:");
                    student.display();
                } else {
                    System.out.println("Student not found.");
                }
            }
            else if (inp.equalsIgnoreCase("grade")) {
                System.out.println("Please enter the ID of the student you want to add a grade for:");
                Student student = searchForStudent(s);
                grade(student, s);
            }
            
            else if (inp.equalsIgnoreCase("remove")) {
                System.out.println("Please enter the student's ID:");
                String id = s.nextLine();
                Student student = removeStudent(id);
                if (student != null) {
                    System.out.println("Student removed:");
                    student.display();
                } else {
                    System.out.println("Student with ID " + id + " not found.");
                }
            } else if (inp.equalsIgnoreCase("display")) {
                displayStudents();
            } 
            else if (inp.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }

        s.close();
    }

    private static Student searchForStudent(Scanner s) {
        System.out.println("Please enter the student's ID:");
        String id = s.nextLine();
        return findStudent(id);
    }

    private static void grade(Student student, Scanner s){
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        
        System.out.println("Student: " + student.name);
        System.out.println("Grades:");
        
        for (String subject : student.grades.keySet()) {
            System.out.println(subject + ": " + Arrays.toString(student.grades.get(subject)));
        }

        System.out.println("Choose the subject");
        String subject = s.nextLine();


        // LINEAR SEARCH
        for (String sub : student.grades.keySet()) {
            if (sub.equalsIgnoreCase(subject)){
                System.out.println("Grade");
                int newGrade = s.nextInt();
                int[] currentGrades = student.grades.get(sub);
                int[] newGrades = Arrays.copyOf(currentGrades, currentGrades.length + 1);
                newGrades[currentGrades.length] = newGrade;
                student.grades.put(sub, newGrades);
            }
        }
    }
}


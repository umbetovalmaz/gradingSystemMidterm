import java.util.*;
import java.io.*;

// Base class for all creatures
abstract class Creature {
    protected String type;

    public Creature(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract void makeSound();
}

// Base class for all living creatures
abstract class LivingCreature extends Creature {
    protected int age;

    public LivingCreature(String type, int age) {
        super(type);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void makeSound() {
        System.out.println("Generic living creature sound");
    }
}

// Abstract class for Person
abstract class Person extends LivingCreature {
    protected String name;

    public Person(String name, int age) {
        super("Human", age);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void makeSound() {
        System.out.println("Hello!");
    }

    public abstract void displayInfo();
}

// Interface for grading system
interface GradingSystem {
    String calculateGrade(double averageMarks);
}

// Base Student class
class Student extends Person implements GradingSystem, Serializable {
    private String studentID;
    private String major;
    private double[] marks;  // Store marks for 6 courses

    public Student(String studentID, String name, int age, String major, double[] marks) {
        super(name, age);
        this.studentID = studentID;
        this.major = major;
        setMarks(marks);
    }

    public String getStudentID() {
        return studentID;
    }

    public double[] getMarks() {
        return marks;
    }

    public double getAverageMarks() {
        return Arrays.stream(marks).average().orElse(0.0);
    }

    public void setMarks(double[] marks) {
        if (marks.length != 6) {
            throw new IllegalArgumentException("Must provide marks for 6 courses.");
        }
        for (double mark : marks) {
            if (mark < 0 || mark > 100) {
                throw new IllegalArgumentException("Marks must be between 0 and 100.");
            }
        }
        this.marks = marks;
    }

    @Override
    public String calculateGrade(double averageMarks) {
        if (averageMarks >= 90) return "A";
        else if (averageMarks >= 87) return "B+";
        else if (averageMarks >= 84) return "B";
        else if (averageMarks >= 80) return "B-";
        else if (averageMarks >= 70) return "C";
        else if (averageMarks >= 60) return "D";
        else return "F";
    }

    @Override
    public void displayInfo() {
        double averageMarks = getAverageMarks();
        System.out.printf("ID: %s | Name: %s | Age: %d | Major: %s | Average Marks: %.2f | Grade: %s\n",
                studentID, name, age, major, averageMarks, calculateGrade(averageMarks));
    }
}

// Specialized subclass with scholarship status
class ScholarshipStudent extends Student {
    private boolean hasScholarship;

    public ScholarshipStudent(String studentID, String name, int age, String major, double[] marks, boolean hasScholarship) {
        super(studentID, name, age, major, marks);
        this.hasScholarship = hasScholarship;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Scholarship: " + (hasScholarship ? "Yes" : "No"));
    }
}

public class FinalProject {
    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Display All Students");
            System.out.println("4. Search for a Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    removeStudent(scanner);
                    break;
                case 3:
                    displayAllStudents();
                    break;
                case 4:
                    searchStudent(scanner);
                    break;
                case 5:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static void addStudent(Scanner scanner) {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();

            System.out.print("Enter Name: ");
            String name = "";
            while (name.trim().isEmpty()) {
                name = scanner.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("Name cannot be empty. Please enter a valid name:");
                }
            }

            System.out.print("Enter Age: ");
            int age = -1;
            while (age <= 0) {
                try {
                    age = scanner.nextInt();
                    if (age <= 0) {
                        System.out.println("Age must be positive. Please try again:");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a positive number:");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // Clear the newline after the number

            System.out.print("Enter Major: ");
            String major = "";
            while (major.trim().isEmpty()) {
                major = scanner.nextLine();
                if (major.trim().isEmpty()) {
                    System.out.println("Major cannot be empty. Please enter a valid major:");
                }
            }

            double[] marks = new double[6];
            for (int i = 0; i < 6; i++) {
                System.out.printf("Enter Marks for Course %d: ", i + 1);
                while (true) {
                    try {
                        marks[i] = scanner.nextDouble();
                        if (marks[i] < 0 || marks[i] > 100) {
                            System.out.println("Marks must be between 0 and 100. Please try again:");
                        } else {
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number between 0 and 100:");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }
            }
            scanner.nextLine(); // Clear the newline after the number

            System.out.print("Scholarship (true/false): ");
            boolean scholarship = false;
            boolean validInput = false;
            while (!validInput) {
                try {
                    scholarship = scanner.nextBoolean();
                    validInput = true;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter 'true' or 'false':");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // Clear the newline after the boolean

            students.add(new ScholarshipStudent(id, name, age, major, marks, scholarship));
            System.out.println("Student added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            scanner.nextLine(); // Clear scanner buffer
        }
    }

    private static void removeStudent(Scanner scanner) {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine();
        boolean removed = students.removeIf(student -> student.getStudentID().equalsIgnoreCase(id));
        if (removed) {
            System.out.println("Student removed successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            students.forEach(Student::displayInfo);
        }
    }

    private static void searchStudent(Scanner scanner) {
        System.out.print("Enter Student ID to search: ");
        String id = scanner.nextLine();
        boolean found = false;
        for (Student student : students) {
            if (student.getStudentID().equalsIgnoreCase(id)) {
                student.displayInfo();
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Student not found.");
        }
    }
}

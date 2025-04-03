import java.util.*;
import java.io.*;

// Abstract class for Person
abstract class Person {
    protected String name;
    protected int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public abstract void displayInfo();
}

// Interface for grading system
interface GradingSystem {
    String calculateGrade(double marks);
}

// Base Student class
class Student extends Person implements GradingSystem, Serializable {
    private String studentID;
    private String major;
    private double marks;

    // Default constructor for serialization
    public Student() {
        super("", 0);
        this.studentID = "";
        this.major = "";
        this.marks = 0.0;
    }

    public Student(String studentID, String name, int age, String major, double marks) {
        super(name, age);
        this.studentID = studentID;
        this.major = major;
        setMarks(marks);
    }

    public String getStudentID() {
        return studentID;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        if (marks >= 0 && marks <= 100) {
            this.marks = marks;
        } else {
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        }
    }

    @Override
    public String calculateGrade(double marks) {
        if (marks >= 90) return "A";
        else if (marks >=87) return "B+";
        else if (marks >=84) return "B";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }

    @Override
    public void displayInfo() {
        System.out.printf("ID: %s | Name: %s | Age: %d | Major: %s | Grade: %s\n",
                studentID, name, age, major, calculateGrade(marks));
    }
}

// Specialized subclass with scholarship status
class ScholarshipStudent extends Student {
    private boolean hasScholarship;

    // Default constructor for serialization
    public ScholarshipStudent() {
        super();
        this.hasScholarship = false;
    }

    public ScholarshipStudent(String studentID, String name, int age, String major, double marks, boolean hasScholarship) {
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
            System.out.println("5. Save Students to File");
            System.out.println("6. Load Students from File");
            System.out.println("7. Exit");
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
                    saveToFile();
                    break;
                case 6:
                    loadFromFile();
                    break;
                case 7:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
        scanner.close();
    }

    private static void addStudent(Scanner scanner) {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Name: ");
            String name = "";
            boolean validName = false;
            while (!validName) {
                name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Name cannot be empty. Please enter a valid name:");
                } else if (!name.matches("^[a-zA-Z\\s]+$")) {
                    System.out.println("Name can only contain letters and spaces. Please enter a valid name:");
                } else {
                    validName = true;
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
            
            System.out.print("Enter Marks: ");
            double marks = -1;
            while (marks < 0 || marks > 100) {
                try {
                    marks = scanner.nextDouble();
                    if (marks < 0 || marks > 100) {
                        System.out.println("Marks must be between 0 and 100. Please try again:");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 0 and 100:");
                    scanner.nextLine(); // Clear the invalid input
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
    

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.txt"))) {
            oos.writeObject(students);
            System.out.println("Student data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        File file = new File("students.txt");
        if (!file.exists()) {
            System.out.println("No saved data found.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            students = (List<Student>) ois.readObject();
            System.out.println("Student data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
    }
}

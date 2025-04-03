import java.util.*;
import java.io.*;

// Base class for all creatures
abstract class Creature implements Serializable {
    protected int age;

    // Default constructor for serialization
    public Creature() {
        this.age = 0;
    }

    public Creature(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

// Base class for all living creatures
abstract class LivingCreature extends Creature {
    protected String name;

    // Default constructor for serialization
    public LivingCreature() {
        super();
        this.name = "";
    }

    public LivingCreature(String name, int age) {
        super(age);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

// Abstract class for Person
abstract class Person extends LivingCreature {
    // Default constructor for serialization
    public Person() {
        super();
    }

    public Person(String name, int age) {
        super(name, age);
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
    private Map<String, double[]> courseMarks = new HashMap<>();

    // Default constructor for serialization
    public Student() {
        super("", 0);
        this.studentID = "";
        this.major = "";
    }

    public Student(String studentID, String name, int age, String major, Map<String, double[]> courseMarks) {
        super(name, age);
        this.studentID = studentID;
        this.major = major;
        setCourseMarks(courseMarks);
    }

    public String getStudentID() {
        return studentID;
    }

    public Map<String, double[]> getCourseMarks() {
        return courseMarks;
    }

    public double getAverageMarks() {
        if (courseMarks.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        int count = 0;
        for (double[] marks : courseMarks.values()) {
            double courseAverage = Arrays.stream(marks).average().orElse(0.0);
            total += courseAverage;
            count++;
        }
        return count > 0 ? total / count : 0.0;
    }

    public void setCourseMarks(Map<String, double[]> courseMarks) {
        if (courseMarks == null || courseMarks.isEmpty()) {
            throw new IllegalArgumentException("Must provide at least one course with marks.");
        }
        for (Map.Entry<String, double[]> entry : courseMarks.entrySet()) {
            for (double mark : entry.getValue()) {
                if (mark < 0 || mark > 100) {
                    throw new IllegalArgumentException("Marks must be between 0 and 100.");
                }
            }
        }
        this.courseMarks = courseMarks;
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
        double overallAverage = getAverageMarks();
        System.out.printf("ID: %s | Name: %s | Age: %d | Major: %s | Overall Average: %.2f | GPA: %s\n",
                studentID, name, age, major, overallAverage, calculateGrade(overallAverage));
        
        if (!courseMarks.isEmpty()) {
            System.out.println("Course Grades:");
            for (Map.Entry<String, double[]> entry : courseMarks.entrySet()) {
                double courseAverage = Arrays.stream(entry.getValue()).average().orElse(0.0);
                System.out.printf("  %s: %.2f (%s)\n", 
                    entry.getKey(), 
                    courseAverage,
                    calculateGrade(courseAverage));
            }
        }
    }
}

public class FinalProject {
    private static List<Student> students = new ArrayList<>();
    private static final String PASSWORD = "ParvinAka";
    private static final String DATA_FILE = "students.dat";

    public static void main(String[] args) {
        loadStudentData();
        Scanner scanner = new Scanner(System.in);
        
        if (!login(scanner)) {
            System.out.println("Access denied. Limited access mode activated.");
            limitedAccessMode(scanner);
            return;
        }

        fullAccessMode(scanner);
    }

    private static void limitedAccessMode(Scanner scanner) {
        int choice;
        do {
            System.out.println("\nStudent Management System (Limited Access)");
            System.out.println("1. Display All Students");
            System.out.println("2. Search for a Student");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayAllStudents();
                    break;
                case 2:
                    searchStudent(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
        scanner.close();
    }

    private static void fullAccessMode(Scanner scanner) {
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
                    saveStudentData();
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        System.out.print("Enter password to access the system: ");
        String inputPassword = scanner.nextLine();
        return PASSWORD.equals(inputPassword);
    }

    private static void addStudent(Scanner scanner) {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Major: ");
        String major = scanner.nextLine();

        Map<String, double[]> courseMarks = new HashMap<>();
        boolean continueAdding = true;
        
        while (continueAdding) {
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();
            
            System.out.print("Enter comma-separated grades (e.g. 85.5,90,87.5): ");
            String gradesStr = scanner.nextLine();
            String[] gradeStrArray = gradesStr.split(",");
            double[] grades = new double[gradeStrArray.length];
            
            for (int i = 0; i < gradeStrArray.length; i++) {
                grades[i] = Double.parseDouble(gradeStrArray[i].trim());
            }
            
            courseMarks.put(courseName, grades);
            
            System.out.print("Add another course? (yes/no): ");
            String response = scanner.nextLine();
            continueAdding = response.trim().equalsIgnoreCase("yes");
        }

        students.add(new Student(id, name, age, major, courseMarks));
        System.out.println("Student added successfully.");
    }

    private static void removeStudent(Scanner scanner) {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine();
        boolean removed = students.removeIf(student -> student.getStudentID().equalsIgnoreCase(id));
        System.out.println(removed ? "Student removed successfully." : "Student not found.");
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
        Optional<Student> student = students.stream()
            .filter(s -> s.getStudentID().equalsIgnoreCase(id))
            .findFirst();
        if (student.isPresent()) {
            student.get().displayInfo();
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void saveStudentData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
            System.out.println("Student data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadStudentData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No existing student data found. Starting with empty list.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            students = (List<Student>) ois.readObject();
            System.out.println("Student data loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading student data: " + e.getMessage());
            students = new ArrayList<>();
        }
    }
}

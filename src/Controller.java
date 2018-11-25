import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private Mode currentMode;
    private Admin admin;
    private User user;
    private ArrayList<Course> currentCourses;
    private ArrayList<Message> currentMessages;

    public Controller() {
        loadData();
    }

    private static final Scanner sc = new Scanner(System.in);

    private final String EXCEPT_CLASS = "Class not found!";
    private final String EXCEPT_FILE = "File not found!";
    private final String EXCEPT_IO = "Input / Output exception!";

    private final String TEACHERS = "teachers.out";
    private final String STUDENTS = "students.out";
    private final String ORMANAGERS = "ormanagers.out";
    private final String MANAGERS = "managers.out";
    private final String EXECUTORS = "executors.out";
    private final String NEWS = "news.out";

    public static ArrayList<Teacher> teachers = new ArrayList<Teacher>();
    public static ArrayList<Manager> managers = new ArrayList<Manager>();
    public static ArrayList<ORManager> orManagers = new ArrayList<ORManager>();
    public static ArrayList<Executor> executors = new ArrayList<Executor>();

    public static ArrayList<Student> students = new ArrayList<Student>();

    public static ArrayList<Course> courses = new ArrayList<Course>();

    public static ArrayList<News> news = new ArrayList<News>();

    public void begin() {
        System.out.println("Are you enter as admin or user?");

        String ans = sc.nextLine().toLowerCase();

        System.out.println("Enter your login and password (2 lines)");

        String login = sc.nextLine().toLowerCase();
        String password = sc.nextLine();

        switch (ans) {
            case "admin":
                session_admin(login, password);
                break;
            case "user":
                session_user(login, password);
                break;
        }
    }

    private void session_user(String login, String password) {
        ArrayList<User> list = new ArrayList<>();

        list.addAll(teachers);
        list.addAll(students);
        list.addAll(managers);
        list.addAll(orManagers);
        list.addAll(executors);

        for (User u: list) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                user = u;

                switch (u.getClass().toString()) {
                    case "Student":
                        currentMode = Mode.STUDENT;
                        break;
                    case "Teacher":
                        currentMode = Mode.TEACHER;
                        break;
                    case "Manager":
                        currentMode = Mode.MANAGER;
                        break;
                    case "OPManager":
                        currentMode = Mode.ORMANAGER;
                        break;
                    case "Executor":
                        currentMode = Mode.EXECUTOR;
                        break;
                }

                return;
            }
        }

        System.out.println(user);
    }

    private void session_admin(String login, String password) {
        admin = new Admin();

        if (admin.getLogin().equals(login) && admin.getPassword().equals(password)) {
            String ans = "";

            while (ans != "exit") {
                System.out.println("Choose the option!");
                System.out.println("1. Add new user");
                System.out.println("2. Delete user");
                System.out.println("3. Show log file");

                ans = sc.nextLine();

                switch (ans) {
                    case "1":
                        adminAdd();
                        break;
                    case "2":
                        adminRemove();
                        break;
                    case "3":
                        adminLogs();
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Invalid option!");
                        break;
                }
            }


        }
        else {
            System.out.println("Invalid login or password!");

            return;
        }
    }

    private void adminAdd() {
        System.out.println("Whom you want to add?");
        System.out.println("1. Student");
        System.out.println("2. Teacher");
        System.out.println("3. Manager");
        System.out.println("4. OR Manager");
        System.out.println("5. Executor");

        String ans = sc.nextLine();
        Mode mode;

        switch (ans) {
            case "1":
                mode = Mode.STUDENT;
                break;
            case "2":
                mode = Mode.TEACHER;
                break;
            case "3":
                mode = Mode.MANAGER;
                break;
            case "4":
                mode = Mode.ORMANAGER;
                break;
            case "5":
                mode = Mode.EXECUTOR;
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }

        System.out.println("Enter lastname");

        String lname = sc.nextLine();

        System.out.println("Enter firstname");

        String name = sc.nextLine();

        System.out.println("Enter login");

        String login = sc.nextLine();

        admin.addUser(lname, name, login, mode);
    }

    private void adminRemove() {

    }

    private void adminLogs() {

    }

    private void loadData() {
        loadStudents();
        loadTeachers();
        loadExecutors();
        loadManagers();
        loadOrManagers();
    }

    private void loadStudents() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENTS));

            students = (ArrayList<Student>) ois.readObject();

            ois.close();

        }
        catch (ClassNotFoundException e) {
            System.out.println(STUDENTS + ": " + EXCEPT_CLASS);
        }
        catch (FileNotFoundException e) {
            System.out.println(STUDENTS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(STUDENTS + ": " + EXCEPT_IO);
        }
    }

    private void loadTeachers() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEACHERS));

            teachers = (ArrayList<Teacher>) ois.readObject();

            ois.close();

        }
        catch (ClassNotFoundException e) {
            System.out.println(TEACHERS + ": " + EXCEPT_CLASS);
        }
        catch (FileNotFoundException e) {
            System.out.println(TEACHERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(TEACHERS + ": " + EXCEPT_IO);
        }
    }

    private void loadManagers() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MANAGERS));

            managers = (ArrayList<Manager>) ois.readObject();

            ois.close();
        }
        catch (ClassNotFoundException e) {
            System.out.println(MANAGERS + ": " + EXCEPT_CLASS);
        }
        catch (FileNotFoundException e) {
            System.out.println(MANAGERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(MANAGERS + ": " + EXCEPT_IO);
        }
    }

    private void loadOrManagers() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORMANAGERS));

            orManagers = (ArrayList<ORManager>) ois.readObject();

            ois.close();

        }
        catch (ClassNotFoundException e) {
            System.out.println(ORMANAGERS + ": " + EXCEPT_CLASS);
        }
        catch (FileNotFoundException e) {
            System.out.println(ORMANAGERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(ORMANAGERS + ": " + EXCEPT_IO);
        }
    }

    private void loadExecutors() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXECUTORS));

            executors = (ArrayList<Executor>) ois.readObject();

            ois.close();

        }
        catch (ClassNotFoundException e) {
            System.out.println(EXECUTORS + ": " + EXCEPT_CLASS);
        }
        catch (FileNotFoundException e) {
            System.out.println(EXECUTORS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(EXECUTORS + ": " + EXCEPT_IO);
        }
    }

}

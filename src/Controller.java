import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Controller {
    private Mode mode;
    private Admin admin;
    private User user;

    private Teacher teacher;
    private ArrayList<Course> curCourses;
    private ArrayList<Message> curMessages;

    private Student student;

    private Manager manager;

    private ORManager orManager;

    private Executor executor;


    public Controller() {
        loadData();
    }

    private static final Scanner sc = new Scanner(System.in);

    private static final String PATH = "/home/dontnicemebr0/IdeaProjects/Intra/src/";
    private static final String LOG = "log.txt";

    private static final String DATE_PATTERN = "dd.MM.yy HH:mm";

    private static final String EXCEPT_CLASS = "Class not found!";
    private static final String EXCEPT_FILE = "File not found!";
    private static final String EXCEPT_IO = "Input / Output exception!";

    private static final String TEACHERS = "teachers.out";
    private static final String STUDENTS = "students.out";
    private static final String ORMANAGERS = "ormanagers.out";
    private static final String MANAGERS = "managers.out";
    private static final String EXECUTORS = "executors.out";
    private static final String NEWS = "news.out";

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
                sessionAdmin(login, password);
                break;
            case "user":
                sessionUser(login, password);
                break;
        }
    }

    public static void writeLog(String msg) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(PATH + LOG, true));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_PATTERN);

            bw.write(dtf.format(LocalDateTime.now())+ " - " + msg + "\n");

            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            System.out.println(EXCEPT_IO);
        }
    }

    private void sessionUser(String login, String password) {
        ArrayList<User> list = new ArrayList<>();

        list.addAll(teachers);
        list.addAll(students);
        list.addAll(managers);
        list.addAll(orManagers);
        list.addAll(executors);

        for (User u: list) {
            if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                user = u;

                Controller.writeLog("User " + u.getLogin() + " logged in!");

                switch (u.getClass().toString().split(" ")[1]) {
                    case "Student":
                        sessionStudent();
                        break;
                    case "Teacher":
                        sessionTeacher();
                        break;
                    case "Manager":
                        sessionManager();
                        break;
                    case "OPManager":
                        sessionORManager();
                        break;
                    case "Executor":
                        sessionExecutor();
                        break;
                }
//                System.out.println(user);

                return;
            }
        }

    }

    private void sessionStudent() {
        Student student = (Student) user;
        mode = Mode.STUDENT;

        System.out.println("You are logged as student!");

        System.out.println("Choose the option you want");
        System.out.println("1. Courses");
        System.out.println("2. Transcript");
        System.out.println("3. Schedule");
        System.out.println("4. News");
        System.out.println("5. Registration");

        String ans = sc.nextLine();

        switch (ans) {
            case "1":
                student.getCourses();
        }
    }

    private void sessionTeacher() {
        teacher = (Teacher) user;
        mode = Mode.TEACHER;

        curCourses = teacher.getCoursesObj();

        String ans = "";

        System.out.println("You are logged as teacher!");

        while (!ans.equals("exit")) {
            System.out.println("Choose the option you want");
            System.out.println("1. Courses");
            System.out.println("2. Schedule");
            System.out.println("3. News");
            System.out.println("4. Send order");

            ans = sc.nextLine();

            switch (ans) {
                case "1":
                    teacherCourses();
                    break;
                case "2":
                    showSchedule();
                    break;
                case "3":
                    showNews();
                    break;
                case "4":
                    sendOrder();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        }

    }

    private void teacherCourses() {
        String ans = "";

        while (!ans.equals("exit")) {
            System.out.println("Your courses:");

            for (int i = 0; i < curCourses.size(); ++i) {
                System.out.println((i + 1) + ". " + curCourses.get(i).getCourseName());
            }

            System.out.println("Select course you want");

            ans = sc.nextLine();

            try {
                int ind = Integer.decode(ans);

                ind--;

                if (ind > -1 && ind < curCourses.size()) {
                    teacherCourse(ind);
                }
                else {
                    System.out.println("Wrong selection");
                }
            }
            catch (Exception e) {
                System.out.println("Wrong selection");
            }
        }

    }

    private void teacherCourse(int ind) {
        String ans = "";

        while (!ans.equals("exit")) {
            System.out.println("Choose the option you want");
            System.out.println("1. Add file");
            System.out.println("2. Delete file");
            System.out.println("3. Show students");
            System.out.println("4. Show Info");

            ans = sc.nextLine();

            switch (ans) {
                case "1":
                    teacherAddFile(ind);
                    break;
                case "2":
                    showSchedule();
                    break;
                case "3":
                    showNews();
                    break;
                case "4":
                    System.out.println(curCourses.get(ind));
                    break;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        }
    }

    private void teacherAddFile(int ind) {
        System.out.println("Load the file you want to add");

        System.out.println("What`s its title?");
        String title = sc.nextLine();

        System.out.println("What`s its text?");
        String text = sc.nextLine();

        CourseFile courseFile = new CourseFile(title, text, teacher.getLogin());

        curCourses.get(ind).addFile(courseFile);

    }

    private void showSchedule() {

    }

    private void showNews() {
        int limit = 7;
        String ans = "";

        for (int i = 0; i < news.size(); ++i) {
            System.out.println((i + 1) + ". " + news.get(news.size() - i - 1).getTitle());

            if (i == limit) break;
        }

        System.out.println("Select the news you want");

        while (!ans.equals("exit")) {
            ans = sc.nextLine();

            try {
                int ind = Integer.decode(ans);

                ind--;

                if (ind > -1 && ind < limit + 1) {
                    showNews(ind);
                }
                else {
                    System.out.println("Wrong selection");
                }
            }
            catch (Exception e) {
                if (ans.equals("exit")) {
                    break;
                }

                System.out.println("Wrong selection");
            }
        }


    }

    private void showNews(int ind) {
        System.out.println(news.get(news.size() - ind - 1));
    }

    private void sendOrder() {
        System.out.println("Write order you want to send");

        System.out.println("What`s its title?");
        String title = sc.nextLine();

        System.out.println("What`s its text?");
        String text = sc.nextLine();

        Order order = new Order(title, text, teacher.getLogin(), Calendar.getInstance().getTime());

        teacher.sendOrder(order);
    }

    private void sessionManager() {
        manager = (Manager) user;
        mode = Mode.MANAGER;


    }

    private void sessionORManager() {
        orManager = (ORManager) user;
        mode = Mode.ORMANAGER;


    }

    private void sessionExecutor() {
        executor = (Executor) user;
        mode = Mode.EXECUTOR;

        String ans = "";

        System.out.println("You are logged as executor!");

        while (!ans.equals("exit")) {

            System.out.println("Choose the option you want");
            System.out.println("1. News");
            System.out.println("2. Show new orders");
            System.out.println("3. Show not done orders");
            System.out.println("4. Show done orders");
            System.out.println("5. Show rejected orders");
            System.out.println("6. Show messages");

            ans = sc.nextLine();

            switch (ans) {
                case "1":
                    showNews();
                    break;
                case "2":
                    executorOrders(OrderStatus.NEW);
                    break;
                case "3":
                    executorOrders(OrderStatus.NOT_DONE);
                    break;
                case "4":
                    executorOrders(OrderStatus.DONE);
                    break;
                case "5":
                    executorOrders(OrderStatus.REJECTED);
                    break;
                case "6":
                    showMessages();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Not valid option!");
                    break;
            }
        }
    }

    private void executorOrders(OrderStatus status) {
        ArrayList<Order> orders = executor.getOrders(status);

        for (int i = 0; i < orders.size(); ++i) {
            System.out.println((i + 1) + ". " + orders.get(i).getTitle());
        }

        String ans = "";

        System.out.println("Select one order!");

        while (!ans.equals("exit")) {
            ans = sc.nextLine();

            try {
                int ind = Integer.decode(ans);

                ind--;

                if (ind > -1 && ind < orders.size()) {
                    executorOrder(orders.get(ind));
                }
                else {
                    System.out.println("Wrong selection");
                }
            }
            catch (Exception e) {
                if (ans.equals("exit")) {
                    break;
                }

                System.out.println("Wrong selection");
            }
        }
    }

    private void executorOrder(Order order) {
        System.out.println(order);

        String ans = "";

        if (order.getStatus() == OrderStatus.NEW) {
            System.out.println("Accept this order or reject?");

            ans = sc.nextLine().toLowerCase();

            switch (ans) {
                case "accept":
                    order.setStatus(OrderStatus.NOT_DONE);
                    break;
                case "reject":
                    order.setStatus(OrderStatus.REJECTED);
                    break;
                default:
                    System.out.println("Not valid operation!");
                    break;
            }
        }

        else if (order.getStatus() == OrderStatus.NOT_DONE) {
            System.out.println("Have this order done?");

            ans = sc.nextLine().toLowerCase();

            switch (ans) {
                case "yes":
                    order.setStatus(OrderStatus.DONE);
                case "no":
                    break;
                default:
                    System.out.println("Not valid operation!");
                    break;
            }
        }

        Executor.saveOrders();
    }

    private void showMessages() {
        Employee employee;

        switch (mode) {
            case TEACHER:
                employee = teacher;
                break;
            case MANAGER:
                employee = manager;
                break;
            case ORMANAGER:
                employee = orManager;
                break;
            case EXECUTOR:
                employee = executor;
                break;
        }


    }

    private void sessionAdmin(String login, String password) {
        admin = new Admin();

        if (admin.getLogin().equals(login) && admin.getPassword().equals(password)) {
            String ans = "";

            Controller.writeLog("Admin logged in!");

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
        }
    }

    private void adminAdd() {
        while (true) {
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
                case "exit":
                    return;
                default:
                    System.out.println("Invalid option!");
                    continue;
            }

            System.out.println("Enter lastname");

            String lname = sc.nextLine();

            System.out.println("Enter firstname");

            String name = sc.nextLine();

            System.out.println("Enter login");

            String login = sc.nextLine();

            admin.addUser(lname, name, login, mode);
        }

    }

    private void adminRemove() {
        System.out.println("Enter user`s login you want to delete");

        String login = sc.nextLine();

        if (admin.deleteUser(login)) {
            System.out.println("Success!");
        }
        else {
            System.out.println("User not found!");
        }
    }

    private void adminLogs() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(PATH + LOG));

            String line = br.readLine();

            while (line != null) {
                System.out.println(line);

                line = br.readLine();
            }
        }
        catch (IOException e) {
            System.out.println(EXCEPT_IO);
        }
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

    public static void saveStudents() {
        try {
            ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(STUDENTS));

            oot.writeObject(students);

            oot.flush();
            oot.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(STUDENTS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(STUDENTS + ": " + EXCEPT_IO);
        }
    }

    public static void saveTeachers() {
        try {
            ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(TEACHERS));

            oot.writeObject(teachers);

            oot.flush();
            oot.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(TEACHERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(TEACHERS + ": " + EXCEPT_IO);
        }
    }

    public static void saveManagers() {
        try {
            ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(MANAGERS));

            oot.writeObject(managers);

            oot.flush();
            oot.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(MANAGERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(MANAGERS + ": " + EXCEPT_IO);
        }
    }

    public static void saveOrManagers() {
        try {
            ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(ORMANAGERS));

            oot.writeObject(orManagers);

            oot.flush();
            oot.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(ORMANAGERS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(ORMANAGERS + ": " + EXCEPT_IO);
        }
    }

    public static void saveExecutors() {
        try {
            ObjectOutputStream oot = new ObjectOutputStream(new FileOutputStream(EXECUTORS));

            oot.writeObject(executors);

            oot.flush();
            oot.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(EXECUTORS + ": " + EXCEPT_FILE);
        }
        catch (IOException e) {
            System.out.println(EXECUTORS + ": " + EXCEPT_IO);
        }
    }

}

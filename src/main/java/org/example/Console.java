package org.example;

import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * main body of the program
 */
public class Console {
    private static final String UPDATE_DEPARTMENT = "1";
    private static final String LIST_DEPARTMENTS = "2";
    private static final String UPDATE_MANAGERS = "3";
    private static final String LIST_MANAGERS = "4";
    private static final String LIST_EMPLOYEES = "5";
    private static final String UPDATE_PROJECTS = "6";
    private static final String LIST_PROJECTS = "7";
    private static final String ADD_EMPLOYEE = "8";
    private static final String UPDATE_EMPLOYEE_PROJECT_LIST = "9";
    private static final String EXIT_CODE = "0";
    private static final SessionFactory SESSION_FACTORY =
            HibernateConfig.getSessionFactory();



    //the menu
    private static void mainMenu() {
        System.out.println("\n1. Update Department Details");
        System.out.println("2. List Departments");
        System.out.println("3. Update Manager Details");
        System.out.println("4. List Managers");
        System.out.println("5. List Employees");
        System.out.println("6. Update Projects Details");
        System.out.println("7. List Projects");
        System.out.println("8. Add Employee");
        System.out.println("9. Update Project List");
        System.out.println("0. Exit");
        System.out.print("\nEnter your choice: ");

    }

    //starting the app
    public static void startApp(){

        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = session.beginTransaction();
        Scanner scanner = new Scanner(System.in);
        mainMenu();
        String choice = scanner.nextLine().trim();
        while (!choice.equals(EXIT_CODE)) {
            if (choice.equals(UPDATE_DEPARTMENT)) {
                updateDepartment(scanner, session);
            } else if (choice.equals(LIST_DEPARTMENTS)) {
                DepartmentManagement.getDepartments(session);
            } else if (choice.equals(UPDATE_MANAGERS)) {
                updateManager(scanner, session);
            } else if (choice.equals(LIST_MANAGERS)) {
                ManagerialOversight.getManagers(session);
            } else if (choice.equals(LIST_EMPLOYEES)) {
                ManagerialOversight.getEmployees(session);
            } else if (choice.equals(UPDATE_PROJECTS)) {
                updateProject(scanner, session);
            } else if (choice.equals(LIST_PROJECTS)) {
                ProjectManagement.getProjects(session);
            } else if (choice.equals(ADD_EMPLOYEE)){
                addEmployee(scanner, session);
            }else if(choice.equals(UPDATE_EMPLOYEE_PROJECT_LIST)){
                updateProjectList(scanner, session);
            }
            mainMenu();
            choice = scanner.nextLine().trim();
        }
        transaction.commit();
        session.close();
        HibernateConfig.shutdown();

    }

    //updating the project list
    private static void updateProjectList(Scanner scanner, Session session) {
        System.out.println("Enter employee ID: ");
        Long id = idCheck(scanner);
        ProjectManagement.getProjects(session);
        System.out.println("Enter project ID");
        Long projectID = idCheck(scanner);
        ManagerialOversight.updateProjectList(session,projectID,id);
    }


    //add employees
    private static void addEmployee(Scanner scanner, Session session) {
        String regexMail = "^(.+)@(.+)$";
        String regexPhone = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
        System.out.println("Enter employee name:");
        String name = scanner.nextLine().trim();
        System.out.println("Enter employee phone number:");
        String phone = scanner.nextLine().trim();
        while(!phone.matches(regexPhone)){
            System.out.println("Enter valid phone number: ");
            phone = scanner.nextLine().trim();
        }
        System.out.println("Enter employee email:");
        String email = scanner.nextLine().trim();
        while(!email.matches(regexMail)){
            System.out.println("Enter valid mail: ");
            email = scanner.nextLine().trim();
        }
        DepartmentManagement.getDepartments(session);
        System.out.println("Enter employee department ID: ");
        Long id = idCheck(scanner);
        System.out.println("Enter hiring date: ");
        LocalDate hireDate = getDateFromUser("Enter end date (YYYY-MM-DD): ", scanner);
        System.out.println("Enter job title: ");
        String jobTitle = scanner.nextLine().trim();
        ProjectManagement.getProjects(session);
        ManagerialOversight.createEmployee(session,name,phone,email,hireDate,null,jobTitle,id);

    }

    //update the project
    private static void updateProject(Scanner scanner, Session session) {
        String choice;
        choice = getProjectsUpdateWay(scanner);
        if (choice.equals("1")) {
            setProjectName(scanner, session);
        } else if (choice.equals("2")) {
            setStartDate(scanner, session);
        } else if (choice.equals("3")) {
            setEndDate(scanner, session);
        } else if (choice.equals("4")) {
            setBudget(scanner, session);
        } else if (choice.equals("5")) {
            assignEmployeeToProject(scanner, session);
        } else if (choice.equals("6")) {
            reassignEmployeeFromProject(scanner, session);
        }else if(choice.equals("7")){
            createProject(scanner, session);
        }
    }

    //create the project
    private static void createProject(Scanner scanner, Session session) {
        System.out.println("Enter projects name: ");
        String name = scanner.nextLine().trim();
        LocalDate startDate = getDateFromUser("Enter start date (YYYY-MM-DD): ", scanner);
        LocalDate endDate = getDateFromUser("Enter start date (YYYY-MM-DD): ", scanner);
        while (endDate.isBefore(startDate)) {
            System.out.println("Error: End date must be on or after the start date.");
        }
        System.out.println("Enter the budget: ");
        BigDecimal budget = BigDecimal.valueOf(numericVal(scanner));
        ProjectManagement.creatingProject(session,name,startDate,endDate,budget);
    }

    //checking if the input is numeric or not and return the value
    private static Long numericVal(Scanner scanner){
        String s = scanner.nextLine().trim();
        while(!s.matches("\\d+")){
            System.out.println("Enter in digits: ");
            s = scanner.nextLine().trim();
        }
        return Long.parseLong(s);
    }


    //updating the manager

    private static void updateManager(Scanner scanner, Session session) {
        String choice;
        choice = getManagersUpdateWay(scanner);
        if(choice.equals("1")){
            assignManager(scanner, session);
        }
        else if(choice.equals("2")){
            reassignManager(scanner, session);
        }else if(choice.equals("3")){
            setManagerLevel(scanner, session);
        }
    }

    private static void updateDepartment(Scanner scanner, Session session) {
        String choice;
        choice = getDepartmentUpdateWay(scanner);
        if(choice.equals("1")){
            departmentUpdate(scanner, session);
        }
        else if(choice.equals("2")){
            departmentNewName(scanner, session);
        }else if(choice.equals("3")){
            departmentNewLocation(scanner, session);
        }else if(choice.equals("4")) {
            newHead(scanner, session);
        }else if(choice.equals("5")){
            deleteDepartment(scanner, session);
        }else if(choice.equals("6")){
            assignEmployeeToDepartment(scanner, session);
        }else if(choice.equals("7")){
            reassignEmployee(scanner, session);
        }
    }

    //reassigning employees

    private static void reassignEmployeeFromProject(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long projectId = idCheck(scanner);
        System.out.println("Enter employee ID: ");
        Long employeeID = idCheck(scanner);
        ProjectManagement.reassignEmployee(session, employeeID, projectId);
    }

    //assigning employees

    private static void assignEmployeeToProject(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long projectId = idCheck(scanner);
        System.out.println("Enter employee ID: ");
        Long employeeID = idCheck(scanner);
        ProjectManagement.assignEmployee(session, employeeID, projectId);
    }

    //set projects budget

    private static void setBudget(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long projectId = idCheck(scanner);
        System.out.println("Enter the new budget: ");
        Long budget = idCheck(scanner);
        ProjectManagement.setBudget(session, projectId, BigDecimal.valueOf(budget));
    }


    //setting the end date of the project

    private static void setEndDate(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long id = idCheck(scanner);
        LocalDate endDate = getDateFromUser("Enter end date (YYYY-MM-DD): ", scanner);
        while (endDate.isBefore(ProjectManagement.getProjectById(session, id).getStartDate())) {
            System.out.println("Error: End date must be on or after the start date.");

        }
        ProjectManagement.setEndDate(session, id, endDate);

    }


    //setting the start date of the project

    private static void setStartDate(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long id = idCheck(scanner);
        LocalDate startDate = getDateFromUser("Enter start date (YYYY-MM-DD): ", scanner);
        while (startDate.isAfter(ProjectManagement.getProjectById(session, id).getEndDate())) {
            System.out.println("Error: End date must be on or after the start date.");
        }
            ProjectManagement.setStartDate(session, id, startDate);

    }

    //set project name

    private static void setProjectName(Scanner scanner, Session session) {
        System.out.println("Enter project ID: ");
        Long id = idCheck(scanner);
        System.out.println("Enter project name: ");
        ProjectManagement.setProjectName(session, id, scanner.nextLine().trim());
    }

    private static void setManagerLevel(Scanner scanner, Session session) {
        System.out.println("Enter the manager ID: ");
        Long id = idCheck(scanner);
        System.out.println("Set the level of the manager: ");
        ManagerialOversight.setManagementLevel(session,id, scanner.nextLine().trim());
    }

    private static void reassignManager(Scanner scanner, Session session) {
        System.out.println("Enter manager ID: ");
        Long managerID = idCheck(scanner);
        ManagerialOversight.reassignManagedDepartment(session,managerID);
    }

    private static void assignManager(Scanner scanner, Session session) {
        System.out.println("Enter manager ID: ");
        Long managerID = idCheck(scanner);
        System.out.println("Enter department ID: ");
        Long departmentId = idCheck(scanner);
        ManagerialOversight.assignManagedDepartment(session,managerID,departmentId);
    }

    private static void reassignEmployee(Scanner scanner, Session session) {
        System.out.println("Enter employee ID: ");
        Long employeeID = idCheck(scanner);
        DepartmentManagement.reassignEmployee(session,employeeID);
    }

    private static void assignEmployeeToDepartment(Scanner scanner, Session session) {
        System.out.println("Enter employee ID: ");
        Long employeeID = idCheck(scanner);
        System.out.println("Enter department ID: ");
        Long departmentId = idCheck(scanner);
        DepartmentManagement.assignEmployee(session,employeeID,departmentId);
    }

    private static void deleteDepartment(Scanner scanner, Session session) {
        System.out.println("Enter department ID: ");
        Long id = idCheck(scanner);
        DepartmentManagement.deleteDepartment(session,id);
    }

    private static void newHead(Scanner scanner, Session session) {
        System.out.println("Enter department ID: ");
        Long id = idCheck(scanner);
        System.out.println("Department new head: ");
        Manager employee = fromEmployeeToHead(session, scanner);
        if(employee != null)
            DepartmentManagement.setDepartmentHead(session, id, employee);
        else
            System.err.println("There is no employee with that ID");
    }

    private static void departmentNewLocation(Scanner scanner, Session session) {
        System.out.println("Enter department ID: ");
        Long id = idCheck(scanner);
        System.out.println("Department new location: ");
        String location = scanner.nextLine().trim();
        DepartmentManagement.setLocation(session,id,location);
    }

    private static void departmentNewName(Scanner scanner, Session session) {
        System.out.println("Enter department ID: ");
        Long id = idCheck(scanner);
        System.out.println("Department new name: ");
        String name = scanner.nextLine().trim();
        DepartmentManagement.setDepartmentName(session,id,name);
    }

    private static void departmentUpdate(Scanner scanner, Session session) {
        System.out.println("Department name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Location: ");
        String location = scanner.nextLine().trim();
        System.out.println("Choose the head: ");
        Manager manager = fromEmployeeToHead(session, scanner);
        if(manager != null)
            DepartmentManagement.createDepartment(session,name,location,manager);
        else
            System.err.println("There is no employee with that ID");
    }

    private static String getManagersUpdateWay(Scanner scanner) {
        String choice;
        System.out.println("\n1.Assign Managed Department");
        System.out.println("2.Reassign Managed Department");
        System.out.println("3.Set Management Level");
        System.out.println("\nEnter your choice: ");
        choice = scanner.nextLine().trim();
        return choice;
    }

    private static String getDepartmentUpdateWay(Scanner scanner) {
        String choice;
        System.out.println("\nWhat do you want to update? ");
        System.out.println("1.Create Department");
        System.out.println("2.Set Department Name");
        System.out.println("3.Set Location");
        System.out.println("4.Set Department Head");
        System.out.println("5.Delete Department");
        System.out.println("6.Assign Employee");
        System.out.println("7.Reassign Employee");
        System.out.println("\nEnter your choice: ");
        choice = scanner.nextLine().trim();
        return choice;
    }

    //getting the ways of project updates
    private static String getProjectsUpdateWay(Scanner scanner) {
        String choice;
        System.out.println("\n1.Set Project Name");
        System.out.println("2.Set Start Date");
        System.out.println("3.Set End Date");
        System.out.println("4.Set Budget");
        System.out.println("5.Assign Employee");
        System.out.println("6.Reassign Employee");
        System.out.println("7.Create a Project");
        System.out.println("\nEnter your choice: ");
        choice = scanner.nextLine().trim();
        return choice;
    }

    //creating department head from employee
    private static Manager fromEmployeeToHead(Session session, Scanner scanner) {
        ManagerialOversight.getEmployees(session);
        System.out.println("Enter the employee ID that you want to be the head: ");
        Long employeeID = idCheck(scanner);
        Manager employee = (Manager) DepartmentManagement.getEmployeeById(session, employeeID);
        System.out.println("Set the level of the manager: ");
        if(employee!=null)
            employee.setManagementLevel(scanner.nextLine().trim());
        return employee;
    }

    //checking the id's
    private static Long idCheck(Scanner scanner) {
        String id = scanner.nextLine().trim();
        while(!id.matches("\\d+")){
            System.out.println("Enter ID(digits): ");
            id = scanner.nextLine().trim();
        }
        return Long.parseLong(id);
    }


    //getting the local data from the user
    private static LocalDate getDateFromUser(String prompt, Scanner scanner) {
        LocalDate date = null;

        while (date == null) {
            System.out.print(prompt);
            String userInput = scanner.nextLine();

            try {
                date = LocalDate.parse(userInput);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        return date;
    }




}

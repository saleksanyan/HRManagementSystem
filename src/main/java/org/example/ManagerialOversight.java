package org.example;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ManagerialOversight {

    public static Manager getManagerById(Session session,long id) {
        return session.get(Manager.class, id);
    }


    public static void assignManagedDepartment(Session session, Long managerId, Long departmentId) {
        DepartmentManagement.getEmployeeById(session,managerId).setManagedDepartment(
                DepartmentManagement.getDepartmentById(session,departmentId));
    }

    public static void reassignManagedDepartment(Session session, Long managerId) {
        DepartmentManagement.getEmployeeById(session,managerId).setManagedDepartment(null);
    }

    public static void setManagementLevel(Session session, Long managerId, String managementLevel) {
        getManagerById(session,managerId).setManagementLevel(managementLevel);
    }

    public static void getEmployees(Session session){
        Query<Employee> getEmployees = session.createNamedQuery("get_employees", Employee.class);
        List<Employee> employees = getEmployees.getResultList();
        for(Employee e: employees){
            System.out.println("Employee ID: " + e.getEmployeeId());
            System.out.println("Name: " + e.getName());
            System.out.println("Phone: " + e.getPhoneNumber());
            System.out.println("Email: " + e.getEmail());
            System.out.println("Hiring date: " + e.getHireDate());
            System.out.println("Job Title: " + e.getJobTitle());
            System.out.println("Projects: " + e.getProjects());
            System.out.println("Department: " + e.getDepartment());
            System.out.println("------------------------------");
        }
    }

    public static void getManagers(Session session){
        Query<Manager> getManagers = session.createNamedQuery("get_managers", Manager.class);
        List<Manager> managers = getManagers.getResultList();
        for(Manager e: managers){
            System.out.println("Employee ID: " + e.getEmployeeId());
            System.out.println("Name: " + e.getName());
            System.out.println("Manager level: " + e.getManagementLevel());
            System.out.println("Department: " + e.getManagedDepartment());
            System.out.println("------------------------------");
        }
    }

    public static void createEmployee(Session session, String name, String phone,String email,
                                      LocalDate hireDate, Set<Project> projects,
                                      String jobTitle, Long departmentId){
        Employee employee = new Employee(name,email,phone,hireDate,jobTitle,
                projects, DepartmentManagement.getDepartmentById(session,departmentId));
        session.persist(employee);
        session.flush();
        System.out.println("Employee has been created successfully!");
    }

    public static void updateProjectList(Session session, Long projectId, Long employeeId){
        DepartmentManagement.getEmployeeById(session,employeeId).
                addProjects(ProjectManagement.getProjectById(session,projectId));
        System.out.println("The project list has been updated!");
    }
}

package org.example;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class DepartmentManagement {

    public static void createDepartment(Session session, String departmentName, String location, Manager departmentHead){
        Department department = new Department(departmentName,location,departmentHead);
        session.persist(department);
        session.flush();
        System.out.println("Department has been created successfully!");
    }

    public static void setDepartmentName(Session session, Long departmentId, String name) {

        getDepartmentById(session,departmentId).setDepartmentName(name);
        System.out.println("Department name has been updated!");
    }

    public static void setLocation(Session session, Long departmentId, String location) {
        getDepartmentById(session,departmentId).setLocation(location);
        System.out.println("Department name has been updated!");
    }

    public static void setDepartmentHead(Session session, Long departmentId, Employee departmentHead) {
        getDepartmentById(session,departmentId).setDepartmentHead(departmentHead);
//        departmentHead.setManagedDepartment(getDepartmentById(session,departmentId));
        System.out.println("Department head has been updated!");
    }

    public static Department getDepartmentById(Session session,long id) {
        return session.get(Department.class, id);
    }

    public static Employee getEmployeeById(Session session,long id) {
            return session.get(Employee.class, id);
    }

    public static void deleteDepartment(Session session, Long id){
        session.remove(getDepartmentById(session,id));
        System.out.println("Department has been deleted successfully!");

    }


    public static void assignEmployee(Session session,Long employeeId, Long departmentId){
        getEmployeeById(session,employeeId).setDepartment(getDepartmentById(session,departmentId));
    }
    public static void reassignEmployee(Session session,Long employeeId){
            getEmployeeById(session,employeeId).setDepartment(null);
    }

    //lists departments

    public static void getDepartments(Session session){
        Query<Department> getDepartments = session.createNamedQuery("get_departments", Department.class);
        List<Department> departments = getDepartments.getResultList();
        for(Department d: departments){
            System.out.println("Department ID: " + d.getDepartmentId());
            System.out.println("Name: " + d.getDepartmentName());
            System.out.println("Location: " + d.getLocation());
            System.out.println("Department Head: " + d.getDepartmentHead());
            System.out.println("------------------------------");
        }
    }




}

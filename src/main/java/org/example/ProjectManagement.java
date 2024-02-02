package org.example;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.LongConsumer;

public class ProjectManagement {

    public static Project getProjectById(Session session,long id) {
        return session.get(Project.class, id);
    }



    public static void creatingProject(Session session, String name, LocalDate start, LocalDate end,
                                       BigDecimal budget){
        Project p = new Project();
        p.setProjectName(name);
        p.setStartDate(start);
        p.setEndDate(end);
        p.setBudget(budget);
        session.persist(p);
        session.flush();

    }
    public static void setProjectName(Session session, Long projectId, String projectName) {
        getProjectById(session,projectId).setProjectName(projectName);
        System.out.println("Project name has been updated!");

    }





    public static void setStartDate(Session session, Long projectId, LocalDate startDate) {
        getProjectById(session,projectId).setStartDate(startDate);
        System.out.println("Project start date has been updated!");

    }



    public static void setEndDate(Session session, Long projectId, LocalDate endDate) {
        getProjectById(session,projectId).setEndDate(endDate);
        System.out.println("Project end date has been updated!");

    }


    public static void setBudget(Session session, Long projectId, BigDecimal budget) {
        getProjectById(session,projectId).setBudget(budget);
        System.out.println("Project budget has been updated!");

    }




    public static void setEmployees(Session session, Long projectId, Set<Employee> employees) {
        getProjectById(session,projectId).setEmployees(employees);
        System.out.println("Project employee list has been updated!");

    }

    public static void addEmployee(Session session, Long projectId, Long employeeId){
        getProjectById(session,projectId).getEmployees().add(DepartmentManagement.getEmployeeById(session,employeeId));
        DepartmentManagement.getEmployeeById(session,employeeId).addProjects(getProjectById(session,projectId));
        System.out.println("Project employee list has been updated!");

    }


    public static void assignEmployee(Session session, Long employeeId, Long projectId){
        addEmployee(session,projectId, employeeId);
    }
    public static void reassignEmployee(Session session, Long employeeId, Long projectId){
            getProjectById(session,projectId).getEmployees().remove(DepartmentManagement.getEmployeeById(session,employeeId));
    }


    public static void getProjects(Session session){
        Query<Project> getProject = session.createNamedQuery("get_projects", Project.class);
        List<Project> projects = getProject.getResultList();
        for(Project p: projects){
            System.out.println("Project ID: " + p.getProjectId());
            System.out.println("Name: " + p.getProjectName());
            System.out.println("Start Date: " + p.getStartDate());
            System.out.println("End Date: " + p.getEndDate());
            System.out.println("Budget: " + p.getBudget());
            System.out.println("Employees: " + p.getEmployees());
            System.out.println("------------------------------");
        }
    }

}

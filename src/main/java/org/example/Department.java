package org.example;
//import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "departments")
@NamedQueries({
//get all departments
    @NamedQuery(
            name = "get_departments",
            query = "SELECT d FROM Department d"
    )
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "location", nullable = false)
    private String location;

    @OneToOne
    @JoinColumn(name = "department_head_id", referencedColumnName = "employee_id")
    private Employee departmentHead;


    public Department(String departmentName, String location, Employee departmentHead) {
        this.departmentName = departmentName;
        this.location = location;
        this.departmentHead = departmentHead;
    }

    public Department(){};

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Employee getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(Employee departmentHead) {
        this.departmentHead = departmentHead;
    }

    @Override
    public String toString() {
        return "Department ID: "+departmentId +"\nDepartment Name: "+departmentName+"\nDepartment Location: "+location;
    }
}

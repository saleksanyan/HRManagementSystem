package org.example;
//import jakarta.persistence.*;
import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("manager")
@NamedQueries({
//get all projects
        @NamedQuery(
                name = "get_managers",
                query = "SELECT m FROM Manager m"
        )
})

public class Manager extends Employee {


    @OneToOne
    @PrimaryKeyJoinColumn
    private Department managedDepartment;

    @Column(name = "management_level")
    private String managementLevel;



    public Department getManagedDepartment() {
        return managedDepartment;
    }

    public void setManagedDepartment(Department managedDepartment) {
        this.managedDepartment = managedDepartment;
    }

    public String getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(String managementLevel) {
        this.managementLevel = managementLevel;
    }

    @Override
    public String toString() {
        return "Manager ID: "+ this.getEmployeeId()+"\nManager Name: "+ this.getName();
    }
}

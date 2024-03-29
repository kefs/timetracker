package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;

import java.util.List;

public class CustomerBuilder {

    private String name;

    private List<Project> projects;
    private Integer id;

    public Customer build() {
        Preconditions.checkNotNull(name);

        Customer customer = new Customer(name);
        customer.setProjects(projects);

        if (id != null) {
            customer.setId(id);
        }

        return customer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

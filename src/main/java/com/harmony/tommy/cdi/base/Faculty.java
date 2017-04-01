package com.harmony.tommy.cdi.base;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class Faculty {

    private List<String> facultyMembers;
    private String facultyName;

    @PostConstruct
    public void initialize() {
        this.facultyMembers = new ArrayList<String>();
        facultyMembers.add("张三");
        facultyMembers.add("李四");
        facultyName = "计算机科学";
    }

    public List<String> getFacultyMembers() {
        return facultyMembers;
    }

    public String getFacultyName() {
        return facultyName;
    }

}

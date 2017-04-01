package com.harmony.tommy.cdi.base;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class Course {

    @Inject
    private Faculty faculty;
    private String courseName;

    @PostConstruct
    private void init() {
        assert faculty != null;
        this.courseName = "计算机基础知识I";
    }

    public String getCourseName() {
        return courseName;
    }

    public Faculty getFaculty() {
        return faculty;
    }
}

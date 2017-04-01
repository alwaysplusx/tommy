package com.harmony.tommy.bval;

import javax.validation.constraints.NotNull;

public class Dog implements Animal {

    private String name;
    private String ownerName;
    @NotNull(message = "dog type may not be empty")
    private String type;
    @NotNull(message = "dog age may not be empty", groups = { Animal.class })
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}

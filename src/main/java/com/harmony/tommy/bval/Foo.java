package com.harmony.tommy.bval;

import javax.validation.constraints.NotNull;

import com.harmony.tommy.bval.constraints.ZipCode;

public class Foo {

    @NotNull(message = "foo.name can't be null ")
    private String name;
    @ZipCode
    private String zipCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}

package com.harmony.tommy.bval;

import javax.validation.Valid;

public class Bar {

    @Valid
    private Foo foo;

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(Foo foo) {
        this.foo = foo;
    }

}

package com.harmony.tommy.bval;

import javax.validation.GroupSequence;

@GroupSequence(value = { FirstNameGroup.class, MiddleNameGroup.class, LastNameGroup.class })
public interface MyGroup {

}

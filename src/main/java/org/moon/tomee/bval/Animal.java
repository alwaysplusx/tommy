package org.moon.tomee.bval;

import javax.validation.constraints.NotNull;

public interface Animal {
	@NotNull(message = "animal name may not be null")
	String getName();
	@NotNull(message = "animal ownerName may not be null")
	String getOwnerName();
}

package org.moon.tomee.bval;

import org.moon.tomee.bval.constraints.Contains;

public class Baz {

	@Contains.List(
		value = {
			@Contains(content = "this is automata"),
			@Contains(content = "this is manual machines") 
		}
	)
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

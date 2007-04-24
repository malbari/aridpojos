package net.chrisrichardson.arid.beans2;

import net.chrisrichardson.arid.beans3.Baz;

public class Bar {

	private Baz baz;

	public void setBaz(Baz baz) {
		this.baz = baz;
	}

	public String doit() {
		return "Bar" + baz.doit();
	}
	
	
}

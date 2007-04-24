package net.chrisrichardson.arid.beans1;

import net.chrisrichardson.arid.beans2.Bar;

public class Foo {

	private Bar bar;

	public Foo(Bar bar) {
		this.bar = bar;
	}

	public String doit() {
		return "Foo" + bar.doit();
	}
	
	
}

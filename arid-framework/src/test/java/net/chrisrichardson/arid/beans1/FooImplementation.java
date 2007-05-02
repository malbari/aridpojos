package net.chrisrichardson.arid.beans1;

import net.chrisrichardson.arid.beans2.Bar;

public class FooImplementation implements Foo {

	private Bar bar;

	public FooImplementation(Bar bar) {
		this.bar = bar;
	}

	public String doit() {
		return "Foo" + bar.doit();
	}
	
	
}

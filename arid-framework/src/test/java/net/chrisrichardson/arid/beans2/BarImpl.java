package net.chrisrichardson.arid.beans2;

import net.chrisrichardson.arid.beans3.Baz;

public class BarImpl implements Bar {

	private Baz baz;

	public void setBaz(Baz baz) {
		this.baz = baz;
	}

	/* (non-Javadoc)
	 * @see net.chrisrichardson.arid.beans2.Bar#doit()
	 */
	public String doit() {
		return "Bar" + baz.doit();
	}
	
	
}

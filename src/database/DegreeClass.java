package database;

public enum DegreeClass {
	FC ("First Class"),
	SCU ("Second Class Upper"),
	SCL ("Second Class Lower"),
	TC ("Third Class"),
	Pass ("Pass"),
	Fail ("Fail");
	private String degClass;
	DegreeClass(String degreeClass) {
		this.degClass = degreeClass;
	}
	public String printClass () {
		return this.degClass.toUpperCase();
	}
}

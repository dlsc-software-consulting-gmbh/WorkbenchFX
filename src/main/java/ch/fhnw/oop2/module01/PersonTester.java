package ch.fhnw.oop2.module01;

/**
 * @author Dieter Holz
 */
public class PersonTester {
	public static void main(String[] args) {
		Person p = new Person(42, 90, 1.85);

		System.out.println("weight " + p.getWeight());
		System.out.println("height  " + p.getHeight());
		System.out.println("BMI " + p.getBMI());
	}
}

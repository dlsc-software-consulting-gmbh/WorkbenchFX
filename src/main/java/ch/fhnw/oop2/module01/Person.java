package ch.fhnw.oop2.module01;

/**
 * @author Dieter Holz
 */
public class Person {
	private int    age;
	private double weight;
	private double height;

	public Person(int age, double weight, double height) {
		this.age = age;
		this.weight = weight;
		this.height = height;
	}

	public double getBMI() {
		return weight / (height * height);
	}

	//alle Getter und Setter

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}

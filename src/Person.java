package src;

import java.util.*;

public class Person implements Comparable<Person> {
	private String lastName;// private variables for person object
	private String firstName;
	private String telephone;
	private String address;
	private String email;

	public Person(String lastN, String firstN) {
		this.lastName = lastN;// if only names, other variables are set to null
		this.firstName = firstN;
		this.telephone = null;
		this.address = null;
		this.email = null;
	}

	public Person(String lastN, String firstN, String adr, String tel, String em) {
		this.lastName = lastN;// get the variables from constructor
		this.firstName = firstN;
		this.telephone = tel;
		this.address = adr;
		this.email = em;
	}

	public String getFirstName() {
		return this.firstName;// get
	}

	public String getLastName() {
		return this.lastName;// get
	}

	public String getPhone() {
		return this.telephone;// get
	}

	public String getAddress() {
		return this.address;//get
	}

	public String getEmail() {
		return this.email;//get
	}

	public void setFirstName(String strFN) {
		this.firstName = strFN;//set firstname
	}

	public void setLastName(String strLN) {
		this.lastName = strLN;//set lastname
	}

	public void setPhone(String strPh) {
		this.telephone = strPh;//set phone number
	}

	public void setAddress(String strAds) {
		this.address = strAds;//set address
	}

	public void setEmail(String strEm) {
		this.email = strEm;//set email
	}

	public String toString() {
		return (getFirstName() + " " + getLastName() + " " + getPhone() + " " + getEmail());//display personal informaiton
	}

	public int compareTo(Person o) {
		if (!o.getLastName().equals(this.getLastName()))
			return this.getLastName().compareTo(o.getLastName());//compare a person's name as priority
		else
			return this.getFirstName().compareTo(o.getFirstName());
	}
}

// class email comparator
class emailComp implements Comparator<Person> {
	public int compare(Person o1, Person o2) {
		return o1.getEmail().compareTo(o2.getEmail());
	}
}

// class telephone comparator
class phoneComp implements Comparator<Person> {
	public int compare(Person o1, Person o2) {
		return o1.getPhone().compareTo(o2.getPhone());
	}
}
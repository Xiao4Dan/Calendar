package src;

import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;

public class Contacts {
	private ArrayList<Person> people;//people
	private String filename = "contacts.txt";

	public Contacts() {
		this.people = new ArrayList<Person>();//set up list object
		try {
			readContactsFile();//call the read file method with exception handling
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);//show exception for file reading
		}
	}

	public void add(Person p) {
		if(!people.contains(p)) this.people.add(p);//add if person is not repeated
		else JOptionPane.showMessageDialog(null, "Person exists! info dumped");
	}

	public Person findPerson_Nm(String firstName, String lastName) {
		Person re = null;//return person object
		for (Person p : this.people) {
			if (p.getFirstName().equals(firstName) && (p.getLastName().equals(lastName))) {
				re = p;
				break;
			}
		}
		return re;
	}

	public Person findPerson_Ph(String phone) {
		Person re = null;//return person object
		for (Person p : this.people) {
			if (p.getPhone().equals(phone)) {
				re = p;
				break;
			}
		}
		return re;
	}

	public Person findPerson_Em(String email) {
		Person re = null;//return person object
		for (Person p : this.people) {
			if (p.getEmail().equals(email)) {
				re = p;
				break;
			}
		}
		return re;
	}

	public void readContactsFile() throws IOException {
		/*int count = 0;// total number of lines
		BufferedReader read = new BufferedReader(new FileReader(filename));
		while (read.readLine() != null)
			count++;
		read.close();
		*/

		Scanner scanner = new Scanner(new File(filename));
		String firstline = scanner.nextLine();// 1st: number of persons
		int records;// number of person
		if (Integer.parseInt(firstline) == 0) {
			scanner.close();
			throw new IOException("null number of people ERROR");
		}
		records = Integer.parseInt(firstline);
		//
		String lastNames[] = new String[records], firstNames[] = new String[records], addresses[] = new String[records],
				telephones[] = new String[records], emails[] = new String[records];

		// read info
		int num = 0;
		while (scanner.hasNextLine()) {
			lastNames[num] = scanner.nextLine();// 1st last name
			firstNames[num] = scanner.nextLine();// 2nd first name
			addresses[num] = scanner.nextLine();// 3rd address
			telephones[num] = scanner.nextLine();// 4th telephone
			emails[num] = scanner.nextLine();// 5th email
			if (lastNames[num] == null) {
				scanner.close();
				throw new IOException("null last name ERROR");// last name
																// exception
			}
			if (emails[num] == null) {
				scanner.close();
				throw new IOException("null email ERROR");// email exception
			}
			//if not more exceptions, then set up a new person object
			people.add(new Person(lastNames[num], firstNames[num], addresses[num], telephones[num], emails[num]));
		}
		scanner.close();
	}
}

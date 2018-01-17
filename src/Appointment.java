package src;

//package src;

import java.util.*;
/**
	This program allows the frame to create appointment objects
	Yuanbo Shi 500745024
*/
public class Appointment implements Comparable<Appointment>
{
	private Calendar date;//date instant variable
	private String description;//description instant variable
	private int year;//year
	private int month;//month
	private int day;//day
	private int hour;//hour
	private int minute;//minute
	private Person person;//person
	
	public Appointment(int y, int m, int d, int h, int min, String dspt, Person p)
	{
		//constructor
		this.date = new GregorianCalendar(y,m,d,h,min);//just 
		this.year = y;//get it
		this.month = m;//get it
		this.day = d;//get it
		this.hour = h;//get it
		this.minute = min;//get it
		this.description = dspt;//get it
		this.person = p;//get it
	}
	
	public String getDescription()
	{
		return this.description;//accessor
	}
	
	public Calendar getDate()
	{
		return this.date;//accesser
	}
	
	public int getYear()//accessor
	{
		return this.year;
	}
	public int getMonth()//accessor
	{
		return this.month;
	}
	public int getDay()//accessor
	{
		return this.day;
	}
	public int getHour()//accessor
	{
		return this.hour;
	}
	public int getMinute()//accessor
	{
		return this.minute;
	}
	
	public Person getPerson()//accessor
	{
		return this.person;
	}
	
	public void setPerson(String lastN, String firstN, String tel, String adr, String em)
	{
		this.person = new Person(lastN, firstN, tel, adr, em);//set new person object
	}
	
	public String print()
	{
		//to string method
		//the assignment says "print method", so i can just make up this name
		//format the time and display the appointment with description
		String ps = "unknown";
		if(getPerson()!=null)	 ps = getPerson().toString();
		return String.format("%02d: %02d",date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE)) + " <" + description
				+ "> WITH: " + ps + "\n";//formated appointment printing with person object
	}
	
	public boolean occursOn(int yearO, int monthO, int dayO, int hourO, int minuteO)
	{
		//see if this time had reserved for some appointment and return true/false
		//I did not use date.getTime etc. for simplicity
		//well, i did have too much instance variables
		if(yearO == this.year && monthO == this.month && dayO == this.day
				&& hourO == this.hour && minuteO == this.minute) return true;
		else return false;
	}
	
	public boolean occursOn(int yearO, int monthO, int dayO)
	{
		//see if this time had reserved for some appointment and return true/false
		//I did not use date.getTime etc. for simplicity
		//well, i did have too much instance variables
		if(yearO == this.year && monthO == this.month && dayO == this.day) return true;
		else return false;
	}
	
	public int compareTo(Appointment other)
	{
		//compare appointment = compare occuring time
		return (this.getDate().getTime().compareTo(other.getDate().getTime()));
	}
}

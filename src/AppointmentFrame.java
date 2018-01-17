package src;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.text.*;

/**
 * This program allows the users to create, cancel, modify etc. the appointments
 * as planned It also constructs all the basic components and user-friendly
 * interface Yuanbo Shi 500745024
 */
public class AppointmentFrame extends JFrame {
	private static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
	// contains all the appointments
	private static Stack<Appointment> orderApps = new Stack<Appointment>();
	// stores all the appointments in order
	private static JButton[] buttons = new JButton[31];//buttons for calendar panel, max 31 days
	private Contacts contacts;//contacts read from file
	private Calendar current;// current date
	private SimpleDateFormat sdf;// simple date format
	private static final int standardW = 600;// standard width
	private static final int standardH = 600;// standard height
	private JLabel label;// instance label
	private JTextField day;// instance day field
	private JTextField month;// instance month field
	private JTextField year;// instance year field
	private JTextField hour;// instance hour field
	private JTextField minute;// instance minute field
	private JTextField lastname;
	private JTextField firstname;
	private JTextField tel;
	private JTextField email;
	private JTextArea address;
	private JTextArea textarea;// instance textarea
	private JTextArea description;// instance description area
	private JPanel controlpane;
	private JPanel scrollpane;
	private JPanel calendarpane;
	private JPanel datepane;
	private JPanel contactpane;
	private JPanel appointmentpane;
	private JPanel descriptionpane;
	private ActionListener click;// instance clicker
	private Color def;//default buttons color

	// clicker class
	class ClickListener implements ActionListener {
		// responds to all button actions of whom added me~
		public void actionPerformed(ActionEvent event) {
			// click-listener that listens to all the button clicks
			JButton n = (JButton) event.getSource();// get button object
			String command = n.getName();// get the name of the button
			def = day.getBackground();
			switch (command)// perform different actions depending on the
							// name/command of the button
			{
			case "next":
				current.add(Calendar.DATE, 1);// increment the date
				calendarpane.removeAll();
				calendarpane.add(createCalendarPanel(current));
				printAppointments();// print
				break;
			case "previous":
				current.add(Calendar.DATE, -1);// decrement the date
				calendarpane.removeAll();
				calendarpane.add(createCalendarPanel(current));
				printAppointments();// print
				break;
			case "show":
				checkDate();
				printAppointments();// simply display the relative content
				break;
			case "create":
				checkDate();
				createAppointment();// perform creation when corresponding
									// button is clicked
				year.setText("");
				month.setText("");
				day.setText("");
				description.setText("");
				hour.setText("");
				minute.setText("");
				break;
			case "cancel":
				checkDate();
				cancelAppointment();// perform cancellation when
									// corresponding button is clicked
				year.setText("");
				month.setText("");
				day.setText("");
				description.setText("");
				hour.setText("");
				minute.setText("");
				break;
			case "recall":
				recallAppointment();
				checkDate();
				printAppointments();
				break;
			case "find":
				if (findPerson() != null)
					fillPerson(findPerson());
				break;
			case "clear":
				firstname.setText("");
				lastname.setText("");
				tel.setText("");
				address.setText("");
				email.setText("");
				break;
			default:
				for (int i = 0; i < buttons.length; i++) {
					if (buttons[i] != null)//reset the button's back color
						buttons[i].setBackground(def);
					if (buttons[i] == n) {//then set clicked button color to red
						n.setBackground(Color.RED);
						//update the calendar time as well
					current.set(Calendar.DAY_OF_MONTH, Integer.parseInt(n.getText()));
					printAppointments();//print out
					}
				}
				break;
			}
			
		}
	}
	
	// MAIN
	// FRAME________________________________________________________________________________
	public AppointmentFrame() {
		Appointment base = new Appointment(2017, 3, 6, 9, 30, (1 + "th set"), null);
		appointments.add(base);
		orderApps.push(base);
		base = new Appointment(2017, 3, 6, 15, 15, (1 + "th set"), null);
		appointments.add(base);
		orderApps.push(base);
		// object declarations
		click = new ClickListener();// clicker object
		textarea = new JTextArea(""); // display appointment for the current day
		textarea.setEditable(false);// not expected to be edited by user
		sdf = new SimpleDateFormat("EEE, MMM dd, yyyy");// date format for read
														// and write
		current = Calendar.getInstance();// get current date
		label = new JLabel(sdf.format(current.getTime()));// show today's date
		scrollpane = createScrollPanel();
		calendarpane = new JPanel();
		calendarpane.add(createCalendarPanel(current));
		datepane = createDatePanel();
		contactpane = createContactPanel();
		appointmentpane = createAppointmentPanel();
		descriptionpane = createDescriptionPanel();
		//
		contacts = new Contacts();// if error, print error in description area
		//
		// frame components
		controlpane = controlPanel();
		add(controlpane);
		pack();
		setVisible(true);// display
	}

	// PRIVATE SUB
	// PANELS________________________________________________________________________________
	// control panel
	private JPanel controlPanel()
	{
		JPanel control = new JPanel();
		control.setLayout(new GridLayout(3, 2));// set layout of whole frame
		control.setSize(standardW, standardH);// set standard size of whole frame
		control.add(scrollpane);
		control.add(calendarpane);
		control.add(datepane);
		control.add(contactpane);
		control.add(appointmentpane);
		control.add(descriptionpane);
		return control;
	}
	// scroll panel
	private JPanel createScrollPanel() {
		JPanel detailsPanel = new JPanel();// return panel
		JScrollPane scroll = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// ->add two sided scroll panes to textarea
		detailsPanel.setLayout(new BorderLayout());// set layout
		scroll.setVisible(true);// display
		detailsPanel.add(scroll, BorderLayout.CENTER);// add scroll pane to the
														// center of panel
		detailsPanel.add(label, BorderLayout.NORTH);
		return detailsPanel;// return
	}

	// date panel
	private JPanel createDatePanel() {
		JPanel datePanel = new JPanel();// return panel
		datePanel.setBorder(new TitledBorder(new EtchedBorder(), "Date"));// set
																			// panel's
																			// display
																			// name
		datePanel.setLayout(new BorderLayout());// set border layout
		JButton previousDay = new JButton("<");// change display to previous day
		previousDay.setName("previous");// name the button for functionality
		previousDay.addActionListener(click);// add clicker into this button
		JButton nextDay = new JButton(">");// change display to next day
		nextDay.setName("next");// name the button for functionality
		nextDay.addActionListener(click);// add clicked into this button
		JLabel dayPrompt = new JLabel("Day ");// prompt for user
		dayPrompt.setHorizontalAlignment(SwingConstants.RIGHT);// text alignment
		day = new JTextField("");// get day
		day.setSize(20, 100);// set see-able size
		day.isDisplayable();
		day.setAutoscrolls(true);// auto scroll
		JLabel monthPrompt = new JLabel("Month ");// prompt for user
		monthPrompt.setHorizontalAlignment(SwingConstants.RIGHT);// text
																	// alignment
		month = new JTextField("");// get month
		month.setSize(20, 100);// set see-able size
		month.setAutoscrolls(true);// auto scroll
		JLabel yearPrompt = new JLabel("Year ");
		yearPrompt.setHorizontalAlignment(SwingConstants.RIGHT);// text
																// alignment
		year = new JTextField("");// get year
		year.setSize(20, 100);// set see-able size
		year.setAutoscrolls(true);// auto scroll
		JButton show = new JButton(" SHOW ");// display the select info
		show.setName("show");// name button for functionality
		show.setSize(20, 100);
		show.addActionListener(click);// add clicker
		JPanel dateButtons = new JPanel();// button panel
		dateButtons.add(previousDay);
		dateButtons.add(nextDay);// <> change date
		JPanel dateInputs = new JPanel();// inputs sub panel
		dateInputs.setLayout(new GridLayout(1, 6));// size components
		dateInputs.add(dayPrompt);
		dateInputs.add(day);// add them one by one
		dateInputs.add(monthPrompt);
		dateInputs.add(month);
		dateInputs.add(yearPrompt);
		dateInputs.add(year);
		JPanel showBtn = new JPanel();
		showBtn.add(show);
		datePanel.add(dateButtons, BorderLayout.NORTH);// insert
		datePanel.add(dateInputs, BorderLayout.CENTER);// to
		datePanel.add(showBtn, BorderLayout.SOUTH);//
		return datePanel;// return
	}

	// action panel
	private JPanel createAppointmentPanel() {
		JPanel appointmentPanel = new JPanel();// return object
		appointmentPanel.setBorder(new TitledBorder(new EtchedBorder(), "Action"));// set
																					// panel's
																					// display
																					// name
		appointmentPanel.setLayout(new BorderLayout());// set border layout
		JLabel hourPrompt = new JLabel("Hour ");// prompt for user
		hourPrompt.setHorizontalAlignment(SwingConstants.RIGHT);// text
																// alignment
		hour = new JTextField("");// get hour
		hour.setSize(20, 100);// set see-able size
		hour.setAutoscrolls(true);// see-able
		JLabel minutePrompt = new JLabel("Minute ");// prompt for user
		minutePrompt.setHorizontalAlignment(SwingConstants.RIGHT);// text
																	// alignment
		minute = new JTextField("");// get minute
		minute.setSize(20, 100);// set see-able size
		minute.setAutoscrolls(true);// see-able
		JButton create = new JButton(" CREATE ");// display the select info
		create.setName("create");// name button for functionality
		create.addActionListener(click);// add clicker
		JButton cancel = new JButton(" CANCEL ");// erase display
		cancel.setName("cancel");// name button for functionality
		cancel.addActionListener(click);// add clicker
		JButton recall = new JButton(" RECALL ");
		recall.setName("recall");
		recall.addActionListener(click);
		JPanel actionInputs = new JPanel();// get inputs objects
		actionInputs.setLayout(new GridLayout(1, 4));
		actionInputs.add(hourPrompt);
		actionInputs.add(hour);// add them one by one
		actionInputs.add(minutePrompt);
		actionInputs.add(minute);
		appointmentPanel.add(actionInputs, BorderLayout.NORTH);// to north
		JPanel actionButtons = new JPanel();// make action objects
		actionButtons.add(create);
		actionButtons.add(cancel);// add them one by one
		actionButtons.add(recall);
		appointmentPanel.add(actionButtons, BorderLayout.SOUTH);// to south
		return appointmentPanel;// return
	}

	// description panel
	private JPanel createDescriptionPanel() {
		JPanel descriptionPanel = new JPanel();// return object
		descriptionPanel.setBorder(new TitledBorder(new EtchedBorder(), "Description"));// set
																						// panel's
																						// display
																						// name
		descriptionPanel.setLayout(new BorderLayout());// set border layout
		description = new JTextArea("input your descriptions");// a show of
																// prompt
		description.setEditable(true);// see-able
		descriptionPanel.add(description, BorderLayout.CENTER);// add to center
		description.setVisible(true);// see-able
		return descriptionPanel;// return
	}

	// calendar panel
	private JPanel createCalendarPanel(Calendar cd) {
		JPanel calendarPanel = new JPanel();
		SimpleDateFormat mth = new SimpleDateFormat("MMM");
		JLabel labelMonth = new JLabel();
		labelMonth.setText(mth.format(cd.getTime()));
		calendarPanel.setLayout(new BorderLayout());
		calendarPanel.add(labelMonth, BorderLayout.NORTH);
		// day buttons
		JPanel days = new JPanel();
		days.setLayout(new GridLayout(7, 7));
		//
		JLabel[] weekdays = { new JLabel("Sun"), new JLabel("Mon"), new JLabel("Tue"), new JLabel("Wed"),
				new JLabel("Thu"), new JLabel("Fri"), new JLabel("Sat") };
		for (int c = 0; c < 7; c++) {
			days.add(weekdays[c]);
		}
		//
		JPanel[] empty = new JPanel[15];// empty j-filling-panel
		for (int e = 0; e < 15; e++)
			empty[e] = new JPanel();
		// button starting point
		Calendar tempD = new GregorianCalendar(2017, cd.get(Calendar.MONTH), 1);
		int firstdayofweek = tempD.get(Calendar.DAY_OF_WEEK);
		for (int f = 0; f < firstdayofweek - 1; f++)
			days.add(empty[f]);

		for (int b = 0; b < tempD.getActualMaximum(Calendar.DATE); b++) {
			buttons[b] = new JButton(String.valueOf(b + 1));
			buttons[b].setName(String.valueOf(b));
			buttons[b].addActionListener(click);
			days.add(buttons[b]);
		}
		for (int lim = 0; lim < (42 - firstdayofweek - tempD.getActualMaximum(Calendar.DATE)); lim++)
			days.add(empty[14 - lim]);
		calendarPanel.add(days);
		return calendarPanel;
	}

	// contact panel
	private JPanel createContactPanel() {
		JPanel contactPanel = new JPanel();
		contactPanel.setBorder(new TitledBorder(new EtchedBorder(), "Contact"));//set panel name
		contactPanel.setLayout(new GridLayout(2, 1));//layout
		JPanel info = new JPanel();//information panel for contatcts
		info.setLayout(new GridLayout(4, 2));//firstly, the name email part
		JPanel info2 = new JPanel();//address part
		info2.setLayout(new BorderLayout());
		//get information from contact part
		JPanel contactBtns = new JPanel();
		JLabel lnameprompt = new JLabel("Last Name");
		lastname = new JTextField();
		JLabel fnameprompt = new JLabel("First Name");
		firstname = new JTextField();
		JLabel telprompt = new JLabel("Tel");
		tel = new JTextField();
		JLabel emailprompt = new JLabel("Email");
		email = new JTextField();
		JLabel addressprompt = new JLabel("Address");
		address = new JTextArea();
		//set up the buttons and click listener
		JButton find = new JButton("Find");
		find.setName("find");
		find.addActionListener(click);
		JButton clear = new JButton("Clear");
		clear.setName("clear");
		clear.addActionListener(click);
		//add objects one by one to corresponding panel
		info.add(lnameprompt);
		info.add(fnameprompt);
		info.add(lastname);
		info.add(firstname);
		info.add(telprompt);
		info.add(emailprompt);
		info.add(tel);
		info.add(email);
		info2.add(addressprompt, BorderLayout.NORTH);
		info2.add(address, BorderLayout.CENTER);
		contactBtns.add(find);
		contactBtns.add(clear);
		info2.add(contactBtns, BorderLayout.SOUTH);
		//add sub panel to main panel
		contactPanel.add(info);
		contactPanel.add(info2);
		//return main panel
		return contactPanel;
	}

	// FUNCTIONS____________________APPOINTMENT____________________________________________________________
	// print appointments
	private void printAppointments() {
		/*
		 * Uses the current day, month, year and goes through the arraylist of
		 * appointments. If one of the appointment objects occurs on the current
		 * day,month,year then print it in the main textArea
		 */
		textarea.setText("");// clear all the previous content
		label.setText(sdf.format(current.getTime()));// display the current date
														// using the Calendar
														// class and the
														// SimpleDateFormat
														// class
		// label.setHorizontalAlignment(SwingConstants.LEFT);
		for (Appointment objs : appointments) {
			if (objs.occursOn(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
					current.get(Calendar.DAY_OF_MONTH))) {
				textarea.append(objs.print() + "\n");
			}
		}
		// select the buttons
		for (int i = 0; i < buttons.length && buttons[i] != null; i++)//reset color of buttons first
			buttons[i].setBackground(def);
		for (int i = 0; i < buttons.length && buttons[i] != null; i++)//then paint the corresponding button
			if (Integer.parseInt(buttons[i].getText()) == current.get(Calendar.DAY_OF_MONTH))
				buttons[i].setBackground(Color.RED);
	}

	// find appointment
	private Appointment findAppointment(int y, int m, int d, int h, int min) {
		/*
		 * Is passed a year, month, day, hour and minute and goes through the
		 * list of appointments to find one that has the same date and time. If
		 * one is found, return it otherwise return null. Or return true/false
		 * instead.
		 */
		Appointment re = null;// return object default as null
		for (Appointment finder : appointments) {
			// looking through arraylist and find event occurs on same day
			if (finder.occursOn(y, m, d, h, min)) {
				re = finder;// find it!
				break;// break out of the loop
			}
		}
		return re;// return
	}

	// create appointment
	private void createAppointment() {
		/*
		 * Takes the current year, month, day, and the hour, minute entered by
		 * the user and checks to see if an appointment already exists at this
		 * time. If so, prints “CONFLICT!!” in the description textarea. If not,
		 * then creates a new appointment object, adds it to the appointments
		 * arraylist, sorts the arraylist and prints it.
		 */
		int hourVal, minuteVal = 0;
		// config hour input
		if (hour.getText().equals("") || Integer.parseInt(hour.getText()) < 0
				|| Integer.parseInt(hour.getText()) > 23) {
			JOptionPane.showMessageDialog(null, "HOUR invalid (#0~23)");
			return;// exit the method
		}
		hourVal = Integer.parseInt(hour.getText());
		
		// config minute input
		if (minute.getText().equals(""))
			minuteVal = 0;
		else if (Integer.parseInt(minute.getText()) < 0 || Integer.parseInt(minute.getText()) > 59) {
			JOptionPane.showMessageDialog(null, "HOUR invalid (#0~59 OR blank)");
			return;// exit the method
		}
		else minuteVal = Integer.parseInt(minute.getText());
		current.set(Calendar.HOUR_OF_DAY, hourVal);
		current.set(Calendar.MINUTE, minuteVal);
		// create
		Appointment app = new Appointment(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
				current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE),
				description.getText(), findPerson());
		// test if there is one for confliction
		if (findAppointment(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH),
				current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE)) == null) {
			appointments.add(app);// create one!
			orderApps.push(app);// push into stack
			Collections.sort(appointments);// at last sort the arraylist
			printAppointments();// and print it
		} else
			// description.setText("CONFLICT!");
			JOptionPane.showMessageDialog(null, "CONFLICT");
		scrollpane.repaint();
	}

	// cancel appointment
	private void cancelAppointment() {
		/*
		 * Determines if an appointment exists for the current day,month,year
		 * and entered hour, minute. If so, removes it from the appointments
		 * arraylist, sorts the arraylist and prints the arraylist.
		 */
		// check time
		int hourX, minuteX;
		if (hour.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "You must input HOUR!");
			return;
		} else
			hourX = Integer.parseInt(hour.getText());
		//check minute
		if (minute.getText().equals(""))
			minuteX = 0;
		else
			minuteX = Integer.parseInt(minute.getText());
		//
		Appointment target = findAppointment(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
				current.get(Calendar.DAY_OF_MONTH), hourX, minuteX);
		if (target != null) {
			appointments.remove(target);
			// remove from stack
			ListIterator<Appointment> iterator = orderApps.listIterator();
			while (iterator.hasNext()) {
				if (iterator.next() == target)
					iterator.remove();
			}
		} else
			JOptionPane.showMessageDialog(null, "Appointment does not exist");
		Collections.sort(appointments);// at last sort the arraylist
		printAppointments();// and print it
		scrollpane.repaint();
	}

	private void recallAppointment() {
		Appointment remover = null;//target to remove from stack or appointments
		if (orderApps.isEmpty())
			JOptionPane.showMessageDialog(null, "Empty appointment stack!");
		else {
			remover = orderApps.peek();
			//String yearR = String.valueOf(remover.getYear());
			year.setText("2017");//default as year 2017
			month.setText(String.valueOf(remover.getMonth() + 1));//get
			day.setText(String.valueOf(remover.getDay()));//information
			hour.setText(String.valueOf(remover.getHour()));//from
			minute.setText(String.valueOf(remover.getMinute()));//input
			description.setText(remover.getDescription());
		}
	}

	// check date
	private void checkDate() {
		// configure date time input
		// if the input text is null, default to take current date
		// else get it from the text field
		int yearC = current.get(Calendar.YEAR), monthC = current.get(Calendar.MONTH),
				dayC = current.get(Calendar.DAY_OF_MONTH);//get date from now
		if (!year.getText().equals("") && !month.getText().equals("") && !day.getText().equals("")) {
			yearC = Integer.parseInt(year.getText());//read in the date
			monthC = Integer.parseInt(month.getText()) - 1;
			dayC = Integer.parseInt(day.getText());
		}
		Calendar tempD = new GregorianCalendar(yearC, monthC, dayC);
		// error checking
		if (yearC < 0) {
			JOptionPane.showMessageDialog(null, "YEAR invalid (positive)");
			return;
		}
		if (monthC < 0 || monthC > 11) {
			JOptionPane.showMessageDialog(null, "MONTH invalid (1~12)");
			return;
		}
		if (dayC < 1 || dayC > tempD.getActualMaximum(Calendar.DATE)) {
			JOptionPane.showMessageDialog(null, "DAY invalid (within month range)");
			return;
		}
		current.clear();//clear the current time
		current.set(yearC, monthC, dayC);//and then reset it
		label.setText(sdf.format(current.getTime()));//show it in label
		calendarpane.removeAll();//repaint the calendar panel for month layout
		calendarpane.add(createCalendarPanel(current));//refill
	}

	// FUNCTIONS____________________CONTACTS____________________________________________________________
	private Person findPerson() {
		Person temp = null;
		String findemail = email.getText();//get
		String findtel = tel.getText();//all
		String findfirst = firstname.getText();//data
		String findlast = lastname.getText();//from input
		if (findemail != null)
			temp = contacts.findPerson_Em(findemail);
		if (findtel != null)
			temp = contacts.findPerson_Ph(findtel);
		if (findfirst != null && findlast != null)
			temp = contacts.findPerson_Nm(findfirst, findlast);
		if(temp == null) JOptionPane.showMessageDialog(null, "Person not found");
		return temp;
	}

	private void fillPerson(Person target) {
		lastname.setText(target.getLastName());
		firstname.setText(target.getFirstName());
		tel.setText(target.getPhone());
		address.setText(target.getAddress());
		email.setText(target.getEmail());
	}
	// END
}

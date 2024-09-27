package Week1;

import java.time.Instant;
import java.util.Date;

public class Document {

	private int ID;
	private String name;
	private String author;
	private Date startDate;
	private int cost;
	private Student studentBorrow;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public Student getStudentBorrow() {
		return studentBorrow;
	}
	public void setStudentBorrow(Student studentBorrow) {
		this.studentBorrow = studentBorrow;
	}
	/*public static Date getTime(Document BookA) {
		Date currentDate = new Date();
		currentDate.setYear(2024);
		currentDate.setMonth(9);
		currentDate.setDate(27);
		return currentDate.getTimezoneOffset;
		
	}*/
	
	
}

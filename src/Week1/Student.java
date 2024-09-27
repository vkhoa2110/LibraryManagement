package Week1;

import java.util.Date;

public class Student {

	private int ID;
	private String name;
	private Date birthday;
	private String department;
	private String majoy;
	private Boolean isReturnBook;
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
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMajoy() {
		return majoy;
	}
	public void setMajoy(String majoy) {
		this.majoy = majoy;
	}
	public Boolean getIsReturnBook() {
		return isReturnBook;
	}
	public void setIsReturnBook(Boolean isReturnBook) {
		this.isReturnBook = isReturnBook;
	}
	
	
}

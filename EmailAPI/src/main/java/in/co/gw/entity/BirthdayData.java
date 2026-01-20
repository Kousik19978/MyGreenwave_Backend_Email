package in.co.gw.entity;

public class BirthdayData {

	private String employeeId;
	private String userId;
	private String userName;
	private String dateofBirth;
	public BirthdayData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BirthdayData(String employeeId, String userId, String userName, String dateofBirth) {
		super();
		this.employeeId = employeeId;
		this.userId = userId;
		this.userName = userName;
		this.dateofBirth = dateofBirth;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDateofBirth() {
		return dateofBirth;
	}
	public void setDateofBirth(String dateofBirth) {
		this.dateofBirth = dateofBirth;
	}
	@Override
	public String toString() {
		return "BirthdayEmailData [employeeId=" + employeeId + ", userId=" + userId + ", userName=" + userName
				+ ", dateofBirth=" + dateofBirth + "]";
	}
	
	
	
}

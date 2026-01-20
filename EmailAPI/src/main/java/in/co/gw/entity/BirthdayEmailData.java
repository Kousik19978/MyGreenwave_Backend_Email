package in.co.gw.entity;

public class BirthdayEmailData {

	private String category;
	private String addressingTo;
	private String cc;
	private String bcc;
	private String subject;
	private String greeting;
	private String userName;
	private String bodyMessage;
	private String profilePictureURL;
	private String wishSrc;
	private String regards;
	private String signature;
	private String congratulationGif;
	
	public BirthdayEmailData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BirthdayEmailData(String category, String addressingTo, String cc, String bcc, String subject,
			String greeting, String userName, String bodyMessage, String regards, String signature) {
		super();
		this.category = category;
		this.addressingTo = addressingTo;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.greeting = greeting;
		this.userName = userName;
		this.bodyMessage = bodyMessage;
		this.regards = regards;
		this.signature = signature;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAddressingTo() {
		return addressingTo;
	}
	public void setAddressingTo(String addressingTo) {
		this.addressingTo = addressingTo;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBodyMessage() {
		return bodyMessage;
	}
	public void setBodyMessage(String bodyMessage) {
		this.bodyMessage = bodyMessage;
	}
	public String getRegards() {
		return regards;
	}
	public void setRegards(String regards) {
		this.regards = regards;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "BirthdayEmailData [category=" + category + ", addressingTo=" + addressingTo + ", cc=" + cc + ", bcc="
				+ bcc + ", subject=" + subject + ", greeting=" + greeting + ", userName=" + userName + ", bodyMessage="
				+ bodyMessage + ", regards=" + regards + ", signature=" + signature + "]";
	}
	public String getProfilePictureURL() {
		return profilePictureURL;
	}
	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}
	public String getWishSrc() {
		return wishSrc;
	}
	public void setWishSrc(String wishSrc) {
		this.wishSrc = wishSrc;
	}
	
	public String getCongratulationGif() {
		return congratulationGif;
	}
	public void setCongratulationGif(String congratulationGif) {
		this.congratulationGif = congratulationGif;
	}
	
	
}

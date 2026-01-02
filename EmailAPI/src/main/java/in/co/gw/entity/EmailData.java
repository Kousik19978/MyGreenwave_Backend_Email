package in.co.gw.entity;

public class EmailData {
	
	private String entryTime;
	private String category;
	private String addressingTo;
	private String cc;
	private String bcc;
	private String subject;
	private String body;
	private String send;
	
	public EmailData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public EmailData(String entryTime, String category,String addressingTo, String cc, String bcc, String subject, String body,
			String send) {
		super();
		this.entryTime = entryTime;
		this.category=category;
		this.addressingTo = addressingTo;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.body = body;
		this.send = send;
	}
	public EmailData( String category,String addressingTo, String cc, String bcc, String subject, String body,
			String send) {
		super();
	
		this.category=category;
		this.addressingTo = addressingTo;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.body = body;
		this.send = send;
	}
	
	public String getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSend() {
		return send;
	}
	public void setSend(String send) {
		this.send = send;
	}

	@Override
	public String toString() {
		return "EmailData [entryTime=" + entryTime + ", category=" + category + ", addressingTo=" + addressingTo
				+ ", cc=" + cc + ", bcc=" + bcc + ", subject=" + subject + ", body=" + body + ", send=" + send + "]";
	}

	


	
}

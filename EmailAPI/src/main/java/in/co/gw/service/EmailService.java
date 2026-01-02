package in.co.gw.service;

import java.util.List;

import in.co.gw.entity.EmailData;

public interface EmailService {
	public List<EmailData> getSentEmail();
	public String sendEmail( EmailData emailData);
	public String sentEmailUpdateStatus(String entryTime,String category,String addressingTo,String subject);
}

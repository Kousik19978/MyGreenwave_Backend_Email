package in.co.gw.service.implementation;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import in.co.gw.dao.EmailDao;
import in.co.gw.entity.EmailData;
import in.co.gw.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private EmailDao  dao;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${mail.from.employeeportal}")
	private String employeeportal;

	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public List<EmailData> getSentEmail() {
		List<EmailData> empList;
		empList=dao.getSentEmail();
		
		int i=1;
		for(EmailData ed: empList) {
			//System.out.println(ed);
			String response = sendEmail(ed);
			if(response.equalsIgnoreCase("Mail Sent Successfully")) {
				sentEmailUpdateStatus(ed.getEntryTime(),ed.getCategory(),ed.getAddressingTo(),ed.getSubject());
				
				
				//return empList;
				System.out.println(i);
				i++;
			}

		}
		return empList;
	}

	@Override
	public String sendEmail( EmailData emailData) {



		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

			// Set sender
			helper.setFrom(employeeportal);
			/*
			// Split comma-separated email list and send to all
			String[] recipients = emailData.getAddressingTo().split("\\s*,\\s*");
			helper.setTo(recipients);

			// Set CC recipients
			if (emailData.getCc() != null && emailData.getCc()!="null" && !emailData.getCc().trim().isEmpty()) {
				String[] ccRecipients = emailData.getCc().split("\\s*,\\s*");
				helper.setCc(ccRecipients);
			}

			// Set BCC recipients
			if (emailData.getBcc() != null && emailData.getBcc()!="null" &&!emailData.getBcc().trim().isEmpty()) {
				String[] bccRecipients = emailData.getBcc().split("\\s*,\\s*");
				helper.setBcc(bccRecipients);
			}

			//helper.setTo(emailList.toArray(new String[0]));
			*/
			
			 // To
	        String[] recipients = Arrays.stream(emailData.getAddressingTo().split(","))
	                                    .map(String::trim)
	                                    .toArray(String[]::new);
	        helper.setTo(recipients);

	        // CC
	        if (emailData.getCc() != null && !"null".equalsIgnoreCase(emailData.getCc().trim()) && !emailData.getCc().trim().isEmpty()) {
	            String[] ccRecipients = Arrays.stream(emailData.getCc().split(","))
	                                          .map(String::trim)
	                                          .toArray(String[]::new);
	            helper.setCc(ccRecipients);
	        }

	        // BCC
	        if (emailData.getBcc() != null && !"null".equalsIgnoreCase(emailData.getBcc().trim()) && !emailData.getBcc().trim().isEmpty()) {
	            String[] bccRecipients = Arrays.stream(emailData.getBcc().split(","))
	                                           .map(String::trim)
	                                           .toArray(String[]::new);
	            helper.setBcc(bccRecipients);
	        }
			// Set subject and HTML body
			helper.setSubject(emailData.getSubject());
			helper.setText(emailData.getBody(), true); // true = HTML

			// Send email
			mailSender.send(mimeMessage);

			return "Mail Sent Successfully";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Failed to send mail: " + e.getMessage();
		}
	}

	public String sentEmailUpdateStatus(String entryTime,String category,String addressingTo,String subject) {
		int count=dao.sentEmailUpdateStatus(entryTime,category,addressingTo,subject);
		if(count>0) {
			return "Status Updated Successfully";
		}
		return null;

	}





}

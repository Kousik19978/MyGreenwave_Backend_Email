package in.co.gw.dao;

import java.util.List;

import in.co.gw.entity.EmailData;

public interface EmailDao {
	public List<EmailData> getSentEmail();

	public int sentEmailUpdateStatus(String entryTime, String category, String addressingTo, String subject);

}

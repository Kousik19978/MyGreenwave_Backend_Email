package in.co.gw.dao;

import java.util.List;

import in.co.gw.entity.BirthdayData;



public interface BirthdayMailDao {

	public List<BirthdayData> getbirthday();
	public int emailSaveData(String category,String addressingTo,String cc,String bcc,String mailBody,String subject,String send);
}

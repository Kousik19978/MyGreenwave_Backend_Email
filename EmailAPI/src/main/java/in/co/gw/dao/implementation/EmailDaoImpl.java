package in.co.gw.dao.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.co.gw.dao.EmailDao;
import in.co.gw.entity.EmailData;
import in.co.gw.utility.Security;


@Repository
public class EmailDaoImpl implements EmailDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	

	@Override
	public List<EmailData> getSentEmail() {
		String query = "SELECT top 50 [entryTime] ,[category] ,[addressingTo] ,[cc] ,[bcc] ,[subject] ,[body] ,[sent] FROM [dbo].[EMail] where [sent] =?";
		Security security = new Security();
		security.generateKey();

		String zero = "0";
		try {
			zero = security.encrypt(String.valueOf("0"));
		} catch (Exception e) {

			e.printStackTrace();
		}
		List<EmailData> emailDataDatalist = jdbcTemplate.query(query, (rs, rowNum) -> {

			EmailData emailData = new EmailData();
			if (rs != null) {

				try {
					emailData = new EmailData(
							rs.getString("entryTime"),						
							security.decrypt(rs.getString("category")),
							security.decrypt(rs.getString("addressingTo")),
							security.decrypt(rs.getString("cc")),
							security.decrypt(rs.getString("bcc")),
							security.decrypt(rs.getString("subject")),
							security.decrypt(rs.getString("body")),
							security.decrypt(rs.getString("sent"))
							);


				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			return emailData;
		}, zero);

		return emailDataDatalist;
	}

	@Override
	public int sentEmailUpdateStatus(String entryTime, String category, String addressingTo, String subject) {
		Security security = new Security();
		security.generateKey();
		String query=" update [dbo].[EMail] set [sent]=? Where [entryTime] =? and [category]=? and [addressingTo]=? and [subject]=?";
		int count=0;
		try {
			 count= jdbcTemplate.update(query,security.encrypt(String.valueOf("1")),entryTime,security.encrypt(String.valueOf(category)),
					security.encrypt(String.valueOf(addressingTo)),security.encrypt(String.valueOf(subject)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	



}

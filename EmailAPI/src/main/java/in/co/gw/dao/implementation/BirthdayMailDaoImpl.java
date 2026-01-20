package in.co.gw.dao.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.co.gw.dao.BirthdayMailDao;
import in.co.gw.entity.BirthdayData;
import in.co.gw.utility.Security;

@Repository
public class BirthdayMailDaoImpl implements BirthdayMailDao{


	

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${user.db.name}")
	private String userDB;
	
	@Value("${mail.from.mailallow}")
	private boolean mailallow;
	

	@Override
	public List<BirthdayData> getbirthday() {

	String query = "select [employeeId], [UserId],[UserName],[DateofBirth] from( select [employeeId],[UserId],[UserName],coalesce(dob, (select [Date] FROM [dbo].[Birthdays] where email =  [UserId]))[DateofBirth] FROM ["+userDB+"].[dbo].[UserCredential] where Active =1) t where [DateofBirth] is not null and  datepart(day,[DateofBirth]) = datepart(day,getdate()) and datepart(month,[DateofBirth]) = datepart(month,getdate())";
	List<BirthdayData> todayBirthdayEmailList = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(BirthdayData.class));
	return todayBirthdayEmailList;

		
	}
	@Override
	public int emailSaveData(String category,String addressingTo,String cc,String bcc,String subject,String body,String send) {

		Security security = new Security();
		security.generateKey();
		int count=0;
		if(mailallow) {	
			String query = "INSERT INTO [dbo].[EMail] ([category],[addressingTo],[cc],[bcc],[subject],[body],[sent]) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?);";

			try {
				return jdbcTemplate.update(query,
						security.encrypt(String.valueOf(category)),
						security.encrypt(String.valueOf(addressingTo)),
						security.encrypt(String.valueOf( cc)),
						security.encrypt(String.valueOf( bcc)),
						security.encrypt(String.valueOf( subject)),
						security.encrypt(String.valueOf( body)),
						security.encrypt(String.valueOf(  send))

						);
			} catch (DataAccessException e) {

				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count;
	}

}

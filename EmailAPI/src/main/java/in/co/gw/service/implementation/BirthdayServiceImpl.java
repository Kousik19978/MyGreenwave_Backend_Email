package in.co.gw.service.implementation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.co.gw.dao.BirthdayMailDao;
import in.co.gw.entity.BirthdayData;
import in.co.gw.entity.BirthdayEmailData;
import in.co.gw.service.BirthdayService;
import in.co.gw.utility.ImageData;
import java.util.Base64;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    @Autowired
    BirthdayMailDao birthdayMailDao;

    private static final Logger logger = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    @Value("${profile.image-dir}")
    private String profilePicdir;

    @Value("${app.base-url:http://localhost:8083}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    private String getBirthdayWish(String userName) {
        try {
            UUID uuid = UUID.randomUUID();

            String prompt = "gimme random birthday wish between 10 to 15 words for employee with their first name, emp name is "
                    + userName
                    + ", don't use the word \"happy birthday\"";

            // String encodedPrompt = URLEncoder.encode(prompt, StandardCharsets.UTF_8);

            String url = "https://text.pollinations.ai/" + prompt + uuid.toString();

            return restTemplate.getForObject(url, String.class);

        } catch (Exception e) {
            return "Warm wishes for a bright and successful year ahead, " + userName + "!";

        }
    }

    @Override    
    @Scheduled(cron = "0 30 15 * * ?", zone = "Asia/Kolkata")
    public Map<String, String> saveBirthdayMail() throws IOException, java.io.IOException {

        List<BirthdayData> birthdayEmailDataList = birthdayMailDao.getbirthday();
        Map<String, String> map = new HashMap<String, String>();

        for (BirthdayData br : birthdayEmailDataList) {
            BirthdayEmailData birthdayEmailData = new BirthdayEmailData();
            birthdayEmailData.setCategory("BirthDay");
            birthdayEmailData.setAddressingTo(br.getUserId());
            birthdayEmailData.setSubject("Happy Birthday");
            birthdayEmailData.setGreeting(br.getUserName());
            birthdayEmailData.setUserName(br.getUserName());

            // Set body Message
            birthdayEmailData.setBodyMessage(getBirthdayWish(br.getUserName()));

            // For Profile Picture
            String imageName = br.getEmployeeId() + br.getUserName();
            System.out.println(imageName + "imageName");
            logger.info(imageName);

            ImageData img = getProfilePicture(imageName);

            String base64Image = Base64.getEncoder()
            .encodeToString(img.getData());

            String imageSrc = "data:" + img.getContentType() + ";base64," + base64Image;
            birthdayEmailData.setProfilePictureURL(imageSrc);

            //----

            //For mail gif file

            byte[] bytes = Files.readAllBytes(Paths.get(profilePicdir+"WISH_SRC.png"));
            String base64 = Base64.getEncoder().encodeToString(bytes);

            String wishSrc = "data:image/png;base64," + base64;
            birthdayEmailData.setWishSrc(wishSrc);

            //Cog gif
            byte[] gifBytes = Files.readAllBytes(Paths.get(profilePicdir +
            "CONG_GIF.gif"));
            String gifBase64 = Base64.getEncoder().encodeToString(gifBytes);

            String gifSrc = "data:image/gif;base64," + gifBase64;
            birthdayEmailData.setCongratulationGif(gifSrc);
            // -----
            
            birthdayEmailData.setRegards("Regards");
            birthdayEmailData.setSignature("My Greenwave");

            saveBirthdayDB(birthdayEmailData);

            map.put(br.getUserId(), br.getDateofBirth());
        }

        System.out.println(birthdayEmailDataList);
        return map;
    }
    @Override    
    @Scheduled(cron = "0 30 15 * * ?", zone = "Asia/Kolkata")
    public Map<String, String> saveBirthdayMailv2() throws IOException, java.io.IOException {

        List<BirthdayData> birthdayEmailDataList = birthdayMailDao.getbirthday();
        Map<String, String> map = new HashMap<String, String>();

        for (BirthdayData br : birthdayEmailDataList) {
            BirthdayEmailData birthdayEmailData = new BirthdayEmailData();
            birthdayEmailData.setCategory("BirthDay");
            birthdayEmailData.setAddressingTo(br.getUserId());
            birthdayEmailData.setSubject("Happy Birthday");
            birthdayEmailData.setGreeting(br.getUserName());
            birthdayEmailData.setUserName(br.getUserName());

            // Set body Message
            birthdayEmailData.setBodyMessage(getBirthdayWish(br.getUserName()));

            // For Profile Picture
            String imageName = br.getEmployeeId() + br.getUserName();
            String actualImageName = findImageWithExtension(imageName);
            System.out.println("Imagename : " + imageName);
            System.out.println("Actualname : " + actualImageName);
            logger.info(imageName);            

            System.out.println("baseUEL : " + baseUrl);

            String profilePictureURL = baseUrl + "/images/" + actualImageName;
            String congWishURL = baseUrl + "/images/" + "CONG_GIF.gif";
            String wishImageURL = baseUrl + "/images/" + "WISH_SRC.png";

            birthdayEmailData.setProfilePictureURL(profilePictureURL);
            birthdayEmailData.setCongratulationGif(congWishURL);
            birthdayEmailData.setWishSrc(wishImageURL);

            birthdayEmailData.setRegards("Regards");
            birthdayEmailData.setSignature("My Greenwave");

            saveBirthdayDB(birthdayEmailData);

            map.put(br.getUserId(), br.getDateofBirth());
        }

        System.out.println(birthdayEmailDataList);
        return map;
    }

    private String findImageWithExtension(String imageName) {
        Path directory = Paths.get(profilePicdir);
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, imageName + ".*")) {
            for (Path file : stream) {
                String contentType = Files.probeContentType(file);
                if (contentType != null && contentType.startsWith("image")) {
                    return file.getFileName().toString();
                }
            }
        } catch (IOException e) {
            logger.error("Error finding image: " + e.getMessage());
        }
    
        // Fallback to default image
        return "default.jpg"; // or whatever your default image name is
    }


<<<<<<< HEAD
    public ImageData getProfilePicture(String imageName) throws IOException {
        Path directory = Paths.get(profilePicdir);
=======
	@Override
	//@Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Kolkata")
	//@Scheduled(cron = "0 30 15 * * ?", zone = "Asia/Kolkata")
	public Map<String, String> saveBirthdayMail()  throws IOException, java.io.IOException{
		
		List<BirthdayData> birthdayEmailDataList =birthdayMailDao.getbirthday();
		Map<String, String> map = new HashMap<String, String>();
		
		for(BirthdayData br : birthdayEmailDataList) {
			BirthdayEmailData birthdayEmailData = new BirthdayEmailData();
			birthdayEmailData.setCategory("BirthDay");
			birthdayEmailData.setAddressingTo(br.getUserId());
			birthdayEmailData.setSubject("Happy Birthday");
			birthdayEmailData.setGreeting(br.getUserName());
			birthdayEmailData.setUserName(br.getUserName());
			
			
			//Set body Message
			birthdayEmailData.setBodyMessage(getBirthdayWish(br.getUserName()));
			
			//For Profile Picture
			String imageName=br.getEmployeeId()+br.getUserName();
			System.out.println(imageName+"imageName");
			logger.info(imageName);
			
			ImageData img = getProfilePicture(imageName);
>>>>>>> 9851860dc4ec8b896f406396a0a596eb18ba1e05

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new FileNotFoundException("Image directory not found.");
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, imageName + ".*")) {
            for (Path file : stream) {
                String contentType = Files.probeContentType(file);
                if (contentType != null && contentType.startsWith("image")) {
                    byte[] imageBytes = Files.readAllBytes(file);
                    return new ImageData(imageBytes, contentType);
                }
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "JohnDoe" + ".*")) {
            for (Path file : stream) {
                String contentType = Files.probeContentType(file);
                if (contentType != null && contentType.startsWith("image")) {
                    byte[] imageBytes = Files.readAllBytes(file);
                    return new ImageData(imageBytes, contentType);
                }
            }
        }
        return null;

    }

    public String saveBirthdayDB(BirthdayEmailData birthdayEmailData) {
        // emailDaoImpl=daoFactory.getEmailDaoImpl();

        String mailBody = getbirthdayTemplate(birthdayEmailData);
        // System.out.println(mailBody);
        int count = birthdayMailDao.emailSaveData(birthdayEmailData.getCategory(), birthdayEmailData.getAddressingTo(),
                birthdayEmailData.getCc(),
                birthdayEmailData.getBcc(), birthdayEmailData.getSubject(), mailBody, "0");
        if (count > 0) {
            return "BirthDay mail info saved for " + birthdayEmailData.getUserName();
        }

        return null;

    }

    public String getbirthdayTemplate(BirthdayEmailData birthdayEmailData) {

        String mailBody = """
                				<!doctype html>
                <html lang="en">

                <head>
                    <meta charset="UTF-8" />
                    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <title>Happy Birthday, {{}}</title>
                </head>

                <body style="
                      margin: 0;
                      padding: 0;
                      width: 100%;
                      height: 100%;
                      font-family: Arial, Helvetica, sans-serif;
                      ">

                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="">
                        <tr>
                            <td align="center" style="padding: 30px 15px">
                                <table border="0" cellpadding="0" cellspacing="0" width="850" style="
                                    max-width: 1350px;
                                    width: 100%;
                                    background-color: #ffffff;
                                    border-radius: 20px;
                                    overflow: hidden;
                                ">
                                    <tr>
                                        <td width="320" valign="middle" style="
                                     background-color: #ebf2fa;
                                     padding: 20px 25px;
                                     text-align: center;
                                     border-right: 1px solid #e0e6ed;
                                     position: relative;
                                     ">
                                            <div style="
                                        display: flex;
                                        flex-direction: column;
                                        align-items: center;
                                        justify-content: center;
                                        gap: 150px;
                                        height: 100%;
                                        ">
                                                <div style="flex-shrink: 0">
                                                    <table align="center" width="100%" cellpadding="0" cellspacing="0" border="0">
                                                        <tr>
                                                            <td align="center">
                                                                <img src="{{CONG_GIF}}" alt="" width="150" height="150" style="display:block;">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <div style="
                                              width: 160px;
                                              height: 160px;
                                              border-radius: 50%;
                                              overflow: hidden;
                                              border: 3px solid #ffffff;
                                              ">
                                                    <img src="{{PROFILE_IMAGE}}"
                                                        alt="PROFILE_IMAGE" style="
                                                    width: 160px;
                                                    height: 160px;
                                                    display: block;
                                                    border-radius: 50%;
                                                    object-fit: fit;
                                                    " />

                                                    </div>
                                                </div>

                                                <div style="flex-shrink: 0">
                                                    <img src="{{WISH_SRC}}" alt="Birthday Celebration" style="
                                              width: 300px;
                                              height: auto;
                                              max-height: 205px;
                                              display: block;
                                              " />
                                                </div>
                                            </div>
                                        </td>
                                        <td width="530" valign="top" style="width: 530px; padding: 25px 45px;">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td align="center" style="padding-bottom: 15px">
                                                        <h1 style="
                                                 margin: 0;
                                                 font-size: 42px;
                                                 font-weight: 700;
                                                 color: #144081;
                                                 font-family: Arial, Helvetica, sans-serif;;
                                                 letter-spacing: 2px;
                                                 line-height: 1.2;
                                                 ">
                                                            HAPPY BIRTHDAY!
                                                        </h1>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td style="padding-bottom: 15px">
                                                        <h2 style="
                                                 margin: 0;
                                                 color: #2c3e50;
                                                 font-size: 24px;
                                                 font-weight: 700;
                                                 text-align: center;
                                                 font-family: Arial, Helvetica, sans-serif;
                                                 font-style: italic;
                                                 ">
                                                            Dear {{NAME}},
                                                        </h2>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td style="
                                              padding-bottom: 18px;
                                              color: #5a6c7d;
                                              font-size: 13px;
                                              line-height: 1.7;
                                              font-weight: 400;
                                              ">
                                                        <p style="
                                                 margin: 0 0 15px 0;
                                                 text-align: justify;
                                                 font-family: Arial, Helvetica, sans-serif;
                                                 ">
                                                            🎊 Warmest birthday wishes to you! On this special day,
                                                            we celebrate not just another year of your remarkable
                                                            journey, but the incredible person you are and all the
                                                            wonderful moments, dedication, and positivity you bring
                                                            to our team every single day.
                                                        </p>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td style="padding: 18px 0">
                                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                            <tr>
                                                                <td style="
                                                       background-color: #F4F6F8;
                                                       border-left: 5px solid #144081;
                                                       border-right: 5px solid #A3B8D9;
                                                       border-radius: 12px;
                                                       padding: 20px;
                                                       ">
                                                                    <p style="
                                                          margin: 0 0 12px 0;
                                                          color: #2C3E50;
                                                          font-size: 16px;
                                                          font-style: italic;
                                                          text-align: center;
                                                          line-height: 1.6;
                                                          font-weight: 600;
                                                          font-family: Arial, Helvetica, sans-serif;
                                                          ">
                                                                        “{{MESSAGE}}
                                                                        <strong>Happy Birthday!</strong>”
                                                                    </p>
                                                                    <p style="
                                                          margin: 0;
                                                          text-align: center;
                                                          font-size: 22px;
                                                          line-height: 1;
                                                          ">
                                                                        <span style="margin: 0 4px;">💝</span>
                                                                        <span style="margin: 0 4px;">🎁</span>
                                                                        <span style="margin: 0 4px;">💝</span>
                                                                    </p>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <td align="center" style="padding: 18px 0 15px 0">
                                                                    <table border="0" cellpadding="0" cellspacing="0">
                                                                        <tr>
                                                                            <td bgcolor="#144081" style="border-radius: 30px; box-shadow: 0 4px 20px rgba(20, 64, 129, 0.4);">
                                                                                <a href="https://www.linkedin.com/" target="_blank" style="display: inline-block;
                                                                                    padding: 14px 36px;
                                                                                    font-family: Arial, Helvetica, sans-serif;
                                                                                    font-size: 16px;
                                                                                    font-weight: 700;
                                                                                    color: #144081;
                                                                                    text-decoration: none;
                                                                                    border-radius: 30px;
                                                                                    background-color: #FFFFFF;
                                                                                    border: 2px solid #144081;
                                                                                ">
                                                                                   Let’s Celebrate
                                                                                </a>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>

                                                            <tr>
                                                                <td align="center" style="padding-top: 15px">
                                                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                                        <tr>
                                                                            <td style="
                                                                border-top: 2px dashed #144081;
                                                                padding-top: 15px;
                                                                ">
                                                                                <p style="
                                                                   margin: 0;
                                                                   text-align: center;
                                                                   font-size: 28px;
                                                                   line-height: 1;
                                                                   ">
                                                                                    <span style="margin: 0 6px">🎉</span>
                                                                                    <span style="margin: 0 6px">🎊</span>
                                                                                    <span style="margin: 0 6px">🥳</span>
                                                                                </p>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>

                                            </table>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" style="
                                     background-color: #144081;
                                     padding: 20px 25px;
                                     text-align: center;
                                     border-top: 1px solid #DCE3EA;
                                     ">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td align="center">
                                                        <p style="
                                                 margin: 0 0 6px 0;
                                                 color: #fff;
                                                 font-size: 14px;
                                                 line-height: 1.6;
                                                 font-family: Arial, Helvetica, sans-serif;
                                                 ">
                                                            With warm wishes and heartfelt appreciation,
                                                        </p>
                                                        <p style="
                                                 margin: 0;
                                                 color: #fff;
                                                 font-size: 15px;
                                                 font-weight: 700;
                                                 font-family: Arial, Helvetica, sans-serif;
                                                 ">
                                                            My Greenwave
                                                        </p>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>

                </body>

                </html>

                				""";
        // formatted(birthdayEmailData.getProfilePictureURL(),birthdayEmailData.getUserName(),birthdayEmailData.getBodyMessage());

        // return mailBody;

        return mailBody
                .replace("{{NAME}}", birthdayEmailData.getUserName())
                .replace("{{PROFILE_IMAGE}}", birthdayEmailData.getProfilePictureURL())
                // .replace("{{PROFILE_IMAGE}}",
                //         "https://media.istockphoto.com/id/1682296067/photo/happy-studio-portrait-or-professional-man-real-estate-agent-or-asian-businessman-smile-for.jpg?s=2048x2048&w=is&k=20&c=4PPfwN_6yoEHwLeYK3S2Pi-Ck6JYEFLvUIrGpAHqpeQ=")
                .replace("{{WISH_SRC}}", birthdayEmailData.getWishSrc())
                .replace("{{MESSAGE}}", birthdayEmailData.getBodyMessage())
                .replace("{{CONG_GIF}}", birthdayEmailData.getCongratulationGif());

        // return mailBody
        // .replace("{{NAME}}", "SUPRIYO DHARA")
        // .replace("{{PROFILE_IMAGE}}",
        // "https://plus.unsplash.com/premium_photo-1689568126014-06fea9d5d341?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
        // .replace("{{WISH_SRC}}",
        // "https://www.picgifs.com/glitter-gifs/h/happy-birthday/picgifs-happy-birthday-401601.gif")
        // .replace("{{MESSAGE}}", birthdayEmailData.getBodyMessage())
        // .replace("{{CONG_GIF}}",
        // "https://www.picgifs.com/glitter-gifs/h/happy-birthday/picgifs-happy-birthday-401601.gif");

    }

    // String mailBody ="";
    // return mailBody;
    // }

}

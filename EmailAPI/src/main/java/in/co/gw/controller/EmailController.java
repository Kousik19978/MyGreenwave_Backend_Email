package in.co.gw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.gw.entity.EmailData;
import in.co.gw.service.EmailService;

@RestController
@RequestMapping("/emails")
public class EmailController {
	
	@Autowired
	EmailService emailService;
	
	@GetMapping("/getsentmail")
    public ResponseEntity<?>  getSentEmail() {
		
       List<EmailData> response= emailService.getSentEmail();
       
        if (response != null) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email  Faild");
		}
	
    }
	
	

}

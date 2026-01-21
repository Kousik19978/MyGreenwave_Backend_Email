package in.co.gw.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.gw.service.BirthdayService;
import in.co.gw.utility.ImageData;



@RestController
@RequestMapping("/birthday")
public class Birthday {
	
	@Autowired
	BirthdayService birthdayService;
	
	@GetMapping
	public ResponseEntity<?> saveBirthDayForMail() throws IOException, java.io.IOException{
		
		Map<String,String> response=birthdayService.saveBirthdayMail();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
//	@GetMapping("/profile-image/{empId}")
//	public ResponseEntity<byte[]> getProfileImage(@PathVariable String empId)
//	        throws IOException {
//
//	    ImageData img = getProfilePicture(empId);
//
//	    return ResponseEntity.ok()
//	            .contentType(MediaType.parseMediaType(img.getContentType()))
//	            .body(img.getData());
//	}

}

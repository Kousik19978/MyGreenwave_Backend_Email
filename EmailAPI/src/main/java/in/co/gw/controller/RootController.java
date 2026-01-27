package in.co.gw.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
// @RequestMapping("/")
public class RootController {

    @GetMapping("/")
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.status(200).body(Map.of("health", "Email API is Running"));
    }
    
}

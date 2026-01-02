package in.co.gw.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
public class Security {

	private String keyString;
	
	public  String encrypt(String password) throws Exception {
		byte key[] = Base64.getDecoder().decode(keyString);
		Key aesKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	public  String decrypt(String encryptedString) throws Exception {
		byte key[] = Base64.getDecoder().decode(keyString);
		byte[] encrypted = Base64.getDecoder().decode(encryptedString);
		Key aesKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		byte[] decrypted = cipher.doFinal(encrypted);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

	public String generateKey() {
		
		byte[] tempKey = null;
		
		try {
			InetAddress ip = InetAddress.getLocalHost();
			//System.out.println("Current IP address : " + ip.getHostAddress());
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();

			//System.out.println(mac.toString());
            String[] hexadecimal = new String[mac.length];
            for (int i = 0; i < mac.length; i++) {
                hexadecimal[i] = String.format("%02X", mac[i]);
            }
            String macAddress = String.join("-", hexadecimal);
            
            macAddress="B4-7A-F1-33-85-A5";
            
            //System.out.println("Mac Id is : "+macAddress);
            String salt = "ESS Portal Of Greenwave Solutions Pvt. Ltd....................";
            String newKey = macAddress + salt;
			//System.out.println(mac);
			tempKey = newKey.substring(0 , 16).getBytes();

		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		
		keyString = Base64.getEncoder().encodeToString(tempKey);
		return Base64.getEncoder().encodeToString(tempKey);
	}
	
	

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}
}

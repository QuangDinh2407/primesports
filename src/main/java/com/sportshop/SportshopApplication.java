package com.sportshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class SportshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportshopApplication.class, args);
	}

	/*@Override
	public void run(String... args) throws Exception {
		String url = "http://localhost:8081/home";

		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			// Mở Chrome hoặc Edge trên Windows
			Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start chrome " + url});
			// Nếu muốn mở bằng Edge:
			// Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start msedge " + url});
		} else if (os.contains("mac")) {
			// Mở Chrome trên macOS
			Runtime.getRuntime().exec(new String[]{"open", "-a", "Google Chrome", url});
		} else if (os.contains("nix") || os.contains("nux")) {
			// Mở Chrome trên Linux
			Runtime.getRuntime().exec(new String[]{"google-chrome", url});
		} else {
			System.out.println("Không thể mở trình duyệt tự động. Vui lòng mở thủ công tại: " + url);
		}
	}*/

}

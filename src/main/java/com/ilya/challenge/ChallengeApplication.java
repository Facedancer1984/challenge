package com.ilya.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;


@SpringBootApplication
public class ChallengeApplication {

	public static int coresCount = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) {
		System.out.println("There are " + coresCount + " cores on this PC");
		//SpringApplication.run(ChallengeApplication.class, args);
		for(int core = 1;core<=coresCount;core++){
			Thread thread = new Thread(new SearchProcess(core));
			thread.start();
		}
	}
}

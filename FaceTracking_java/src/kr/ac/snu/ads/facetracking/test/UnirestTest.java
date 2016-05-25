package kr.ac.snu.ads.facetracking.test;
import static org.junit.Assert.*;

import org.junit.Test;
import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UnirestTest {

	@Test
	public void test() {
		try {
			HttpResponse<String> request = Unirest.get("https://yoda.p.mashape.com/yoda?sentence=You%20will%20learn%20how%20to%20speak%20like%20me%20someday.%20%20Oh%20wait.")
					.header("X-Mashape-Authorization", "<Insert your Mashape key here>")
					.asString();
			System.out.println(request.getStatus());
			assertEquals(200, request.getStatus());
		} catch (UnirestException ex) {
			fail(ex.getMessage());
		}


 	}

}

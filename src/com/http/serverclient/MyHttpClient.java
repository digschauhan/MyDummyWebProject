package com.http.serverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpClient {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://localhost:4567");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			String test = "<name>Hello</name>";
			byte[] bytes = test.getBytes();
			con.setRequestProperty("Content-length", String.valueOf(bytes.length));
			con.setRequestProperty("Content-type", "text/html");
			OutputStream out = con.getOutputStream();
			out.write(bytes);
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String temp;
			while ((temp = in.readLine()) != null){
				System.out.println(temp);
			}
			out.close();
			in.close();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}

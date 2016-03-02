package com.http.serverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class MyHttpServer {

	
	public void startServer(){
		try {
			ServerSocket serverSocket = new ServerSocket(4567);
			System.out.println("HTTP Server (only POST implemented) is ready and is listening on Port Number 4567 \n");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println(clientSocket.getInetAddress().toString() + " " + clientSocket.getPort());
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				OutputStream out = clientSocket.getOutputStream();
				StringBuffer req=new StringBuffer();
				String temp=".";
				
				//String response = "HTTP/1.1 200 OK\n\r";
				//response = response + "Date: Fri, 04 May 2001 20:08:11 GMT\n\r";
				//response = response + "1";
				//req.append(response);
				int i=1;
				
				/*while ((temp = in.readLine()) != null){
					req.append(temp);
					System.out.println("Ln : " + i++ + " : " + temp);
				}*/
				 while (temp != null && !temp.equals("")){
			          temp = in.readLine();
			          if(temp!=null){
						req.append(temp);
						System.out.println(temp);
			          }
				 }

				 
				System.out.println("XXXXXX out of the loop ......");
				
				System.out.println("XXXX Request : " + req.toString());
				
				byte[] bytes = req.toString().getBytes();
				out.write(bytes);
				out.flush();
				in.close();
				out.close();
				//serverSocket.close();
				
				String response = "{"+
						"\"pusher\":{" +
							"\"name\":\"digschauhan\" , " + 
							"\"email\":\"digs.chauhan@gmail.com\"  " +
						//	"\"username\":\"digschauhan\", \"password\":\"Digs@1234\", \"X-GitHub-Event\":\"push\" ," +
						//	"\"X-GitHub-Delivery\":\"c093c600-e09f-11e5-83c8-d6a64f9eaeef\"" +
						 "},"+
						 "\"repository\":{" +
						 	"\"name\":\"myworking\" , " +
						 	"\"url\":\"https://github.com/digschauhan/MyDummyWebProject\"  " +
						 "}"+
					"}";
			System.out.println("JSON : " + response +"\n");
			sendHookup(response);
				//sendHookup(req.toString());
			}
		} catch (Exception e) {
			System.out.println("ERROR Starting Server : " + e.getMessage());
			
		}
		
	}
	
	public void sendHookup(String response){
		try {
			System.out.println("XXXXX Sending POST request to Jenkins ...");
			
			URL url = new URL("http://localhost:8080/github-webhook/");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			//con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			
			//String test = "<name>Hello</name>";
			byte[] bytes = response.getBytes();
			con.setRequestProperty("Content-length", String.valueOf(bytes.length));
			con.setRequestProperty("Content-type", "application/json");
			
			con.setRequestProperty("User-Agent", "GitHub-Hookshot/7a65dd9");
			con.setRequestProperty("X-GitHub-Event", "push");
			con.setRequestProperty("X-GitHub-Delivery", "c093c600-e09f-11e5-83c8-d6a64f9eaeef");
			
			con.setRequestProperty("username", "digschauhan");
			con.setRequestProperty("password", "Digs@1234");
			
			//OutputStream out = con.getOutputStream();
			
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(response);
			//out.write(bytes);
			//out.flush();
			//out.close();
			writer.flush();
			writer.close();
			
			
			
			int responseCode = con.getResponseCode();
	        System.out.println("POST Response Code :: " + responseCode);
	 
	        if (responseCode == HttpURLConnection.HTTP_OK) { //success
	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String inputLine;
	            StringBuffer res = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                res.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	            System.out.println(response.toString());
	        } else {
	            System.out.println("POST request not worked");
	        }			
			
			con.disconnect();
			System.out.println("XXXXX Sent request to Jenkins ...");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	
	}

	public static void main(String[] args) {
		MyHttpServer server = new MyHttpServer();
		server.startServer();
	}

}

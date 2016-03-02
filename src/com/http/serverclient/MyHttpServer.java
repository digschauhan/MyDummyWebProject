package com.http.serverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
				
				String response = "HTTP/1.1 200 OK\n\r";
				response = response + "Date: Fri, 04 May 2001 20:08:11 GMT\n\r";
				response = response + "1";
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
						System.out.println("Ln : " + i++ + " : " + temp);
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
				
				sendHookup(req.toString());
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
			con.setDoInput(true);
			con.setRequestMethod("POST");
			con.setUseCaches(false);
			//String test = "<name>Hello</name>";
			byte[] bytes = response.getBytes();
			con.setRequestProperty("Content-length", String.valueOf(bytes.length));
			con.setRequestProperty("Content-type", "text/html");
			OutputStream out = con.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
			/*BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String temp;
			while ((temp = in.readLine()) != null){
				System.out.println(temp);
			}
			out.close();
			in.close();*/
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

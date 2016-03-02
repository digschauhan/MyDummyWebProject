package com.http.serverclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyHttpServer {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(4567);
			System.out.println("HTTP Server (only POST implemented) is ready and is listening on Port Number 4567 \n");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println(clientSocket.getInetAddress().toString() + " " + clientSocket.getPort());
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				OutputStream out = clientSocket.getOutputStream();
				StringBuffer req=new StringBuffer();
				String temp;
				
				String response = "HTTP/1.1 200 OK\n\r";
				response = response + "Date: Fri, 04 May 2001 20:08:11 GMT\n\r";
				response = response + "1";
				req.append(response);
				int i=1;
				while ((temp = in.readLine()) != null){
					req.append(temp);
					System.out.println("Ln : " + i++ + " : " + temp);
				}
				
				System.out.println("XXXX Request : " + req.toString());
				
				byte[] bytes = req.toString().getBytes();
				out.write(bytes);
				out.flush();
				in.close();
				out.close();
				
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			System.exit(1);
		}

	}

}

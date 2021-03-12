//Muhammed Murat Dilmaç 150116017 ---- Malik Türkoğlu 150116044

import java.io.*;
import java.net.*;

public class ProxyServer {

	public static void main(String[] args) throws IOException {

		
		System.out.println("Starting proxy server to 8080 on port 8888");//information 

		ServerSocket server = new ServerSocket(8888);// socket with proxy local port

		while (true) {

			ProxyThread proxyThread = new ProxyThread(server.accept()); // with incoming request accept it and create new Thread as ProxyThread
			proxyThread.start();//start it 
		}

	}
}

class ProxyThread extends Thread {

	final static String CarriageReturn = "\r\n"; // this value usually using http server, it helps us next line.
	private Socket proxySocket;

	ProxyThread(Socket proxySocket) {
		
		this.proxySocket = proxySocket;
		
	}

	public void run() {
		try {

			// communication field is byte its from 
			byte[] messageFromServer = new byte[65536];
			DataOutputStream outputProxy = new DataOutputStream(proxySocket.getOutputStream());
			
			//This socket is HTTP server socket with 8080 port
			Socket server = null;		
			// connects a socket to the server
			try {
				server = new Socket("localhost", 8080);// connected server port 8080
			} catch (IOException e) {
				
				String contentTypeHeader = "HTTP/1.0 400 Bad Request";
				String contentLengthHeader = "Content-Lenght:" + CarriageReturn;
				outputProxy.writeBytes(contentTypeHeader);
				outputProxy.writeBytes(contentLengthHeader);
				outputProxy.writeBytes(CarriageReturn);
				System.out.println("Couldnt Find Server.");
				throw new IOException("Couldnt Find Server.");
			}

			final InputStream inFromServer = server.getInputStream();// new thread server to client
			uploadToServerThread(server);// This method upload input to specified server with another Thread

			// Take messages from server to client
			try {
				int readBytes;
				// Here we are taking message from server and writing it back to screen.
				while ((readBytes = inFromServer.read(messageFromServer)) != -1) {
					outputProxy.write(messageFromServer, 0, readBytes);

				}
			} catch (IOException e) {

			}
			// after implementation we are closing the socket
			outputProxy.close();
			proxySocket.close();
		} catch (IOException e) {

		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void uploadToServerThread(Socket server) throws Throwable {// This method upload input to specified server

		final InputStream inFromClient = proxySocket.getInputStream();
		DataOutputStream outToServer = new DataOutputStream(server.getOutputStream());// new thread to upload to server

		new Thread() {
			public void run() {
				final byte[] request = new byte[65536];//request message will save here in byte and will send to server 
				int readBytes;
				try {
					// Here we are reading and sending it to http server.
					while ((readBytes = inFromClient.read(request)) != -1) {
						outToServer.write(request, 0, readBytes);//send to server
					}
				} catch (IOException e) {

				}
			}
		}.start();
	}
}

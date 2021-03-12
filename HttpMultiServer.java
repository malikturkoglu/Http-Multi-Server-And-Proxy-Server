//Malik Türkoğlu 150116044 ---- Muhammed Murat Dilmaç 150116017 
import java.io.*;
import java.net.*;
import java.util.*;


final class incomingHttpDemand implements Runnable {
	Socket socketForEachThread;                                // socket for each thread
	final static String CarriageReturn = "\r\n";   // this value usually using http server, it helps us next line.


	public incomingHttpDemand(Socket socketForThread) throws Exception {
		this.socketForEachThread = socketForThread;
	}
	public void run() {                            // run method comes from Runnable Class
		try {
			answerIncomingHttpRequest();
		} catch (Exception e) {    // it can be error any type, therefore we implement try catch
			System.out.println(e);
		}
	}

	private void answerIncomingHttpRequest() throws Exception {

		InputStream readIncomingMessage = socketForEachThread.getInputStream();
		DataOutputStream output = new DataOutputStream(socketForEachThread.getOutputStream());

		BufferedReader takeIncomingMessageInBuffer = new BufferedReader(new InputStreamReader(readIncomingMessage));


         String takeFirstLine = takeIncomingMessageInBuffer.readLine();



		System.out.println("******Message received******");
		System.out.println(takeFirstLine);    // gelen talebi bastırıyoruz.
		String headerLine = null;
		while ((headerLine = takeIncomingMessageInBuffer.readLine()).length() != 0) {
			System.out.println(headerLine);   // gelen tüm mesajı bastır
		}

		StringTokenizer tokens = new StringTokenizer(takeFirstLine);

		tokens.nextToken();
		String firstLineWhole = tokens.nextToken();


		String theUrlNumber = "";    // burada url ye bakıyoruz
		for (int i = 1; i < firstLineWhole.length(); i++) {
			theUrlNumber = theUrlNumber + firstLineWhole.charAt(i);
		}


		String contentTypeHeader = null;
		String contentLengthHeader = null;

		contentTypeHeader = "HTTP/1.0 200 OK" + CarriageReturn;          // change after these
		contentLengthHeader = "Content-Lenght:" + CarriageReturn;

		String checkIsItGet = takeFirstLine;
		boolean isItGet = checkIsItGet.indexOf("GET") != -1 ? true : false;

		Boolean everthingOk = false;
		String totalA = "";
		if (isItGet) {                                               // if is it get, inside in, otherwise go else
			boolean givenUrlChecker = isNumeric(theUrlNumber);
			if (!givenUrlChecker) {                                    // if the url is not number, go inside
				contentTypeHeader = "HTTP/1.0 400 Bad Request";
				totalA = "Your URL is not digit. Please enter again [error code 400]";
			} else {            // if url is number
				int printA = Integer.parseInt(theUrlNumber);
				if (printA < 100 || printA > 20000) {        // check limits
					contentTypeHeader = "HTTP/1.0 400 Bad Request";
					totalA = "Your URL has to be more than 100 and less than 10.000 [error code 400]";
				} else {
					for (int i = 0; i < printA - 83; i++)
						totalA = totalA + "a";
					everthingOk = true;
					contentLengthHeader = "Content-Lenght: "+ printA + CarriageReturn;
				}

			}
		} else {       // if is not get, print not implemented
			contentTypeHeader = "HTTP/1.0 501 Not Implemented";
			totalA = "Not Implemented (501)";
		}

		System.out.println("******Message sent******");

		output.writeBytes(contentTypeHeader);
		System.out.println(contentTypeHeader);

		output.writeBytes(contentLengthHeader);
		System.out.println(contentLengthHeader);

		output.writeBytes(CarriageReturn);
		System.out.println(CarriageReturn);

		if (everthingOk) {                                      // this if statement prodive if everthing is ok, print A and body.
			output.writeBytes("<HTML>" + CarriageReturn);
			System.out.println("<HTML>" + CarriageReturn);

			output.writeBytes("<HEAD>" + CarriageReturn);
			System.out.println("<HEAD>" + CarriageReturn);

			output.writeBytes("<TITLE>I am " + theUrlNumber + " bytes long</TITLE>" + CarriageReturn);
			System.out.println("<TITLE>I am " + theUrlNumber + " bytes long</TITLE>" + CarriageReturn);

			output.writeBytes("</HEAD>" + CarriageReturn);
			System.out.println("</HEAD>" + CarriageReturn);

			output.writeBytes("<BODY>" + totalA + "</BODY>" + CarriageReturn);
			System.out.println("<BODY>" + totalA + "</BODY>" + CarriageReturn);

			output.writeBytes("</HTML>");
			System.out.println("</HTML>");
		}





		output.close();
		takeIncomingMessageInBuffer.close();
		socketForEachThread.close();

	}

	public static boolean isNumeric(final String str) {    // this method check this string int or not
		if (str == null || str.length() == 0) {
			return false;
		}

		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

}


public final class HttpMultiServer {
	public static void main(String argv[]) throws Exception {

// write here the port number   - our port number is 8080
	ServerSocket newServerSocket = new ServerSocket(8080);       // never close this newServerSocket connection.
	
	System.out.println("Starting HTTP server on port 8080");//information 
// when new request come, take all of them.

boolean alwaysTakingRequest=true;
		while (alwaysTakingRequest) {

			Socket bindSocket = newServerSocket.accept();   // accept.
			incomingHttpDemand takingHttpRequest = new incomingHttpDemand(bindSocket);
// this area for multi thread http server, we created multi thread Http Server.

			Thread threadForMultiHttp = new Thread(takingHttpRequest);
			// this code help us to built multi http server,
			// when new request come, the server dont wait to finish previous Http request
			// take the new http request directly.
			threadForMultiHttp.start();
		}
	}
}



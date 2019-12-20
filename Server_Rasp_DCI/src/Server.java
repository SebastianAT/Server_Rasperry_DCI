import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

public class Server {

	public static final int SERVER_PORT = 8071;
	private static final String OUTPUT = "Got Data!\n";
	private static final String OUTPUT_ERROR = "ERROR!\n";
	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
	    "Content-Type: text/html\r\n" + 
	    "Content-Length: ";
	private static final String OUTPUT_HEADERS_ERROR = "HTTP/1.1 500 Internal Server Error\r\n" +
		    "Content-Type: text/html\r\n" + 
		    "Content-Length: ";
	private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";

	public static void main(String[] args) {
		BufferedReader in;
		BufferedWriter out;
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
		String dateFormat = df.format(d);
		try {
			
			ServerSocket srvSocket = new ServerSocket(Server.SERVER_PORT);
			InetAddress IP=InetAddress.getLocalHost();
			System.out.println("Server running at IP: " + IP.getHostAddress() + " port: " + Server.SERVER_PORT);
			
			while (true) {
				Socket ConnectionSocket = srvSocket.accept();
				System.out.println("Server got new request from client with IP: " + ConnectionSocket.getInetAddress()
						+ " and port: " + ConnectionSocket.getPort());

				try {
					in = new BufferedReader(new InputStreamReader(ConnectionSocket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(ConnectionSocket.getOutputStream()));

					String input = in.readLine();
					StringTokenizer token = new StringTokenizer(input);
					String method = token.nextToken().toUpperCase(); // we get the HTTP method of the client

					System.out.println(dateFormat + " " + method + " " + ConnectionSocket.getInetAddress()
							+ " " + ConnectionSocket.getPort());
					
					if (!method.equals("GET") && !method.equals("POST") && !method.equals("PUT")) {
						System.out.println("501 Not Implemented : " + method + " method.");

					} else {
						// GET or POST method

						if (method.equals("GET")) { // GET method so we return content
								
						} else if (method.equals("POST")) { // POST method so we return content
							JSONObject json;
							String headerLines = null;
							while ((headerLines = in.readLine()).length() != 0) {
								System.out.println(headerLines);
							}
							
							try {
								//we need this sleep else data will often be not complete
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							StringBuilder sb = new StringBuilder();
							while (in.ready()) {
								sb.append((char) in.read());
							}
							
							try {
								json = new JSONObject(sb.toString().trim());
								System.out.println("Data is: " + json.toString());
								
								//check if json has content
								if(!json.toString().isEmpty()) {
									out.write(OUTPUT_HEADERS + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT);
									out.flush();
									out.close();
								} else {
									out.write(OUTPUT_HEADERS_ERROR + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT_ERROR);
									out.flush();
									out.close();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								out.write(OUTPUT_HEADERS + OUTPUT_ERROR.length() + OUTPUT_END_OF_HEADERS + OUTPUT_ERROR);
								out.flush();
								out.close();
								e.printStackTrace();
							}
						}
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

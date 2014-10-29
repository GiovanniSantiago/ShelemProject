
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class MessageLine {
	Socket sock;
	PrintStream output;
	BufferedReader input;
	
	public MessageLine(Socket sock) throws IOException {
		this.sock = sock;
		this.output = new PrintStream(sock.getOutputStream());
		this.input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	public void sendMessage(Message m) {
		
		output.println(m.getCompleteString());
		System.out.println("MESSAGE OUT: "+m.getCompleteString());
	}
	
	public boolean isReady() throws IOException {
		return input.ready();
	}
	
	public Message receiveMessage() throws IOException {
		Message m = new Message(input.readLine());
		System.out.println("MESSAGE IN: " + m.getCompleteString());
		return m;
	}
}

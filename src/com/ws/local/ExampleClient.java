/**
 * 
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Exception.AutoError;
import Exception.AutoException;
import Network.Request;
import Network.RequestType;
import Network.SocketInterface;

/**
 * @author arthurc
 *
 */
public class ConfigClient extends Thread implements SocketInterface{
	private String host;
	private int port;
	private Socket sock;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ClientState state = ClientState.INIT;
	private CarModelOptionsIO cmo;
	private SelectCarOption sco;

	public ConfigClient(String host, int port) {
		setHost(host);
		setPort(port);
	}

	public void run() {
		if (openConnection()) {
			handleSession();
			closeSession();
		}
	}

	public boolean openConnection() {
		try {
			sock = new Socket(host, port);
			System.out.println("Config Client has a socket with "+host+":"+port);
		} catch (IOException e) {
			new AutoException(AutoError.SOCKET_FAILURE,
					"Unable to connect to " + host +".\n" + e.getMessage());
			return false;
		}
		try {
			out = new ObjectOutputStream(sock.getOutputStream());
			out.flush();
			in = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			new AutoException(AutoError.OTHER,
					"Unable to obtain stream to/from " + host);
			return false;
		}
		return true;
	}
	
	public void handleSession() {
		boolean runLoop = true;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		cmo = new CarModelOptionsIO(in, out);
		sco = new SelectCarOption(in, out);
		String fromUser;
		String outputLine = "(a) upload an automotive, (b) configure a car or (c) exit: ";
		
		System.out.println(outputLine);
		
		try {
			while(runLoop && (fromUser = stdIn.readLine()) != null) {
				outputLine = null;
				
				switch (state) {
					case INIT:
						if (fromUser.equalsIgnoreCase("a")) {
							outputLine = "Enter the file name: ";
							state = ClientState.UPLOAD;
						} else if (fromUser.equalsIgnoreCase("b")) {
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
							
						} else if (fromUser.equalsIgnoreCase("c")) {
							Object[] args = {};
							Request request = new Request(RequestType.CLOSE,args);
							out.writeObject(request);
							outputLine = "Goodbye.";
							runLoop = false;
						} else {
							outputLine = "(a) upload an automotive, (b) configure a car or (c) exit: ";
							state = ClientState.INIT;
						}
						break;
					case UPLOAD:
						cmo.read(fromUser);
						outputLine = "(a) upload an automotive, (b) configure a car or (c) exit: ";
						state = ClientState.INIT;
						break;
					case CONFIGURE:

						if (fromUser.equalsIgnoreCase("a") && sco.getModels()) {
							state = ClientState.SELECT_MODEL;
						} else if (fromUser.equalsIgnoreCase("b")) {
							sco.displaySelected();
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
						} else if (fromUser.equalsIgnoreCase("c") && sco.getOptionSets()) {
							state = ClientState.SELECT_OPTIONSET;
						} else if (fromUser.equalsIgnoreCase("d")) {
							outputLine = "(a) upload an automotive, (b) configure a car or (c) exit: ";
							state = ClientState.INIT;
						} else {
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
						}
						break;
					case SELECT_MODEL:
						sco.getModel(fromUser);
						outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
						state = ClientState.CONFIGURE;
						break;
					case SELECT_OPTIONSET:
						if (sco.getOptionSet(fromUser) && sco.getOptions()) {
							state = ClientState.SELECT_OPTION;
						} else {
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
						}
						break;
					case SELECT_OPTION:
						if (sco.chooseOption(fromUser)) {
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
						} else {
							outputLine = "(a) select a Car Model, (b) display selected options, (c) select Car OptionSet or (d) exit configuration:";
							state = ClientState.CONFIGURE;
						}
						break;
				}
				if (outputLine != null) {
					System.out.println(outputLine);
				}

			}
		} catch (IOException e) {
			new AutoException(AutoError.OTHER, e.getMessage());		
		}
	}
	
	public void closeSession() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			new AutoException(AutoError.OTHER, e.getMessage());		
		}
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
}

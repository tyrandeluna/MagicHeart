package app;

import java.io.IOException;
import java.net.SocketException;

import servidor.UDPServer;

public class ServerGame {
	static UDPServer server = new UDPServer();
	//só serve pra rodar o server
	public static void main(String args[]) throws SocketException, IOException{
		server.run();
	}
}

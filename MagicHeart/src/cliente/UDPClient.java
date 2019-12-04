package cliente;

import java.net.*;
import java.io.*;

public class UDPClient {
	static DatagramSocket aSocket;// socket
	static InetAddress aHost;
	static int serverPort;

	public UDPClient(String host, int sport) {
		try {
			this.aHost = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.serverPort = sport;

	}

	// envia a mensagem pro servidor
	public static void sendRequest(String requisicao) throws IOException {
		aSocket = new DatagramSocket();
		DatagramPacket request = new DatagramPacket(requisicao.getBytes(), requisicao.length(), aHost, serverPort);
		aSocket.send(request);
	}

	// pega a resposta do servidor
	public static byte[] getReply() throws IOException {
		byte[] buffer = new byte[10000];

		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

		try {
			aSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			aSocket.receive(reply);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}
		return buffer;

	}

	// m�todo pra finalizar, deixa pra dispois
	public void finaliza() throws IOException {
		aSocket.close();
	}
}
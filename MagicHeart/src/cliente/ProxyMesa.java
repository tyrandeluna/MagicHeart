package cliente;

import java.io.IOException;
import java.io.StringReader;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import control.MesaController;
import entidades.Deck;
import entidades.Identificador;
import entidades.Mesa;
import entidades.Message;

public class ProxyMesa {

	// O ideal seria solicitar os dados de conexao ao cliente
	// atrav�s de um nome de dom�nio (ex: www.ufc.br)
	UDPClient udpClient = new UDPClient("localhost", 6789);

	public String printaMesa(int id) throws IOException {
		Gson gson = new Gson();
		String args = Integer.toString(id);

		String reply = "";

		try {
			reply = doOperation("MesaController", "printaMesa", args);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}

		reply = reply.replaceAll("u250D", "\u250D");
		reply = reply.replaceAll("u2501", "\u2501");
		reply = reply.replaceAll("u252F", "\u252F");
		reply = reply.replaceAll("u2511", "\u2511");
		reply = reply.replaceAll("u251D", "\u251D");
		reply = reply.replaceAll("u253F", "\u253F");
		reply = reply.replaceAll("u2519", "\u2519");
		reply = reply.replaceAll("u2537", "\u2537");
		reply = reply.replaceAll("u2525", "\u2525");
		reply = reply.replaceAll("u2502", "\u2502");
		reply = reply.replaceAll("u2515", "\u2515");

		String resposta = gson.fromJson(reply, String.class);
		return resposta;
	}

	public int enroll(Deck deck) throws IOException {
		Gson gson = new Gson();
		String json = gson.toJson(deck);
		String reply = "";
		try {
			reply = doOperation("MesaController", "enroll", json);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}
		int resposta;

		try {
			resposta = gson.fromJson(reply, int.class);
		} catch (NumberFormatException e) {
			throw new NumberFormatException();
		}

		return resposta;
	}

	public int fimDeTurno(int id) throws IOException {
		String args = Integer.toString(id);
		String reply = "";
		try {
			reply = doOperation("MesaController", "fimDeTurno", args);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}

		Gson gson = new Gson();
		int resposta = gson.fromJson(reply, int.class);

		return resposta;
	}

	public String summon(int IDP, int pos, int ID) throws IOException {
		int[] data = new int[3];
		data[0] = IDP;
		data[1] = pos;
		data[2] = ID;

		Gson gson = new Gson();
		String json = gson.toJson(data);
		String reply = "";
		try {
			reply = doOperation("MesaController", "summon", json);
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}

		String resposta = gson.fromJson(reply, String.class);

		return resposta;
	}

	public Mesa getMesa() throws IOException {
		Gson gson = new Gson();

		String reply = "";
		try {
			reply = doOperation("MesaController", "getMesa", "");
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}
//		JsonReader reader = new JsonReader(new StringReader(reply));
//		reader.setLenient(true);

		if (reply.equals("QUIT")) {
			System.out.println("O outro jogador desistiu! Parab�ns voc� ganhou!");
			System.exit(1);
			Mesa resposta = null;
			return resposta;
		}

		Mesa resposta = gson.fromJson(reply, Mesa.class);

		return resposta;
	}

	public String exit() throws IOException {
		try {
			String reply = doOperation("MesaController", "exit", "");
		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}
		udpClient.finaliza();

		return "Voc� saiu!";
	}

	public String doOperation(String objectRef, String method, String args) throws IOException {
		// envio
		String data = empacotaMensagem(objectRef, method, args);

		// recebimento
		Message resposta = new Message();
		int count=0;
		while(count<3) {
			try {
				udpClient.sendRequest(data);
				resposta = desempacotaMensagem(udpClient.getReply());
				return resposta.getArgs();
			} catch (SocketTimeoutException g) {
				count++;
			}
		}
		throw new SocketTimeoutException();
	}

	private String empacotaMensagem(String objectRef, String method, String args) {
		// empacota a Mensagem de requisicao
		Identificador ID = Identificador.getInstance();
		Message msg = new Message();
		msg.setRequestId(ID.getId());
		msg.setObjRef(objectRef);
		msg.setMethod(method);
		msg.setArgs(new String(args));

		Gson gson = new Gson();
		String json = gson.toJson(msg);

		return json;
	}

	private Message desempacotaMensagem(byte[] resposta) {
		// desempacota a mensagem de resposta
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
		String msg = new String(resposta);

		JsonReader reader = new JsonReader(new StringReader(msg));
		reader.setLenient(true);

		Message M = gson.fromJson(reader, Message.class);

		return M;
	}

}
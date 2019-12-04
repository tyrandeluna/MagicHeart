package cliente;

import java.io.IOException;
import java.io.StringReader;
import java.net.SocketTimeoutException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import entidades.Deck;
import entidades.Identificador;
import entidades.Message;

public class ProxyDeckLoader{
	
	UDPClient udpClient = new UDPClient("localhost", 6789);
	
	public Deck getDeck(int ID) throws IOException {
		// (1) Empacota argumentos de entrada
		String id = String.valueOf(ID);
		
		// (2) Chama doOperation
		String reply = new String();
		try {
		reply = doOperation("DeckLoader", "getDeck", id);}
		
		catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		}
		
		// (3) Desempacota argumento de resposta (retorno de doOperation)
		
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(reply));
		reader.setLenient(true);
		
		if(reply.equals("Deck nao encontrado!")) {
			Deck deck = null;
			return deck;
		}
		Deck deck = gson.fromJson(reader, Deck.class);
		
		// (4) Retorna reposta desempacotada
		
		return deck;
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

	public void finaliza() throws IOException {
		udpClient.finaliza();
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
		Gson gson = new Gson();
		String msg = new String(resposta);
		
		JsonReader reader = new JsonReader(new StringReader(msg));
		reader.setLenient(true);
		Message M = gson.fromJson(reader, Message.class);
		
		return M;
	}
}
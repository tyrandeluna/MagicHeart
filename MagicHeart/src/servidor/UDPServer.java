package servidor;
import java.net.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import entidades.Message;

import java.io.*;

public class UDPServer{
	static DatagramSocket aSocket = null;
	static DatagramPacket request = null;
	Despachante desp;

	String[][] lastCalls = new String[2][2];

	public void run(){ 
	    try{
	    	aSocket = new DatagramSocket(6789);
	    	String resposta;
	    	lastCalls[0][0] = null;
	    	lastCalls[1][0] = null;
	    	//cria despachante
	    	desp = new Despachante();
	    	
	 		while(true){
	 			//recebendo mensagem de requisicao do cliente
	 			Message requisicao = desempacotaRequisicao(getRequest());
	 			

	 			int val = (int)(Math.random()*101);
	 			if(val>40) {//MUDAR O VALOR DE 100 SERÁ O MESMO QUE ADICIONAR 100-VAL% DE CHANCE DE REJEIÇÃO DA RESPOSTA!!!
	 				
	 				System.out.println("Não respondi!"+requisicao.getMethod());
	 				continue;
	 			}
	 			String chave = request.getAddress().toString()+Integer.toString(request.getPort())+requisicao.getRequestId();
//	 			String resultado = desp.selecionaEqueleto(requisicao);
//	 			sendReply(empacotaResposta(resultado));
	 			
	 			if(lastCalls[0][0] == null) {
	 				String resultado = desp.selecionaEqueleto(requisicao);
		 			resposta = empacotaResposta(resultado);
	 				lastCalls[0][0] = chave;
	 				lastCalls[0][1]= new String(resposta);
	 			}else if(lastCalls[1][0] == null) {
	 				String resultado = desp.selecionaEqueleto(requisicao);
		 			resposta = empacotaResposta(resultado);
	 				lastCalls[1][0]=chave;
	 				lastCalls[1][1]= new String(resposta);
	 			}else if(chave==lastCalls[0][0]){
	 				resposta = lastCalls[0][1];
	 			}else if(chave==lastCalls[1][0]){
	 				resposta = lastCalls[1][1];
	 			}else{
		 			//resultado que vem do esqueleto
		 			String resultado = desp.selecionaEqueleto(requisicao);
		 			resposta = empacotaResposta(resultado);
		 			if(lastCalls[0][0].contains(request.getAddress().toString())){
		 				lastCalls[0][0]=chave;
		 				lastCalls[0][1]= new String(resposta);
		 			}else{
		 				lastCalls[0][0]=chave;
		 				lastCalls[0][1]= new String(resposta);
		 			}
	 			}
	 			sendReply(resposta);

			}
	    } catch (SocketException e) {
	    	System.out.println("Socket: " + e.getMessage());
	    } catch (IOException e) {
	    	System.out.println("IO: " + e.getMessage());
		} finally {
			if(aSocket != null) {
				aSocket.close();
			}
		}
    }
	
	//pega a requisição do cliente
	public static byte[] getRequest() throws IOException {
		byte[] buffer = new byte[10000];
		request = new DatagramPacket(buffer, buffer.length);
		aSocket.receive(request);

		return request.getData();
	}
	
	//desempacota a requisicao bytes -> json -> objeto
	public static Message desempacotaRequisicao(byte[] array) throws IOException {
		Gson gson = new Gson();
		String msg = new String(array);
	
		JsonReader reader = new JsonReader(new StringReader(msg));
		reader.setLenient(true);
		
		Message m = gson.fromJson(reader, Message.class);
		
		return m;
	}
	
	//empacota a resposta objeto -> json -> bytes
	public static String empacotaResposta(String resultado) throws IOException {
		//não to usando esse requestID ainda mas ele vai servir pro clientes multiplos
		Gson gson = new Gson();
		Message msg = desempacotaRequisicao(request.getData());
		Message resposta = new Message();
		
		//monta a reposta de acordo com o modelo do objeto Message
		resposta.setRequestId(msg.getRequestId());
		resposta.setObjRef(msg.getObjRef());
		resposta.setMethod(msg.getMethod());
		resposta.setArgs(resultado);
		
		String reply = gson.toJson(resposta);
		
		return reply;
	}
	
	//envia a resposta pro cliente
	public static void sendReply(String resposta) throws IOException {
		DatagramPacket reply = new DatagramPacket(resposta.getBytes(), resposta.length(), request.getAddress(), 
					request.getPort());
		aSocket.send(reply);
	}

}

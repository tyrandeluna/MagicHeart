package servidor;



import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import control.MesaController;
import entidades.Card;
import entidades.Deck;
import entidades.Mesa;


public class MesaControllerEsqueleto {
	MesaController mesaC;
	
	public MesaControllerEsqueleto() {
		mesaC = new MesaController();
	}
	
	public String printaMesa(String args) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
		int id = gson.fromJson(args, int.class);
		
		String reply = MesaController.printaMesa(id);

		reply = reply.replaceAll("┍", "u250D");
		reply = reply.replaceAll("━", "u2501");
		reply = reply.replaceAll("┯", "u252F");
		reply = reply.replaceAll("┑", "u2511");
		reply = reply.replaceAll("┝", "u251D");
		reply = reply.replaceAll("┿", "u253F");
		reply = reply.replaceAll("┙", "u2519");
		reply = reply.replaceAll("┷", "u2537");
		reply = reply.replaceAll("┥", "u2525");
		reply = reply.replaceAll("│", "u2502");
		reply = reply.replaceAll("┕", "u2515");
		
		
		String jsonResp = gson.toJson(reply);
		return jsonResp;
		
	}
	
	public String enroll(String args){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		Deck player = gson.fromJson(args, Deck.class);
		
		int resp = mesaC.enroll(player);
		String jsonResp = gson.toJson(resp);

		return jsonResp;
	}

	public String summon(String args){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		//LEMBRAR DE ENVIAR COMO UM ARRAY DE INTEIROS!!!
		int[] data1 = gson.fromJson(args, int[].class);

		String resp = mesaC.summon(data1[0], data1[1], data1[2]);
		resp = gson.toJson(resp);

		return resp;
	}

	public String fimDeTurno(String args){
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		int resp = mesaC.fimDeTurno(Integer.parseInt(args));
		String jsonResp = gson.toJson(resp);

		return jsonResp;

	}
	
	public String getMesa(String args) throws IOException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		Mesa mesa = Mesa.getInstance();
		
		if(mesa.isDestroyed()) {
			String resposta = "QUIT";
			return resposta;
		}
		
		String resposta = gson.toJson(mesa);
		return resposta;
	}
	
	public String exit(String args) throws IOException {
		mesaC.exit();

		return "Um jogador saiu!";
	}
	
	
}

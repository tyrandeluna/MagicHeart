package servidor;

import java.io.FileNotFoundException;


import com.google.gson.Gson;

import control.DeckLoader;
import entidades.Deck;



public class DeckLoaderEsqueleto {
	
	public DeckLoaderEsqueleto() {
		
	}
	
	public String getDeck(String args) throws FileNotFoundException{
		Gson gson = new Gson();
		DeckLoader deckLoader = new DeckLoader();
		String reply;
		int deckNum = gson.fromJson(args, int.class);

		Deck deck = deckLoader.getDeck(deckNum);
		
		if (deck == null) {
			 reply = "Deck nao encontrado!";
			return reply;
		}
		reply = gson.toJson(deck);

		return reply;
		
	}
}

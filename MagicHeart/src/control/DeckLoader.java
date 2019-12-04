package control;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import entidades.Card;
import entidades.Deck;

public class DeckLoader{
	Deck deck = new Deck();
	String str = new String();

	public DeckLoader(){

	}

	public Deck getDeck(int deckNum) throws FileNotFoundException{
		if(deckNum==1){
//			 str = "deck1.txt";
			File arq = new File("C:\\Users\\mbalm\\Downloads\\MagicHeart\\MagicHeart\\src\\servidor\\deck1.txt");
			Scanner sc = new Scanner(arq);
			Gson gson = new Gson();

			while(sc.hasNextLine()){
				String data = sc.nextLine();
				Card card = gson.fromJson(data, Card.class);
				deck.addCard(card);
			}
		}else if(deckNum==2){
			//str = "deck2.txt";
			File arq = new File("C:\\Users\\mbalm\\Downloads\\MagicHeart\\MagicHeart\\src\\servidor\\deck2.txt");
			Scanner sc = new Scanner(arq);
			Gson gson = new Gson();

			while(sc.hasNextLine()){
				String data = sc.nextLine();
				Card card = gson.fromJson(data, Card.class);
				deck.addCard(card);
			}
		}else{
			return null;
		}
		
		//embaralhando o deck
		List<Card> cardList = Arrays.asList(deck.getDeck());

		Collections.shuffle(cardList);

		deck.setDeck(cardList.toArray(deck.getDeck()));
		
		return deck;
	}
}
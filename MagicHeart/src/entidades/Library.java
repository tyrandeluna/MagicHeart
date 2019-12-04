package entidades;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Library {
	@Expose(serialize = true)
	@SerializedName("library")
	private ArrayList<Card> library;

	public Library() {
		this.library = new ArrayList<Card>();
		
		//adicionando uma carta aqui só pra testar
		Card c = new Card(8,1,10,10);
		library.add(c);
	}
	
	public Card getCard(int ID) {
		return library.get(ID-1);
	}
	
	public ArrayList<Card> getLib(){
		return library;
	}
	
	public void AddCard(Card card) {
		library.add(card);
	}
	
}

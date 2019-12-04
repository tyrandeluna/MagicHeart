package entidades;

import java.util.Arrays;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deck {
	@Expose(serialize = true)
	@SerializedName("deck")
	private Card[] deck;
	@Expose(serialize = true)
	@SerializedName("curLen")
	private int curLen;

	public Deck() {
		this.curLen=0;
		this.deck = new Card[30];
	}
	
	public void addCard(Card card) {
		this.deck[this.curLen] = card;
		this.curLen++;
	}
	
	public void delCard(int ID) {
		int a=0;
		for(int i = 0; i<curLen-1; i++) {
			if(deck[i].getID() == ID || a != 0) {
				deck[i]=deck[i+1];
				a=1;
			}
		}
		curLen--;
	}
	
	private Card getCard(int pos) {
		return deck[pos];
	}

	public Card[] getDeck() {
		return deck;
	}

	public void setDeck(Card[] deck) {
		this.deck = deck;
	}

	public int getCurLen() {
		return curLen;
	}
	
	public Card draw() {
		int i = curLen - 1;
		if(i >= 0 && i < 31) {
			
			Card card = this.getCard(curLen-1);
			this.delCard(card.getID());
			
			return card;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Deck [deck=" + Arrays.toString(deck) + "]";
	}
}

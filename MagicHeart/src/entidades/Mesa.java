package entidades;

import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mesa {
	@Expose(serialize = true)
	@SerializedName("player1")
	private Deck player1;
	
	@Expose(serialize = true)
	@SerializedName("player2")
	private Deck player2;
	
	@Expose(serialize = true)
	@SerializedName("turno")
	private int turno;
	
	@Expose(serialize = true)
	@SerializedName("mana1")
	private int mana1;
	
	@Expose(serialize = true)
	@SerializedName("mana2")
	private int mana2;
	
	@Expose(serialize = true)
	@SerializedName("vida1")
	private int vida1;
	
	@Expose(serialize = true)
	@SerializedName("vida2")
	private int vida2;
	
	@Expose(serialize = true)
	@SerializedName("cards1")
	private HashMap< Integer, Card> cards1 = new HashMap<Integer, Card>();
	
	@Expose(serialize = true)
	@SerializedName("cards2")
	private HashMap< Integer, Card> cards2 = new HashMap<Integer, Card>();
	
	@Expose(serialize = true)
	@SerializedName("campo1")
	private Card[] campo1 = new Card[5];
	
	@Expose(serialize = true)
	@SerializedName("campo2")
	private Card[] campo2 = new Card[5];
	
	@Expose(serialize = true)
	@SerializedName("vitoria")
	private int vitoria;
	
	@SerializedName("players")
	@Expose(serialize = true)
	private int players = 0;
	
	@SerializedName("destroyed")
	@Expose(serialize = false)
	private static boolean destroyed = false;
	
	@Expose(serialize = false)
	@SerializedName("mesaInstance")
	private static Mesa mesaInstance = null;


	public static Mesa getInstance(){
		if(mesaInstance==null){
			synchronized(Mesa.class){
				mesaInstance = new Mesa();
			}
		}

		return mesaInstance;
	}	

	private Mesa() {
		this.vitoria = 0;
		this.vida1 = this.vida2=30;
		this.turno = 0;
		this.mana1 = this.mana2=1;
	}

	public Deck getPlayer1() {
		return player1;
	}

	public void setPlayer1(Deck player1) {
		this.player1 = player1;
	}

	public Deck getPlayer2() {
		return player2;
	}

	public void setPlayer2(Deck player2) {
		this.player2 = player2;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public int getMana1() {
		return mana1;
	}

	public void setMana1(int mana1) {
		this.mana1 = mana1;
	}

	public int getMana2() {
		return mana2;
	}

	public void setMana2(int mana2) {
		this.mana2 = mana2;
	}

	public int getVida1() {
		return vida1;
	}

	public void setVida1(int vida1) {
		this.vida1 = vida1;
	}

	public int getVida2() {
		return vida2;
	}

	public void setVida2(int vida2) {
		this.vida2 = vida2;
	}

	public HashMap<Integer, Card> getCards1() {
		return cards1;
	}

	public void setCards1(HashMap<Integer, Card> cards1) {
		this.cards1 = cards1;
	}

	public HashMap<Integer, Card> getCards2() {
		return cards2;
	}

	public void setCards2(HashMap<Integer, Card> cards2) {
		this.cards2 = cards2;
	}

	public Card[] getCampo1() {
		return campo1;
	}

	public void setCampo1(Card[] campo1) {
		this.campo1 = campo1;
	}

	public Card[] getCampo2() {
		return campo2;
	}

	public void setCampo2(Card[] campo2) {
		this.campo2 = campo2;
	}

	public int getVitoria() {
		return vitoria;
	}

	public void setVitoria(int vitoria) {
		this.vitoria = vitoria;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public static void reset() {
		Mesa.mesaInstance = new Mesa();
		destroyed = true;
	}
	
	public String toString() {
		return "Mesa [turno=" + getTurno() + ", mana1=" + this.mana1 + ", mana2=" + this.mana2 + "]";
	}
}

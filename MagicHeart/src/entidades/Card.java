package entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {
	@Expose(serialize = true)
	@SerializedName("mana")
	private int mana;
	@Expose(serialize = true)
	@SerializedName("ID")
	private int ID;
	@Expose(serialize = true)
	@SerializedName("ataque")
	private int ataque;
	@Expose(serialize = true)
	@SerializedName("vida")
	private int vida;
	
	public Card(int mana, int iD, int ataque, int vida) {
		super();
		this.mana = mana;
		ID = iD;
		this.ataque = ataque;
		this.vida = vida;
	}
	
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		this.ID = iD;
	}
	public int getAtaque() {
		return ataque;
	}
	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}
	public int getVida() {
		return vida;
	}
	public void setVida(int vida) {
		this.vida = vida;
	}
	
	public String toString() {
		return "Carta: " +this.ID+" "+this.mana+" "+this.ataque+" "+this.vida;
	}
}

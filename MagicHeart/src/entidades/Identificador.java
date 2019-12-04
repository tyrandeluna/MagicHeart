package entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//essa classe é só pra testar a comunicação
public class Identificador {
	@Expose(serialize = true)
	@SerializedName("id")
	private int id = 0;
	@Expose(serialize = false)
	@SerializedName("i")
	private static Identificador i = null;

	public static Identificador getInstance(){
		if(i == null){
			synchronized(Identificador.class){
				i =  new Identificador();
			}
		}
		return i;
	}

	private Identificador() {
		
	}

	public int getId() {
		return id++;
	}

	public String toString() {
		String Id = new Integer(id).toString();
		return Id;
	}
}

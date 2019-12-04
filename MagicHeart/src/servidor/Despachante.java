package servidor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import entidades.Message;

public class Despachante {
	
	public String selecionaEqueleto(Message request) {

		Class<?> objRef = null;
		Method method = null;
		String resposta = null;
		try {
			objRef = Class.forName("servidor."+ request.getObjRef()+ "Esqueleto");
			String methodName = request.getMethod();
			System.out.println("Executando: " + methodName);

			method = objRef.getMethod(methodName, String.class);
			resposta = (String) method.invoke(objRef.newInstance(),
					request.getArgs());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return resposta;
	}
}

package app;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Scanner;

import cliente.ProxyDeckLoader;
import cliente.ProxyMesa;
import entidades.Card;
import entidades.Deck;
import entidades.Mesa;

public class Game {

	public static void main(String args[]) throws SocketException, IOException, ClassNotFoundException {
		ProxyMesa pM = new ProxyMesa();
		ProxyDeckLoader pDL = new ProxyDeckLoader();
		Scanner sc = new Scanner(System.in);
		Deck deck = null;
		Mesa mesa;
		int opI = 0;
		String Dec = null;

		System.out.println("" + "\r\n" + "┌──────────────────────────────────────────────────────┐\r\n"
				+ "                    _                              _   \r\n"
				+ "  /\\/\\   __ _  __ _(_) ___    /\\  /\\___  __ _ _ __| |_ \r\n"
				+ " /    \\ / _` |/ _` | |/ __|  / /_/ / _ \\/ _` | '__| __|\r\n"
				+ "/ /\\/\\ \\ (_| | (_| | | (__  / __  /  __/ (_| | |  | |_ \r\n"
				+ "\\/    \\/\\__,_|\\__, |_|\\___| \\/ /_/ \\___|\\__,_|_|   \\__|\r\n"
				+ "              |___/                                    \r\n"
				+ "└──────────────────────────────────────────────────────┘");

		while (Dec == null) {

			System.out.println("    Para entrar no jogo você deve escolher um deck:\n "
					+ "         1-Caçador             2-Paladino");
			Dec = sc.nextLine();
			try {
				// checando se o há um inteiro valido usando parseInt
				opI = Integer.parseInt(Dec);
				deck = pDL.getDeck(opI);
				if (deck == null) {
					System.out.println("Deck não encontrado! Tente novamente!");
					Dec = null;
				}
			} catch (NumberFormatException e) {
				System.out.println("Código Invalido! Tente novamente!");
				Dec = null;
			} catch (SocketTimeoutException c) {
				System.out.println("Falha na conexão. Tente novamente!");
				Dec = null;
			}
		}

		int id = -1;
		try {
			id = pM.enroll(deck);
		} catch (SocketTimeoutException e) {
			System.out.println("Falha na conexão. Reiniciando o jogo.");
			String[] s = {};
			Game.main(s);
		}

		if (id > 2) {
			System.out.println("Mesa cheia!");
			return;
		}
		while (true) {
			try {
				mesa = pM.getMesa();
			} catch (SocketTimeoutException e) {
				int count=0;
				while (count<10) {
					count++;
					try {
						pM.exit();
						break;
					} catch (SocketTimeoutException f) {
						
					}
				}
				System.out.println("Falha na conexão. Reiniciando o jogo.");
				String[] s = {};
				Game.main(s);
			}

			for (int i = 0; i < 10; i++) {
				System.out.println("");
			}

			try {
				System.out.println(pM.printaMesa(id));
			} catch (SocketTimeoutException e) {
				
			
				int count=0;
				while (count<10) {
					count++;
					try {
						pM.exit();
						break;
					} catch (SocketTimeoutException f) {
						
					}
				}
				System.out.println("Falha na conexão. Reiniciando o jogo.");
				String[] s = {};
				Game.main(s);
			}

			System.out.println(
					"Suas opções: 1-Sumonnar 2-Finalizar turno 3-Sair (*)-Digite qualquer outra tecla para atualizar");
			String inp = sc.nextLine();
			int op;

			try {
				// checando se o há um inteiro valido usando parseInt
				op = Integer.parseInt(inp);
			} catch (NumberFormatException e) {
				op = 0;
			}

			switch (op) {
			case 1:
				System.out.println("Digite o Id da carta e a posição de summon: ");
				int idCard;
				int pos;
				String card = sc.nextLine();
				String posC = sc.nextLine();

				try {
					// checando se o há um inteiro valido usando parseInt
					idCard = Integer.parseInt(card);
					pos = Integer.parseInt(posC);
				} catch (NumberFormatException e) {
					idCard = 0;
					pos = 0;
					System.out.println("Comando inválido!");
					break;
				}

				try {
					String resposta = pM.summon(id, pos, idCard);
					System.out.println(resposta);
				} catch (SocketTimeoutException e) {
					int count=0;
					while (count<10) {
						count++;
						try {
							pM.exit();
							break;
						} catch (SocketTimeoutException f) {
							
						}
					}
					System.out.println("Falha na conexão. Reiniciando o jogo.");
					String[] s = {};
					Game.main(s);
				}
				break;
			case 2:
				try {
					pM.fimDeTurno(id);
				} catch (SocketTimeoutException e) {
					int count=0;
					while (count<10) {
						count++;
						try {
							pM.exit();
							break;
						} catch (SocketTimeoutException f) {
							
						}
					}
					System.out.println("Falha na conexão. Reiniciando o jogo.");
					String[] s = {};
					Game.main(s);
				}

				break;
			case 3:
				System.out.println(
						"Você quer mesmo desistir da partida?: \n1-Sim (*)-Qualquer outra tecla para cancelar");
				String decision = sc.nextLine();
				int dec;

				try {
					// checando se o há um inteiro valido usando parseInt
					dec = Integer.parseInt(decision);
				} catch (NumberFormatException e) {
					break;
				}

				switch (dec) {
				case 1:
					int count=0;
					while (count<10) {
						count++;
						try {
							pM.exit();
							System.exit(1);
							break;
						} catch (SocketTimeoutException e) {
							
						}
					}
					break;
				}

				break;
			default:
				break;
			}

		}

	}
}

package control;

import java.util.HashMap;

import entidades.Card;
import entidades.Deck;
import entidades.Mesa;

public class MesaController {

	private void firstDraw() {
		Mesa mesa = Mesa.getInstance();
		HashMap<Integer, Card> cards1 = mesa.getCards1();
		HashMap<Integer, Card> cards2 = mesa.getCards2();

		for (int i = 0; i < 5; i++) {
			Card aux = mesa.getPlayer1().draw();
			cards1.put(aux.getID(), aux);

			mesa.setCards1(cards1);

			aux = mesa.getPlayer2().draw();
			cards2.put(aux.getID(), aux);

			mesa.setCards2(cards2);
		}
	}

	public int enroll(Deck player) {
		Mesa mesa = Mesa.getInstance();
		int players = mesa.getPlayers();
		if (players == 0) {
			mesa.setPlayer1(player);
			mesa.setPlayers(1);
			return 1;
		} else if (players == 1) {
			mesa.setPlayer2(player);
			mesa.setPlayers(2);
			firstDraw();
			return 2;
		}
		return 3;

	}

	public String summon(int IDP, int pos, int ID) { // IDP = ID PLAYER
		Mesa mesa = Mesa.getInstance();

		Card[] campo1 = mesa.getCampo1();
		Card[] campo2 = mesa.getCampo2();

		HashMap<Integer, Card> cards1 = mesa.getCards1();
		HashMap<Integer, Card> cards2 = mesa.getCards2();

		if (getTurno() % 2 != IDP - 1) {
			if (mesa.isDestroyed() == true)
				return "O outro jogador desistiu! Parabéns você ganhou!";
			return "Não é seu turno.";
		} else if (mesa.isDestroyed() == true) {
			mesa.setVitoria(IDP);
			return "O outro jogador desistiu! Parabéns você ganhou!";
		} else if (mesa.getPlayers() < 2) {
			return "Numero insuficiente de jogadores.";
		} else if (getTurno() % 2 == 0) {
			if (cards1.get(ID).getMana() > mesa.getMana1())
				return "Mana insuficiente";
			campo1[pos] = cards1.get(ID);
			cards1.remove(ID);
			int mana = mesa.getMana1();
			mesa.setMana1(mana -= campo1[pos].getMana());
			mesa.setCards1(cards1);
		} else {
			if (cards2.get(ID).getMana() > mesa.getMana2())
				return "Mana insuficiente";
			campo2[pos] = cards2.get(ID);
			cards2.remove(ID);
			int mana = mesa.getMana2();
			mesa.setMana2(mana -= campo2[pos].getMana());
			mesa.setCards2(cards2);
		}
		return "Ok";
	}

	public void attack() {
		Mesa mesa = Mesa.getInstance();
		Card[] campo1 = mesa.getCampo1();
		Card[] campo2 = mesa.getCampo2();
		int vida1 = mesa.getVida1();
		int vida2 = mesa.getVida2();
		int vitoria = mesa.getVitoria();

		if (vitoria != 0)
			return;
		for (int i = 0; i < 5; i++) {
			if (campo1[i] != null && campo2[i] != null) {
				campo1[i].setVida(campo1[i].getVida() - campo2[i].getAtaque());
				campo2[i].setVida(campo2[i].getVida() - campo1[i].getAtaque());
				if (campo1[i].getVida() <= 0)
					campo1[i] = null;
				if (campo2[i].getVida() <= 0)
					campo2[i] = null;
			} else if (campo1[i] == null && campo2[i] != null) {
				vida1 = vida1 - campo2[i].getAtaque();
				mesa.setVida1(vida1);
				if (vida1 <= 0) {
					vitoria = 2;
					mesa.setVitoria(vitoria);
				}
			} else if (campo1[i] != null && campo2[i] == null) {
				vida2 = vida2 - campo1[i].getAtaque();
				mesa.setVida2(vida2);
				if (vida2 <= 0) {
					vitoria = 1;
					mesa.setVitoria(vitoria);
				}
			}
		}
	}

	public int fimDeTurno(int id) {
		Mesa mesa = Mesa.getInstance();
		HashMap<Integer, Card> cards1 = mesa.getCards1();
		HashMap<Integer, Card> cards2 = mesa.getCards2();

		if (getTurno() % 2 != id - 1)
			return -1;

		if (getTurno() % 2 == 0) {
			if (cards1.size() < 5 && mesa.getPlayer1().getCurLen() > 0) {
				Card aux = mesa.getPlayer1().draw();
				cards1.put(aux.getID(), aux);

				mesa.setCards1(cards1);
			} else {
				mesa.getPlayer1().draw();
			}
		} else {
			if (cards2.size() < 5 && mesa.getPlayer2().getCurLen() > 0) {
				Card aux = mesa.getPlayer2().draw();
				cards2.put(aux.getID(), aux);

				mesa.setCards2(cards2);
			} else {
				mesa.getPlayer2().draw();
			}
		}
		this.attack();
		int turno = getTurno();
		int novaMana = 0;

		if (1 + turno > 16) {
			novaMana = 10;
		} else {
			novaMana = ((2 + turno) / 2) + 1;
		}
		mesa.setMana2(novaMana);
		mesa.setMana1(novaMana);

		mesa.setTurno(turno + 1);

		return mesa.getVitoria();
	}

	public int getTurno() {
		Mesa mesa = Mesa.getInstance();
		return mesa.getTurno();
	}

	public static String printaMesa(int id) {
		Mesa mesa = Mesa.getInstance();
		if (mesa.getVitoria() != 0 && mesa.getVitoria() == id) {
			return "\r\n" + " __   __   __     ______   ______     ______     __     ______    \r\n"
					+ "/\\ \\ / /  /\\ \\   /\\__  _\\ /\\  __ \\   /\\  == \\   /\\ \\   /\\  __ \\   \r\n"
					+ "\\ \\ \\'/   \\ \\ \\  \\/_/\\ \\/ \\ \\ \\/\\ \\  \\ \\  __<   \\ \\ \\  \\ \\  __ \\  \r\n"
					+ " \\ \\__|    \\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\  \\ \\_\\ \\_\\ \r\n"
					+ "  \\/_/      \\/_/     \\/_/   \\/_____/   \\/_/ /_/   \\/_/   \\/_/\\/_/ \r\n"
					+ "                                                                  \r\n" + "";
		} else if (mesa.getVitoria() != 0 && mesa.getVitoria() != id) {
			return "\r\n" + " _____     ______     ______     ______     ______     ______   ______    \r\n"
					+ "/\\  __-.  /\\  ___\\   /\\  == \\   /\\  == \\   /\\  __ \\   /\\__  _\\ /\\  __ \\   \r\n"
					+ "\\ \\ \\/\\ \\ \\ \\  __\\   \\ \\  __<   \\ \\  __<   \\ \\ \\/\\ \\  \\/_/\\ \\/ \\ \\  __ \\  \r\n"
					+ " \\ \\____-  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\ \\_\\  \\ \\_____\\    \\ \\_\\  \\ \\_\\ \\_\\ \r\n"
					+ "  \\/____/   \\/_____/   \\/_/ /_/   \\/_/ /_/   \\/_____/     \\/_/   \\/_/\\/_/ \r\n"
					+ "                                                                          \r\n" + "";
		}

		if (mesa.getPlayers() < 2) {
			return "Apenas um player! Aguarde.";
		}

		String montada = "";
		Card[] campo1 = mesa.getCampo1();
		Card[] campo2 = mesa.getCampo2();
		String[] card = { "│ ┍━━━━━━", "│ │  I D  │ ", "│ │  ", "  │ ", "│ │       │ ", "│ │", "   ", "│ ",
				"│ ┕━━━━━━━┙ " };
		String cabecalho = "┍━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┑\r\n"
				+ "│                              CAMPO                                    │ \r\n"
				+ "┕━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┙\r\n" + "\r\n"
				+ "┍━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┯━━━━━━━━━━━┑\r\n"
				+ "│                          HAND: " + Integer.toString(mesa.getCards1().size())
				+ " CARDS                    │  -=DECK=- │\r\n";
		if (mesa.getVida1() > 9) {
			cabecalho += "│---------------------- -= HP : " + Integer.toString(mesa.getVida1())
					+ " =- ----------------------│";
			if (mesa.getPlayer1().getCurLen() > 9) {
				cabecalho += "     " + Integer.toString(mesa.getPlayer1().getCurLen()) + "    │\r\n";
			} else {
				cabecalho += "     0" + Integer.toString(mesa.getPlayer1().getCurLen()) + "    │\r\n";
			}
		} else {
			cabecalho += "│---------------------- -= HP : 0" + Integer.toString(mesa.getVida1())
					+ " =- ----------------------│";
			if (mesa.getPlayer1().getCurLen() > 9) {
				cabecalho += "     " + Integer.toString(mesa.getPlayer1().getCurLen()) + "    │\r\n";
			} else {
				cabecalho += "     0" + Integer.toString(mesa.getPlayer1().getCurLen()) + "    │\r\n";
			}
		}

		cabecalho += "┝━━━━━━━━━━━┯━━━━━━━━━━━┯━━━━━━━━━━━┯━━━━━━━━━━━┯━━━━━━━━━━━┿━━━━━━┯━━━━┙\n";
		montada += cabecalho;

		String[] lines = new String[13];
		if (mesa.getTurno() % 2 == id - 1) {
			lines[6] = "┝━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┥  YOU TURN  │\n";
		} else {
			lines[6] = "┝━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┿━━━━━━━━━━━┥  OP. TURN  │\n";
		}
		for (int i = 0; i < 6; i++) {
			String line1 = "";
			String line2 = "";
			switch (i) {

			case 0:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[0];
						if (campo1[k].getMana() < 10) {
							line1 += "0";
						}
						line1 += Integer.toString(campo1[k].getMana()) + " ";
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[0];
						if (campo2[k].getMana() < 10) {
							line2 += "0";
						}
						line2 += Integer.toString(campo2[k].getMana()) + " ";
					} else {
						line2 += "│           ";
					}
				}
				line1 += "│      │\n";
				line2 += "│            │\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;
			case 1:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[1];
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[1];
					} else {
						line2 += "│           ";
					}
				}
				line1 += "│ MANA │\n";
				line2 += "┝━━━━━━┯━━━━━┙\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;

			case 2:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[2];
						if (campo1[k].getID() < 10) {
							line1 += "0";
						}
						line1 += "0" + Integer.toString(campo1[k].getID());
						line1 += card[3];
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[2];
						if (campo2[k].getID() < 10) {
							line2 += "0";
						}
						line2 += "0" + Integer.toString(campo2[k].getID());
						line2 += card[3];
					} else {
						line2 += "│           ";
					}
				}
				line1 += "│  0" + Integer.toString(mesa.getMana1()) + "  │\n";
				line2 += "│      │\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;

			case 3:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[4];
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[4];
					} else {
						line2 += "│           ";
					}
				}
				line1 += "│      │\n";
				line2 += "│ MANA │\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;

			case 4:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[5];
						if (campo1[k].getAtaque() > 9) {
							line1 += Integer.toString(campo1[k].getAtaque());
						} else {
							line1 += "0" + Integer.toString(campo1[k].getAtaque());
						}
						line1 += card[6];
						if (campo1[k].getVida() > 9) {
							line1 += Integer.toString(campo1[k].getVida());
						} else {
							line1 += "0" + Integer.toString(campo1[k].getVida());
						}
						line1 += card[7];
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[5];
						if (campo2[k].getAtaque() > 9) {
							line2 += Integer.toString(campo2[k].getAtaque());
						} else {
							line2 += "0" + Integer.toString(campo2[k].getAtaque());
						}
						line2 += card[6];
						if (campo2[k].getVida() > 9) {
							line2 += Integer.toString(campo2[k].getVida());
						} else {
							line2 += "0" + Integer.toString(campo2[k].getVida());
						}
						line2 += card[7];
					} else {
						line2 += "│           ";
					}
				}
				line1 += "┝━━━━━━┷━━━━━┑\n";
				line2 += "│  0" + Integer.toString(mesa.getMana2()) + "  │\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;
			case 5:
				for (int k = 0; k < 5; k++) {
					if (campo1[k] != null) {
						line1 += card[8];
					} else {
						line1 += "│           ";
					}
					if (campo2[k] != null) {
						line2 += card[8];
					} else {
						line2 += "│           ";
					}
				}
				line1 += "│            │\n";
				line2 += "│      │\n";
				lines[i] = line1;
				lines[i + 7] = line2;
				break;
			}

		}
		for (String s : lines) {
			montada += s;
		}
		String rodape = "┝━━━━━━━━━━━┷━━━━━━━━━━━┷━━━━━━━━━━━┷━━━━━━━━━━━┷━━━━━━━━━━━┿━━━━━━┷━━━━┑\r\n";
		if (mesa.getVida2() > 9) {
			rodape += "│---------------------- -= HP : " + Integer.toString(mesa.getVida2())
					+ " =- ----------------------│  -=DECK=- │\r\n";
		} else {
			rodape += "│---------------------- -= HP : 0" + Integer.toString(mesa.getVida2())
					+ " =- ----------------------│  -=DECK=- │\r\n";
		}
		if (mesa.getPlayer2().getCurLen() > 9) {
			rodape += "│                          HAND: " + Integer.toString(mesa.getCards2().size())
					+ " CARDS                    │     " + Integer.toString(mesa.getPlayer2().getCurLen())
					+ "    │\r\n";
		} else {
			rodape += "│                          HAND: " + Integer.toString(mesa.getCards2().size())
					+ " CARDS                    │     0" + Integer.toString(mesa.getPlayer2().getCurLen())
					+ "    │\r\n";
		}
		rodape += "┕━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┷━━━━━━━━━━━┙\n";

		montada += rodape;

		int eu = 0;
		HashMap<Integer, Card> cards = new HashMap<Integer, Card>();
		if (id == 1) {
			eu = mesa.getCards1().size();
			cards = mesa.getCards1();
		} else {
			eu = mesa.getCards2().size();
			cards = mesa.getCards2();
		}

		Card[] meusCards = new Card[eu];
		int count = 0;
		for (int c : cards.keySet()) {
			meusCards[count] = cards.get(c);
			count++;
		}
		montada += "┍━━━━━━━━━━━";
		for (int i = 0; i < eu - 2; i++) {
			montada += "┯━━━━━━━━━━━";
		}
		montada += "┯━━━━━━━━━━━┑\n";
		String[] linha = new String[6];
		for (int i = 0; i < 6; i++) {
			String line1 = "";
			switch (i) {
			case 0:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[0];
						if (meusCards[k].getMana() < 10) {
							line1 += "0";
						}
						line1 += Integer.toString(meusCards[k].getMana()) + " ";
					} else {
						line1 += "│           ";
					}
				}
				linha[i] = line1 + "│\n";
				break;
			case 1:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[1];
					} else {
						line1 += "│           ";
					}

				}
				linha[i] = line1 + "│\n";
				break;

			case 2:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[2] + "0";
						if (meusCards[k].getID() < 10) {
							line1 += "0";
						}
						line1 += Integer.toString(meusCards[k].getID());
						line1 += card[3];
					} else {
						line1 += "│           ";
					}
				}
				linha[i] = line1 + "│\n";
				break;

			case 3:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[4];
					} else {
						line1 += "│           ";
					}

				}
				linha[i] = line1 + "│\n";
				break;

			case 4:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[5];
						if (meusCards[k].getAtaque() < 10) {
							line1 += "0" + Integer.toString(meusCards[k].getAtaque());
						} else {
							line1 += Integer.toString(meusCards[k].getAtaque());
						}
						line1 += card[6];
						if (meusCards[k].getVida() < 10) {
							line1 += "0" + Integer.toString(meusCards[k].getVida());
						} else {
							line1 += Integer.toString(meusCards[k].getVida());
						}
						line1 += card[7];
					} else {
						line1 += "│           ";
					}
				}
				linha[i] = line1 + "│\n";
				break;
			case 5:
				for (int k = 0; k < eu; k++) {
					if (meusCards[k] != null) {
						line1 += card[8];
					} else {
						line1 += "│           ";
					}
				}
				linha[i] = line1 + "│\n";
				break;
			}

		}
		for (String s : linha) {
			montada += s;
		}
		montada += "┕━━━━━━━━━━━";
		for (int i = 0; i < eu - 2; i++) {
			montada += "┷━━━━━━━━━━━";
		}
		montada += "┷━━━━━━━━━━━┙\n";

		return montada;
	}

	public void exit() {
		Mesa mesa = Mesa.getInstance();
		mesa.reset();
	}
}

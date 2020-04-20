package zup.webservices.adm;

import java.util.ArrayList;
import java.util.Arrays;

public class PasswordGenerator {

	public static String randomPassword() {
		String[] auxLetters = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
				"r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

		ArrayList<String> letters = new ArrayList<String>(Arrays.asList(auxLetters));

		String[] auxNumbers = { "0", "1", "2", "4", "5", "6", "7", "8", "9" };

		ArrayList<String> numbers = new ArrayList<String>(Arrays.asList(auxNumbers));

		StringBuilder password = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			int posicao = (int) (Math.random() * letters.size());
			password.append(letters.get(posicao));
			letters.remove(posicao);
		}

		for (int j = 0; j < 3; j++) {
			int posicao = (int) (Math.random() * numbers.size());
			password.append(numbers.get(posicao));
			numbers.remove(posicao);
		}

		return password.toString();
	}
}

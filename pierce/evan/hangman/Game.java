/*
 Evan Pierce
 5.26.16
 Final Project - Game Thread Class
 JDK 1.8.0
 */
package pierce.evan.hangman;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

// implements runnable class for multi-threading
public class Game implements Runnable {

	// Variable controlling thread On/Off
	public volatile static boolean running = false;

	// Hidden word set by user #1
	public volatile static String correctWord;

	// displayed variables
	public volatile static String displayWord;
	public volatile static String lettersRemaining;
	public volatile static int errors;
	public volatile static String status;

	// Status variables
	public volatile static boolean playerTwoPlaying;
	public volatile static boolean started = false;
	public volatile static boolean finished = false;

	// Stores letters being guessed & already guessed
	public volatile static LetterBank lb;

	// Stores guess
	public volatile static String g;

	// Used for checking for input from playAgain dialog
	public volatile static boolean chosen;
	public volatile static boolean reset;
	public volatile static boolean exit;

	public static void startGame() {

		// Initializes letter bank
		lb = new LetterBank();
		lettersRemaining = lb.getRemainingGuesses();
		// Resets error count
		errors = 0;
		// Sets first status message
		status = "Player 1 is entering in a word :)";

		// debug info
		System.out.println("Enter string of 4+ letters and any amount of words: ");

		// Valid word check
		boolean validAnswer = false;
		do {
			correctWord = null;
			// asks player 1 to enter in word
			// Run from main JavaFX thread
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Set up dialog
					TextInputDialog dialog = new TextInputDialog();
					dialog.setTitle(Settings.TITLE);
					dialog.setHeaderText("Enter string of 4+ letters and any amount of words: ");
					dialog.setGraphic(null);
					// execute dialog
					Optional<String> input = dialog.showAndWait();
					if (input.isPresent()) {
						correctWord = input.get().toLowerCase();
						System.out.println("correctWord = " + correctWord);
					}
				}
			});

			// Wait for answer
			while (correctWord == null) {
				try {
					Thread.sleep(Settings.TICKRATE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (correctWord.length() >= 4) {
				validAnswer = true;
			}
		} while (validAnswer == false);

		// Sets display word to underscores for each letter in correctWord
		displayWord = "";

		for (int i = 0; i < correctWord.length(); i++) {

			displayWord += "_";
			//Exception for space values (allows for more than one word)
			//Fills in spaces
			if (String.valueOf(correctWord.charAt(i)).equals(" ")) {

				StringBuilder newDisplayWord = new StringBuilder(displayWord);
				newDisplayWord.setCharAt(i, " ".charAt(0));
				displayWord = newDisplayWord.toString();

			}
		}

		// debug info
		System.out.println(displayWord);

		// Start Game
		// sets internal status variable
		playerTwoPlaying = true;
		// sets external status message
		status = "Enter letter to guess";
		// Print every tick (debug info)
		// displayWord with correct letters and underlines
		// Letters remaining to guess
		// Chances left
		while (playerTwoPlaying) {
			boolean validGuess = false;
			// gets input and checks validity of it until input is valid
			while (validGuess == false) {
				// debug info
				System.out.println("Enter letter to guess.");
				// Prompts input for guess
				g = null;
				// Run from main JavaFX thread
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// set up dialog
						TextInputDialog dialog = new TextInputDialog();
						dialog.setTitle(Settings.TITLE);
						dialog.setHeaderText("Enter letter to guess.");
						dialog.setGraphic(null);
						// execute dialog
						Optional<String> input = dialog.showAndWait();
						if (input.isPresent()) {
							g = input.get();
							System.out.println("guess = " + input.get());
						}
					}
				});

				// Wait for answer
				while (g == null) {
					try {
						Thread.sleep(Settings.TICKRATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Checks to see if entered string is a letter
				boolean isLetter = false;
				for (int i = 0; i < 26; i++) {
					String letter = String.valueOf((char) (i + 97));
					if (g != null && g.equals(letter)) {
						isLetter = true;
					}

				}
				// If it passes the letter check, goes through the already
				// guessed check
				if (isLetter) {
					if (g != null && lb.checkValidGuess(g)) {

						validGuess = true;

					} else {
						status = "Letter already guessed!";
						System.out.println(status);
					}
				} else {
					status = "Invalid character.";
					System.out.println(status);
				}
			}

			// Updates guess list
			lb.removeGuess(g);

			// Checks to see if guessed letter is contained in correctWord
			// result is stored in correctGuess
			boolean correctGuess = false;
			for (int i = 0; i < correctWord.length(); i++) {

				if (g.equals(String.valueOf(correctWord.charAt(i)))) {

					correctGuess = true;
					status = "Correct Guess!";
					System.out.println(status);

					StringBuilder newDisplayWord = new StringBuilder(displayWord);
					newDisplayWord.setCharAt(i, g.toCharArray()[0]);
					displayWord = newDisplayWord.toString();

				}

			}

			// If guess was incorrect, add to error count
			if (correctGuess == false) {
				errors++;
				status = "Incorrect Guess!";
				System.out.println(status);
			}

			// Debug info + update display variable
			System.out.println(displayWord);
			lettersRemaining = lb.getRemainingGuesses();
			System.out.println(lettersRemaining);
			System.out.println("Chances Remaining: " + (6 - errors));

			// Checks error count for loss
			if (errors == 6) {
				status = "YOU LOST M8. HE DEAD.";
				System.out.println(status);

				promptReset("You LOST! Would you like to play again?");

			}

			// Checks if displayWord is equal to correctWord, if they are,
			// player 2 won
			if (displayWord != null && correctWord != null && displayWord.equals(correctWord)) {
				status = "YOU WON M8. HE ALIVE?";
				System.out.println(status);
				promptReset("You WON! Would you like to play again?");
			}

		}
	}

	// prompts user if you want to play again
	private static void promptReset(String message) {
		// helps determine if input has been given by user
		chosen = false;
		// Run from main JavaFX thread
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Set up dialog
				Alert playAgain = new Alert(AlertType.CONFIRMATION);
				playAgain.setHeaderText(message);
				playAgain.setTitle(Settings.TITLE);
				playAgain.setGraphic(null);
				ButtonType play = new ButtonType("Play Again");
				ButtonType close = new ButtonType("Exit");
				playAgain.getButtonTypes().setAll(play, close);
				// Execute dialog
				Optional<ButtonType> input = playAgain.showAndWait();
				// If exit gets pressed
				if (input.get() == close) {
					exit = true;
					reset = false;
					chosen = true;
					// If play again gets pressed
				} else if (input.get() == play) {
					reset = true;
					exit = false;
					chosen = true;
				}
			}
		});
		// Wait for choice
		while (chosen == false) {
			try {
				Thread.sleep(Settings.TICKRATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		// If player wants to play again
		if (reset == true) {
			playerTwoPlaying = false;
			started = false;

			// If player wants to quit
		} else if (exit == true) {
			finished = true;
			playerTwoPlaying = false;

		}
	}

	@Override
	public void run() {
		// While thread is alive (Always true while thread is being run)
		while (Thread.currentThread().isAlive()) {
			// Thread control variable is running. Started is so the code only
			// runs once
			if (running && !started) {
				started = true;
				startGame();

			} else {

				try {
					// Checks this if statement at the TICKRATE defined in
					// settings
					Thread.sleep(Settings.TICKRATE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
/*
 Evan Pierce
 5.26.16
 Final Project - Main Thread Class / GUI
 JDK 1.8.0
 */
package pierce.evan.hangman;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HangmanMain extends Application {

	@Override
	public void start(Stage primaryStage) {
		// Start main game thread
		Thread gameThread = new Thread(new Game());
		gameThread.setDaemon(true);
		Game.running = true;
		gameThread.start();

		//Main word used by game
		Text displayWord = new Text();
		displayWord.setX(50);
		displayWord.setY(400);

		//Displays remaining letters
		Text lettersRemaining = new Text();
		lettersRemaining.setX(50);
		lettersRemaining.setY(425);
		
		//Displays chances remaining
		Text chancesLeft = new Text();
		chancesLeft.setX(50);
		chancesLeft.setY(450);

		//Displays status messages
		Text statusText = new Text();
		statusText.setX(250);
		statusText.setY(450);
		
		//Imports Images
		Image[] errorImage = new Image[]{
				new Image("pierce/evan/hangman/assets/0.png"),
				new Image("pierce/evan/hangman/assets/1.png"),
				new Image("pierce/evan/hangman/assets/2.png"),
				new Image("pierce/evan/hangman/assets/3.png"),
				new Image("pierce/evan/hangman/assets/4.png"),
				new Image("pierce/evan/hangman/assets/5.png"),
				new Image("pierce/evan/hangman/assets/6.png")};
		
		ImageView errorIV = new ImageView();
		errorIV.setImage(errorImage[0]);
		errorIV.setX(75);
		
		//Thread that takes information from gameThread and uses it to update GUI
		Task<Void> updateDisp = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				//Constantly runs
				while (true) {
					//Checks to see if the game thead is running
					if (Game.running) {
						//Checks to see if game is done being played
						if (!Game.finished) {
							//updates GUI
							displayWord.setText(Game.displayWord);
							lettersRemaining.setText(Game.lettersRemaining);
							chancesLeft.setText("Chances left: " + (6 - Game.errors) + " / 6");
							statusText.setText(Game.status);
							errorIV.setImage(errorImage[Game.errors]);
							
						} else {

							//Exit message
							displayWord.setText("Thanks for playing!");
							lettersRemaining.setText(" ");
							chancesLeft.setText(" ");
							statusText.setText(" ");
							
							//counts down from 3
							for (int i = 3; i >= 0; i--) {
								lettersRemaining.setText("Shutting down in: " + i);
								Thread.sleep(1000);
							}
							//Closes game
							System.exit(0);
						}
					}
					//Refreshes GUI at the TICKRATE, set in the settings class
					Thread.sleep(Settings.TICKRATE);
				}
			}
		};

		//Starts thread that takes information from Game thread and uses it to update the main GUI
		Thread update = new Thread(updateDisp);
		update.setDaemon(true);
		update.start();

		//Initializes then displays main GUI
		try {
			Pane pane = new Pane();
			pane.setPadding(new Insets(10, 10, 10, 10));
			pane.setStyle("-fx-background-color: #FFFFFF;");

			pane.getChildren().add(displayWord);
			pane.getChildren().add(lettersRemaining);
			pane.getChildren().add(chancesLeft);
			pane.getChildren().add(statusText);
			pane.getChildren().add(errorIV);
			Scene scene = new Scene(pane, 500, 500);
			primaryStage.setTitle(Settings.TITLE);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

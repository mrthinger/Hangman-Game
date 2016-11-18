/*
Evan Pierce
5.26.16
Final Project - Keeps track of letter guesses
JDK 1.8.0
*/
package pierce.evan.hangman;

import java.util.HashMap;

public class LetterBank {
   //Stores if letter has been used or not
   public HashMap<String, Boolean> letters;
   
   public LetterBank(){
       letters = new HashMap<String, Boolean>();
       //Initializes HashMap of all valid guesses
       //TRUE = letter has not been used
       letters.put("a", Boolean.TRUE);
       letters.put("b", Boolean.TRUE);
       letters.put("c", Boolean.TRUE);
       letters.put("d", Boolean.TRUE);
       letters.put("e", Boolean.TRUE);
       letters.put("f", Boolean.TRUE);
       letters.put("g", Boolean.TRUE);
       letters.put("h", Boolean.TRUE);
       letters.put("i", Boolean.TRUE);
       letters.put("j", Boolean.TRUE);
       letters.put("k", Boolean.TRUE);
       letters.put("l", Boolean.TRUE);
       letters.put("m", Boolean.TRUE);
       letters.put("n", Boolean.TRUE);
       letters.put("o", Boolean.TRUE);
       letters.put("p", Boolean.TRUE);
       letters.put("q", Boolean.TRUE);
       letters.put("r", Boolean.TRUE);
       letters.put("s", Boolean.TRUE);
       letters.put("t", Boolean.TRUE);
       letters.put("u", Boolean.TRUE);
       letters.put("v", Boolean.TRUE);
       letters.put("w", Boolean.TRUE);
       letters.put("x", Boolean.TRUE);
       letters.put("y", Boolean.TRUE);
       letters.put("z", Boolean.TRUE);

   }
   
   //Returns valid guesses as string
   public String getRemainingGuesses(){
       String remainingGuesses = "";
       
       for (int i = 0; i < letters.size(); i++){
       String letter = String.valueOf((char)(i + 97));
           if(letters.get(letter).equals(Boolean.TRUE)){
               
               if(i == 0){
                   remainingGuesses += letter;
               }else{
                   remainingGuesses += " " + letter;
               }
           }
           
       }
       
       return remainingGuesses;
   }
   
   //marks a letter as invalid
   public void removeGuess(String g){
       letters.replace(g, Boolean.FALSE); 
   }
   
   //Checks the validity of a single guess
   public boolean checkValidGuess(String g){
       return letters.get(g).booleanValue();
   }
   
}

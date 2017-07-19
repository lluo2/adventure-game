import java.util.Scanner;

/**
 * PlayGame.java
 * Written By: Adrianna Valle & Jessenia Aquilar & Lauren Luo
 * Written On: Dec 19, 2016
 * 
 * Driver class. User can use PlayGame class to play the game through
 * the interactions panel and user input. Instantiates a GameMap object.
 */

public class PlayGame {
	private static String hangmanFile = "C:/workspace/adventure-time/src/hangmanText.txt";
	
	public static void main(String[] args) {
	  
		Scanner scan = new Scanner(System.in);
		String resp = "y";
		Boolean won = false;
    
		do {
			//User input to start a new game or start game for first time.
			System.out.println("Welcome to Aventure Time!!\nDo you want to play? Y/N");
			resp = scan.nextLine().toLowerCase(); //nextLine to account for blank entries
      
			while(!resp.equals("y") && !resp.equals("n")) {
				System.out.println("Please enter a valid response to proceed.\nDo you want to play? Y/N");
				resp = scan.nextLine();
			}
      
			if(resp.equals("n")) break; //User doesn't want to play anymore      
      
			GameMap game = new GameMap(); //Start game properties
  
			while(!game.endOfMap() && game.chancesLeft()) {
				//Showing rooms.
				System.out.println("\nEnter the room number you wish to enter. Enter Q to quit. (-)locked & (+)unlocked");
				System.out.println(game.printRooms());
      	
				//Gets the response for the room number.
				String choosenRoom = "";
				while(game.getRoom(choosenRoom)==null&& !choosenRoom.equals("q")) {
					try {
						System.out.println("Please select a valid door number.");
						choosenRoom = scan.nextLine().toLowerCase();
					} catch(IllegalArgumentException ex) {
						System.out.println("Input is not a valid number entry.");
					} catch(NullPointerException ex) {
						System.out.println("Input is not a valid number entry.");
					}
				}
        
				//User doesn't want to play anymore
				if(choosenRoom.equals("q")) { resp = "n"; break; } 
        
				Room selectedRoom = game.getRoom(choosenRoom); 
				Door selectedDoor = game.getDoor(selectedRoom);
      		
				//Check if door is locked or not
				if(selectedDoor.isLocked()) {
					System.out.println("\n"+selectedDoor.getLockedMsg());
					String choice = "n";
					//key loop: until door is resolved or you have no more chances
					while(!choice.equals("q") &&!choice.equals("y") && game.chancesLeft()) {
						//Deals with user interaction and correct response
						System.out.println("You have " + game.getChances()+" chances left to guess wrong or the dragon carries your Don off!\n");
						System.out.println("Type the name of the key you want to view. Type Q to quit the game");
						System.out.println(game.printKeys());
            
						String selectedKeyStr =scan.nextLine().toLowerCase();    
						//Checks if key is in the list
						while(!game.getCurrentRoom().validKey(selectedKeyStr) && !selectedKeyStr.equals("q")) {
							System.out.println("Response is invalid. Type in a vaild key or enter Q to quit.");
							selectedKeyStr = scan.nextLine().toLowerCase();
						}
      				
						if(selectedKeyStr.equals("q")) { resp = "n"; break; } //User doesn't want to play anymore
            
						Key choosenKey = game.getCurrentRoom().getKey(selectedKeyStr);
						System.out.println("\nThe key tells you: " +choosenKey.getActiveMsg());
            
						//User interaction to get it to get a vaild response 
						System.out.println("Is this the key you choose to use to unlock?(Y/N). Enter Q to Quit."); 
						choice = scan.nextLine().toLowerCase();
	      				
						while(!choice.equals("y") && !choice.equals("n") && !choice.equals("q")) {
							System.out.println("Response is invalid. Enter y/n or Q to quit.");
							System.out.println("Is this the key you choose to use to unlock the door?(Y/N). Enter Q to Quit."); 
							choice = scan.nextLine().toLowerCase();
						}
      				
						if(choice.equals("q")) { resp = "n"; break; }
            
						if(choice.equals("y")) {
							if(selectedDoor.rightKey(choosenKey)) {
								System.out.println("\n"+selectedDoor.getUnlockedMsg()+"\n");
								game.unlockDoor(selectedRoom);
							} else {
								System.out.println("The key says: " + choosenKey.getInactiveMsg());
								System.out.println("Oh no! That's not the right key!");
								game.wrongAnswer();
								choice = "n";
							}
						} 
					}
				} else { //Door is not locked
					System.out.println("\n" + selectedDoor.getUnlockedMsg());
          
					//Ensures correct user response
					System.out.println("Do you wish to enter room "+ selectedRoom + "?(Y/N) Enter Q to quit.");
					String enteringDoorResp = scan.nextLine().toLowerCase();
					while(!enteringDoorResp.equals("y") && !enteringDoorResp.equals("n") && !enteringDoorResp.equals("q")) {
						System.out.println("Response is invalid. Enter a vaild response");
						System.out.println("Is this the key you choose to use to unlock the door?(Y/N). Enter Q to Quit."); 
						enteringDoorResp = scan.nextLine().toLowerCase();
					}
          
					if(enteringDoorResp.equals("q")) { resp = "n"; break; }
          
					if(enteringDoorResp.equals("y")) {
						game.setCurrentRoom(selectedRoom);
					}	
				}
        
				if(resp.equals("n")) break;
			} //end of while loop
      
			if(game.endOfMap()) {
				Hangman minigame = new Hangman(hangmanFile);
				won = minigame.playHangman();
			}
      	
			if(won) { //winning message
				StringBuilder winnermsg = new StringBuilder();

				winnermsg.append("You entered the room and managed to save your Don right before the Dragon took him away! He tells you,'Aahh my hero' right before he takes you in for a kiss\n");
				winnermsg.append("     Congrat   ulation     \n");
				winnermsg.append("  Congratulat s!Congratul  \n");  		
				winnermsg.append("Congratulations!Congratulat\n");      		
				winnermsg.append("Congratulations!Congratulat\n");      		
				winnermsg.append("  Congratulations!Congrat  \n");      		
				winnermsg.append("     Congratulations!C     \n");     		
				winnermsg.append("       Congratulatio       \n");      		
				winnermsg.append("          Congrat          \n");      		
				winnermsg.append("            Con            \n");      		
				winnermsg.append("Would you like to play again?(Y/N)");
      		
				System.out.println(winnermsg.toString());
      		
				resp =scan.nextLine().toLowerCase();
				while(!resp.equals("y") && !resp.equals("n")) {
					System.out.println("Response is invalid.");
					System.out.println("Would you like to play again?(Y/N)");
					resp = scan.next();
				}
			} else { //game was lost, loser message
				System.out.println("Oh no! One of the dragon's henchmans approaches you and tells you the dragon knew you were coming and hid him again!");
				System.out.println("You have to go save him! Would you like to play again?(Y/N)");
				resp =scan.nextLine().toLowerCase();
      
				while(!resp.equals("y") && !resp.equals("n")) {
					System.out.println("Response is invalid.");
					System.out.println("Would you like to play again?(Y/N)");        
					resp = scan.next();
				}
			}  
		} while(resp.equals("y"));
    
		System.out.println("Sorry to see you go! Come again!");
		scan.close();
	}
}
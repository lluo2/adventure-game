/**
 * GameMap.java
 * Written By: Lauren Luo & Adrianna Valle
 * Written On: Dec 16, 2016
 * 
 * Game map creates the conditions for a game using information that a user enters for the
 * key text responses, the door text responses, the images, etc. Note that the rooms and weights do not correspond
 * nor do they need to be in a certain order with regards to the map.
 * 
 * This class allows you to create a game in which the weights of the entered .tgf file corresponds
 * to a specified door reference. If the door contains a weight of zero, that mean the door is unlocked
 * and no further action is required.
 * 
 * TRAVERSING:Every key value present contains a corresponding door value in which they interact together depending on
 * the decisions the user makes. These values are saved in their own dictionary. Every room contains keys that corresponds
 * to all of the weighted edges[represented as doors]that connect to other rooms. In total, every room contains 
 * 10 keys where some keys correspond to a door that connects from the room as some that are used merely to challenge 
 * the player.
 * 
 * ORIENTATION: The number value assigned to the room name is used to help the user orient themselves around the game.
 * The structure is maze like and therefore, the user can not enter the same room various times in order to find the end.
 * 
 * HOW TO WIN: The player does not necessarily need to unlock all the doors but must reach the assigned final room and
 * complete the minigame. What's more, the player needs to not run out of chances or the lose the game.
 * 
 * The user of this class MUST:
 * #1Create a .tgf with vertices that correspond to a saved image file name.
 * #2 Not include multiple edges of the same weight unless it is of weight 0.
 * #3 Must create the Needed Text file for the Key and Dictionary in the exact format specified throughout the program.
 * 
 * OVERALL GAME EXPANSION: 
 * 1--> Control more of the IO input.
 * 2--> Automatically determine best starting and final room ad sets it
 * 3--> Use polymorphism to create diversity in key door interaction.
 * 4--> More game features such as mingames, player character
 */
import java.util.*;
import java.io.*;

public class GameMap {
  //instance variables
  private AdjMatGraph<String> map; 
  private Room currentRoom, finalRoom;
  private LinkedList<Room> allRooms;
  private Hashtable<Room, LinkedList<Room>> rooms;
  private static Hashtable<Integer, Key> allKeys; //will contain 25 keys
  private static Hashtable<Integer, Door> allDoors;
  private int chancesLeft;
  private static final int MAX_CHANCES = 3; 
  
  public GameMap() {
    map = new AdjMatGraph<String>();
    try {
      AdjMatGraph.loadTGF("gameMap.tgf", map);   //Check if valid tgf file
    } catch (FileNotFoundException ex) {
      System.out.println("error: file not found");
    }
    
    chancesLeft = MAX_CHANCES;
    allRooms = new LinkedList<Room>();
    rooms = new Hashtable<Room, LinkedList<Room>>();
    allKeys = new Hashtable<Integer, Key>();
    allDoors = new Hashtable<Integer, Door>();
    
    buildKeyDict("keyText.txt");
    buildDoorDict("doorText.txt");
    buildRooms();
    
    setCurrentRoom(findRoom("1room")); 
    setFinalRoom(findRoom("10room"));
  }
  
  /** Builds the key Dictionary out of a text file that contains the key name, a message to give
    * when user first interacts with it and a message it gives if it is not one of the
    * designated keys for the room.
    *  
    * EXPANSION CAPABILITIES: Allows for the key dictionary to expand increasing the possible map size.
    * ASSUMPTIONS: Assumes the user enters a vaild format of the necessary text. Note some special characters
    * are not accepted.
    * FUTURE EXPANSION:Could allow for a scanner option for input and resolve special character issue
    *
    * @param: String TextFile @return: none*/
  private void buildKeyDict(String fileIn) {
    try {
      Scanner sc = new Scanner(new File(fileIn));
      int count = 1;
      
      while (sc.hasNextLine()) {
        
        String[] temp = sc.nextLine().trim().split(" \\+ ");     
        allKeys.put(count, new Key(temp[0], temp[1], temp[2]));
        count++;
      } 
      sc.close();
    } catch (IOException ex) {
      System.out.println("error in reading keys from file");
    }
  } 
  
  /** Builds the Door Dictionary out of a text file that contains a messsage when the door
    * is locked and a message it returns when it is sucessfully unlocked by a key.
    * 
    * EXPANSION CAPABILTIES: Allows for the number of doors to increase therefore the number
    * of connections between rooms.
    * ASSUMPTIONS: Assumes user formatted the file correctly. Note some special characters are not accepted.
    * FUTURE EXPANSION: Could allow for a scanner option for input and could allow for special characters.
    * 
    * @param: String TextFile @return: none*/
  private void buildDoorDict(String fileIn) {
    try {
      Scanner sc = new Scanner(new File(fileIn));
      int count = 0;  
      while (sc.hasNextLine()) {
        String[] temp = sc.nextLine().trim().split(" \\+ "); 
        allDoors.put(count, new Door(count, temp[0], temp[1]));
        count++;
      }      
      sc.close();      
    } catch (IOException ex) {
      System.out.println("error in reading doors from file");
    }
  } 
  
  /**Builds the rooms from the tgf vertices where all of the names correspond to a saved img. In building
    * the rooms also finds the keys that must go in the room[determined by the weighted edges which are
    * equivalent to the doors] from allKeys and stores it. Also, creates a dictionary of the rooms, and one
    * for the connecting rooms to refer to.
    * EXAPNSION CAPABILITIES: Allows you to build lots of rooms 
    * ASSUMPTIONS: User enters correct img filename.
    * FUTURE EXPANSION: It may be better to organize our rooms in a different structure.*/
  private void buildRooms() {
    //Loads the img names[without '.jpg'] onto an array 
    String[] vertices = new String[map.n()];    
    for (int h = 0; h < map.n(); h++) {
      vertices[h] = map.getVertex(h);
    }
    
    //Gets the connecting room names for each room
    for (int i = 0; i < map.n(); i++) {
      LinkedList<String> successors = map.getSuccessors(vertices[i]);
      
      //Loads the keys that correspond to any of the locked doors that connect from the room.
      LinkedList<Key> activeKeys = new LinkedList<Key>();
      for (int j = 0; j < successors.size(); j++) {
        int keyNum = map.getWeight(vertices[i], successors.get(j));
        if(keyNum!=0)                               //no key is loaded for a weight of zero.
          activeKeys.add(allKeys.get(keyNum));
        
      }
      
      String fileName = vertices[i] + ".jpg";
      allRooms.add(new Room(fileName, activeKeys)); //Creates the room with coresponding img name and keys & stores it.
    }
    
    //Creates a Dictionary of all the rooms with their connecting rooms
    for (int k = 0; k < map.n(); k++) {
      LinkedList<String> successors = map.getSuccessors(vertices[k]);
      LinkedList<Room> successorRooms = new LinkedList<Room>();
      
      for (int m = 0; m < successors.size(); m++) {
        successorRooms.add(findRoom(successors.get(m)));
      }
      rooms.put(findRoom(vertices[k]), successorRooms);
    }  
  }
  
  /*SETTER: Allows the user to set the starting room. Setting also allows for
   * a room traversal to be simulated as the player enters different rooms.
   * 
   * EXPANSION CAPABILITES: Can choose any room to be the first room.
   * ASSUPTIONS: none
   * FUTURE EXPANSION: can select any room at random and from this, use this point of reference
   * to create pointer to final room
   * @param: Room selectedRoom @return: -- */
  public void setCurrentRoom(Room selectedRoom) {
    currentRoom = selectedRoom;
  }
  
  /**GETTER: Returns the current room
    * @param: -- @reuturn: Room currentRoom */
  public Room getCurrentRoom() {
    return currentRoom;
  }
  
  /*SETTER: Allows the user to set the final room. 
   * 
   * EXPANSION CAPABILITES: Can choose any room to be the final room.
   * ASSUPTIONS: The room isn't close enough to the start where it will end the game to quickly
   * FUTURE EXPANSION: Using a traversal to find a far enoguh room to be the future room. User won't need to
   * manually enter it.
   * @param: Room selectedRoom @return: -- */
  public void setFinalRoom(Room selectedRoom) {
    finalRoom = selectedRoom;
  }
  
  /**GETTER: Returns the final room
    * @param: -- @reuturn: Room finalRoom */
  public Room getFinalRoom() {
    return finalRoom;
  }
  
  /** Returns the Room with the corresponding room name. If no room is found, returns null
    * NOTE: When using findRoom, check if null before assigning it anywhere.
    * @param: String roomName @return: Room correspondingRoom */
  private Room findRoom(String roomName) {
    for (int i = 0; i < map.n(); i++) {
      if (allRooms.get(i).getRoomName().equals(roomName)) {
        return allRooms.get(i);
      }
    }
    return null;
  }
  
  /** Returns the room that is in the list of the connecting rooms to the current room. If the roomName
    * is not a corresponding room, returns null.
    * Note: When using getRoom, check if null before assigning it anywhere.
    * @param: String roomName @return: Room correspondingRoom*/
  public Room getRoom(String roomName) {     
    for (int i = 0; i < getConnectingRooms().size(); i++) {
      if (getConnectingRooms().get(i).getRoomName().equals(roomName+"room")) {  
        return getConnectingRooms().get(i); 
      }
    }
    return null;
  }    
  
  /** Returns a list of the rooms that connect to the current room.
    * @param: -- @return: LinkedList<Room> connectingRooms */
  public LinkedList<Room> getConnectingRooms() {
    return rooms.get(currentRoom);
    
  }
  
  /** Returns true if user player reaches the final room.
    * @param: -- @return: boolean foundFinalRoom */
  public boolean endOfMap(){
    return currentRoom.equals(finalRoom);
  }
  
  
  /**GETTER: Returns a randomly selected key from the key Dictionary.Used to fill the spots of the remaining
    * keys needed[to make 10] in order to challenge the user.
    * @param: -- @return: Key randomKey */
  public static Key getRandomKey() {
    int ran = (int)((Math.random() * 25) + 1);
    return allKeys.get(ran);
  }
  
  /**Returns all of the keys that exist. Static in order to be accessed by the room class. 
    * @param: -- @return: Hashtable<Integer, Key> allKeys */
  public static Hashtable<Integer, Key> getAllKeys() {
    return allKeys;
  }  
  
  /**Returns the Door of that connects the currentRoom with the selectedRoom by getting the weight
    * and pulling the assigned integer door value from the door dictionary.
    * @param: Room selectedRoom @return: Door connectingDoor */
  public Door getDoor(Room selectedRoom) {
    int doorNum = map.getWeight(currentRoom.getRoomName(), selectedRoom.getRoomName());
    return allDoors.get(doorNum);
  }
  
  /** Replaces the door poniter to that of door zero to simulate the door being unlocked.
    * After this, the door can be entered however many times and would still be unlocked.
    * @param: @return: -- */
  public void unlockDoor(Room lockedRoom) { 
    map.addEdge(currentRoom.getRoomName(),lockedRoom.getRoomName(),0);
  }
  
  
  /** Returns whether the player still have chances to guess wrong.
    * @return: boolean chancesExist @param: -- */
  public boolean chancesLeft() {
    return chancesLeft > 0;
  }
  
  /**Returns the number of chances left to guess wrong.
    * @param: -- @return: int numberOfChances */
  public int getChances() {
    return chancesLeft;
  }
  
  /** Decrements the number of chances when the player guesses incorrectly.
    * @param: --  @return: -- */
  public void wrongAnswer() {
    chancesLeft--;
  }
  
  /** Prints a string representation of the possible rooms the player can enter. Only used for the Driver
    * class.*/
  public String printRooms() {
    String s = "";
    LinkedList<Room> neighbors = getConnectingRooms();
    for (int i = 0; i < neighbors.size(); i++) {
      String name = neighbors.get(i).getRoomName();
      s += "[" + name.substring(0,name.indexOf("r"));
      if (map.getWeight(currentRoom.getRoomName(),neighbors.get(i).getRoomName()) == 0) {
        s += "+";
      } else {
        s += "-";
      }
      s += "]";
    }
    return s;
  }
  
  /** Prints a string representation of the possible keys a player can enter. Only used for the Driver Class. */
  public String printKeys() {
    String s = "";
    Key[] currentKeys = currentRoom.getRoomKeys();
    for (int i = 0; i < currentKeys.length; i++) {
      s += "[" + currentKeys[i].getName() + "]";
    }
    return s;
  }
  
  public static void main(String[] args) {
    //Testing AdjMatGraph with game map graph, with (test) and without weights (test2)
//    AdjMatGraph<String> test = new AdjMatGraph<String>();
//    try {
//      AdjMatGraph.loadTGF("gameMapNoWeights.tgf", test);
//    } catch (FileNotFoundException ex) {
//      System.out.println("error: file not found");
//    }
//    System.out.println(test);
    
//    AdjMatGraph<String> test2 = new AdjMatGraph<String>();
//    try {
//      AdjMatGraph.loadTGF("gameMap.tgf", test2);
//    } catch (FileNotFoundException ex) {
//      System.out.println("error: file not found");
//    }
//    System.out.println(test2);
    
//  GameMap map = new GameMap();
    
//    map.printAllKeys();
//    System.out.println();
//    map.printAllDoors();
//    System.out.println();
//    map.printAllRoomNeighbors();
//    System.out.println();
//    map.printRooms();
//    map.printMapGraph();
//    System.out.println(map.contains(4));
  }
}
import java.util.LinkedList;
import java.util.Random;

/**
 * Room.java
 * for Adventure Time
 * Created by: Jessenia Aguilar-Hernandez
 * Modified By: Lauren Luo & Adrianna Valle
 * 
 * Creates a Room object that contains an array of 10 keys. 
 * For each room that is connected to this room, the karray
 * holds a key that can unlock the door to the connecting room. 
 */

public class Room implements Comparable<Room> {
	private String img;
	private Key[] karray;//holds the keys in the room
	private final int DEFAULT_CAPACITY = 10;
  
	/**
	 * Constructor takes in image file name, and the active key in the room 
	 */
	public Room(String imgFile, LinkedList<Key> activeKeys) { 
		img = imgFile;
    
		karray = new Key[DEFAULT_CAPACITY]; //each room will always have 10 keys
		for (int i = 0; i < activeKeys.size(); i++) {
			karray[i] = activeKeys.get(i);
		}
    
		for (int i = activeKeys.size(); i < karray.length; i++) {
			Key temp = GameMap.getRandomKey(); //assigns random keys to the remaining key slots in karray
			while(contains(temp)) {
				temp = GameMap.getRandomKey();
			}
			karray[i] = temp.copyKey();
		}
    
		//Shuffles the key placement so that all the active keys are not in the beginning
		Random ran = new Random();
		for(int j = 0; j<karray.length; j++) {
			int swapVal = ran.nextInt(karray.length-1)+1;                         
			Key tempEle = karray[swapVal];
			karray[swapVal] = karray[karray.length-1-j];
			karray[karray.length-1-j] = tempEle;   
		}
	}
  
	/**
	 * Returns true if karray contains given key
	 * Otherwise returns false
	 */
	private boolean contains(Key kIn) {
		for (int i = 0; i < karray.length; i++) {
			if (karray[i] != null && karray[i].getName().equals(kIn.getName())) {
				return true;
			}
		}	
		return false;
	}
  
	public String getRoomName() {
		return img.substring(0,img.indexOf("."));
	}
  
	/**
	 * Getter: @return the keys in the room
	 */
	public Key[] getRoomKeys() { return karray; }
  
	/**
	 * Checks if the input key name is in this room's karray (is it a valid key)
	 * @return boolean true if valid, else false
	 */
	public boolean validKey(String keyString) {
		for (Key k: karray) {
			if (k.getName().toLowerCase().equalsIgnoreCase(keyString)) {
				return true;
			}
		}
		return false;
	}
  
	/**
	 * Takes in name of the key and returns the Key object with that name.
	 * If key doesn't exist in this room's karray, returns null.
	 */
	public Key getKey(String nameOfSelectedKey) {
		for (int i = 0; i < karray.length; i++) {
			if (karray[i].getName().equalsIgnoreCase(nameOfSelectedKey)) {
				return karray[i];
			}
		}
		return null;
	}
  
	/**
	 * compareTo: @return returns an int based on the string compareTo of room names
	 */
	public int compareTo(Room other) {
		//calls on integers compareTo
		return getRoomName().compareTo(other.getRoomName());
	}
  
	/**
	 * Returns String representation of the object.
	 * For testing purposes
	 */
	public String toString() {
		return img.substring(0,img.indexOf("r"));
	}
}
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayGameGui {
  
  //things for the function of the game
  private GameMap game;
  private final int MAX_DOORS = 4;
  private final int MAX_KEYS = 10;
  private final int MAX_IMG_X = 815;
  private final int MIN_IMG_X = 200;
  private final int MAX_IMG_Y = 340;
  private final int MIN_IMG_Y = 40;
  private int numOfButts;  //trace...
  private Boolean won = false;
  private String spaces; //trace...
  
  private Key pickedKey;
  private Door pickedDoor;
  private Room pickedRoom;

  //containers
  private JFrame frame;
  private JPanel instructs;

  //objects for the panel
  private JButton startButt;
  private JButton isSure;
  private JButton notSure;
  
  private JButton[] roomButts;//holds the generated door buttons to enter other rooms
  private JButton[] keyButts;//holds the generated key buttons 
  
  private JLabel instructionTxt;
  private JLabel gameTxt;
  private JLabel doorTxt;
  private JLabel chancesL;
  private JLabel keyTxt;
  private JLabel areYouSure;
  
  private ImageIcon imageG;
  private JLabel imgLabel;
  
  //frame and panel will be created but mostly just the frame
  public PlayGameGui(){
    numOfButts = 0;//keeps track of how many buttons where generated for roomButts
    spaces = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; //for aesthic purposes
    game = new GameMap();
    pickedKey = null;
    pickedDoor = null;
    pickedRoom = null;
    
    instructs = new JPanel();
    instructs.setLayout(null);
    
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(870,700));
    
    frame.setTitle("Adventure Time");
    frame.setResizable(false);
    frame.pack();
    
    
    startButt = new JButton("");
    
    /*Objects created in the constructer are place on the panel
     * and setVisibility to false unless noted bc it makes it 
     * easier to remove, setVisible(true) or manipulate variables
     * */
    
    isSure = new JButton("YES!");
    event5 eventSure = new event5();
    isSure.addActionListener(eventSure);
    instructs.add(isSure);
    isSure.setVisible(false);
    isSure.setBounds(250, 640, 50,30 );
    
    notSure = new JButton("No!");
    event5 eventNotSure = new event5();
    notSure.addActionListener(eventNotSure);
    instructs.add(notSure);
    notSure.setVisible(false);
    notSure.setBounds(310, 640, 50,30 );
    
    roomButts = new JButton[MAX_DOORS];
    keyButts = new JButton[MAX_KEYS];
    
    instructionTxt = new JLabel("");
    gameTxt = new JLabel("");
    doorTxt = new JLabel("");
    
    keyTxt = new JLabel("");
    instructs.add(keyTxt);
    keyTxt.setVisible(false); 
    keyTxt.setBounds(190 , 470, 680, 235);
    
    chancesL = new JLabel("");
    instructs.add(chancesL);
    chancesL.setVisible(false); 
    chancesL.setBounds(650,450, 120,50);
    
    areYouSure = new JLabel("");
    instructs.add(areYouSure);
    areYouSure.setVisible(false);
    areYouSure.setBounds(190 , 495, 680, 235);
    
    imageG = new ImageIcon("10room.jpg");
    
    imgLabel = new JLabel(imageG);
    instructs.add(imgLabel);
    Color customColor2 = new Color(0,0,0);
    imgLabel.setOpaque(true);
    imgLabel.setBackground(customColor2);
    imgLabel.setBounds(190 , 30, 680, 370);

  }
  
  /*the first page the user encounters with
   * will begin the entirety of the game by calling
   * action listener also sets up how the static 
   * vars are going to look like for the entirety of the game
   * 
   * */
  public void startPage(){
    
    JLabel titleTxt = new JLabel("");
    instructs.add(titleTxt);
    titleTxt.setOpaque(true);
    Color black = new Color(0,0,0);
    titleTxt.setBackground(black);
    titleTxt.setBounds(190 , 0 , 680, 30);
    
    //this will set the instructions on the side column and will not be changed throughout the game
    instructionTxt.setText("<html><font color= 'white'><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adventure Time &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Instructions</b><br><br>"
                                         +"&nbsp;&nbsp;This game simulates a role playing game in which, you the user, chooses"+
                                       "a path to take in order to save your beloved Don who is kept prisoner by the Dragon. Along your journey through this maze, you will encounter many challenges."+
                                         "There will be doors presented to you some of which you cannot enter so you will have to find the Keys that open the doors."+
                                       "Each door will give you a hint as to what it requires to be opended and the doors already opened will be unlocked. Keep in mind that all items in the room want"+
                                       "to be used so they may lie and try to trick you to choose them, however, only one is a key to open the door. "+
                                       "If you happen to choose the wrong key three times, we're sorry but the dragon will then become aware of your presence"+
                                       " and move your Don to a different location and you will lose the game. Your goal is to get to the final final room"+
                                        " where you will face the final challenge and win or lose you Don (and the game).</font><html>");
    instructionTxt.setFont(new Font("Source Code Pro Light",Font.BOLD, 10)); //Serif
    instructs.add(instructionTxt);
    instructs.setForeground(new Color(0xffffdd));
    instructionTxt.setOpaque(true);
    instructionTxt.setBackground(black);
    instructionTxt.setBounds(0 , 0 , 190, 678);
    
    
   
    String x = "<html>"+spaces+spaces+ "&nbsp;&nbsp;&nbsp;Welcome to ADVENTURE TIME</html>";
    gameTxt.setText("<html><font color = 'lime' size = '5'>" + x+"");
    instructs.add(gameTxt);
    gameTxt.setOpaque(true);
    gameTxt.setBackground(black);
    gameTxt.setBounds(190 , 400, 680, 235);
    
    
    
    startButt = new JButton("Start Game");
    event3 eve = new event3();
    startButt.addActionListener(eve);//calls to action listener and when pressed the game begins
    instructs.add(startButt);
    startButt.setBounds(480 , 640, 100, 30);

    
    JButton quit = new JButton("Exit");
    event2 ev = new event2();
    quit.addActionListener(ev);
    instructs.add(quit);
    quit.setBounds(600 , 640, 100, 30);
    
    frame.add(instructs)  ; 
    frame.setVisible(true);
  }
  
  /**
   * sets the domino effect for the rest of the game
   * **/
  public void startGame(){
    instructs.remove(startButt);
    gameTxt.setText("");
    createRoom();
    
  }
  
  /**
   * sets the display for the rest game
   * **/
  public void createRoom(){
    
    if(!game.endOfMap() && game.chancesLeft()){
      
      String imgNom = game.getCurrentRoom().getRoomName();
      ImageIcon n = new ImageIcon(imgNom);
      
      gameTxt.setText("<html><font color = 'lime' size = '5'>"+spaces + spaces+"&nbsp;&nbsp;&nbsp;"+
                      "Click the room number you wish to enter. </font></html>");
      LinkedList<Room> neighbors = game.getConnectingRooms();
      Color gray = new Color(211,211,211);
      
      //generates rooom buttons to enter other rooms
      for (int i = 0 ; i < neighbors.size(); i++){
        String name = neighbors.get(i).getRoomName();
        roomButts[i] = new JButton(name);
        event e = new event();
        roomButts[i].addActionListener(e); 
        roomButts[i].setForeground(new Color(0,128,0));
        roomButts[i].setBackground(gray);
        instructs.add(roomButts[i]);
        roomButts[i].setBounds(300 + (130 * i),410, 75,35);
        numOfButts++;
        
      }
      }
    if(game.endOfMap()){ //hangman
      System.out.println("almost done");
    }
       
  }
  
  
  public void createKeys(){
     Key[] currentKeys = game.getCurrentRoom().getRoomKeys();//gets an array of all the keys in the room
      Random ran = new Random();
      
      //makes keys show up on screen in random x & y coordinates
      for (int i = 0 ; i < MAX_KEYS; i++){
        String name = currentKeys[i].getName() ;
        String bImage = name + ".jpg";
        int x = ran.nextInt((MAX_IMG_X - MIN_IMG_X) + 1) + MIN_IMG_X;
        int y = ran.nextInt((MAX_IMG_Y - MIN_IMG_Y) + 1) + MIN_IMG_Y;
        
        keyButts[i]= new JButton(new ImageIcon(bImage,bImage));
        keyButts[i].setOpaque(true);
        event4 e4 = new event4();
        keyButts[i].addActionListener(e4);
        keyButts[i].setVisible(true);
        instructs.add(keyButts[i]);
        keyButts[i].setBounds(x,y, 30,30);              
      }

  }
  
  /**
   * while taversing through a door actions will take place
   * **/
  public void goThroughDoor(Room rM){
     Door selectedDoor = game.getDoor(rM);
     pickedDoor = selectedDoor;
     
                              
      if(selectedDoor.isLocked()){
        gameTxt.setText("<html><font = 'Source Code Pro Light' color = 'lime' size = '4'>" +selectedDoor.getLockedMsg()+"</html>");
        
        //NEEDS TO MOVED BETTER LEFT FOR LATERS
        chancesL.setText("<html>You have " + game.getChances()+" chances left to guess wrong or the dragon carries your Don off!</html>");
        chancesL.setVisible(true);
        instructs.add(chancesL);
        chancesL.setBounds(200,450, 120,35);
        
      }
      else{
        gameTxt.setText("<html><font = 'Source Code Pro Light' color = 'lime' size = '4'>" +selectedDoor.getUnlockedMsg()+"</html>");
        game.setCurrentRoom(rM);
        createRoom();
      }
  }
  
  public void useKey(Key k){
    keyTxt.setText("<html><font = 'Source Code Pro Light' color = 'lime' size = '4'>"+ "The key tells you: <br>" + k.getActiveMsg() +"</html>");
    keyTxt.setVisible(true);
    
    areYouSure.setText("<html><font = 'Source Code Pro Light' color = 'lime' size = '4'>"+ "Is this the key you choose to use to unlock?</html>");
    areYouSure.setVisible(true);
    
    isSure.setVisible(true);
    
    notSure.setVisible(true);    
  }
  
  //action listener for door buttons
  public class event implements ActionListener{
    public void actionPerformed(ActionEvent e){
      
      Object o = e.getSource();//gets which button it was from
      JButton b = null;//crerates a new button
      
      for (int i = 0; i < numOfButts ; i++){ //sets that all other buttons can't be pressed once one is pressed
        roomButts[i].setVisible(false);
        roomButts[i].remove(startButt);
        roomButts[i] = null;
       
      }
      numOfButts=0; //resets the count
      
      if(o instanceof JButton){
        b = (JButton)o; //the null button is given the same attributes as source button
      }
      
      String bName = b.getText();//it gets the buttons name
      
      bName = bName.substring(0,bName.indexOf("r"));                                          
      Room selectedRoom = game.getRoom(bName);
      
      pickedRoom = selectedRoom;
      goThroughDoor(pickedRoom);
      
      createKeys();

           
    }
  }
  
   public class event2 implements ActionListener{
    public void actionPerformed(ActionEvent ev) {
      System.exit(0);
    }
    
  }
   
   public class event3 implements ActionListener{
     public void actionPerformed(ActionEvent eve){
       startGame();
     }
   }
   
   public class event4 implements ActionListener{
     public void actionPerformed(ActionEvent even){
      
      String selectedKeyStr = ((ImageIcon)(((JButton)even.getSource()).getIcon())).getDescription(); 
      System.out.println(selectedKeyStr);
      
      for (int i = 0; i < MAX_KEYS ; i++){ //disables all other keys from clicking
        keyButts[i].setEnabled(false); 
      }
      
      Key choosenKey = game.getCurrentRoom().getKey(selectedKeyStr.substring(0,selectedKeyStr.indexOf(".")));
      
      pickedKey = choosenKey;
      
      useKey(pickedKey);
      
     }
   }
   
   public class event5 implements ActionListener{
     public void actionPerformed(ActionEvent event){
       if(event.getSource() == isSure){
         if(pickedDoor.rightKey(pickedKey)){
           keyTxt.setText("<html>" + pickedDoor.getUnlockedMsg() + "</html>");
           game.unlockDoor(pickedRoom);
           for(int i =0; i < MAX_KEYS ; i++){
             keyButts[i].setVisible(false);
             instructs.remove(keyButts[i]);
           }
           areYouSure.setVisible(false);
           
           createRoom();
         }else{
           keyTxt.setText("<html>The key says: " + pickedKey.getInactiveMsg()+ "</html>");
           areYouSure.setText("Oh no! That's not the right key!");
           game.wrongAnswer();
         }
       }else{
         for (int i = 0; i < MAX_KEYS ; i++){
           keyButts[i].setEnabled(true); 
         }
         keyTxt.setText("");
         areYouSure.setText("");
         
       }
       isSure.setVisible(false);
       //instructs.remove(isSure);
       
       notSure.setVisible(false);
       //instructs.remove(notSure);
            
     }
   }
   
  public static void main(String[] args){
    PlayGameGui n = new PlayGameGui();
    n.startPage();
  }
}
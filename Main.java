import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;

import javax.imageio.ImageIO;
import java.io.File;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

import java.lang.reflect.Constructor;

public class Main{
    public static final int MENU_LAYER = 1; //these are just set values used for the JLayeredPane
    public static final int MENU_LAYER_2 = 2;
    public static final int SOUL_LAYER = 3;
    public static final int PROJECTILE_LAYER_HIDDEN = 4;
    public static final int BOX_LAYER = 5;
    public static final int PROJECTILE_LAYER_VISIBLE = 6;
    
    //fonts
    public static Font MAIN_FONT;
    public static Font HUD_FONT;
    public static Font DAMAGE_FONT;
    
    //this is used to store the file path of the battle that is currently loaded
    public static String battleLoaded = "";

    public static void main(String args[]) throws Exception{
        //initialize the fonts used
        MAIN_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/DTM-Sans.otf")).deriveFont(26f);
        HUD_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/MarsNeedsCunnilingus.ttf")).deriveFont(22f);
        DAMAGE_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Hachicro.ttf")).deriveFont(20f);

        //load the various fonts used
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(MAIN_FONT);
        ge.registerFont(HUD_FONT);
        ge.registerFont(DAMAGE_FONT);

        //Open the window
        JFrame frame = new JFrame("Javatale");
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(ImageIO.read(new File("imgs/soul/red.png")));

        frame.addKeyListener(new RLDetector());
        frame.addKeyListener(new UDDetector());
        frame.addKeyListener(new ZXDetector());
        
        //Give credit to Toby (creator of Undertale) here and link to site
        JLabel titleCard = new JLabel(new ImageIcon("imgs/titleCard.png"));
        titleCard.setSize(640, 480);
        
        JPanel panel = new JPanel();
        panel.setSize(640, 480);
        panel.setVisible(true);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(640, 480));
        panel.setBackground(Color.BLACK);
        panel.add(titleCard);
        
        frame.add(panel);
        frame.pack();
        
        Input.clear();
        int timer = 0;
        while(!Input.getZRisingEdge() && timer < 200){ //keep the screen up until the user presses z or for 2 seconds, whichever comes first
            try {
                timer++;
                Thread.sleep(10);
            } catch (Exception e){}
        }
        
        titleCard.setVisible(false); //remove the title card
        panel.remove(titleCard);
        
        //The battle selection menu 
        String[] battleClasses = getModFolders(); //get an array of the filepaths of the battles
        ArrayList<JLabel> battleLabels = new ArrayList<JLabel>();
        
        int rows = 10; //some math thingies to be used later
        int columns = 3;
        int count = 0;
        
        int width = 640 / columns - 3;
        int height = 480 / rows;
        
        //create a grid of the battles in the mod folder for the battle selection screen
        for(String battle : battleClasses){
            JLabel label = new JLabel(battle);
            label.setFont(MAIN_FONT);
            label.setForeground(Color.ORANGE);
            label.setSize(width, height);
            label.setLocation(((count % columns) * width) + (5 * (count % columns)), ((count / columns) * height) + (5 * (count / columns)));
            label.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
            
            battleLabels.add(label);
            panel.add(label);
            
            count++;
        }
        
        int index = 0; //the battle currently selected
        final Color orange = new Color(253, 127, 54); //color of unselected menu item
        
        while (true){
            while(!Input.getZRisingEdge()){ //while nothing selected
                //change which option is selected based on input
                if(Input.getUpRisingEdge() && index >= columns){
                    index -= columns;
                } else if(Input.getDownRisingEdge() && count > index + columns){
                    index += columns;
                }
                
                if(Input.getRightRisingEdge() && index + 1 < count){
                    index += 1;
                } else if (Input.getLeftRisingEdge() && index - 1 >= 0){
                    index -= 1;
                }
                
                //make all labels the default color
                for(JLabel label : battleLabels){
                    label.setForeground(orange);
                    label.setBorder(BorderFactory.createLineBorder(orange, 3));
                }
            
                //update the labels, turning the selected one yellow
                battleLabels.get(index).setForeground(Color.YELLOW);
                battleLabels.get(index).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                
                try {
                    Thread.sleep(10);
                } catch (Exception e){}
            }
        
            //begin the selected battle
            Class<?> c = Class.forName(getBattleClass("mods/" + battleClasses[index] + "/"));
            if(c.getSuperclass().getCanonicalName().equals("Battle")){
                //Set the battleLoaded variable so that filepaths will work
                battleLoaded = battleClasses[index];
                
                //remove the panel containing the battle selection menu
                panel.setVisible(false);
                frame.remove(panel);
                
                //finish creating the selected battle
                Constructor<?> constructor = c.getConstructor(JFrame.class);
                Battle battle = (Battle)constructor.newInstance(frame);
                battle.run();
                
                //clear the frame
                battle.soulPanel.setVisible(false);
                frame.remove(battle.soulPanel);
                
                //re-add the battle selection menu
                panel.setVisible(true);
                frame.add(panel);
            }
        }
    }
    
    public static String[] getModFolders(){
        String[] mods = new File("mods/").list();
        
        if(mods[0].equals(".DS_Store")){
            String[] fullList = new String[mods.length - 1];
            for(int i = 1; i < mods.length; i++){
                fullList[i-1] = mods[i];
            }
            return fullList;
        }
        
        return mods;
    }
    
    public static String getBattleClass(String filepath){
        File folder = new File(filepath);
        String[] files = folder.list();
        
        for(String fileName : files){
            if(fileName.length() > 12){
                int index = fileName.length() - 12;
                if(fileName.substring(index).equals("Battle.class")){
                    return (fileName.substring(0, fileName.length() - 6)); //returns the name of the battle subclass to the array, removing the .class extention     
                }
            }
        }
        
        return "";
    }
}

class RLDetector extends KeyAdapter{
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_RIGHT){
            Input.setRight(true);
        }

        if (keyCode == e.VK_LEFT){
            Input.setLeft(true);
        }
    }

    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_RIGHT){
            Input.setRight(false);
        }

        if (keyCode == e.VK_LEFT){
            Input.setLeft(false);
        }
    }
}

class UDDetector extends KeyAdapter{
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_UP){
            Input.setUp(true);
        }

        if (keyCode == e.VK_DOWN){
            Input.setDown(true);
        }
    }

    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_UP){
            Input.setUp(false);
        }

        if (keyCode == e.VK_DOWN){
            Input.setDown(false);
        }
    }
}

class ZXDetector extends KeyAdapter{
    public void keyPressed(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_Z || keyCode == e.VK_ENTER){
            Input.setZ(true);
        }

        if (keyCode == e.VK_X || keyCode == e.VK_SHIFT){
            Input.setX(true);
        }
    }

    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode();

        if (keyCode == e.VK_Z || keyCode == e.VK_ENTER){
            Input.setZ(false);
        }

        if (keyCode == e.VK_X || keyCode == e.VK_SHIFT){
            Input.setX(false);
        }
    }
}
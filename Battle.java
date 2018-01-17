import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public abstract class Battle{
    public JFrame frame; //the window
    public JLayeredPane soulPanel; //the panel
    public JPanel hiddenPanel; //I don't think this is used, but I'm scared to remove it
    
    //menu labels
    public JLabel fight, act, item, mercy;
    public JLabel name, lv, hp, hpLabel, hpBar, hpBarBackground;
    public Projectile attackCursor;
    
    public DialogueLabel bubble;
    public JLabel bubbleImage;
    
    //variables related to the monster
    public JLabel slash; //the slashing animation (the player's attack animation
    public JLabel damage; //damage display
    public JLabel monsterHealthBar, monsterHealthBarBackground;
    public JLabel monster;
    public String monsterFilePath, monsterHitFilePath, monsterSparedFilePath;
    public double monsterDefense;
    public int monsterAttack;
    public int monsterHealth;
    public int monsterMaxHealth;
    public int goldEarned, expEarned;
    public String monsterName = "Placeholder";
    public String checkText, actOption1, actOption2, actOption3;
    public ArrayList<String> actOptions = new ArrayList<String>();
    public boolean sparable = false; //this will be true when you can win the battle peaceably
    
    public Soul soul; //the little heart you control
    public Attack attack; //the object managing the attack for one turn
    public BoxLabel box; //the dialogue/attack box
    
    public ArrayList<Item> items = new ArrayList<Item>();
    
    //behind-the-scenes variables that control the running of the battle
    public static final int ATTACK_TIMER_MAX = 370; //7 seconds = 370 ticks
    public int attackTimer = 100;
    public int turn = 1;
    public boolean playerTurn = true;
    public int menuPosition = 0;      
    public int subMenuPosition = 0;
    public boolean onSubMenu = false;
    public int subMenuOptions = 1;
    public boolean fightUpdate = false;
    public boolean gameRunning = true;
    public boolean canProceed = false;
    
    public int sleepTime = 0;

    /*Sub Menu Positions Table
     * 0    * 2    |    * 4     * 6
     * 1    * 3    |    * 5     * 7
     */

    public static final int MENU_OPTION_DISTANCE = 152;
    public static final double SOUL_SPEED = 2;

    public Battle(JFrame f){
        frame = f;
        
        //intitialize the soul on the fight menu
        soul = new Soul(31 + 7, 433 + 13); //7, 13 centers it on the menu option, while 31, 411 is the height of it
        defineSoulStats();
        
        //initialize the dialogue box
        box = new BoxLabel(this);
        box.setFont(Main.MAIN_FONT);
        box.setForeground(Color.WHITE);
        box.setVerticalAlignment(SwingConstants.TOP);
        
        //initialize the panel used to display projectiles hidden ouside of the box
        hiddenPanel = new JPanel();
        hiddenPanel.setSize(box.getWidth(), box.getHeight());
        hiddenPanel.setLocation(box.getX(), box.getWidth());
        hiddenPanel.setLayout(null);
        hiddenPanel.setBackground(new Color(0, 0, 0, 0));

        //Initialize the menu labels (each option is 152 to the right of the last one)
        fight = new JLabel(new ImageIcon("imgs/menu/fight_selected.png"));
        fight.setLocation(31, 433);
        fight.setSize(110, 42);
        act = new JLabel(new ImageIcon("imgs/menu/act.png"));
        act.setLocation(33 + MENU_OPTION_DISTANCE, 433);
        act.setSize(110, 42);
        item = new JLabel(new ImageIcon("imgs/menu/item.png"));
        item.setLocation(41 + MENU_OPTION_DISTANCE * 2, 433);
        item.setSize(110, 42);
        mercy = new JLabel(new ImageIcon("imgs/menu/mercy.png"));
        mercy.setLocation(44 + MENU_OPTION_DISTANCE * 3, 433);
        mercy.setSize(110, 42);
        
        attackCursor = new Projectile("imgs/menu/attackCursor.gif", box.getX() + box.getWidth() + 14, box.getY() + 5, new Vector(-SOUL_SPEED * 2, 0), false);
        attackCursor.velocity = new Vector(-SOUL_SPEED * 2, 0);
        attackCursor.setVisible(false);
        
        //Initialize the hpBar
        hpBar = new JLabel(new ImageIcon("imgs/yellow.png"));
        hpBar.setBackground(Color.YELLOW);
        hpBar.setLocation(275, 400);
        hpBar.setSize(soul.hp, 21);
        hpBarBackground = new JLabel(new ImageIcon("imgs/red.png"));
        hpBarBackground.setBackground(Color.RED);
        hpBarBackground.setLocation(275, 400);
        hpBarBackground.setSize(soul.hp, 21);

        //Initialize the text labels
        name = new JLabel(soul.name);
        name.setLocation(30, 403);
        name.setSize(100, 15);
        name.setFont(Main.HUD_FONT);
        name.setForeground(Color.WHITE);
        
        hpLabel = new JLabel(new ImageIcon("imgs/menu/hp.png"));
        hpLabel.setSize(23, 10);
        hpLabel.setLocation(238, 406);
        
        lv = new JLabel("LV " + soul.lv);
        lv.setLocation(149, 403);
        lv.setSize(68, 15);
        lv.setFont(Main.HUD_FONT);
        lv.setForeground(Color.WHITE);

        hp = new JLabel(soul.hp + " / " + soul.maxHP);
        hp.setLocation(hpBarBackground.getX() + soul.maxHP + 14, 403);
        hp.setSize(90, 15);
        hp.setFont(Main.HUD_FONT);
        hp.setForeground(Color.WHITE);
        
        //set up the monster's label, stats, and act options
        monster = new JLabel();
        setUpMonster();
        monsterFilePath = "mods/" + Main.battleLoaded + "/" + monsterFilePath; //adjust the filepath so that the root directory is the main project folder, not the mod folder
        monsterHitFilePath = "mods/" + Main.battleLoaded + "/" + monsterHitFilePath; //adjust the filepath so that the root directory is the main project folder, not the mod folder
        monsterSparedFilePath = "mods/" + Main.battleLoaded + "/" + monsterSparedFilePath; //adjust the filepath so that the root directory is the main project folder, not the mod folder
        monster.setIcon(new ImageIcon(monsterFilePath));
        actOption1 = (new Item(actOption1, 0)).name; //make the act options exactly 8 characters long
        actOption2 = (new Item(actOption2, 0)).name; //make the act options exactly 8 characters long
        actOption3 = (new Item(actOption3, 0)).name; //make the act options exactly 8 characters long
        if(actOption1 != null){
            actOptions.add(actOption1); System.out.println("1");}
        if(actOption2 != null){
            actOptions.add(actOption2); System.out.println("2"); }
        if(actOption3 != null){
            actOptions.add(actOption3); System.out.println("3");}
        
        //fill item arraylist with items
        setUpItems();
        
        //set up the dialogue bubble
        bubbleImage = new JLabel();
        bubbleImage.setLocation(monster.getX() + monster.getWidth(), monster.getY() - 27);
        bubbleImage.setIcon(new ImageIcon("imgs/bubble.png"));
        bubbleImage.setSize(217, 120);
        bubbleImage.setVisible(false);
        
        bubble = new DialogueLabel();
        bubble.setLocation(bubbleImage.getX() + 37, bubbleImage.getY());
        bubble.setSize(bubbleImage.getWidth() - 37 - 5, bubbleImage.getHeight()); //-14 for the left border, -2 for the right border
        bubble.setFont(Main.MAIN_FONT.deriveFont(20f));
        bubble.setBackground(new Color(0, 0, 0, 0));
        bubble.setVerticalTextPosition(JLabel.TOP);
        bubble.setVerticalAlignment(JLabel.TOP);
        bubble.setVisible(false);
        //bubble.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0)); //the bubble border. say that five times fast
        
        //set up the attack graphic
        slash = new JLabel(new ImageIcon("imgs/slash.gif"));
        slash.setSize(26, 110);
        slash.setLocation((monster.getX() + monster.getWidth()/2 - slash.getWidth() / 2), (monster.getY() + monster.getHeight()/2 - slash.getHeight() / 2));
        slash.setVisible(false);
        
        //set up the damage label
        damage = new JLabel();
        damage.setFont(Main.DAMAGE_FONT);
        damage.setSize(200, 30);
        damage.setLocation((monster.getX() + monster.getWidth()/2 - damage.getWidth() / 2), (monster.getY() + monster.getHeight()/2 - damage.getHeight() / 2) - 20);
        damage.setForeground(Color.RED);
        damage.setBackground(Color.BLACK);
        damage.setHorizontalAlignment(JLabel.CENTER);
        
        //set up the monster's health bar
        monsterHealthBarBackground = new JLabel(new ImageIcon("imgs/menu/gray.jpg"));
        monsterHealthBarBackground.setBackground(Color.GRAY);
        monsterHealthBarBackground.setSize(monsterMaxHealth, 10);
        monsterHealthBarBackground.setLocation((monster.getX() + monster.getWidth()/2 - monsterMaxHealth / 2), (monster.getY() + monster.getHeight()/2 - 5) + 20);
        monsterHealthBarBackground.setVisible(false);
        
        monsterHealthBar = new JLabel(new ImageIcon("imgs/menu/green.jpg"));
        monsterHealthBar.setBackground(Color.GREEN);
        monsterHealthBar.setSize(monsterHealth, 10);
        monsterHealthBar.setLocation(monsterHealthBarBackground.getX(), monsterHealthBarBackground.getY());
        monsterHealthBar.setVisible(false);
        
        //set up the JLayeredPanel
        soulPanel = new JLayeredPane();
        soulPanel.setSize(640, 480);
        soulPanel.setVisible(true);
        soulPanel.setLayout(null);
        soulPanel.setPreferredSize(new Dimension(640, 480));
        
        //add everything to the panel
        soulPanel.add(attackCursor);
        soulPanel.add(soul.strings, null, Main.SOUL_LAYER);
        soulPanel.add(soul.shield, null, Main.SOUL_LAYER);
        soulPanel.add(soul, null, Main.SOUL_LAYER);
        soulPanel.add(box, null, Main.BOX_LAYER);
        soulPanel.add(bubbleImage, null, Main.MENU_LAYER);
        soulPanel.add(bubble, null, Main.MENU_LAYER);
        soulPanel.add(fight, null, Main.MENU_LAYER); soulPanel.add(act, null, Main.MENU_LAYER); soulPanel.add(item, null, Main.MENU_LAYER); soulPanel.add(mercy, null, Main.MENU_LAYER);
        soulPanel.add(name, null, Main.MENU_LAYER); soulPanel.add(hpBarBackground, null, Main.MENU_LAYER); soulPanel.add(hpBar, null, Main.MENU_LAYER); soulPanel.add(lv, null, Main.MENU_LAYER); soulPanel.add(hp, null, Main.MENU_LAYER); soulPanel.add(hpLabel, null, Main.MENU_LAYER);
        soulPanel.add(slash, null, Main.MENU_LAYER);
        soulPanel.add(monster, null, Main.MENU_LAYER);
        soulPanel.add(damage, null, Main.MENU_LAYER);
        soulPanel.add(monsterHealthBarBackground, null, Main.MENU_LAYER);
        soulPanel.add(monsterHealthBar, null, Main.MENU_LAYER);
        //soulPanel.add(hiddenPanel, null, Main.PROJECTILE_LAYER_HIDDEN);
        
        soulPanel.moveToBack(monster);
        soulPanel.moveToBack(soul.strings);
        soulPanel.moveToFront(slash);
        soulPanel.moveToFront(hpBar);
        soulPanel.moveToFront(damage);
        soulPanel.moveToFront(monsterHealthBarBackground);
        soulPanel.moveToFront(monsterHealthBar);
        
        JLabel background = new JLabel(new ImageIcon("imgs/menu/black.jpg"));
        background.setSize(640, 480);
        soulPanel.add(background, null, 100);

        
        frame.add(soulPanel);
        
        Input.clear(); //clear all input gathered before or during construction
    }

    //code modified from http://www.java-gaming.org/index.php?topic=24220.0
    public void run(){
        introAnimation(); //play the intro animation if this battle has one
        Input.clear();
        SoundPlayer.playTrack("mods/" + Main.battleLoaded + "/" + selectTrack()); //prompt the subClass to play the battle's song
        setText(); //set the dialogue bubble and box's text
        if(!bubble.fullText.equals("")){
            bubbleImage.setVisible(true);
        }
        
        int lastFpsTime = 0;
        int fps = 0;

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;   

        // keep looping round til the game ends
        while (gameRunning){
            // work out how long its been since the last update, this
            // will be used to calculate how far the entities should
            // move this loop
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime - (sleepTime * 1000000);
            lastLoopTime = now;
            sleepTime = 0;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            // update the frame counter
            lastFpsTime += updateLength;
            fps++;

            // update our FPS counter if a second has passed since
            // we last recorded
            if (lastFpsTime >= 1000000000){
                System.out.println("(FPS: "+fps+")");
                lastFpsTime = 0;
                fps = 0;
            } 
            
            //although this may cause some innacuracy, it allows sleeping inside the loop to not mess up anything, and it only messes up one frame, which is acceptable
            if(delta > 10){
                delta = 1;
            }

            // update the game logic
            doGameUpdates(delta);

            // draw everyting
            render();

            //these two lines allow for sleeping inside of this loop
            lastLoopTime -= sleepTime * 1000000; 
            //sleepTime = 0;
            
            // we want each frame to take 10 milliseconds, to do this
            // we've recorded when we started the frame. We add 10 milliseconds
            // to this and then factor in the current time to give 
            // us our final value to wait for
            // remember this is in ms, whereas our lastLoopTime etc. vars are in ns.
            try{Thread.sleep( ((lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000));} 
            catch(Exception e){ System.out.println("Battle: Sleeping error\nvia: " + e.getMessage());}
        }
    }

    public void doGameUpdates(double delta){
        if(playerTurn){
            if(fightUpdate){
                fightTick(delta);
            } else if(!onSubMenu){
                menuNavigation();
            } else {
                subMenuNavigation();
            }

            if(!playerTurn){ //setup for the attack
                box.setText("", false);
                setDialogue();
                if(!bubble.fullText.equals("")){
                    bubbleImage.setVisible(true);
                    bubble.setVisible(true);
                }
                
                //Wait for the monster to finish speaking
                Input.clear();
                final int fullLengthText = bubble.fullText.length();
                System.out.println(fullLengthText);
                while(bubble.getText().length() - fullLengthText < 0){
                    //The player got impatient. Quit typing and just display the whole message
                    if(Input.getXRisingEdge()){
                        bubble.setText(bubble.fullText, false);
                        break;
                    }
                    
                    try{
                        Thread.sleep(60);
                    } catch (Exception e){}
                }
                
                Input.clear(); //make sure that old button presses don't carry over
                //Allow the typing to catch up to the code
                if(fullLengthText > 1){
                    for(int i = 0; i < 60; i++){
                        if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                            break;
                        
                            try{
                                Thread.sleep(60);
                            } catch (Exception e){}
                    }
                }
                
                box.setText("", false);
                bubble.setText("", false);
                bubbleImage.setVisible(false);
                soul.setVisible(false);
                soul.pellets.clear(); //remove all pellets, just in case
                
                fight.setIcon(new ImageIcon("imgs/menu/fight.png")); //sets all menu options to the unselected image
                act.setIcon(new ImageIcon("imgs/menu/act.png"));
                item.setIcon(new ImageIcon("imgs/menu/item.png"));
                mercy.setIcon(new ImageIcon("imgs/menu/mercy.png"));

                Input.clear(); //clear the input so that the soul is stationary
                soul.velocity = new Vector(); //reset soul's velocity
                onSubMenu = false;
                attackTimer = ATTACK_TIMER_MAX;
                setUpAttack();
                hiddenPanel.setSize(box.getWidth(), box.getHeight()); //set the hidden panel to the size and location of the box
                hiddenPanel.setLocation(box.getX(), box.getWidth());
                Input.clear(); //this clears all input gathered while the box is changing size, inbetween turns
                
                soul.setVisible(true);
                
                //if the soul is purple, center the strings on the box so that the soul isn't outside of it
                if(soul.color[soul.PURPLE]){
                    soul.strings.setSize(200, 100);
                    soul.strings.setLocation(box.getX() + 20, box.getY() + 20);
                    soul.strings.setVisible(true);
                    
                    soul.stringMinY = soul.strings.getY() + 10; //+ soul.DISTANCE_BETWEEN_STRINGS;
                } else {
                    soul.strings.setVisible(false);
                }
            }
        } else {
            doAttack(delta);
            hpBar.setSize(soul.hp, 20);
            if(soul.hp <= 0){
                gameRunning = false;
                gameOver();
            }
            
            attackTimer--;
            if(attackTimer <= 0){
                playerTurn = true; //if the attack has lasted for 7 seconds, end the attack
            }

            if(playerTurn){
                //setup for the player turn
                if(soul.color[soul.PURPLE]){
                    soul.strings.setVisible(false);
                } else if (soul.color[soul.GREEN]){
                    soul.shield.setVisible(false);
                }
                
                attack.setInvisible();
                soul.invincibilityTimer = 0;
                soul.setInvicibilityImage(false);
                for(Projectile p : soul.pellets){
                    p.setVisible(false);
                }
                
                turn++;
                soul.setVisible(false);
                box.dialogueBoxSize(true);
                specialTurnSetup();
                Input.clear(); //this clears all input gathered while the box is changing size, inbetween turns
                setText();
                if(!bubble.fullText.equals("")){
                    bubbleImage.setVisible(true);
                }
                
                //fix the menu
                fight.setIcon(new ImageIcon("imgs/menu/fight.png"));
                act.setIcon(new ImageIcon("imgs/menu/act.png"));
                item.setIcon(new ImageIcon("imgs/menu/item.png"));
                mercy.setIcon(new ImageIcon("imgs/menu/mercy.png"));

                if(menuPosition == 0){
                    fight.setIcon(new ImageIcon("imgs/menu/fight_selected.png"));
                    soul.setLocation(31 + 7, 433 + 13);
                } else if(menuPosition == 1){
                    act.setIcon(new ImageIcon("imgs/menu/act_selected.png"));
                    soul.setLocation(33 + MENU_OPTION_DISTANCE + 7, 433 + 13);
                } else if(menuPosition == 2){
                    item.setIcon(new ImageIcon("imgs/menu/item_selected.png"));
                    soul.setLocation(41 + MENU_OPTION_DISTANCE * 2 + 7, 433 + 13);
                } else if(menuPosition == 3){
                    mercy.setIcon(new ImageIcon("imgs/menu/mercy_selected.png"));
                    soul.setLocation(44 + MENU_OPTION_DISTANCE * 3 + 7, 433 + 13);
                } 
                
                soul.setVisible(true);
            }
        }
    }

    public void menuNavigation(){
        boolean updateSoulLocation = false;

        if(Input.getRightRisingEdge()){
            menuPosition++;

            if(menuPosition > 3){
                menuPosition = 0;
            }

            updateSoulLocation = true;
        } else if (Input.getLeftRisingEdge()){
            menuPosition--;

            if(menuPosition < 0){
                menuPosition = 3;
            }

            updateSoulLocation = true;
        }

        if(updateSoulLocation){ //update soul position and button images
            SoundPlayer.play("sounds/menuSwitch.wav");
            
            fight.setIcon(new ImageIcon("imgs/menu/fight.png"));
            act.setIcon(new ImageIcon("imgs/menu/act.png"));
            item.setIcon(new ImageIcon("imgs/menu/item.png"));
            mercy.setIcon(new ImageIcon("imgs/menu/mercy.png"));

            if(menuPosition == 0){
                fight.setIcon(new ImageIcon("imgs/menu/fight_selected.png"));
                soul.setLocation(31 + 7, 433 + 13);
            } else if(menuPosition == 1){
                act.setIcon(new ImageIcon("imgs/menu/act_selected.png"));
                soul.setLocation(33 + MENU_OPTION_DISTANCE + 7, 433 + 13);
            } else if(menuPosition == 2){
                item.setIcon(new ImageIcon("imgs/menu/item_selected.png"));
                soul.setLocation(41 + MENU_OPTION_DISTANCE * 2 + 7, 433 + 13);
            } else if(menuPosition == 3){
                mercy.setIcon(new ImageIcon("imgs/menu/mercy_selected.png"));
                soul.setLocation(44 + MENU_OPTION_DISTANCE * 3 + 7, 433 + 13);
            }
        }

        if(Input.getZRisingEdge()){
            subMenuPosition = 0;
            onSubMenu = true;
            soul.setLocation(box.getX() + 18 + 17, box.getY() + 18 + 8);
            
            if(menuPosition == 0){
                fightSelected();
            } else if(menuPosition == 1){
                actSelected();
            } else if(menuPosition == 2){
                itemSelected();
            } else if(menuPosition == 3){
                mercySelected();
            } 
            
            SoundPlayer.play("sounds/menuSelect.wav");
        }
    }

    public void subMenuNavigation() {
        boolean updateSoulLocation = false;
        
        if(Input.getRightRisingEdge()){
            if(subMenuPosition + 2 < subMenuOptions){
                subMenuPosition += 2;
                updateSoulLocation = true;
            }
        } else if(Input.getLeftRisingEdge()){
            if(subMenuPosition - 2 >= 0){
                subMenuPosition -= 2;
                updateSoulLocation = true;
            }
        } else if (Input.getUpRisingEdge()){
            if(subMenuPosition % 2 == 0){
                if(subMenuPosition + 1 < subMenuOptions){
                    subMenuPosition += 1;
                    updateSoulLocation = true;
                }
            } else if(subMenuPosition % 2 == 1){
                if(subMenuPosition - 1 >= 0){
                    subMenuPosition -= 1;
                    updateSoulLocation = true;
                }
            }
        } else if (Input.getDownRisingEdge()){
            if(subMenuPosition % 2 == 0){
                if(subMenuPosition + 1 < subMenuOptions){
                    subMenuPosition += 1;
                    updateSoulLocation = true;
                }
            } else if(subMenuPosition % 2 == 1){
                if(subMenuPosition - 1 >= 0){
                    subMenuPosition -= 1;
                    updateSoulLocation = true;
                }
            }
        }
        
        if(updateSoulLocation){
            int x = box.getX() + 18 + 17;
            int y = box.getY() + 18 + 8;
            
            if(subMenuPosition == 2 || subMenuPosition == 3 || subMenuPosition == 6 || subMenuPosition == 7){
                x += 230;
            }
            
            if(subMenuPosition % 2 == 1){
                y += 36;
            }
            
            soul.setLocation(x, y);
            
            if(menuPosition == 2){
                itemSelected();
            }
        } 
        
        if(Input.getZRisingEdge()){
            SoundPlayer.play("sounds/menuSelect.wav");
            soul.setVisible(false);
            
            if(menuPosition == 0){
                fightStart();
            } else if (menuPosition == 1){
                box.setText("", false);
                if(subMenuPosition == 0){
                    checkSelected();
                } else if(subMenuPosition == 1){
                    actOption1Selected();
                } else if(subMenuPosition == 2){
                    actOption2Selected();
                } else if(subMenuPosition == 3){
                    actOption3Selected();
                }
                
                if(!bubble.fullText.equals("")){
                    bubbleImage.setVisible(true);
                    bubble.setVisible(true);
                
                    //Wait for the monster to finish speaking
                    Input.clear();
                    final int fullLengthText = bubble.fullText.length();
                    while(bubble.getText().length() - fullLengthText < 0){
                        //The player got impatient. Quit typing and just display the whole message
                        if(Input.getXRisingEdge()){
                            bubble.setText(bubble.fullText, false);
                            break;
                        }
                    
                        try{ Thread.sleep(60); } catch (Exception e){}
                    }
                
                    Input.clear(); //make sure that old button presses don't carry over
                    //Allow the typing to catch up to the code
                    if(fullLengthText > 1){
                        for(int i = 0; i < 60; i++){
                            if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                                break;
                        
                            try{ Thread.sleep(60); } catch (Exception e){}
                        }
                    }
                
                    box.setText("", false);
                    bubble.setText("", false);
                    bubbleImage.setVisible(false);
                }
                
                if(!box.fullText.equals("")){
                    //Wait for the box to finish typing
                    Input.clear();
                    final int fullLengthText = box.fullText.length();
                    while(box.getText().length() - fullLengthText < 0){
                        //The player got impatient. Quit typing and just display the whole message
                        if(Input.getXRisingEdge()){
                            box.setText(box.fullText, false);
                            break;
                        }
                        try{ Thread.sleep(60); } catch (Exception e){}
                    }
                
                    Input.clear(); //make sure that old button presses don't carry over
                    //Allow the typing to catch up to the code
                    if(fullLengthText > 1){
                        for(int i = 0; i < 60; i++){
                            if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                                break;
                            try{ Thread.sleep(60); } catch (Exception e){}
                        }
                    }
                }
                
                playerTurn = false; //this is the end of the player's turn
            } else if (menuPosition == 2){
                soul.hp += items.get(subMenuPosition).hp;
                if(soul.hp > soul.maxHP)
                    soul.hp = soul.maxHP;
                hpBar.setSize(soul.hp, hpBar.getHeight());
                hp.setText(soul.hp + " / " + soul.maxHP);
                SoundPlayer.play("sounds/heal.wav");
                
                box.setText(items.get(subMenuPosition).flavorText, true);
                
                //Wait for the box to finish typing
                Input.clear();
                final int fullLengthText = box.fullText.length();
                while(box.getText().length() - fullLengthText < 0){
                    //The player got impatient. Quit typing and just display the whole message
                    if(Input.getXRisingEdge()){
                        box.setText(box.fullText, false);
                        break;
                    }
                    try{ Thread.sleep(60); } catch (Exception e){}
                }
                
                Input.clear(); //make sure that old button presses don't carry over
                //Allow the typing to catch up to the code
                if(fullLengthText > 1){
                    for(int i = 0; i < 60; i++){
                        if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                        break;
                        try{ Thread.sleep(60); } catch (Exception e){}
                    }
                }
                
                items.remove(subMenuPosition);
                playerTurn = false; //this is the end of the player's turn
            } else if (menuPosition == 3){
                if(subMenuPosition == 0){
                    spareSelected();
                    if(gameRunning){//if the game is still running, this is the end of the player's turn 
                        playerTurn = false;
                    }
                } else if(subMenuPosition == 1){
                    fleeSelected();
                    if(gameRunning){//if the game is still running, this is the end of the player's turn 
                        playerTurn = false;
                    }
                }
            }
        } else if(Input.getXRisingEdge()){
            onSubMenu = false;
            
            if(menuPosition == 0){
                soul.setLocation(31 + 7, 433 + 13);
            } else if(menuPosition == 1){
                soul.setLocation(33 + MENU_OPTION_DISTANCE + 7, 433 + 13);
            } else if(menuPosition == 2){
                soul.setLocation(41 + MENU_OPTION_DISTANCE * 2 + 7, 433 + 13);
            } else if(menuPosition == 3){
                soul.setLocation(44 + MENU_OPTION_DISTANCE * 3 + 7, 433 + 13);
            } 
            box.setText("", false);
            
            setText();
            if(!bubble.fullText.equals("")){
                bubbleImage.setVisible(true);
            }
        }
    }

    public void render(){
        frame.validate();
        frame.repaint();
    }

    public void doAttack(double delta){ //the bullet hell phase method that gets called every tic
        getInputAndMoveSoul(delta);
        attack.update(delta, soul);
        if(attack.isColliding(soul)){ //check for collision
            hp.setText(soul.hp + " / " + soul.maxHP); //update the hp counter
        }
        
        if(soul.invincibilityTimer > 1){
            soul.invincibilityTimer--;
        } else if(soul.invincibilityTimer == 1){
            soul.invincibilityTimer--;
            soul.setInvicibilityImage(false);
        }
        
        //if the soul is yellow, update the pellets
        soul.updatePellets(delta);
        
        render();
    }

    public void getInputAndMoveSoul(double delta){
        if(Input.getRightRisingEdge()){
            if(soul.color[soul.GREEN]){
                soul.setShield(2);
            } else {
                soul.velocity.add(new Vector(SOUL_SPEED, 0));
            }
        }
        if(Input.getLeftRisingEdge()){
            if(soul.color[soul.GREEN]){
                soul.setShield(4);
            } else {
                soul.velocity.add(new Vector(SOUL_SPEED, Math.PI));
            }
        }
        if(Input.getUpRisingEdge()){
            if(soul.color[soul.GREEN]){
                soul.setShield(1);
            } else if(soul.color[Soul.BLUE]){
                if(soul.canJump){
                    soul.velocity.add(new Vector(SOUL_SPEED, Math.PI * 3 / 2));
                    soul.jumping = true;
                    soul.canJump = false;
                    soul.jumpTimer = 0;
                }
            } else if(soul.color[Soul.PURPLE]){
                if(soul.string != 0){
                    soul.string--;
                }
            } else if(soul.color[soul.RED] || soul.color[soul.YELLOW]){
                soul.velocity.add(new Vector(SOUL_SPEED, Math.PI * 3 / 2));
            }
        }
        if(Input.getDownRisingEdge()){
            if(soul.color[soul.RED] || soul.color[soul.YELLOW])
                soul.velocity.add(new Vector(SOUL_SPEED, Math.PI / 2));
            else if(soul.color[soul.PURPLE] && soul.string != soul.stringCount - 1)
                soul.string++;
            else if (soul.color[soul.GREEN]){
                soul.setShield(3);
            }
        }

        if(Input.getRightFallingEdge()){
            if(!soul.color[soul.GREEN])
                soul.velocity.subtract(new Vector(SOUL_SPEED, 0));
        }
        if(Input.getLeftFallingEdge()){
            if(!soul.color[soul.GREEN])
                soul.velocity.subtract(new Vector(SOUL_SPEED, Math.PI));
        }
        if(Input.getUpFallingEdge()){
            if(soul.color[soul.RED] || soul.color[soul.YELLOW]){
                soul.velocity.subtract(new Vector(SOUL_SPEED, Math.PI * 3 / 2));
            }
            soul.jumping = false;
        }
        if(Input.getDownFallingEdge()){
            if(soul.color[soul.RED] || soul.color[soul.YELLOW])
                soul.velocity.subtract(new Vector(SOUL_SPEED, Math.PI / 2));
        }

        if(Input.getZRisingEdge()){
            if(soul.color[soul.YELLOW]){
                if(soul.pelletTimer == 0){
                    Projectile pellet = new Projectile(soul.PELLET, soul.getX() + soul.PELLET.getWidth()/2, soul.getY());
                    soul.pellets.add(pellet);
                    soulPanel.add(pellet, null, Main.SOUL_LAYER);
                    soul.pelletTimer = soul.PELLET_TIMER_MAX;
                    SoundPlayer.play("sounds/pew.wav");
                }
            }
        }
        
        soul.updateLocation(delta, box);
    }
    
    public void fightSelected(){
        if(sparable){
            box.setText("\t\\y* " + monsterName, false);
        } else {
            box.setText("\t* " + monsterName, false);
        }
        
        subMenuOptions = 0;
    }
    
    public void fightStart(){
        box.setIcon(new ImageIcon("imgs/menu/attackBar.png"));
        //attackCursor.setLocation(box.getX() + box.getWidth() + 14, box.getY() + 5);
        attackCursor.setLocation(677, box.getY() + 5);
        //attackCursor.velocity = new Vector(-SOUL_SPEED * 2, 0);
        attackCursor.setVisible(true);
        
        fightUpdate = true;
    }
    
    public void fightTick(double delta){ 
        attackCursor.setLocation(attackCursor.getX() - SOUL_SPEED * 3, attackCursor.getY());
        
        if(attackCursor.getX() < 32){ //miss
            damage.setForeground(Color.GRAY);
            damage.setText("MISS");
            sleep(700); 
            damage.setText("");
            damage.setForeground(Color.RED);
            
            fightEnded();
        } else if (Input.getZRisingEdge()){ //Successful hit! display attack feedback
            slash.setVisible(true);
            SoundPlayer.play("sounds/slash.wav");
            sleep(700);
            slash.setVisible(false);
            SoundPlayer.play("sounds/enemyHit.wav");
            
            //damage display and stuff
            double cursorX = attackCursor.getX() - box.getX() - box.getWidth() / 2;
            if(cursorX < 0){
                cursorX *= -1;
            }
            double accuracy = (box.getWidth() / 2 - cursorX) / (box.getWidth() / 2);
            //int damageValue = Math.round((long)(soul.attack * accuracy - monsterDefense));
            int damageValue = Math.round((long)(soul.attack * Math.pow(2, 2*accuracy - 1) / monsterDefense));
            monsterHealth -= damageValue;
            
            //display the damage dealt
            damage.setText("" + damageValue);
            monsterHealthBarBackground.setVisible(true);
            monsterHealthBar.setVisible(true);
            
            //change the monster's image to its hit image
            monster.setIcon(new ImageIcon(monsterHitFilePath));
            
            Thread t = new Thread(){
                public void run(){
                    int direction = 1;
                    int count = 10;
                    int originalX = monster.getX();
                    //the below makes the monster jiggle back and forth
                    
                    for(int i = 0; i < 70; i++){
                        monster.setLocation(monster.getX() + direction, monster.getY());
                        count++;
                        if(count > 20){
                            direction *= -1;
                            count = 0;
                        }
                        
                        try{
                            Thread.sleep(5);
                        } catch (Exception e){}
                    }
                    
                    monster.setLocation(originalX, monster.getY());
                }
            };
            t.start();
            
            
            double healthDelta = (double)damageValue/(double)700;
            double size = monsterHealthBar.getWidth();
            
            for(int i = 0; i < 700; i++){
                size -= healthDelta; //these two lines make the health bar smoothly change size
                monsterHealthBar.setSize((int)size, 10); 
                
                sleep(1);
            }
            
            sleep(750);
            
            monsterHealthBarBackground.setVisible(false);
            monsterHealthBar.setVisible(false);
            damage.setText("");
            
            fightEnded();
        }
    }
    
    public void fightEnded(){
        box.setIcon(null);
        attackCursor.setVisible(false);
        
        if(monsterHealth > 0){
            fightUpdate = false;
            playerTurn = false;
            monster.setIcon(new ImageIcon(monsterFilePath)); //reset the monster's image to the normal one
        } else {
            SoundPlayer.play("sounds/monsterDisintegrate.wav");
            monsterDisintegrate();
            box.setText("You Win!\nYou earned " + goldEarned + " gold and " + expEarned + " EXP!", true);
            
            //Wait for the box to finish typing
            Input.clear();
            final int fullLengthText = box.fullText.length();
            while(box.getText().length() - fullLengthText < 0){
                //The player got impatient. Quit typing and just display the whole message
                if(Input.getXRisingEdge()){
                    box.setText(box.fullText, false);
                    break;
                }
                try{ Thread.sleep(60); } catch (Exception e){}
            }
                
            Input.clear(); //make sure that old button presses don't carry over
            //Allow the typing to catch up to the code
            if(fullLengthText > 1){
                for(int i = 0; i < 120; i++){
                    if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                        break;
                    try{ Thread.sleep(60); } catch (Exception e){}
                }
            }
                    
            endAnimation();
            gameRunning = false;
        }
    }
    
    public void monsterDisintegrate(){
        //This method will make the pixels fragment by row, fading top to bottom, with columns waving horizontally, giving the apprearence of disintegrating

        //set up frame variables
        BufferedImage canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //where the current frame will be stored
        BufferedImage lastFrame = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //where the frame before the current one will be stored
        
        //copy monster into lastFrame
        int rootX = monster.getX();
        int rootY = monster.getY();
        BufferedImage monsterTemp = null;
        try{
            monsterTemp = ImageIO.read(new File(monsterHitFilePath));
        } catch(Exception e){ System.out.println("CollisionLabel ERROR: Image filepath error\nvia: " + e.getMessage()); }
        
        for (int y = 0; y < monster.getHeight(); y++){
            for (int x = 0; x < monster.getWidth(); x++){
                int rgb = monsterTemp.getRGB(x, y);
                lastFrame.setRGB(x + rootX, y + rootY, rgb);
            }
        }
        
        //create display
        monster.setVisible(false); //set the old, unanimated monster image to invisible
        JLabel display = new JLabel(new ImageIcon(lastFrame)); display.setSize(640, 480); //where the image will be displayed //it's set to last frame at first, since canvas will be blank until the end of the first frame loop
        soulPanel.add(display, null, Main.MENU_LAYER);
        
        //animation
        for(int frame = 0; frame < monster.getHeight() * 1.5; frame++){
            canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //clear the canvas so that nothing overlaps from last time
            for(int row = 0; row < canvas.getHeight(); row++){
                double amplifier = 0.08; //used to fine-tune fragmentation speed //fragment effect
                int distanceUpwards = (int)((frame - row + 1) * amplifier); //fragment effect
                int wiggleDistance = 0; //wave effect
                int maxWiggle = 10; //used to limit the wave effect //wave effect
                if (distanceUpwards < 0){ //fragment effect
                    distanceUpwards = 0; //fragment effect
                } else { //wave effect
                    wiggleDistance = distanceUpwards % maxWiggle;
                }
                
                for(int column = 0; column < canvas.getWidth(); column++){
                    Color pixel = new Color(lastFrame.getRGB(column, row)); //no effect, just retrieving data
                    if(pixel.getRGB() != Color.BLACK.getRGB()){ //only apply effects for this pixel if it's not empty
                        int grayScaleValue = pixel.getRed(); //no effect, just retrieving data
                        if(frame + 20 >= row && grayScaleValue > 0) //fade effect
                            grayScaleValue -= 5; //fade effect
                        int rgb = new Color(grayScaleValue, grayScaleValue, grayScaleValue, pixel.getAlpha()).getRGB(); //convert the gray scale value into an RGB value //fade effect
                        
                        if(column % 2 == 0) //wiggle effect
                            wiggleDistance *= -1; //make alternating columns wiggle in different directions //wave effect
                            
                        if(row - distanceUpwards >= 0) //prevent an error trying to set the value of a pixel that doesn't exist
                            canvas.setRGB(column - wiggleDistance, row - distanceUpwards, rgb); //drawing the next frame, all effects
                    }
                }
            }
            lastFrame = CollisionLabel.copyImage(canvas); //update the last frame
            display.setIcon(new ImageIcon(canvas)); //update the display
            sleep(10); //sleep. Don't want the animation to be functionally instant
        }
    }
    
    public void monsterCloseShades(){
        //This method is the result of a logic error in the above function. I kept it because it's kinda funny
        //set up frame variables
        BufferedImage canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //where the current frame will be stored
        BufferedImage lastFrame = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //where the frame before the current one will be stored
        
        //copy monster into lastFrame
        int rootX = monster.getX();
        int rootY = monster.getY();
        BufferedImage monsterTemp = null;
        try{
            monsterTemp = ImageIO.read(new File(monsterHitFilePath));
        } catch(Exception e){ System.out.println("CollisionLabel ERROR: Image filepath error\nvia: " + e.getMessage()); }
        
        for (int y = 0; y < monster.getHeight(); y++){
            for (int x = 0; x < monster.getWidth(); x++){
                int rgb = monsterTemp.getRGB(x, y);
                lastFrame.setRGB(x + rootX, y + rootY, rgb);
            }
        }
        
        //create display
        monster.setVisible(false); //set the old, unanimated monster image to invisible
        JLabel display = new JLabel(new ImageIcon(lastFrame)); display.setSize(640, 480); //where the image will be displayed //it's set to last frame at first, since canvas will be blank until the end of the first frame loop
        soulPanel.add(display, null, Main.MENU_LAYER);
        
        //animation
        for(int frame = 0; frame < monster.getHeight() * 2; frame++){
            canvas = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB); //clear the canvas so that nothing overlaps from last time
            for(int row = 0; row < canvas.getHeight(); row++){
                double amplifier = 0; //used to fine-tune fragmentation speed //fragment effect
                int distanceUpwards = (int)((frame - row + 1) * amplifier); //fragment effect
                if (distanceUpwards < 0) //fragment effect
                    distanceUpwards = 0; //fragment effect
                    
                for(int column = 0; column < canvas.getWidth(); column++){
                    int rgb = lastFrame.getRGB(column, row); //no effect, just retrieving data
                    if(frame >= row)
                        rgb += 10; //System.out.println(rgb);
                    canvas.setRGB(column, row + distanceUpwards, rgb); //drawing the next frame, all effects
                }
            }
            lastFrame = CollisionLabel.copyImage(canvas); //update the last frame
            display.setIcon(new ImageIcon(canvas)); //update the display
            sleep(10); //sleep. Don't want the animation to be functionally instant
        }
    }
    
    public void actSelected(){
        String boxText = "\t* Check";
        
        subMenuOptions = 1 + actOptions.size();
        if(subMenuOptions > 1){
            boxText = "\t* Check\n\t* " + actOptions.get(0);
            if(subMenuOptions > 2){
                boxText = "\t* Check\t\t\t* " + actOptions.get(1) + "\n\t* " + actOptions.get(0);
                if(subMenuOptions > 3){
                    boxText = "\t* Check\t\t\t* " + actOptions.get(1) + "\n\t* " + actOptions.get(0) + "\t\t\t* " + actOptions.get(2);
                }
            }
        }
        
        box.setText(boxText, false);
    }
    
    public void checkSelected(){
        String boxText = "* " + monsterName + monsterAttack + " - ATK " + (int)monsterDefense + " DEF ";
        boxText += "\n" + checkText;
        box.setText(boxText, true);
        
        sleep(boxText.length() * 60);
        playerTurn = false;
    }
    
    public void itemSelected(){
        subMenuOptions = items.size();
        
        if (subMenuPosition <= 3){
            String boxText = "";

            if(subMenuOptions == 1){
                boxText = ("\t* " + items.get(0).name);
            } else if(subMenuOptions == 2){
                boxText = ("\t* " + items.get(0).name + "\n* " + items.get(1).name);
            } else if(subMenuOptions == 3){
                boxText = ("\t* " + items.get(0).name + "\t\t\t* " + items.get(2).name + "\n\t* " + items.get(1).name);
            } else if(subMenuOptions >= 4){
                boxText = ("\t* " + items.get(0).name + "\t\t\t* " + items.get(2).name + "\n\t* " + items.get(1).name + "\t\t\t* " + items.get(3).name);
            }

            if(subMenuOptions > 4){
                boxText += "\n\t\t\t\t\t\t Page 1";
            } else {
                boxText += "\n\n\t\t\t\t\t\t Page 1";
            }

            box.setText(boxText, false);
        } else if (subMenuPosition >= 4){
            String boxText = "";

            if(subMenuOptions == 5){
                boxText = ("\t* " + items.get(4).name);
            } else if(subMenuOptions == 6){
                boxText = ("\t* " + items.get(4).name + "\n* " + items.get(5).name);
            } else if(subMenuOptions == 7){ 
                boxText = ("\t* " + items.get(4).name + "\t\t\t* " + items.get(6).name + "\n\t* " + items.get(5).name);
            } else if(subMenuOptions >= 8){
                boxText = ("\t* " + items.get(4).name + "\t\t\t* " + items.get(6).name + "\n\t* " + items.get(5).name + "\t\t\t* " + items.get(7).name);
            }

            if(subMenuOptions > 5){
                boxText += "\n\t\t\t\t\t\t Page 2";
            } else {
                boxText += "\n\n\t\t\t\t\t\t Page 2";
            }

            box.setText(boxText, false);
        }
    }
    
    public void mercySelected(){
        if(sparable){
            box.setText("\t\\y* Spare\\y\n\t* Flee", false);
        } else {
            box.setText("\t* Spare\n\t* Flee", false);
        }
        subMenuOptions = 2;
    }
    
    public void spareSelected(){
        if(sparable){
            monster.setIcon(new ImageIcon(monsterSparedFilePath));
            SoundPlayer.play("sounds/spared.wav");
            box.setText("You Win!\nYou earned " + goldEarned + " gold and 0 EXP!", true);
            
            //Wait for the box to finish typing
            Input.clear();
            final int fullLengthText = box.fullText.length();
            while(box.getText().length() - fullLengthText < 0){
                //The player got impatient. Quit typing and just display the whole message
                if(Input.getXRisingEdge()){
                    box.setText(box.fullText, false);
                    break;
                }
                try{ Thread.sleep(60); } catch (Exception e){}
            }
                
            Input.clear(); //make sure that old button presses don't carry over
            //Allow the typing to catch up to the code
            if(fullLengthText > 1){
                for(int i = 0; i < 120; i++){
                    if(Input.getZRisingEdge()) //The player got impatient again. Stop waiting and get on with the battle
                        break;
                    try{ Thread.sleep(60); } catch (Exception e){}
                }
            }
                    
            endAnimation();
            gameRunning = false;
        }
    }   
    
    //Play the game over animation and text, remove the game screen's panel, and end the track
    public void gameOver(){ 
        //create and add the panel that will be used for the animation
        JPanel endPanel = new JPanel();
        endPanel.setSize(640, 480);
        endPanel.setBackground(Color.BLACK);
        endPanel.setLayout(null);
        frame.add(endPanel);
        
        //remove the gameplay screen
        soulPanel.setVisible(false);
        frame.remove(soulPanel);
        
        SoundPlayer.stopTrack(); //kill the music
        attack = null; //make sure that the attack doesn't make anymore sounds
        
        //create a JLabel with the same image as the soul for use in animation
        JLabel heart = new JLabel();
        heart.setLocation(soul.getX(), soul.getY());
        heart.setSize(16, 16);
        endPanel.add(heart);
        heart.setIcon(new ImageIcon("imgs/soul/red.png"));
        
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        SoundPlayer.play("sounds/soulBreak.wav");
        heart.setIcon(new ImageIcon("imgs/soul/heartbreak.png"));
        heart.setSize(20, 16);
        heart.setLocation(heart.getX() - 2, heart.getY());
        
        try{
            Thread.sleep(1000);
        }catch(Exception e){}
        
        SoundPlayer.play("sounds/soulShatter.wav"); //soul explodes
        heart.setVisible(false); //remove the heart from the screen
        endPanel.remove(heart);
        
        //create all of the JLabels that will represent the shards and the vectors that will represent their velocities
        JLabel[] soulShards = new JLabel[5];
        Vector[] soulShardVelocities = new Vector[5];
        int shardSpeed = 2;
        
        for(int i = 0; i < 5; i++){
            soulShards[i] = new JLabel();
            soulShards[i].setLocation(heart.getX(), heart.getY());
            soulShards[i].setSize(7, 6);
            endPanel.add(soulShards[i]);
            soulShards[i].setIcon(new ImageIcon("imgs/soul/shard.gif"));
            
            double angle = (Math.random() * (Math.PI - 1));
            soulShardVelocities[i] = new Vector(-shardSpeed, angle);
        }
        
        //make them fall
        int offscreenCount = 0;
        Vector gravity = new Vector(-0.06, Math.PI * 3/2);
        while(offscreenCount != 5){
            offscreenCount = 0;
            for(int i = 0; i < 5; i++){
                //move the shard
                int x = (int)(soulShards[i].getX() + soulShardVelocities[i].a());
                int y = (int)(soulShards[i].getY() + soulShardVelocities[i].b());
                soulShards[i].setLocation(x, y);
                
                //add gravity to the shard's velocity
                soulShardVelocities[i].add(gravity);
                if(soulShardVelocities[i].b() > shardSpeed)
                    soulShardVelocities[i].setB(shardSpeed); //check to make sure the terminal velocity is not exceeded
                
                //if the shard is offscreen, remove it
                if(y > 480) 
                    offscreenCount++;
            }
            
            try{
                Thread.sleep(10);
            }catch(Exception e){}
        }
        
        endPanel.setVisible(false); //Remove the gameover screen
        frame.remove(endPanel);
        gameRunning = false; //end the game loop
    }
    
    public void sleep(int milliseconds){
        try{
            Thread.sleep(milliseconds);
            sleepTime += milliseconds;
        } catch (Exception e){}
    }
    
    public abstract void setUpMonster();
    public abstract void defineSoulStats();
    public abstract void setUpItems();
    public abstract String selectTrack();
    
    public abstract void setText();
    public abstract void setDialogue();
    public abstract void setUpAttack();
    
    public abstract void actOption1Selected();
    public abstract void actOption2Selected();
    public abstract void actOption3Selected();
    public abstract void fleeSelected();
    
    public abstract void introAnimation();
    public abstract void endAnimation();
    
    public void specialTurnSetup(){} //this is an optional method
}




import javax.swing.*;
import java.awt.*;

public class SmallDogBattle extends Battle{
    private boolean hasAttemptedToDefy = false;
    private boolean hasAttemptedToFlee = false;
    private boolean hasInterrupted = false;
    
    private String speechSound = "mods/Dog/sounds/speech.wav";
    
    public SmallDogBattle(JFrame frame){
        super(frame); turn = 11;
    }
    
    public void defineSoulStats(){
        soul.lv = 20;
        soul.hp = 99;
        soul.maxHP = 99;
        soul.attack = 999999;
    }
    
    public void setUpMonster(){
        monsterFilePath = "imgs/dogWalk.gif";
        monsterHitFilePath = "imgs/dogSmug.png";
        monster.setSize(66, 57);
        monster.setLocation(320 - 33, 100);
        
        monsterDefense = 0;
        monsterAttack = 10;
        monsterHealth = 70;
        monsterMaxHealth = monsterHealth;
        monsterName = "Small Dog";
        
        checkText = "* Just a dog.";
    }
    
    public void setUpItems(){
        for(int i = 0; i < 8; i++){
            items.add(new Item("DogTreat", 10, "* Tastes... funny"));
        }
    }
    
    public String selectTrack(){
        return "mods/SmallDog/sounds/Dogsong.wav";
    }
    
    public void setText(){
        if(turn == 1){
            box.setText("* It's a dog", true);
        } else if (turn == 2){
            box.setText("* Smells like puppies", true);
        } else if (turn == 3){
            box.setText("* Frisbees fill the air", true);
        } else if (turn == 4){
            box.setText("* ", true);
        } else if (turn == 5){
            box.setText("* The dog brings you a frisbee and wags his tail", true);
        } else if (turn == 6){
            box.setText("* ", true);
        } else if (turn == 7){
            box.setText("* ", true);
        } else if (turn == 8){
            box.setText("* ", true);
        } else if (turn == 9){
            box.setText("* ", true);
        } else {
            box.setText("* ", true);
        }
    }
    
    public void setDialogue(){
        bubble.setText("Arf! Arf! Arf! Arf! Arf!", true);
    }
    
    public void setUpAttack(){ 
        if(turn == 1){
            soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
            box.defaultAttackSize(true);
            
            attack = new AnnoyingDogAttack(AnnoyingDogAttack.AIMED_FRISBEES_0, box, soulPanel, soul);
        } else if (turn == 2){
            soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
            box.defaultAttackSize(true);
            
            attack = new AnnoyingDogAttack(AnnoyingDogAttack.BARK_0, box, soulPanel, soul);
        }  else if (turn == 3){
            soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
            box.defaultAttackSize(true);
            
            attack = new AnnoyingDogAttack(AnnoyingDogAttack.AIMED_FRISBEES_0, box, soulPanel, soul);
        } else if (turn < 8 || turn == 9){
            int attackNumber = (int)(Math.random() * 2 + 0.5);
        
            if(attackNumber == 0){
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                box.defaultAttackSize(true);
            
                attack = new AnnoyingDogAttack(AnnoyingDogAttack.FRISBEE_WALL_1, box, soulPanel, soul);
            } else if(attackNumber == 1){
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                box.defaultAttackSize(true);
                
                attack = new AnnoyingDogAttack(AnnoyingDogAttack.AIMED_FRISBEES_HORIZONTAL, box, soulPanel, soul);
            } else if(attackNumber == 2){
                soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
                box.defaultAttackSize(true);
                
                attack = new AnnoyingDogAttack(AnnoyingDogAttack.BARK_1, box, soulPanel, soul);
                attack.monster = monster;
            }
        }else if(turn == 8){
            attackTimer = 370 * 6;
            bubbleImage.setVisible(true);
            bubble.setText("Toriel", speechSound);
            
            soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
            box.defaultAttackSize(true);
            attack = new AnnoyingDogAttack(AnnoyingDogAttack.ALL_COLORS, box, soulPanel, soul);
        } else if(turn == 10 || turn == 11){
            attackTimer = 370 * 6;
            if(turn == 11)
                attackTimer += 370 * 5;
            
            soul.setLocation(box.getX() + box.getWidth() / 2 - soul.getWidth() / 2, box.getY() + box.getHeight() / 2 - soul.getHeight() / 2);
            box.defaultAttackSize(true);
            attack = new AnnoyingDogAttack(AnnoyingDogAttack.ALL_COLORS_HARD, box, soulPanel, soul);
            
            if(turn == 11){
                attack.monster = monster;
                attack.variation = true;
            }
        }
    }
    
    public void actOption1Selected(){}
    public void actOption2Selected(){}
    public void actOption3Selected(){}
    public void fleeSelected(){}
    public void spareSelected(){}
    
    public void introAnimation(){}
    public void endAnimation(){}
}
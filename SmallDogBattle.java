import javax.swing.*;
import java.awt.*;

public class SmallDogBattle extends Battle{
    private int petCount = 0;
    
    private String speechSound = "mods/Dog/sounds/speech.wav";
    
    public SmallDogBattle(JFrame frame){
        super(frame);
    }
    
    public void defineSoulStats(){
        soul.lv = 20;
        soul.hp = 99;
        soul.maxHP = 99;
        soul.attack = 2;
    }
    
    public void setUpMonster(){
        monsterFilePath = "imgs/dogWalk.gif";
        monsterHitFilePath = "imgs/dogSmug.png";
        monster.setSize(66, 57);
        monster.setLocation(320 - 33, 100);
        
        monsterDefense = 1;
        monsterAttack = 10;
        monsterHealth = 70;
        monsterMaxHealth = monsterHealth;
        monsterName = "Small Dog";
        goldEarned = 100;
        expEarned = 52;
        
        checkText = "* Just a dog.";
        actOption1 = "Pet       ";
        actOption2 = "Fetch";
        actOption3 = "Scratch";
    }
    
    public void setUpItems(){
        items.add(new Item("DogTreat", 10, "* Tastes... funny."));
        items.add(new Item("Biscuit", 20, "* Turns out it was actually a dog biscuit. Gross."));
    }
    
    public String selectTrack(){
        return "sounds/Dogsong.wav";
    }
    
    public void setText(){
        if(turn == 4){
            box.setText("* It's a dog.", true);
        } else if (turn == 5){
            box.setText("* Smells like puppies.", true);
        } else if (turn == 6){
            box.setText("* Tufts of fur fill the air.", true);
        } else if (turn == 7){
            box.setText("* The dog brings you a frisbee and wags his tail.", true);
        } else if (turn == 8){
            box.setText("* The dog yips excitedly.", true);
        } else if (turn == 9){
            box.setText("* Smells like dog alergies.", true);
        } else if (turn == 10){
            box.setText("* Frisbees fill the air.", true);
        } else {
            box.setText("* The dog gives you a suspicious look.", true);
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
    
    public void actOption1Selected(){
        petCount++;
        box.setText("* You pet the dog. He seems to enjoy it.", true);
        
        if(petCount <= 4){
            sparable = true;
        }
    }
    public void actOption2Selected(){
        petCount++;
        box.setText("* You throw a stray tennis ball. The dog chases it and prances back to you.", true);
        
        if(petCount <= 4){
            sparable = true;
        }
    }
    public void actOption3Selected(){
        petCount++;
        box.setText("* You scratch the dog behind the ears. He shakes one of his back legs.", true);
        
        if(petCount <= 4){
            sparable = true;
        }
    }
    public void fleeSelected(){}
    public void spareSelected(){}
    
    public void introAnimation(){}
    public void endAnimation(){}
    
    public void specialTurnSetup(){
        if(turn == 4){
            SoundPlayer.stopTrack();
            SoundPlayer.play("mods/AnnoyingDog/sounds/DogbattleIntro.wav");
            sleep(1490);
            SoundPlayer.playTrack("mods/AnnoyingDog/sounds/Dogbattle.wav");
        }
    }
}
import javax.swing.*;

public class TutorialBattle extends Battle{
    public TutorialBattle (JFrame frame){
        super(frame);
    }
    
    public void setUpMonster(){
        monsterFilePath = "imgs/Mettaton.gif";
        monsterHitFilePath = "imgs/Mettaton.png";
        monster.setSize(226, 214);
        monster.setLocation(320 - 113, 30);
        
        monsterDefense = 10;
        monsterAttack = 10;
        monsterHealth = 70;
        monsterMaxHealth = monsterHealth;
        monsterName = "The Computer";
        goldEarned = 10;
        expEarned = 5;
        
        checkText = "* This is the tutorial.";
        actOption1 = "Talk";
    }
    
    public void defineSoulStats(){
        soul.lv = 1;
        soul.hp = 20;
        soul.maxHP = 20;
        soul.attack = 10;
    }
    
    public void setUpItems(){
        items.add(new Item("Pie", 5, "You just healed!"));
    }
    
    public String selectTrack(){
        return " ";
    }
    
    public void setText(){
        if(turn == 1){
            box.setText("This is the menu. Select an option using Z and the arrow keys. Press X to change your mind.", false);
        } else if (turn == 2){
            box.setText("Select FIGHT to fight back. A cursor will move accros this box. Press Z to stop it. Aim for the middle.", false);
        } else if (turn == 3){
            box.setText("Select ACT to respond non-violently. Select ITEM and then select an item on the menu to heal. Select MERCY to act without engaging the monster.", false);
        } else if (turn == 4){
            box.setText("Once you have pacified the monster (usually by choosing the right ACT option) their name will turn yellow. Use MERCY to win.", false);
        } else if (turn == 5){
            box.setText("Now, your SOUL will start changing color. Each color has a unique mechanic to it. See if you can figure them out. Blue first.", false);
        } else if (turn == 6){
            box.setText("Green next.", false);
        } else if (turn == 7){
            box.setText("Now purple.", false);
        } else if (turn == 8){
            box.setText("And lastly, yellow. Press Z to fire.", false);
        } else if (turn == 9){
            box.setText("That's everything! Good luck! Please end this battle either using FIGHT or ACT and MERCY.", false);
        }
    }
    
    public void setDialogue(){
        if(turn == 1){
            bubble.setText("This is your SOUL. Try moving using the arrow keys.", true);
        }
    }
    
    public void setUpAttack(){
        attackTimer = ATTACK_TIMER_MAX / 2;
        attack = new TutorialAttack(turn, box, soulPanel, soul);
    }
    
    public void specialTurnSetup(){
        if(turn == 2){
            monsterDefense = 0.75;
        } else if (turn >= 8){
            monsterDefense = 0.75;
        } else {
             monsterDefense = 10;
        }
    }
    
    public void actOption1Selected(){
        bubble.setText("Why hello there!", true);
        sparable = true;
    }
    public void actOption2Selected(){}
    public void actOption3Selected(){}
    public void fleeSelected(){}
    
    public void introAnimation(){}
    public void endAnimation(){}
}
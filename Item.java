
public class Item{
    String name;
    int hp;
    String flavorText = "* I forgot to put flavor text!";

    public Item(String s, int i){
        name = s;
        hp = i;
    }

    public Item(String s, int i, String flavor){
        name = s;
        hp = i;
        flavorText = flavor;
    }

    private void adjustName(){
        if(name.length() > 8){
            name = name.substring(0, 7) + ".";
        } else if(name.length() < 8){
            for(int i = name.length(); i < 8; i++){
                name += " ";
            }
        }
    }
}
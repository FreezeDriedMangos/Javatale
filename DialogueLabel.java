import javax.swing.JLabel;

class DialogueLabel extends JLabel{
    String fullText = "";
    TextThread thread = new TextThread(this);;
    boolean canType = true;
    String soundFilePath = "";
    boolean typing = false;

    public DialogueLabel(){
        super();
    }
    
    public void setText(String s, boolean type){
        soundFilePath = "";
        thread.unlink(); //makes sure that the old thread won't interfere with the new one
        if(!typing){
            setVisible(true);
            fullText = s;
            
            if(type){
                canType = true;
                thread = new TextThread(this);
                thread.start();
            } else {
                canType = false;
                super.setText(toHTML(s));
            }
        }
    }
    
    public void setText(String s, String filePath){
        if(!typing){
            fullText = s;
            soundFilePath = filePath;
        
            if(true){
                canType = true;
                thread = new TextThread(this);
                thread.soundFilePath = soundFilePath;
                thread.start();
            } else {
                canType = false;
                super.setText(toHTML(s));
            }
        }
    }
    
    public void addText(String s){
        soundFilePath = "";
        if(!typing){
            canType = true;
            
            final DialogueLabel label = this;
            final String addText = s;
            new Thread(){
                public void run(){
                    for(int i = 1; i <= addText.length(); i++){
                        label.setText(DialogueLabel.toHTML(fullText + addText.substring(0, i)));
                        try{
                            if(soundFilePath != ""){
                                SoundPlayer.play(soundFilePath);
                            }
                        } catch (Exception e){
                            System.out.println("DialogueLabel, TextThread: Sound error, via:\n" + e.getMessage());
                        }

                        try{
                            Thread.sleep(60);
                        } catch (Exception e){}
                    }
                    label.fullText += addText;
                }
            }.start();
        } else {
            canType = false;
            super.setText(toHTML(s));
        }
    }
    
    public void addText(String s, String filePath){
        soundFilePath = filePath;
        addText(s);
    }
    
    public void setText(String s){
        if(canType){
            super.setText(s);
            
            try{
                if(soundFilePath != ""){
                    SoundPlayer.play(soundFilePath);
                }
            } catch (Exception e){
                System.out.println("DialogueLabel, TextThread: Sound error, via:\n" + e.getMessage());
            }
            
            //if(s.equals(fullText))
                //typing = false;
        }
    }
    public static String toHTML(String s){
        String htmlString = "<html>";
        int lastToken = -1;
        boolean fontTokenFound = false;

        for(int i = 0; i < s.length(); i++){ 
            if(s.charAt(i) == '\n'){
                htmlString += s.substring(lastToken + 1, i) + "<p style='margin-top:7'>";
                lastToken = i;
            } else if (s.charAt(i) == '\t'){
                htmlString += s.substring(lastToken + 1, i) + "<font color=black>[][][</font>";
                lastToken = i;
            } else if (s.charAt(i) == '\\'){ //custom token detected!
                if(i != s.length()){
                    if(!fontTokenFound){
                        if(s.charAt(i+1) == 'r'){ //make the next bit of text red
                            htmlString += s.substring(lastToken + 1, i) + "<font color=red>";
                            lastToken = i+1;
                            fontTokenFound = true;
                            //skip the 'r'
                            i++;
                            if(i >= s.length()){
                                i = s.length() - 1;
                                lastToken = i;
                            }
                        } else if(s.charAt(i+1) == 'y'){ //make the next bit of text yellow
                            htmlString += s.substring(lastToken + 1, i) + "<font color=yellow>";
                            lastToken = i+1;
                            fontTokenFound = true;
                            //skip the 'y'
                            i++;
                            if(i >= s.length()){
                                i = s.length() - 1;
                                lastToken = i;
                            }
                        }
                    } else { //end the special color
                        htmlString += s.substring(lastToken + 1, i) + "</font>";
                        fontTokenFound = false;
                        i++;
                        lastToken = i;
                    }
                }
            } else if(i == s.length() - 1){
                htmlString += s.substring(lastToken + 1);
            }
        }

        if (fontTokenFound){
            htmlString += "</font>";
        }

        htmlString += "</html>";
        return htmlString;
    }
}

class TextThread extends Thread{
    DialogueLabel label;
    String fullText;
    String soundFilePath = "";
    
    public TextThread(DialogueLabel l){
        super();
        label = l;
        fullText = l.fullText;
    }

    public void run(){
        for(int i = 1; i <= label.fullText.length(); i++){
            String text = fullText.substring(0, i);
            if(text.charAt(text.length() - 1) == '\\'){
                text = text.substring(0, text.length() - 1);
            }
            
            label.setText(DialogueLabel.toHTML(text));
            
            try{
                Thread.sleep(60);
            } catch (Exception e){}
        }
        
        label.typing = false;
    }
    
    //Severs the connection between the DialogueLabel that owns this thread and this thread
    public void unlink(){
        label = new DialogueLabel();
    }
}
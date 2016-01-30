package baylon.app;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Created by Troll173 on 1/30/2016.
 */
public class NumberBox {

    private TextField txt;


    public NumberBox(TextField txt) {
        this.txt = txt;
        txt.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (!"0123456789.".contains(keyEvent.getCharacter())) {
                    keyEvent.consume();
                }else{
                    if(keyEvent.getCharacter().contains(".") && txt.getText().contains(".")){
                        keyEvent.consume();
                    }
                }
            }
        });
    }


}

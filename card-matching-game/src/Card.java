import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Card extends JButton{
    //attributs
    //identifiant de la carte
    private int id;
    //Icon verso
    final private char backIcon;
    //Icon recto
    final private char frontIcon;
    //carte est renversée
    private boolean isFlipped = false;
    //carte peut etre renversee
    private boolean isFlippable = true;



    //constructeur
    public Card(int id, char backIcon, char frontIcon) {
        super(new ImageIcon("img/" + backIcon + ".png"));
        //on change le manager de l'UI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception e) { }
        this.id = id;
        this.backIcon = backIcon;
        this.frontIcon = frontIcon;
        this.setBorder(new MetalBorders.ButtonBorder());
    }

    //getters
    public int getId() {
        return id;
    }
    public char getFrontIcon() {
        return frontIcon;
    }
    public boolean isFlipped() {
        return isFlipped;
    }
    public boolean isFlippable() {
        return isFlippable;
    }

    //setters
    public void setFlippable(boolean flippable) {
        isFlippable = flippable;
    }
    //renverser carte
    public void flip(){
        isFlipped = !isFlipped;
        super.setIcon(new ImageIcon("img/" + this.actualIcon() + ".png") {});
        //super.setText(this.actualIcon() + "");
    }

    //Icon actuel
    public char actualIcon(){
        return isFlipped ? frontIcon:backIcon;
    }

}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;

import static java.lang.Thread.sleep;

public class CardMatchingGame extends JFrame implements  ActionListener {

    //Constantes
        //nombre de cartes
    private final int NUMBER_OF_CARDS = 16;
    private final int NUMBER_OF_DISTINCT_CARDS = NUMBER_OF_CARDS/2;
        //icons de cartes
    private final char[] ICONS= {'a','b','c','d','e','f','g','h'};

    //cartes
    private Card[] cards;


    //melanger les cartes
    private void shuffleCards(){
        List<Card> tmp = Arrays.asList(this.cards);
        Collections.shuffle(tmp);
        this.cards = tmp.toArray(new Card[0]);
    }
        //carte actuellement renversees
    private Card currentlyFlipped1,currentlyFlipped2;
        //numero de cliques
    private int numOfFlips;
        //numero de matchs
    private int numOfMatches;

    //paneaux
    private JPanel scorePanel,gamePanel,menuPanel,eastPanel;

    //label
    private JLabel scoreLabel;

    //buttons
    private JButton restartBtn,exitBtn;


    //constructeur
    public CardMatchingGame() {
        initCards();
        build();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch (Exception e){}

        Dimension dimEcran = this.getToolkit().getScreenSize();
        this.setSize(new Dimension((int)(dimEcran.getWidth()*0.85),dimEcran.height-20));
        this.setResizable(false);
        this.setLocation(dimEcran.width/2-this.getSize().width/2, dimEcran.height/2-this.getSize().height/2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    private void initCards() {
        this.cards = new Card[NUMBER_OF_CARDS];
        for (int i = 0; i < NUMBER_OF_DISTINCT_CARDS; i++) {
            this.cards[2*i] = new Card(i,'i',ICONS[i]) ;
            this.cards[2*i + 1] = new Card(i,'i',ICONS[i]) ;
        }
        this.shuffleCards();
    }

    //recommencer
    private void restart(){

//initscore
        numOfFlips = 0;
        scoreLabel.setText("Flips: " + numOfFlips);
        numOfMatches = 0;
        initCards();
        this.remove(gamePanel);
        buildGamePanel();
        this.add(gamePanel,BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
    }

    //Construction
    private void build() {
        this.buildMenuPanel();
        this.buildScorePanel();
        this.buildGamePanel();
        this.setLayout(new BorderLayout());
        GridLayout gridLayout = new GridLayout(2,1);
        eastPanel = new JPanel(gridLayout);
        eastPanel.add(scorePanel);
        eastPanel.add(menuPanel);
//        this.add(scoreLabel,BorderLayout.WEST);
        this.add(gamePanel,BorderLayout.CENTER);
        this.add(eastPanel,BorderLayout.EAST);
//        this.add(menuPanel,BorderLayout.EAST);
        this.pack();
    }

    private void buildScorePanel() {
        scoreLabel = new JLabel("Flips : " + numOfFlips);
        scoreLabel.setBorder(new EmptyBorder(0,10,0,10));
//        scoreLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        scorePanel = new JPanel(new BorderLayout());
//        scorePanel.setSize(menuPanel.getWidth(),menuPanel.getHeight());
        scorePanel.add(scoreLabel,BorderLayout.CENTER);
    }

    //construire paneau du jeu
    private void buildGamePanel(){
        GridLayout gridLayout = new GridLayout(4,4);
        gridLayout.setHgap(10);
        gridLayout.setVgap(10);

        this.gamePanel = new JPanel(gridLayout);
        this.gamePanel.setMaximumSize(new Dimension(600,600));
        this.gamePanel.setBorder(new EmptyBorder(10,10,10,10));


        for (int i = 0; i < this.cards.length; i++) {
            this.cards[i].addActionListener(this);
            this.gamePanel.add(this.cards[i]);
        }
    }

    //Construction paneau du menu
    private void buildMenuPanel(){
        Font btnFont = new Font("Monospaced", Font.BOLD, 20);
        restartBtn = new JButton("\u21BA" + " Restart ");
        restartBtn.setFont(btnFont);
        restartBtn.addActionListener(this);
        restartBtn.setMargin(new Insets(2,0,2,0));

        exitBtn = new JButton("  \u274C" + " Quit  ");
        exitBtn.setFont(btnFont);
        exitBtn.addActionListener(this);
        exitBtn.setMargin(new Insets(2,0,2,0));

        menuPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(menuPanel,BoxLayout.Y_AXIS);
        menuPanel.setLayout(boxLayout);
        menuPanel.setSize(exitBtn.getWidth() + 40,this.getHeight()/2);
        //menuPanel.setFont();
        menuPanel.setBorder(new EmptyBorder(180,20,0,20));
        menuPanel.add(restartBtn);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(exitBtn);

    }


//gestion des evnts
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == restartBtn)
            restart();

        if (actionEvent.getSource() == exitBtn)
            System.exit(0);

        if ( Arrays.asList(this.cards).contains(actionEvent.getSource())){
            Card card =(Card)actionEvent.getSource();
            if ( card.isFlippable() && card != currentlyFlipped1){
                card.flip();
                numOfFlips++;
                this.scoreLabel.setText("Flips: "+ numOfFlips);
                if (numOfFlips % 2 == 1)
                    currentlyFlipped1 = card;
                else {
                    currentlyFlipped2 = card;
                    if (currentlyFlipped2.getId() == currentlyFlipped1.getId()){
                        currentlyFlipped1.setFlippable(false);
                        currentlyFlipped2.setFlippable(false);
                        numOfMatches+=2;
                        currentlyFlipped1 = currentlyFlipped2 = null;
                        if (numOfMatches == NUMBER_OF_CARDS){

                        }
                    }else {
                        for (Card c:this.cards)
                            c.setFlippable(false);
                        Timer timer = new Timer(500, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                for (Card c:cards)
                                    c.setFlippable(true);
                                currentlyFlipped1.flip();
                                currentlyFlipped2.flip();
                                currentlyFlipped1 = currentlyFlipped2 = null;
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();

                    }
                }
            }

        }


    }
}

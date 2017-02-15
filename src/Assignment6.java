/* ==================================================================
*
*   PROGRAM NAME:
*       Assignment6 - BUILD Game
*
*   Description:
*	
*	>   Change the program into a Model-View-Controller Design Pattern. 
*           
*	>   Add a new part to the High-Card game by putting a timer on the 
*           side of the screen.  It will be on a timer to update every second,  
*           but in order for you to still play the game, you will need to use  
*           multithreading.  (Timer class) 
*           
*	>   Design a new game. 
*           
*	>   Redraw the UML diagram so that it represents your new structure.
*
*   Classes:
*       Card, Hand, Deck, GUICard, CardTable extends JFrame, CardGameFramework
*
*   Parameters:
*       1. none
*
*   Additional Files:
*
*   Created:
*       2017/02/08
*
*   Author/s:
*       Faiga Revah, Roderick Burkhardt, Oswaldo Minez
*
* ==================================================================*/

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Assignment6
{

    static CardTable myCardTable;
    static int NUM_CARDS_PER_HAND = 7;
    static int NUM_PLAYERS = 2;
    static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
    static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
    static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
    static Counter timerCounter;
    static JButton cannotPlayButton;
    static CardGameFramework highCardGame;
    static boolean gameInPlay = false;
    static Hand[] cardStacks = new Hand[2];
    static int[] cannotPlayCount = new int[NUM_PLAYERS];
    static int playerCardToPlay;
    static int currentPlayer;

    public static void main (String[] args)
    {
        GUICard.loadCardIcons();

        // Create CardGameFramework
        int numPacksPerDeck = 1;
        int numJokersPerPack = 0;
        int numUnusedCardsPerPack = 0;
        Card[] unusedCardsPerPack = null;

        highCardGame = new CardGameFramework(numPacksPerDeck,
                numJokersPerPack, numUnusedCardsPerPack, unusedCardsPerPack,
                NUM_PLAYERS, NUM_CARDS_PER_HAND);

        highCardGame.deal();
        cardStacks[0] = new Hand();
        cardStacks[1] = new Hand();

        myCardTable
                = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
        myCardTable.setSize(800, 600);
        myCardTable.setLocationRelativeTo(null);
        myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCardTable.setVisible(true);

        int playOrNot = JOptionPane.showConfirmDialog(null,
                "Ready to play BUILD?", "", JOptionPane.YES_NO_OPTION);
        if ( playOrNot == JOptionPane.YES_OPTION )
        {
            buildPanels();
            timerCounter.activateTimer();
        }
    }

    public static void buildPanels ()
    {
        int k;
        Icon tempIcon;

        CardClickListener clickListener = new CardClickListener();
        timerCounter = new Counter();

        for ( k = 0; k < NUM_CARDS_PER_HAND; k++ )
        {
            computerLabels[k] = new JLabel(GUICard.getBackCardIcon());
            if ( highCardGame.getHand(1).inspectCard(k) == null )
            {
                tempIcon = GUICard.getBlankIcon();
            }
            else
            {
                tempIcon = GUICard.getIcon(highCardGame.getHand(1).inspectCard(k));
            }
            humanLabels[k] = new JLabel(tempIcon);
            humanLabels[k].addMouseListener(clickListener);
        }

        for ( k = 0; k < NUM_PLAYERS; k++ )
        {
            playLabelText[k] = new JLabel("Stack " + k, JLabel.CENTER);
            cannotPlayCount[k] = 0;
            cardStacks[k].takeCard(highCardGame.getCardFromDeck());
            playedCardLabels[k] = new JLabel(GUICard.getIcon(cardStacks[k].inspectCard(0)),
                    JLabel.CENTER);
            playedCardLabels[k].addMouseListener(clickListener);
        }

        playerCardToPlay = -5;
        currentPlayer = 0;

        cannotPlayButton = new JButton("I cannot play");
        cannotPlayButton.addMouseListener(clickListener);

        // ADD LABELS TO PANELS -----------------------------------------
        for ( k = 0; k < NUM_CARDS_PER_HAND; k++ )
        {
            myCardTable.pnlComputerHand.add(computerLabels[k]);
            myCardTable.pnlHumanHand.add(humanLabels[k]);
        }

        myCardTable.pnlPlayArea.add(playedCardLabels[0]);
        myCardTable.pnlPlayArea.add(timerCounter);
        myCardTable.pnlPlayArea.add(playedCardLabels[1]);
        myCardTable.pnlPlayArea.add(playLabelText[0]);
        myCardTable.pnlPlayArea.add(cannotPlayButton);
        myCardTable.pnlPlayArea.add(playLabelText[1]);

        timerCounter.startCounter();

        myCardTable.setVisible(true);
        myCardTable.repaint();

    }

    public static void updateHumanPanel ()
    {
        highCardGame.getHand(1).takeCard(highCardGame.getCardFromDeck());
        //CardClickListener clickListener = new CardClickListener();
        for ( int k = 0; k < NUM_CARDS_PER_HAND; k++ )
        {

            humanLabels[k].setIcon(GUICard.getIcon(highCardGame.getHand(1).inspectCard(k)));
            //humanLabels[k].addMouseListener(clickListener);            
            myCardTable.pnlHumanHand.add(humanLabels[k]);
            humanLabels[k].revalidate();
        }
        //myCardTable.pnlHumanHand.revalidate();
    }

    public static void updatePlayArea ()
    {
        for ( int i = 0; i < cardStacks.length; i++ )
        {
            playedCardLabels[i].setIcon(GUICard.getIcon(cardStacks[i].inspectCard(cardStacks[i].getNumCards() - 1)));
            playedCardLabels[i].revalidate();
        }
    }

    public static class CardClickListener implements MouseListener
    {

        public void mouseClicked (MouseEvent event)
        {
            if ( event.getSource() == humanLabels[0] )
            {
                playerCardToPlay = 0;
            }
            else if ( event.getSource() == humanLabels[1] )
            {
                playerCardToPlay = 1;
            }
            else if ( event.getSource() == humanLabels[2] )
            {
                playerCardToPlay = 2;
            }
            else if ( event.getSource() == humanLabels[3] )
            {
                playerCardToPlay = 3;
            }
            else if ( event.getSource() == humanLabels[4] )
            {
                playerCardToPlay = 4;
            }
            else if ( event.getSource() == humanLabels[5] )
            {
                playerCardToPlay = 5;
            }
            else if ( event.getSource() == humanLabels[6] )
            {
                playerCardToPlay = 6;
            }
            else if ( event.getSource() == playedCardLabels[0] )
            {
                playCard(1, 0);
                //System.out.println("played card label 0");
            }
            else if ( event.getSource() == playedCardLabels[1] )
            {
                playCard(1, 1);
                //System.out.println("played card label 1");
            }
            else if ( event.getSource() == cannotPlayButton )
            {
                if ( currentPlayer == 1 )
                {
                    currentPlayer = 0;
                }
            }

            if ( currentPlayer == 0 && highCardGame.getNumCardsRemainingInDeck() != 0 )
            {
                computersPlay(highCardGame.getHand(0));
            }

            if ( highCardGame.getNumCardsRemainingInDeck() == 0 )
            {
                if ( cannotPlayCount[0] > cannotPlayCount[1] )
                {
                    JOptionPane.showMessageDialog(null, ("Human Wins Game!\nThanks for Playing"));
                }
                else if ( cannotPlayCount[0] < cannotPlayCount[1] )
                {
                    JOptionPane.showMessageDialog(null, ("Tie Game!\nThanks for Playing"));
                }
                else
                {
                    JOptionPane.showMessageDialog(null, ("Computer Wins Game!\nThanks for Playing"));
                }
            }
        }

        public void mousePressed (MouseEvent e)
        {
        }

        public void mouseReleased (MouseEvent e)
        {
        }

        public void mouseEntered (MouseEvent e)
        {
        }

        public void mouseExited (MouseEvent e)
        {
        }
    }

    public static int computersPlay (Hand hand)
    {
        hand.sortByVal();//sorts hand lowest to highest

        int numCards = hand.getNumCards();
        int middleCard = (numCards - 1) / 2; //middle index of hand

        //if there are two remaining cards, returns the lowest
        //if one remaining card, returns it.
        if ( numCards == 1 || numCards == 2 )
        {
            return 0;
        }
        if ( numCards == 0 )
        {
            return -1;
        }

        return middleCard;
    }

    public static void playCard (int player, int stack)
    {
        System.out.println(Card.valueAsInt(cardStacks[stack].inspectCard(cardStacks[stack].getNumCards() - 1)) + "  /  " + Card.valueAsInt(highCardGame.getHand(player).inspectCard(playerCardToPlay)));
        if ( (Card.valueAsInt(cardStacks[stack].inspectCard(cardStacks[stack].getNumCards() - 1))
                - Card.valueAsInt(highCardGame.getHand(player).inspectCard(playerCardToPlay)) == 1)
                || (Card.valueAsInt(cardStacks[stack].inspectCard(cardStacks[stack].getNumCards() - 1))
                - Card.valueAsInt(highCardGame.getHand(player).inspectCard(playerCardToPlay)) == -1) )

        {
            cardStacks[stack].takeCard(highCardGame.getHand(player).playCard(playerCardToPlay));
            updatePlayArea();
            updateHumanPanel();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Not a Valid Play", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}

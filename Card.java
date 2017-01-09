/******************************************************************************
 *  Compilation:  javac Card.java
 *  Execution:    java -cp .:cards.jar Card     (Unix, OS X)
 *  Execution:    java -cp .;cards.jar Card     (Windows)
 *  Dependencies: StdDraw.java
 *
 *  Implement a playing card in a standard 52 card deck.

 *  The sample client assumes cards.jar is in the current directory
 *  and contains images of the cards named: 0.gif through 51.gif.
 *
 *  The sample client display two blackjack hands.
 *
 ******************************************************************************/

public class Card { 
    private int suit;      // clubs = 0, diamonds = 1, hearts = 2, spades = 3
    private int rank;      // deuce = 0, three = 1, four = 2, ..., king = 11, ace = 12
    private String front;  // filename of image to display front of card
    private String back;   // filename of image to display back of card

    // create a new card based on integer 0 = 2C, 1 = 3C, ..., 51 = AS
    public Card(int card) {
        rank = card % 13;
        suit = card / 13;
    }

    // use this constructor if you want to plot the cards
    public Card(int card, String front, String back) {
        this(card);
        this.front = front;
        this.back  = back;
    }
 
    public void drawFront(double x, double y) { StdDraw.picture(x, y, front); }
    public void drawBack (double x, double y) { StdDraw.picture(x, y, back);  }

    public int rank() { return rank; }
    public int suit() { return suit; }


    // for sorting cards by suit, then rank, as in Bridge or Hearts
    public boolean less(Card c) {
        if      (suit < c.suit) return true;
        else if (suit > c.suit) return false;
        else if (rank < c.rank) return true;
        else                    return false;
    }

    // represent cards like "2H", "9C", "JS", "AD"
    public String toString() {
        String ranks = "23456789TJQKA";
        String suits = "CDHS";
        return ranks.charAt(rank) +  "" + suits.charAt(suit);
    }




   // test client
   public static void main(String[] args) { 
      StdDraw.setCanvasSize(400, 180);
      StdDraw.setXscale(0, 400);
      StdDraw.setYscale(0, 180);
      StdDraw.clear(StdDraw.GRAY);

      // draw dealer's cards
      Card c1 = new Card(27, "27.gif", "back.gif");
      c1.drawBack(100, 120);
      Card c2 = new Card(51, "51.gif", "back.gif");
      c2.drawFront(100, 60);
      StdOut.println(c1 + ", " + c2);

      // draw player's cards
      Card c3 = new Card(23, "23.gif", "back.gif");
      c3.drawFront(300, 120);
      Card c4 = new Card(30, "30.gif", "back.gif");
      c4.drawFront(300, 60);
      StdOut.println(c3 + ", " + c4);
   }

}



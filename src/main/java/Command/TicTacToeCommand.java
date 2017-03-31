/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import Config.Info;
import Config.Prefix;
import Main.*;
import static Main.Command.embed;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TicTacToeCommand extends ListenerAdapter implements Command {

    public final static String HELP = "Play a Tic Tac Toe game with anyone!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "tictactoe` or `" + Prefix.getDefaultPrefix() + "ttt\n"
                                    + "Parameter: `-h | @mention | null`\n"
                                    + "@mention: Mention an opponent to start the game.";
    private MessageReceivedEvent e;
    
    private String input;
    private Board game = new Board(3, 3);
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        input = e.getMessage().getContent();
    }
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        MessageReceivedEvent e = event;
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Tic Tac Toe -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length > 0)
        {
            e.getChannel().sendMessage("Game Mode ON!").queue();
            
            //Game
            int count = 0;
            String id = " ";
            String rows = "", columns = "";

            do
            {
                e.getChannel().sendMessage("Input: row column").queue();

                count++;
                if(count % 2 == 0)
                        id = "O";
                else
                        id = "X";
                try
                {
                    rows = input.substring(0,1);
                    columns = input.substring(2);
                }
                catch(StringIndexOutOfBoundsException es)
                {
                    e.getChannel().sendMessage("The 'Numbers' you enter is not valid.").queue();
                    count--;
                    continue;
                }
                catch(NullPointerException en)
                {
                    System.out.println("Click on the [X] button to quit this wonderful game!\n\n");
                }

                int row = Integer.parseInt(rows);
                int column = Integer.parseInt(columns);

                try
                {
                    if(game.isOccupied(row, column) == false)
                    {
                            game.addPiece(new Piece(id), row, column);
                    }
                    else
                    {
                            e.getChannel().sendMessage("The place is occupied. Use your eyes!").queue();

                            count--;
                            continue;
                    }
                }
                catch(ArrayIndexOutOfBoundsException ea)
                {
                    e.getChannel().sendMessage("This is TicTacToe. We don't have 1,000,000 x 1,000,000 that kind of board.").queue();
                    count--;
                    continue;
                }

                game.drawBoard();
            }
            while(makeLine().equals("none") && catGame() == false);

            game.drawBoard();
            if(makeLine().equals("X"))	e.getChannel().sendMessage("\nPlayer 1 (X) Wins!").queue();
            else if(makeLine().equals("O"))	e.getChannel().sendMessage("\nPlayer 2 (O) Wins!").queue();
            else if(catGame() == true) e.getChannel().sendMessage("Cat Game, no winner.").queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    //Piece Sub-class
    public class Piece
    {
        private String id;

        public Piece()
        {
                id = " ";
        }

        public Piece(String x)
        {
                id = x;
        }

        public String getID()
        {
                return id;
        }

        public boolean equals(Piece p)
        {
                if (this.getID().equals(p.getID()) && !this.getID().equals(" "))
                        return true;
                else
                        return false;
        }
    }
    
    //Board Sub-class
    public class Board
    {
	private int rows;
	private int cols;
	private Piece[][] board;

	public Board(int r, int c)
	{
            rows = r;
            cols = c;
            board = new Piece[r][c];

            Piece p;
            for (int i=0; i<rows; i++)
            {
                    for (int j=0; j<cols; j++)
                    {
                            p = new Piece();
                            addPiece(p, i, j);
                    }
            }
	}

	public void drawBoard()
	{
		e.getChannel().sendMessage("\n\n\nCurrent Board\n").queue();

		for (int i=0; i<rows; i++)
		{
                    for (int j=0; j<cols; j++)
                    {
                            String theID = board[i][j].getID();
                            e.getChannel().sendMessage("| " + theID + " ").queue();
                    }
                    e.getChannel().sendMessage("|").queue();
		}
	}

	public void addPiece(Piece x, int r, int c)
	{
		board[r][c] = x;
	}

	public Piece[][] getBoard()
	{
		return board;
	}

	public boolean isOccupied(int r, int c)
	{
		Piece p = board[r][c];
		String q = p.getID();

		if (q.equals(" "))
			return false;

		return true;
	}
    }
    
    //TicTacToe Methods
    public String makeLine()
    {
            Piece[][] board = game.getBoard();

            if (board[0][0].equals(board[0][1]) && board[0][1].equals(board[0][2]))
                return board[0][0].getID();
            else if (board[1][0].equals(board[1][1]) && board[1][1].equals(board[1][2]))
                return board[1][0].getID();
            else if (board[2][0].equals(board[2][1]) && board[2][1].equals(board[2][2]))
                return board[2][0].getID();

            else if (board[0][0].equals(board[1][0]) && board[1][0].equals(board[2][0]))
                return board[0][0].getID();
            else if (board[0][1].equals(board[1][1]) && board[1][1].equals(board[2][1]))
                return board[0][1].getID();
            else if (board[0][2].equals(board[1][2]) && board[1][2].equals(board[2][2]))
                return board[0][2].getID();

            else if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]))
                return board[0][0].getID();
            else if (board[2][0].equals(board[1][1]) && board[1][1].equals(board[0][2]))
                return board[2][0].getID();
            else
                return "none";
    }

    public boolean catGame()
    {
        Piece[][] end = game.getBoard();

        for(int i = 0; i < end.length; i ++)
        {
            for(int j = 0; j < end[0].length; j++)
            {
                    if(game.isOccupied(i,j) == false)
                            return false;
            }
        }
        return true;
    }
}

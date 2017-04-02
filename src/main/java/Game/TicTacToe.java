/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Config.Emoji;
import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TicTacToe implements Game{
    
    private Board game = new Board(3, 3);
    private static MessageReceivedEvent e;
    private static User starter;
    private static User opponent;
    private static String id;
    private static int row, column;
    private static User turn;
    
    private static EmbedBuilder embedstatus = new EmbedBuilder();
    private static EmbedBuilder embedgame = new EmbedBuilder();
    
    public static String origBoard = "[     ]  [ 0 ][ 1 ][ 2 ]\n"
                                    +"-------------------\n"
                                    +"[ 0 ]  |     |     |     |\n"
                                    +"-------------------\n"
                                    +"[ 1 ]  |     |     |     |\n"
                                    +"-------------------\n"
                                    +"[ 2 ]  |     |     |     |\n"
                                    +"-------------------\n";
    
    public TicTacToe(MessageReceivedEvent event)
    {
        e = event;
        
        startGame();
    }
    
    @Override
    public void startGame() //Keep da game runn'in
    {
        //Set Players
        starter = e.getAuthor();
        List<User> mentionedUsers = e.getMessage().getMentionedUsers();
        opponent = mentionedUsers.get(0);
        
        embedstatus.setColor(Color.green);
        embedstatus.addField(Emoji.game + " Tic Tac Toe: Game Mode ON!", "Starter: " + starter.getAsMention() + "\nOpponent: " + opponent.getAsMention(), true);
        MessageEmbed me = embedstatus.build();
        e.getChannel().sendMessage(me).queue();
        embedstatus.clearFields();
        
        turn = starter;
        e.getChannel().sendMessage(origBoard).queue();
    }
    
    @Override
    public void endGame() //Stop da game
    {
        if(e.getAuthor() == starter || e.getAuthor() == opponent)
        {
            embedstatus.setColor(Color.green);
            embedstatus.setTitle(Emoji.game + " Tic Tac Toe: Game Mode OFF!", null);
            embedstatus.setFooter(e.getAuthor().getName() + " ended the game.", null);

            MessageEmbed me = embedstatus.build();
            e.getChannel().sendMessage(me).queue();
            embedstatus.clearFields();
        }
        game.clearBoard();
    }
    
    @Override
    public void sendInput(String[] in, MessageReceivedEvent event)  //Set the input called by TicTacToeCommand
    {
        String rows = "", columns = "";
        try
        {
            rows = in[0];
            columns = in[1];

            row = Integer.parseInt(rows);
            column = Integer.parseInt(columns);

            //Assign ID (Piece O/X name)
            if(event.getAuthor() == opponent)
                id = "O";
            else if(event.getAuthor() == starter)
                id = "X";
            
            //Check who's turn is it
            if(event.getAuthor() == starter || event.getAuthor() ==  opponent)
            {
                if(event.getAuthor() != turn)
                {
                    e.getChannel().sendMessage(Emoji.error + " It's not your turn yet!").queue();
                    return;
                }
            }
            else
                e.getChannel().sendMessage(Emoji.error + " Do not interfere the game!").queue();
        
            if(game.isOccupied(row, column) == false)
            {
                game.addPiece(new Piece(id), row, column);
                game.drawBoard();
            }
            else
            {
                e.getChannel().sendMessage(Emoji.error + "The place is occupied. Use your eyes!").queue();
                return;
            }

            //Check for winner
            if(makeLine().equals("X")) 
            {
                e.getChannel().sendMessage("\nPlayer " + starter.getAsMention() + " (X) Wins!").queue();
                game.clearBoard();
            }
            else if(makeLine().equals("O"))
            {
                e.getChannel().sendMessage("\nPlayer " + opponent.getAsMention() +  " (O) Wins!").queue();
                game.clearBoard();
            }
            else if(catGame() == true)
            {
                e.getChannel().sendMessage("Cat Game, no winner.").queue();
                game.clearBoard();
            }
            else switchTurn();
            
        } catch(StringIndexOutOfBoundsException | NumberFormatException es)
        {
            e.getChannel().sendMessage("The 'Numbers' you enter is not valid.").queue();
            return;
        } catch(ArrayIndexOutOfBoundsException ea)
        {
            e.getChannel().sendMessage("This is TicTacToe. We don't have 1,000,000 x 1,000,000 that kind of board.").queue();
            return;
        }
    }
    
    @ Override
    public void switchTurn() //Switch turn between starter to opponent
    {
        if(starter == this.turn) 
            this.turn = opponent;
        else
            this.turn = starter;
    }
    
    //TicTacToe Methods
    public String makeLine() //Check for a line
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

    public boolean catGame() //Check for cat game
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
    
    //Board Sub-class
    public class Board
    {
	private int rows;
	private int cols;
	private Piece[][] board;
        private int round;

	public Board(int r, int c)
	{
            rows = r;
            cols = c;
            board = new Piece[r][c];
            round = 0;

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
            round++;
            String line = "------------\n";
            for (int i=0; i<rows; i++)
            {
                for (int j=0; j<cols; j++)
                {
                        String theID = board[i][j].getID();
                        line += "| " + theID + " ";
                }
                line += " |\n------------\n";
            }
            
            embedgame.setColor(Color.green);
            embedgame.setTitle(Emoji.game + " Current Board (Round " + round + ")\n", null);
            embedgame.setFooter(turn.getName() + " finished his/her turn.", null);

            MessageEmbed me = embedgame.build();
            e.getChannel().sendMessage(me).queue();
            embedgame.clearFields();
            
            e.getChannel().sendMessage(line).queue();
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

            if (q.equals("  "))
                    return false;

            return true;
	}
        
        public void clearBoard()
        {
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
    }
    
    public class Piece
    {
        private String id;

        public Piece()
        {
                id = "  ";
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
}

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import constants.Emoji;
import java.awt.Color;
import java.util.List;

import main.AIBot;
import net.dv8tion.jda.core.EmbedBuilder;
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

    public TicTacToe(MessageReceivedEvent event) {
        e = event;
        startGame();
    }
    
    @Override
    public void startGame() // Keep da GAME runn'in
    {
        // Set Players
        starter = e.getAuthor();
        List<User> mentionedUsers = e.getMessage().getMentionedUsers();

        try {
            opponent = mentionedUsers.get(0);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            e.getChannel().sendMessage(Emoji.ERROR + " Please mention a person the start the game.").queue();
        }

        EmbedBuilder embedstatus = new EmbedBuilder().setColor(Color.green)
            .addField(Emoji.GAME + " Tic Tac Toe: game Mode ON!",
            "Starter: " + starter.getAsMention() + "\nOpponent: " + opponent.getAsMention(), true);
        e.getChannel().sendMessage(embedstatus.build()).queue();
        
        turn = starter;
        StringBuilder origBoard = new StringBuilder();

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                origBoard.append(getEmojiPos(i,j));
            }
            origBoard.append("\n");
        }

        e.getChannel().sendMessage(origBoard.toString()).queue();
    }
    
    @Override
    public void endGame() //Stop da GAME
    {
        if(e.getAuthor() == starter || e.getAuthor() == opponent)
        {
            EmbedBuilder embedstatus = new EmbedBuilder()
                .setColor(Color.green)
                .setTitle(Emoji.GAME + " Tic Tac Toe: game Mode OFF!", null)
                .setFooter(e.getAuthor().getName() + " ended the game.", null);
            e.getChannel().sendMessage(embedstatus.build()).queue();
        }
        game.clearBoard();
        AIBot.getGuild(e.getGuild()).resetTicTacToe();
    }
    
    @Override
    public void sendInput(String[] in, MessageReceivedEvent event)  //Set the input called by TicTacToeCommand
    {
        try
        {
            switch (in[0]) {
                case "1" : row = 0; column = 0; break;
                case "2" : row = 0; column = 1; break;
                case "3" : row = 0; column = 2; break;
                case "4" : row = 1; column = 0; break;
                case "5" : row = 1; column = 1; break;
                case "6" : row = 1; column = 2; break;
                case "7" : row = 2; column = 0; break;
                case "8" : row = 2; column = 1; break;
                case "9" : row = 2; column = 2; break;
                default: throw new StringIndexOutOfBoundsException();
            }

            //Assign ID (Piece O/X name)
            if(event.getAuthor() == opponent)
                id = "O";
            else if(event.getAuthor() == starter)
                id = "X";
            
            //Check who's turn is it
            if(event.getAuthor() == starter || event.getAuthor() ==  opponent) {
                if(event.getAuthor() != turn) {
                    e.getChannel().sendMessage(Emoji.ERROR + " It's not your turn yet!").queue();
                    return;
                }
            }
            else
                e.getChannel().sendMessage(Emoji.ERROR + " Do not interfere the game!").queue();
        
            if(!game.isOccupied(row, column)) {
                game.addPiece(new Piece(id), row, column);
                game.drawBoard();
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " The place is occupied. Use your eyes!").queue();
                return;
            }

            //Check for winner
            if(makeLine().equals("X"))  {
                e.getChannel().sendMessage("\n" + Emoji.NO + "Player " + starter.getAsMention() + " (X) Wins!").queue();
                game.clearBoard();
                AIBot.getGuild(e.getGuild()).resetTicTacToe();
            } else if(makeLine().equals("O")) {
                e.getChannel().sendMessage("\n" + Emoji.YES + "Player " + opponent.getAsMention() +  " Wins!").queue();
                game.clearBoard();
                AIBot.getGuild(e.getGuild()).resetTicTacToe();
            } else if(catGame()) {
                e.getChannel().sendMessage("Cat game, no winner.").queue();
                game.clearBoard();
                AIBot.getGuild(e.getGuild()).resetTicTacToe();
            } else {
                switchTurn();
            }
            
        } catch(StringIndexOutOfBoundsException | NumberFormatException es) {
            e.getChannel().sendMessage(Emoji.ERROR + " The 'Numbers' you enter is not valid.").queue();
        } catch(ArrayIndexOutOfBoundsException ea) {
            e.getChannel().sendMessage(Emoji.ERROR + " Invalid place!").queue();
        }
    }
    
    private void switchTurn() { //Switch turn between starter to opponent
        if(starter == turn)
            turn = opponent;
        else
            turn = starter;
    }
    
    //TicTacToe Methods
    private String makeLine() //Check for a line
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

    private String getEmojiPos(int r, int c) {
        String emoji = "";
        if (r == 0) {
            if (c == 0) emoji = Emoji.ONE;
            else if (c == 1) emoji = Emoji.TWO;
            else if (c == 2) emoji = Emoji.THREE;
        } else if (r == 1) {
            if (c == 0) emoji = Emoji.FOUR;
            else if (c == 1) emoji = Emoji.FIVE;
            else if (c == 2) emoji = Emoji.SIX;
        } else if (r == 2) {
            if (c == 0) emoji =  Emoji.SEVEN;
            else if (c == 1) emoji = Emoji.EIGHT;
            else if (c == 2) emoji = Emoji.NINE;
        }

        if(game.isOccupied(r,c)) {
            if (game.getBoard()[r][c].getID().equals("X"))
                emoji = Emoji.NO;
            if (game.getBoard()[r][c].getID().equals("O"))
                emoji = Emoji.YES;
        }
        return emoji;
    }

    private boolean catGame() //Check for cat GAME
    {
        Piece[][] end = game.getBoard();

        for(int i = 0; i < end.length; i ++) {
            for(int j = 0; j < end[0].length; j++)  {
                    if(!game.isOccupied(i,j))
                            return false;
            }
        }
        return true;
    }
    
    // Board Sub-class
    public class Board {
        private int rows;
        private int cols;
        private Piece[][] board;
            private int round;

        private Board(int r, int c) {
            rows = r;
            cols = c;
            board = new Piece[r][c];
            round = 0;

            Piece p;
            for (int i=0; i<rows; i++) {
                for (int j=0; j<cols; j++) {
                    p = new Piece();
                    addPiece(p, i, j);
                }
            }
        }

        void drawBoard() {
            EmbedBuilder embedgame = new EmbedBuilder()
                .setColor(Color.green).setTitle(Emoji.GAME + " Current Board (Round " + round + ")\n", null)
                .setFooter(turn.getName() + " finished his/her turn.", null);
            e.getChannel().sendMessage(embedgame.build()).queue();
            round++;
            StringBuilder line = new StringBuilder();
            for (int i=0; i<rows; i++) {
                for (int j=0; j<cols; j++) {
                    line.append(getEmojiPos(i, j)).append(" ");
                }
                line.append("\n");
            }
            e.getChannel().sendMessage(line.toString()).queue();
        }

        void addPiece(Piece x, int r, int c) {
            board[r][c] = x;
        }

        Piece[][] getBoard()
        {
                return board;
        }

        boolean isOccupied(int r, int c) {
            Piece p = board[r][c];
            String q = p.getID();

            return !q.equals("  ");
        }

        void clearBoard() {
            Piece p;
            for (int i=0; i<rows; i++) {
                for (int j=0; j<cols; j++) {
                        p = new Piece();
                        addPiece(p, i, j);
                }
            }
        }
    }

    // Piece Sub-class
    public class Piece {
        private String id;

        Piece()
        {
                id = "  ";
        }

        Piece(String x)
        {
                id = x;
        }

        public String getID()
        {
                return id;
        }

        public boolean equals(Piece p) {
            return this.getID().equals(p.getID()) && !this.getID().equals(" ");
        }
    }
}

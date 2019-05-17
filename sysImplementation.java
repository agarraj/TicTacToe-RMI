import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

class Game{
    int player1;
    int player2;
    char[] state = new char[]{ '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    boolean turn;
    boolean timeout;
    //constructor
    public Game(int p1, int p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.turn = true;
        this.timeout = false;
    }

    //setter
    public void changeState(int pos, char x){
        state[pos] = x;
    }
}

public class sysImplementation extends UnicastRemoteObject implements sysInterface {

    //
    Integer genClientID = 1;
    Integer genGameID = 1000;

    protected sysImplementation() throws RemoteException {
        super();
    }

    HashMap<Integer, Integer> client2gameID = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> unpairedGame = new HashMap<Integer, Integer>();

    HashMap<Integer, Game> gameID2Game = new HashMap<>();

    //register client
    @Override
    public String registerClient() throws Exception {
        String response = null;
        response = Integer.toString(genClientID);
        response +=":";
        if(unpairedGame.containsKey(genGameID)){
            client2gameID.put(genClientID, genGameID);
            int player1ID = unpairedGame.get(genGameID);
            int player2ID = genClientID;

            //create a new game
            if(new Random().nextInt(2)==1){
                int temp = player1ID;
                player1ID = player2ID;
                player2ID = temp;
            }
            Game newGame = new Game(player1ID, player2ID);
            gameID2Game.put(genGameID, newGame);
            unpairedGame.remove(genGameID);
            response += Integer.toString(genGameID);
            genGameID += 1;

        }
        else{
            unpairedGame.put(genGameID,genClientID);
            client2gameID.put(genClientID,genGameID);
            response += Integer.toString(genGameID);
        }

        genClientID += 1;
        return response;
    }

    //isgameReady
    @Override
    public boolean isGameReady(int gameID) throws Exception {
        if(gameID2Game.containsKey(gameID))
            return true;
        else
            return false;
    }

    @Override
    public String whoAmI(int gameID, int clientID) throws Exception {
        Game game = gameID2Game.get(gameID);
        if(game.player2==clientID)
            return "Player2";
        else
            return "Player1";
    }

    //validate state
    @Override
    public String validateState(int gameID, int clientID) throws Exception {
        Game game = gameID2Game.get(gameID);
        char[] square = game.state;
        if(game.timeout)
            return "timeout";
        if (square[0] == square[1] && square[1] == square[2])
            return Character.toString(square[0]);

        else if (square[3] == square[4] && square[4] == square[5])
            return Character.toString(square[3]);

        else if (square[6] == square[7] && square[7] == square[8])
            return Character.toString(square[6]);

        else if (square[0] == square[3] && square[3] == square[6])
            return Character.toString(square[0]);

        else if (square[1] == square[4] && square[4] == square[7])
            return Character.toString(square[1]);

        else if (square[2] == square[5] && square[5] == square[8])
            return Character.toString(square[2]);

        else if (square[0] == square[4] && square[4] == square[8])
            return Character.toString(square[0]);

        else if (square[2] == square[4] && square[4] == square[6])
            return Character.toString(square[2]);

        else if (square[0] != '1' && square[1] != '2' && square[2] != '3'
                && square[3] != '4' && square[4] != '5' && square[5] != '6'
                && square[6] != '7' && square[7] != '8' && square[8] != '9')
            return "draw";

        return "no";
    }

    //get state
    @Override
    public char[] getstate(int gameID) throws Exception {
        Game game = gameID2Game.get(gameID);
        char[] square = game.state;
        return square;
    }


    //get turn
    @Override
    public boolean turn(String playerName, int gameID) throws Exception {
        Game game = gameID2Game.get(gameID);
        if(game.turn == true && playerName.equals("Player1"))
            return true;
        if(game.turn == false && playerName.equals("Player2"))
            return true;
        return false;
    }


    //update state
    @Override
    public void updateState(String PlayerName, int gameID, int pos) throws Exception {
        Game game = gameID2Game.get(gameID);
        if(PlayerName.equals("Player1"))
            game.changeState(pos,'x');
        else
            game.changeState(pos,'o');
        game.turn ^= true;
    }

    //timeout
    @Override
    public void setTimeout(int gameID) throws Exception {
        Game game = gameID2Game.get(gameID);
        game.timeout = true;
        game.turn ^= true;

    }


}

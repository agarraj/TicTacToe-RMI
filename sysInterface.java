import java.rmi.Remote;

public interface sysInterface extends Remote {
    public String registerClient() throws Exception;
    public boolean isGameReady(int gameID) throws Exception;
    public String whoAmI(int gameID, int clientID) throws Exception;
    public String validateState(int gameID, int clientID) throws Exception;
    public char [] getstate(int gameID) throws Exception;
    public boolean turn(String playerName, int gameID) throws Exception;
    public void updateState(String PlayerName, int gameID, int pos) throws Exception;
    public void setTimeout(int gameID) throws Exception;
}

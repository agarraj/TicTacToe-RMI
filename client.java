import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Helper extends TimerTask
{
    boolean timeout;
    Helper(boolean t){
        this.timeout = t;
    }
    public void run()
    {
        timeout = true;
    }
}

public class client {
//    static boolean timeout;


    public static void main(String[] args) throws Exception {
        sysInterface obj = (sysInterface) Naming.lookup("tictac");
        System.out.println("Client Started");
        System.out.println("join the game--Press ENTER");
        System.in.read();


        //register client and get clientID and gameID
        while(true){
            String response = obj.registerClient();
            String[] arrOfID = response.split(":", 0);

            int clientID = Integer.parseInt(arrOfID[0]);
            int gameID = Integer.parseInt(arrOfID[1]);
            boolean flag = true;
            while(true){
                if(obj.isGameReady(gameID)){
                    System.out.println("Game Ready");
                    break;
                }
                else if(flag){
                    System.out.println("Waiting For another Player");
                    flag = false;
                }

            }

            //get playerNumber
            String myName = obj.whoAmI(gameID,clientID);
            System.out.println(myName);

            while(true){
                String result = obj.validateState(gameID, clientID);
                if(!result.equals("no")){
                    if(result.equals("draw")){
                        System.out.println("Draw");
                        break;
                    }
                    if(myName.equals("Player1") && result.equals("x")){
                        System.out.println("You Won");
                    }
                    else  if(myName.equals("Player2") && result.equals("o")){
                        System.out.println("You Won");
                    }
                    else
                        System.out.println("You Lost");

                    break;
                }

                char [] square = obj.getstate(gameID);
                print(square);

                boolean turn = obj.turn(myName, gameID);
                boolean printFlag = true;
                while(!turn){
                    turn = obj.turn(myName, gameID);
                    if(printFlag)
                        System.out.println("Waiting for opponent Turm");
                    printFlag = false;
                }
                result = obj.validateState(gameID, clientID);
                if(result.equals("timeout")){
                    System.out.println("You Won");
                    break;
                }
                else if(!result.equals("no")){
                    if(result.equals("draw")){
                        System.out.println("Draw");
                        break;
                    }
                    if(myName.equals("Player1") && result.equals("x")){
                        System.out.println("You Won");
                    }
                    else  if(myName.equals("Player2") && result.equals("o")){
                        System.out.println("You Won");
                    }
                    else
                        System.out.println("You Lost");

                    break;
                }


                //print state
                square = obj.getstate(gameID);

                if(printFlag==false)
                    print(square);

                System.out.println("Your Turn");
                Scanner sc = new Scanner(System.in);
                // Character input
                //creating a new instance of timer class
                Timer timer = new Timer();
                boolean timeout = false;
                TimerTask task = new Helper(timeout);
                timer.schedule(task, 10000);

                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                long startTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - startTime) < 10 * 1000
                        && !in.ready()) {
                }

                if (in.ready()) {
                    char c = (char)in.read();
//                        int num = Integer.parseInt(in.readLine());
//                        char c=(char)num;
                    int num = (int) c-'0';
                    if(!Character.isDigit(c) || num <=0 || num >=10 || square[num-1]!=c ){
//                            System.out.println(square[num-1]);
                        System.out.println("Not a Valid input");
                    }
                    else{
                        obj.updateState(myName, gameID, num-1);
//                        break;
                    }
                }
                else{
                    System.out.println("timed out");
                    obj.setTimeout(gameID);
                    break;
                }

            }

            System.out.println("Play another game?");
            Scanner sc = new Scanner(System.in);
            char inp = sc.next().charAt(0);
            int number = (int) inp -'0';
            if(number==0){
                break;
            }
            else if(number == 1){
                continue;
            }
        }


    }

    private static void print(char[] square) {
        System.out.println( "   |   |   ");

        System.out.println(" "+square[0]+" | "+ square[1]+ " | "+ square[2]);
        System.out.println( "___|___|___");
        System.out.println( "   |   |   ");


        System.out.println(" "+square[3]+" | "+ square[4]+ " | "+ square[5]);
        System.out.println( "___|___|___");
        System.out.println( "   |   |   ");

        System.out.println(" "+square[6]+" | "+ square[7]+ " | "+ square[8]);

        System.out.println( "   |   |   ");
    }




}


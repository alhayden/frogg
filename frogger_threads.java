import java.util.*;
import java.util.concurrent.*;
import java.net.*;

public class ControlThread extends Thread{

    //high-level variable determining the overall state of the game:
    //basically, what screen is displayed? main menu? network? high score? gameplay?
    private GameState gamestate;

    //is this instance of the game the server, or a client
    private boolean isServer;

    //which players are local to this machine?
    //store their data (keypresses, username, color, player numbers, etc)
    private Player[] localPlayers;

    //threads to handle IO operations and present data as a queue to the main thread(s)
    private IOThread        ioThread;
    private NetworkRxThread rxThread;
    private NetworkTxThread txThread;

    //queues to communicate with these threads
    private final PriorityBlockingQueue<IOPacketData> ioQueue = new PriorityBlockingQueue<IOPacketData>(/*define comparator for time since epoch*/);
    private final PriorityBlockingQueue<FrogPacketData> rxQueue = new PriorityBlockingQueue<FrogPacketData>();
    private final PriorityBlockingQueue<FrogPacketData> txQueue = new PriorityBlockingQueue<FrogPacketData>();

    //queues to deliver commands to these threads
    private final BlockingQueue<ThreadCommandData> ioCommand = new ArrayBlockingQueue<ThreadCommandData>(4, true);
    private final BlockingQueue<ThreadCommandData> rxCommand = new ArrayBlockingQueue<ThreadCommandData>(4, true);
    private final BlockingQueue<ThreadCommandData> txCommand = new ArrayBlockingQueue<ThreadCommandData>(4, true);


    public ControlThread( )
    {
        //initialize I/O threads
 
    }

    public void run()
    {

        //next gamestate, as determined by the previous state's update function
        GameState newState;

        //in game: run the update functions in 
        //scheduled thread from thread pool
        //using scheduledThreadPoolExecutor

        //CLEANUP_THREAD works as an extra level to GAME_CLOSING.
        //after GAME_CLOSING cleans all other objects, state is
        //CLEANUP_THREAD and this thread shuts down its threads and itself
        while( gamestate != GameState.CLEANUP_THREAD )
        {
            switch( gamestate )
            {
            case GAME_LOADING:
                newState = update_game_loading();
                break;
            case MAIN_MENU:
                newState = update_main_menu();
                break;
            case MULTIPLAYER_MENU:
                newState = update_multiplayer_menu();
                break;
            case LEVEL_SELECT:
                newState = update_level_select();
                break;
            case LOADING_LEVEL:
                //maybe this includes synchronization as well?
                newState = update_loading_level();
                break;
            case LEVEL:
                newState = update_level();
                break;
            case HIGHSCORE_MENU:
                newState = update_highscore_menu();
                break;
            case GAME_CLOSING:
                newState = update_game_closing();
                break;
            default:
                //not sure what to do in this case
            }
        }
        //now, cleanup this thread
    }
    
}

public class IOThread extends Thread{
//keypresses->queue->update for player objects/server
//and pass them to network TX thread

    public void run()
    {

    }
}


public enum GameState{
    GAME_LOADING,
    MAIN_MENU,
    MULTIPLAYER_MENU,
    LEVEL_SELECT,
    LOADING_LEVEL,
    LEVEL,
    HIGHSCORE_MENU,
    GAME_CLOSING,
    CLEANUP_THREAD
}


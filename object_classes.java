class Tile{
    //placeholder class for the tiles in a chunk's logical coordinate system
}

class Chunk{
    //this class represents regions that frog chracters can hop on
    //it is represented by a logical coordinate system that is transformed
    //from the global coordinate system

    //transformation variables for physical coordinates
    private float translateX;
    private float translateY;
    private float translateZ;
    private float eulerPhi;
    private float eulerTheta;
    private float eulerPsi;

    //logical coordinate system
    private int widthX;
    private int widthY;
    private Tile[][] chunk;

    //does this chunk move? (like a log, etc)
    private boolean mobile;

    public void chunkToWorld(){};
    public void tickUpdate(){};

}

class Mobile{
    //almost all objects that move over time

    //position and rotation variables for mobile object
    private float coordX;
    private float coordY;
    private float coordZ;
    private float eulerPhi;
    private float eulerTheta;
    private float eulerPsi;    

    //hitbox

    //counter for animation, lifetime, etc.
    private int animationTick;

    //mesh data?

    //update function? or should that be compiled elsewhere
    private int objID;

    private boolean enable;
}


class Obstacle extends Mobile{
    //determine if the passed player object is killed by the obstacle
    public boolean collide(PlayerObject frog){return false;};
}

class Platform extends Mobile{
    //determine if the passed object with jump parameters will land on the platform
    public boolean land(PlayerObject frog){return false;};
}

class PlayerObject extends Mobile{
    //the object representing the player's characters
    private int timeRemaining;
    private int points;
    private int lives;
    private int[] collectedFlags;   //so it can store the ID of each flag (choose better data type)

    //logical coordinates in current chunk
    private int status; //general status: dead/respawning, on chunk or on platform, etc
    //private   //represent the current chunk. pointer to the object would be ideal
    private int logicalX;
    private int logicalY;
    private int logicalZ;
    private int logicalFacing; 
}


class level{
    private Chunk[] chunks;
    private PlayerObject[] players;
    private Platform[] platforms;
    private Obstacle[] obstacles;

    private long tick;




}
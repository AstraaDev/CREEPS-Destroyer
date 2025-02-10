package mobs;

import action.*;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.response.InitResponse;

public abstract class Citizen extends Thread{

    InitResponse initResponse;
    double timeout;
    ServerCall call;
    Action action;
    String id;

    public Citizen(InitResponse initResponse, double timeout, ServerCall call, Action action, String id) {
        this.initResponse = initResponse;
        this.timeout = timeout;
        this.call = call;
        this.action = action;
        this.id = id;
    }

    public void run() {
        while(true) {
            action.move(id, Direction.UP);
            action.move(id,Direction.LEFT);
            action.move(id,Direction.RIGHT);
            action.move(id,Direction.DOWN);
        }
    }
}


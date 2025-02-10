package mobs;


import action.*;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Objects;

public abstract class Citizen extends Thread {

    InitResponse initResponse;
    ServerCall call;
    Double timeout;
    String id;
    Action action;
    Point position;

    public Citizen(InitResponse initResponse, ServerCall call, Double timeout, String id, Point position) {
        this.initResponse = initResponse;
        this.call = call;
        this.timeout = timeout;
        this.id = id;
        this.action = new Action(initResponse, call, timeout);
        this.position = position;
    }

    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                action.move(id, Direction.LEFT);
                action.move(id, Direction.LEFT);
                action.move(id, Direction.DOWN);
                action.move(id, Direction.DOWN);
                action.move(id, Direction.RIGHT);
                action.move(id, Direction.RIGHT);
                action.move(id, Direction.UP);
                CommandResponse res =  action.move(id, Direction.UP);
                if (Objects.equals(res.errorCode, "noplayer"))
                    break;
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

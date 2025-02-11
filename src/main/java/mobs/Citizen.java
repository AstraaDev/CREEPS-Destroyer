package mobs;

import action.*;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public abstract class Citizen extends Thread {

    ServerCall call;
    InitResponse initResponse;
    String citizenId;
    Action action;
    Position position;
    double timeout;

    public Citizen(ServerCall call, InitResponse initResponse, String citizenId, Action action, Position position, double timeout) {
        this.call = call;
        this.initResponse = initResponse;
        this.citizenId = citizenId;
        this.action = action;
        this.position = position;
        this.timeout = timeout;
    }

    public Point isCycleSafe(Point citizenPosition, int cycleLength) throws UnirestException {
        Point coordHdv = position.findNearestHdv(citizenPosition);
        int hdvDistance = position.calcDistance(citizenPosition, coordHdv);

        if ((position.nextGc() - cycleLength) / (initResponse.costs.move.cast * timeout) <= hdvDistance) {
            return coordHdv;
        }
        return null;
    }

    public void run() {
        int cycleLength = 4;
        // TODO : recover citizen position
        Point citizenPosition = null;

        while(true) {
            Point coordHdv = null;
            try {
                coordHdv = isCycleSafe(citizenPosition, cycleLength);
                if (coordHdv != null) {
                    position.goTo(citizenId, citizenPosition, coordHdv);
                    action.unload(citizenId);
                    continue;
                }
            } catch (UnirestException e) {
                // TODO : manage exception
                throw new RuntimeException(e);
            }

            action.move(citizenId, Direction.UP);
            action.move(citizenId,Direction.LEFT);
            action.move(citizenId,Direction.RIGHT);
            action.move(citizenId,Direction.DOWN);
        }
    }
}


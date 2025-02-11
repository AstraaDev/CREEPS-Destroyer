package action;

import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Position extends Thread {

    ServerCall call;
    InitResponse initResponse;
    Action action;

    public Position(ServerCall call, InitResponse initResponse, Action action) {
        this.call = call;
        this.initResponse = initResponse;
        this.action = action;
    }

    // Find the nearest hdv coord
    public Point findNearestHdv(Point citizenPosition) {
        // Only one hdv for now, to be modified later
        Point hdvPosition = initResponse.townHallCoordinates;
        return hdvPosition;
    }

    // Calculate a distance between two positions
    public int calcDistance(Point start, Point destination) {
        int distance = Math.abs(start.x - destination.x) + Math.abs(start.y - destination.y);
        return distance;
    }

    // Moves user from his current position to a destination
    public void goTo(String playerId, Point citizenPosition, Point destination) {
        int diffX = destination.x - citizenPosition.x;
        int diffY = destination.y - citizenPosition.y;

        Direction hMove = (diffX > 0) ? Direction.RIGHT : Direction.LEFT;
        Direction vMove = (diffY > 0) ? Direction.UP : Direction.DOWN;

        for (int i = 0; i < Math.abs(diffX); i++) {
            action.move(playerId, hMove);
        }
        for (int i = 0; i < Math.abs(diffY); i++) {
            action.move(playerId ,vMove);
        }
    }

    // Calculate the number of ticks remaining before the next GC
    public int nextGc() throws UnirestException {
        int gcTickRate = initResponse.setup.gcTickRate;

        StatisticsResponse stat = Json.parse(call.getStatistics(), StatisticsResponse.class);
        int actualTick = stat.tick;

        int nextGc = gcTickRate - (actualTick % gcTickRate);

        return nextGc;
    }
}

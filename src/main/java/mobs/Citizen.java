package mobs;

import action.*;
import com.epita.creeps.given.exception.NoReportException;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Tile;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.ObserveReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public abstract class Citizen extends Thread {

    ServerCall call;
    InitResponse initResponse;
    String citizenId;
    Action action;
    Position position;
    double timeout;
    boolean debugMode;

    public Citizen(ServerCall call, InitResponse initResponse, String citizenId, Action action, Position position, double timeout, boolean debugMode) {
        this.call = call;
        this.initResponse = initResponse;
        this.citizenId = citizenId;
        this.action = action;
        this.position = position;
        this.timeout = timeout;
        this.debugMode = debugMode;
    }

    public class DirectionInfo {
        public final Direction direction;
        public final int dx;
        public final int dy;

        public DirectionInfo(Direction direction, int dx, int dy) {
            this.direction = direction;
            this.dx = dx;
            this.dy = dy;
        }
    }

    public DirectionInfo getDirection(int direction) {
        return switch (direction % 4) {
            case 0 -> new DirectionInfo(Direction.UP, 0, 1);
            case 1 -> new DirectionInfo(Direction.RIGHT, 1, 0);
            case 2 -> new DirectionInfo(Direction.DOWN, 0, -1);
            case 3 -> new DirectionInfo(Direction.LEFT, -1, 0);
            default -> throw new IllegalArgumentException("Invalid direction");
        };
    }

    public boolean isCycleSafe(Point citizenPosition, int cycleLength) throws UnirestException {
        Point coordHdv = position.findNearestHdv(citizenPosition);
        int hdvDistance = position.calcDistance(citizenPosition, coordHdv);

        if (debugMode)
            System.out.println((position.nextGc() - cycleLength) + " / " + (initResponse.costs.move.cast) + " = " + ((position.nextGc() - cycleLength) / (initResponse.costs.move.cast)) + " <= " + hdvDistance);

        return (position.nextGc() - cycleLength) / (initResponse.costs.move.cast) > hdvDistance;
    }

    public Point nextSingleTarget(Point position, Tile targetType) {
        int[][] directions = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0}, // 1 move
                {1, 1}, {1, -1}, {-1, -1}, {-1, 1}, // 2 pseudo-diagonale moves
                {0, 2}, {2, 0}, {0, -2}, {-2, 0}, // 2 direct moves
                {-1, -2},{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, 2}, {-2, -1}, {-2, 1}, // 3 pseudo-diagonale moves
                {2, 2}, {2, -2}, {-2, -2}, {-2, 2}, // 4 pseudo-diagonale moves
        };

        for (int[] dir : directions) {
            Point p = new Point(position.x + dir[0], position.y + dir[1]);
            Tile tile = Cartographer.INSTANCE.requestTileType(p);
            if (tile != null && tile.equals(targetType)) {
                return p;
            }
        }
        return null;
    }

    public Point nextMultipleeTarget(Point position, Tile targetType1, Tile targetType2, Tile targetType3) {
        int[][] directions = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0}, // 1 move
                {1, 1}, {1, -1}, {-1, -1}, {-1, 1}, // 2 pseudo-diagonale moves
                {0, 2}, {2, 0}, {0, -2}, {-2, 0}, // 2 direct moves
                {-1, -2},{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, 2}, {-2, -1}, {-2, 1}, // 3 pseudo-diagonale moves
                {2, 2}, {2, -2}, {-2, -2}, {-2, 2}, // 4 pseudo-diagonale moves
        };

        for (int[] dir : directions) {
            Point p = new Point(position.x + dir[0], position.y + dir[1]);
            Tile tile = Cartographer.INSTANCE.requestTileType(p);
            if (tile != null && (tile.equals(targetType1) || tile.equals(targetType2) || tile.equals(targetType3))) {
                return p;
            }
        }
        return null;
    }

    public void run() {
        int cycleLength = 4;
        CommandResponse observeResponse = action.observe(citizenId);
        Point citizenPosition = null;

        try {
            ObserveReport observeReport = Json.parseReport(call.getReport(observeResponse.reportId));
            citizenPosition = observeReport.unitPosition;
        } catch (NoReportException | UnirestException e) {
            // TODO : manage exception
            System.out.println("Citizen-1 Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        while(true) {
            try {
                Point coordHdv = position.findNearestHdv(citizenPosition);
                boolean isCycleSafe = isCycleSafe(citizenPosition, cycleLength);
                if (!isCycleSafe) {
                    position.goTo(citizenId, citizenPosition, coordHdv);
                    citizenPosition = new Point(coordHdv.x, coordHdv.y);
                    action.unload(citizenId);
                    continue;
                }
            } catch (UnirestException e) {
                System.out.println("Citizen-2 Error: " + e.getMessage());
                // TODO : manage exception
                // throw new RuntimeException(e);
            }

            action.move(citizenId, Direction.UP);
            citizenPosition = new Point(citizenPosition.x, citizenPosition.y + 1);
            action.move(citizenId, Direction.LEFT);
            citizenPosition = new Point(citizenPosition.x - 1, citizenPosition.y);
            action.move(citizenId, Direction.RIGHT);
            citizenPosition = new Point(citizenPosition.x + 1, citizenPosition.y);
            action.move(citizenId, Direction.DOWN);
            citizenPosition = new Point(citizenPosition.x, citizenPosition.y - 1);
        }
    }
}


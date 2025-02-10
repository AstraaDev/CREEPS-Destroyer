package action;

import asynctask.MyTask;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Resources;
import com.epita.creeps.given.vo.Tile;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.parameter.FireParameter;
import com.epita.creeps.given.vo.parameter.MessageParameter;
import com.epita.creeps.given.vo.report.GatherReport;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Action {

    InitResponse initResponse;
    ServerCall call;
    Double timeout;

    public Action(InitResponse initResponse, ServerCall call, Double timeout) {
        this.initResponse = initResponse;
        this.call = call;
        this.timeout = timeout;
    }

    public CommandResponse noop() {
        return null;
    }

    public StatisticsResponse getStatistics() throws UnirestException {
        String tmp = call.getStatistics();
        return Json.parse(tmp, StatisticsResponse.class);

    }

    public CommandResponse move(String id, Direction direction) throws UnirestException {
        return MyTask.of(() -> {
            try {
                String nextDirection = direction.toString();
                String tmp = call.postCommand(id, "move:" + nextDirection.toLowerCase(), "{}");

                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.move.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse observe(String id) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "observe", "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.observe.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse gather(String id) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "gather", "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.gather.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse unload(String id) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "unload", "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.unload.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse farm(String id) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "farm", "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.farm.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse build(String id, String build) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "build:" + build, "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.farm.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse spawn(String id, String unit) {
        int cost;
        if (Objects.equals(unit, "turret")) {
            cost = initResponse.costs.spawnTurret.cast;
        } else if (Objects.equals(unit, "bomber-bot")) {
            cost = initResponse.costs.spawnBomberBot.cast;
        } else
            return null;

        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "spawn:" + unit.toLowerCase(), "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * cost), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse dismantle() {
        return null;
    }

    public CommandResponse upgrade() {
        return null;
    }

    public CommandResponse refine() {
        return null;
    }

    public CommandResponse message(String id, String recipient) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "message:send", Json.serialize(new MessageParameter(recipient, "spam de t'es mort")));
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).execute();
    }

    public CommandResponse messageFetch(String id) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "message:fetch", "{}");
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).execute();
    }

    public CommandResponse fireTurret(String id, FireParameter parameter) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "fire:turret", Json.serialize(parameter));
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.fireTurret.cast), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse fireBomberBot(String id, FireParameter parameter) {
        return MyTask.of(() -> {
            try {
                String tmp = call.postCommand(id, "fire:bomber-bot", Json.serialize(parameter));
                return Json.parse(tmp, CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (timeout * initResponse.costs.fireTurret.cast), TimeUnit.MILLISECONDS).execute();
    }

    public Direction getDirectionFor(Point pos, Point target) {
        Direction result = null;

        int directionX = target.x - pos.x;
        int directionY = target.y - pos.y;

        if (directionX > 0)
        {
            result = Direction.RIGHT;
        }
        else if (directionX < 0)
        {
            result = Direction.LEFT;
        }
        else if (directionY > 0)
        {
            result = Direction.UP;
        }
        else if (directionY < 0)
        {
            result = Direction.DOWN;
        }

        return result;
    }


    public Point nextTarget(Point position) {
        Point target = null;
        for (int i = position.x - 2; i < position.x + 2; i++) {
            for (int j = position.y - 2; j < position.y + 2; j++) {
                Point pos = new Point(i, j);
                Tile tile = Cartographer.INSTANCE.requestTileType(pos);

                if (tile != null) {
                    if (tile == Tile.Water) {
                        target = pos;
                        return target;
                    }
                }
            }
        }
        return target;
    }

    public Point spiralMagic(Point position, String id) throws UnirestException {

        MoveReport move;
        CommandResponse tmp;
        MoveReport report;

        for (int h = 0; h < 2; h++) {
            int count = 1;
            while (count < 5) {
                for (int i = 0; i < count; i++) {
                    Direction nextDirection = (count % 2 == 0) ? Direction.DOWN : Direction.UP;
                    tmp = move(id, nextDirection);
                    report = (MoveReport) call.getReport(tmp.reportId);
                    if (report != null)
                        position = report.newPosition;
                    gather(id);
                    if (h % 2 == 0)
                        farm(id);
                }

                for (int i = 0; i < count; i++) {
                    Direction nextDirection = (count % 2 == 0) ? Direction.LEFT : Direction.RIGHT;
                    tmp = move(id, nextDirection);
                    report = (MoveReport) call.getReport(tmp.reportId);
                    if (report != null)
                        position = report.newPosition;
                    gather(id);
                    if (h % 2 == 0)
                        farm(id);
                }
                count++;
            }
            tmp = move(id, Direction.RIGHT);
            tmp = move(id, Direction.RIGHT);
            tmp = move(id, Direction.UP);
            tmp = move(id, Direction.UP);
            report = (MoveReport) call.getReport(tmp.reportId);
            if (report != null)
                position = report.newPosition;
        }
        return position;
    }

    public Resources addResources(GatherReport report)
    {
        Resources tmp = new Resources(0,0,0,0,0,0);
        if (report == null || report.resource == null)
            return tmp;
        switch (report.resource)
        {
            case ("rock"):
                tmp.rock = report.gathered;
                break;
            case ("wood"):
                tmp.wood = report.gathered;
                break;
            case ("food"):
                tmp.food = report.gathered;
                break;
            case ("oil"):
                tmp.oil = report.gathered;
                break;
            case ("copper"):
                tmp.copper = report.gathered;
                break;
            case ("woodPlank"):
                tmp.woodPlank = report.gathered;
                break;
        }
        return tmp;
    }

    public static boolean fullResource(Resources resources)
    {
        int total = 0;
        if (resources != null)
        {
            total += resources.rock;
            total += resources.wood;
            total += resources.food;
            total += resources.oil;
            total += resources.copper;
            total += resources.woodPlank;

            if (total >= 20)
                return true;
        }
        return false;
    }


}

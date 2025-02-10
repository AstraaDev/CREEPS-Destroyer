package action;

import asynctask.MyTask;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.geometry.Direction;
import com.epita.creeps.given.vo.parameter.FireParameter;
import com.epita.creeps.given.vo.parameter.MessageParameter;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
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

    public CommandResponse noop(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "noop", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.noop.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse move(String id, Direction direction) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "move:" + direction.toString().toLowerCase(), null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.move.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse observe(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "observe", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.observe.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse gather(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "gather", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.gather.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse unload(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "unload", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.unload.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse farm(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "farm", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.farm.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse build(String id, String building) {
        int buildCast = switch (building) {
            case "town-hall" -> initResponse.costs.buildTownHall.cast;
            case "household" -> initResponse.costs.buildHousehold.cast;
            case "sawmill" -> initResponse.costs.buildSawmill.cast;
            case "smeltery" -> initResponse.costs.buildSmeltery.cast;
            case "road" -> initResponse.costs.buildRoad.cast;
            default -> 1;
        };

        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "build:" + building, null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (buildCast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse spawn(String id, String unit) {
        int spawnCast = 1;
        if (unit.equals("turret"))
            spawnCast = initResponse.costs.spawnTurret.cast;
        else if (unit.equals("bomber-bot"))
            spawnCast = initResponse.costs.spawnBomberBot.cast;

        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "spawn:" + unit, null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (spawnCast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse dismantle(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "dismantle", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.dismantle.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse upgrade(String id, String unit) {
        int upgradeCast = switch (unit) {
            case "turret" -> initResponse.costs.upgradeTurret.cast;
            case "bomber-bot" -> initResponse.costs.upgradeBomberBot.cast;
            case "citizen" -> initResponse.costs.upgradeCitizen.cast;
            default -> 1;
        };

        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "upgrade", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (upgradeCast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse refine(String id, String resource) {
        int refineCast = 1;
        if (resource.equals("turret"))
            refineCast = initResponse.costs.spawnTurret.cast;
        else if (resource.equals("bomber-bot"))
            refineCast = initResponse.costs.spawnBomberBot.cast;

        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "refine:" + resource, null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (refineCast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse send(String id, MessageParameter parameter) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "message:send ", Json.serialize(parameter)), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.sendMessage.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse fetch(String id) {
        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "message:fetch", null), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (initResponse.costs.fetchMessage.cast * timeout), TimeUnit.MILLISECONDS).execute();
    }

    public CommandResponse fire(String id, String unit, FireParameter parameter) {
        int fireCast = 1;
        if (unit.equals("turret"))
            fireCast = initResponse.costs.fireTurret.cast;
        else if (unit.equals("bomber-bot"))
            fireCast = initResponse.costs.fireBomberBot.cast;

        return MyTask.of(() -> {
            try {
                return Json.parse(call.postCommand(id, "fire:" + unit, Json.serialize(parameter)), CommandResponse.class);
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }
        }).andThenWait((long) (fireCast * timeout), TimeUnit.MILLISECONDS).execute();
    }
}

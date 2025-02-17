package mobs;

import action.*;
import com.epita.creeps.given.exception.NoReportException;
import com.epita.creeps.given.extra.Cartographer;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Tile;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.GatherReport;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ObserveReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Cooker extends Citizen {
    public Cooker(ServerCall call, InitResponse initResponse, String citizenId, Action action, Position position, double timeout, boolean debugMode) {
        super(call, initResponse, citizenId, action, position, timeout, debugMode);
    }

    @Override
    public void run() {
        CommandResponse observeResponse = action.observe(citizenId);
        Point citizenPosition = null;

        try {
            ObserveReport observeReport = Json.parseReport(call.getReport(observeResponse.reportId));
            citizenPosition = observeReport.unitPosition;
        } catch (NoReportException | UnirestException e) {
            // TODO : manage exception
            System.out.println("Cooker-1 Error: " + e.getMessage());
            // throw new RuntimeException(e);
        }

        int cycleLength = 7;
        int directionChoice = 0;
        boolean isFull = false;

        while(true) {
            try {
                Point coordHdv = position.findNearestHdv(citizenPosition);
                boolean isCycleSafe = isCycleSafe(citizenPosition, cycleLength);
                // TODO : or inventory is full
                if (!isCycleSafe || isFull) {
                    if (debugMode)
                        System.out.println("BackToHome: from " + citizenPosition + " to " + coordHdv);
                    position.goTo(citizenId, citizenPosition, coordHdv);
                    citizenPosition = new Point(coordHdv.x, coordHdv.y);
                    action.unload(citizenId);
                    directionChoice++;
                    while (position.nextGc() != initResponse.setup.gcTickRate && !isFull) {
                        if (debugMode)
                            System.out.println(position.nextGc() + " != " + initResponse.setup.gcTickRate);
                        action.observe(citizenId);
                    }
                    if (debugMode)
                        System.out.println(position.nextGc() + " == " + initResponse.setup.gcTickRate);
                    isFull = false;
                    continue;
                }

                DirectionInfo d = getDirection(directionChoice);
                CommandResponse moveResponse = action.move(citizenId, d.direction);
                citizenPosition = new Point(citizenPosition.x + d.dx, citizenPosition.y + d.dy);
                MoveReport moveReport = Json.parseReport(call.getReport(moveResponse.reportId));

                Cartographer.INSTANCE.register(moveReport);
                Point nextFood = nextSingleTarget(moveReport.unitPosition, Tile.Food);
                if (nextFood != null) {
                    position.goTo(citizenId, citizenPosition, nextFood);
                    citizenPosition = new Point(nextFood.x, nextFood.y);
                    action.gather(citizenId);
                    CommandResponse gather =  action.gather(citizenId);
                    GatherReport gatherReport = Json.parseReport(call.getReport(gather.reportId));
                    if (gatherReport.gathered == 0)
                        isFull = true;
                }
            } catch (UnirestException | NoReportException e) {
                // TODO : manage exceptions
                System.out.println("Cooker-2 Error: " + e.getMessage());
                // throw new RuntimeException(e);
            }
        }
    }
}

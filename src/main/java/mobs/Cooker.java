package mobs;

import action.*;
import com.epita.creeps.given.exception.NoReportException;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Tile;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.report.MoveReport;
import com.epita.creeps.given.vo.report.ObserveReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Cooker extends Citizen {
    public Cooker(ServerCall call, InitResponse initResponse, String citizenId, Action action, Position position, double timeout) {
        super(call, initResponse, citizenId, action, position, timeout);
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
            throw new RuntimeException(e);
        }

        int cycleLength = 5;
        int directionChoice = 0;

        while(true) {
            try {
                Point coordHdv = isCycleSafe(citizenPosition, cycleLength);
                // TODO : or inventory is full
                if (coordHdv != null) {
                    position.goTo(citizenId, citizenPosition, coordHdv);
                    citizenPosition = new Point(coordHdv.x, coordHdv.y);
                    action.unload(citizenId);
                    directionChoice++;
                    continue;
                }

                DirectionInfo d = getDirection(directionChoice);
                CommandResponse moveResponse = action.move(citizenId, d.direction);
                citizenPosition = new Point(citizenPosition.x + d.dx, citizenPosition.y + d.dy);
                MoveReport moveReport = Json.parseReport(call.getReport(moveResponse.reportId));

                Point nextFood = nextTarget(moveReport.unitPosition, Tile.Food);
                if (nextFood != null) {
                    position.goTo(citizenId, citizenPosition, nextFood);
                    citizenPosition = new Point(nextFood.x, nextFood.y);
                    action.gather(citizenId);
                }
            } catch (UnirestException | NoReportException e) {
                // TODO : manage exceptions
                throw new RuntimeException(e);
            }
        }
    }
}

package mobs;

import action.Action;
import action.Position;
import action.ServerCall;
import com.epita.creeps.given.exception.NoReportException;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.parameter.FireParameter;
import com.epita.creeps.given.vo.report.ObserveReport;
import com.epita.creeps.given.vo.report.Report;
import com.epita.creeps.given.vo.report.SpawnReport;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Fetiche extends Citizen {
    public Fetiche(ServerCall call, InitResponse initResponse, String citizenId, Action action, Position position, double timeout, boolean debugMode) {
        super(call, initResponse, citizenId, action, position, timeout, debugMode);
    }

    @Override
    public void run() {
        try {
            // Place a turret
            CommandResponse turretResponse = action.spawn(citizenId, "turret");
            SpawnReport turretReport = Json.parseReport(call.getReport(turretResponse.reportId));

            while (true) {
                CommandResponse observeResponse = action.observe(citizenId);
                ObserveReport enemyReport = Json.parseReport(call.getReport(observeResponse.reportId));
                for (Report.Unit unit : enemyReport.units) {
                    if (unit.player.equals("Raiders")) {
                        FireParameter fireDestination = new FireParameter(unit.position);
                        action.fire(turretReport.spawnedUnitId, "turret", fireDestination);
                    }
                }
            }
        } catch (UnirestException | NoReportException e) {
            System.out.println("Fetiche-1 Error: " + e.getMessage());
            // throw new RuntimeException(e);
        }
    }
}

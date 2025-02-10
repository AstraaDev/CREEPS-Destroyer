package action;

import com.epita.creeps.given.exception.NoReportException;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.report.Report;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ServerCall {

    private String url;
    private String login;

    public ServerCall(String url, String login) {
        this.url = url;
        this.login = login;
    }

    public String getStatus() throws UnirestException {
        return Unirest.get(this.url+"/status").asJson().getBody().toString();
    }

    public String getStatistics() throws UnirestException {
        return Unirest.get(this.url+"/statistics").asJson().getBody().toString();
    }

    public Report getReport(String id) {
        try {
            return Json.parseReport(Unirest.get(this.url+"/report/"+id).asJson().getBody().toString());
        }
        catch (UnirestException | NoReportException e) {
            return null;
        }

    }

    public String postInit() throws UnirestException {
        return Unirest.post(this.url+"/init/"+this.login).body("{}").asJson().getBody().toString();
    }

    public String postCommand(String unitId, String opCode, String body) throws UnirestException {
        return Unirest.post(this.url+"/command/"+this.login+"/"+unitId+"/"+opCode).body(body).asJson().getBody().toString();
    }
}
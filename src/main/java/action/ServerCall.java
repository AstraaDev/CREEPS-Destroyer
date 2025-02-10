package action;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ServerCall {

    public String url;
    public int port;
    public String login;

    public ServerCall(String url, int port, String login) {
        this.url = url;
        this.port = port;
        this.login = login;
    }

    public String getStatus() throws UnirestException {
        return Unirest.get("http://" + this.url + ':' + this.port + "/status").asJson().getBody().toString();
    }

    public String getStatistics() throws UnirestException {
        return Unirest.get("http://" + this.url + ':' + this.port + "/statistics").asJson().getBody().toString();
    }

    public String postInit() throws UnirestException {
        return Unirest.post("http://" + this.url + ':' + this.port + "/init/" + this.login).body("{}").asJson().getBody().toString();
    }

    public String postCommand(String unitId, String opcode, String body) throws UnirestException {
        if (body == null)
            body = "{}";
        return Unirest.post("http://" + this.url + ':' + this.port + "/command/" + this.login + "/" + unitId + "/" + opcode).body(body).asJson().getBody().toString();
    }

    public String getReport(String reportId) throws UnirestException {
        return Unirest.get("http://" + this.url + ':' + this.port + "/report/" + reportId ).asJson().getBody().toString();
    }
}
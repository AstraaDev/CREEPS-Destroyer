import action.Action;
import action.ServerCall;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.response.InitResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.*;

public class Program {

    public static void main(String[] args) throws UnirestException {
        boolean devMode = true;

        String url = args[0];
        int port = Integer.parseInt(args[1]);
        String login = (devMode) ? args[2] + UUID.randomUUID() : args[2];

        // Connecting to the server
        System.out.println("Connecting to " + url + ":" + port + " as login: " + login);
        System.out.println("-------------------------------------------------------");

        // ServerCall instance declaration
        ServerCall call = new ServerCall(url, port, login);

        // Parsing
        InitResponse initResponse = Json.parse(call.postInit(), InitResponse.class);

        // Commands instance declaration
        double timeout = 1000 / initResponse.setup.ticksPerSeconds;
        Action action = new Action(initResponse, call, timeout);
    }
}

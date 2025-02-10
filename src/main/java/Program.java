import action.ServerCall;
import com.epita.creeps.given.json.Json;
import com.epita.creeps.given.vo.Resources;
import com.epita.creeps.given.vo.geometry.Point;
import com.epita.creeps.given.vo.response.InitResponse;
import mobs.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.*;

public class Program {

    public static String login;
    public static Resources resources = new Resources();
    public static Resources resourcesFutur = new Resources();
    public static Point hdv;
    public static int gcTickRate;
    public static int serverTick;


    public static void main(String[] args) throws UnirestException, InterruptedException {
        boolean dev = false;
        ServerCall call;
        if (dev)
        {
            login= usingRandomUUID();
            call = new ServerCall("http://" + args[0] + ":" + args[1], login);
            System.out.println("New login: " + login);
        }
        else
        {
            call = new ServerCall("http://" + args[0] + ":" + args[1], args[2]);
        }

        InitResponse initResponse = Json.parse(call.postInit(), InitResponse.class);
        double timeout = 1000/initResponse.setup.ticksPerSeconds;
        hdv = initResponse.townHallCoordinates;

        gcTickRate = initResponse.setup.gcTickRate;
        serverTick = initResponse.tick;

        //ResourceMan resourceMan = new ResourceMan(initResponse,call,timeout,initResponse.citizen1Id);
        //resourceMan2.start();
    }

    static String usingRandomUUID() {

        UUID randomUUID = UUID.randomUUID();

        return randomUUID.toString().replaceAll("_", "");

    }
}

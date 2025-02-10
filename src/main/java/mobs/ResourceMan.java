package mobs;

import action.Action;
import action.ServerCall;
import asynctask.MyTask;
import com.epita.creeps.given.vo.response.CommandResponse;
import com.epita.creeps.given.vo.response.InitResponse;
import com.epita.creeps.given.vo.response.StatisticsResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class ResourceMan extends Thread {
    InitResponse initResponse;
    ServerCall call;
    Double timeout;
    String id;

    public ResourceMan(InitResponse initResponse, ServerCall call, Double timeout, String id) {
        this.initResponse = initResponse;
        this.call = call;
        this.timeout = timeout;
        this.id = id;
    }

    public void run() {
        while (true)
        {
            Action action = new Action(initResponse, call, timeout);

            StatisticsResponse stats = null;
            try {
                stats = action.getStatistics();
            } catch (UnirestException e) {
                throw new RuntimeException(e);
            }

            List<StatisticsResponse.PlayerStatsResponse> statplayer = stats.players;

            MyTask.of(() -> {return  serverTick = serverTick + 1;}).andThenWait((long) (timeout * 1), TimeUnit.MILLISECONDS).execute();

            for (StatisticsResponse.PlayerStatsResponse stat : statplayer) {
                if (Objects.equals(stat.name, login))
                {
                    resources = stat.resources;
                }
                else
                {
                    CommandResponse res =  MyTask.of(() -> {return action.message(id, "Hector");}).andThenWait((long) (timeout * initResponse.costs.sendMessage.cast), TimeUnit.MILLISECONDS).execute();
                    CommandResponse res2 =  MyTask.of(() -> {return action.messageFetch(id);}).andThenWait((long) (timeout * initResponse.costs.fetchMessage.cast), TimeUnit.MILLISECONDS).execute();
                    System.out.println(stat.name + "   " + res);
                }

            }
        }
    }
}

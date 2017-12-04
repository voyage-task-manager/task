package voyage.task.zerodois.app;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import database.InputSchema;
import models.Network;
import network.Input;
import network.Predict;
import zerodois.neuralnetwork.NeuralNetwork;

public class NetworkLearn extends IntentService implements Predict.Ready {
    private double[][] data;
    private double[][] out;

    public NetworkLearn() {
        super("");
    }

    public void create (List<Input> input) {
        for (int d=0; d<6; d++)
            for (int h=0; h<23; h++)
                input.add(new Input(d, h, 1));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        start();
    }

    void start () {
        new Predict(this, this).execute();
    }

    @Override
    public void onReady(NeuralNetwork network) {
        List<Input> inputs = InputSchema.find(this, null);
        create(inputs);
        data = new double[inputs.size()][];
        out = new double[inputs.size()][1];
        for (int i=0; i<inputs.size(); i++) {
            data[i] = inputs.get(i).toArray();
            out[i] = new double[]{ inputs.get(i).getValue() };
        }
        //network.learn(data, out);
        //double[][][] exp = network.export();
        //Network.save(this, exp);
    }
}

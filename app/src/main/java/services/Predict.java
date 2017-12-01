package services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import database.NetworkSchema;
import models.Network;
import zerodois.neuralnetwork.NeuralNetwork;

/**
 * Created by zerodois on 29/11/17.
 */

public class Predict extends AsyncTask<Void, Void, NeuralNetwork>{
    private Activity activity;
    private double[][] dataset;
    private double[][][] weights;
    private double[][] out;
    private boolean train;
    private Ready callback;

    public interface Ready {
        public void onReady (NeuralNetwork network);
    }

    private static final int LAYERS = 1;

    private double[] normalize (int day, int hour) {
        double d = ((double) day)/7;
        double h = ((double) hour)/24;
        return new double[]{d, h};
    }

    public Predict (Activity activity, Ready callback) {
        this.activity = activity;
        this.train = false;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute () {
        List<Network> networks = NetworkSchema.find(activity, null);
        if (networks.size() > 0){
            weights = format(networks);
            return;
        }
        train = true;
        int total = 168; // 24*7
        dataset = new double[total][2];
        out = new double[total][1];
        int index = 0;
        for (int d=0; d<6; d++)
            for (int h=0; h<23; h++)
                dataset[index] = normalize(d, h);
        for (int i=0; i<total; i++)
            out[i] = new double[]{ 1 };
    }

    private double[][][] format(List<Network> networks) {
        double[][][] d = new double[LAYERS + 1][][];
        int layer = -1;
        for (Network n : networks) {
            if (layer != n.getLayer()) {
                layer = n.getLayer();
                String[] parts = n.getDimension().split("x");
                d[layer] = new double[Integer.parseInt(parts[0])][Integer.parseInt(parts[1])];
            }
            d[layer][n.getRow()][n.getColumn()] = n.getValue();
        }
        return d;
    }

    @Override
    protected NeuralNetwork doInBackground(Void... params) {
        NeuralNetwork network = new NeuralNetwork(LAYERS);
        if (!train) {
            network.load(weights);
        } else {
            network.learn(dataset, out);
            double[][][] exp = network.export();
            Network.save(activity, exp);
        }
        return network;
    }

    @Override
    protected void onPostExecute(NeuralNetwork network){
        callback.onReady(network);
    }
}

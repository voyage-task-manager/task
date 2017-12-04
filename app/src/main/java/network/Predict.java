package network;

import android.content.Context;
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
    private Context context;
    private double[][] dataset;
    private double[][][] weights;
    private double[][] out;
    private boolean train;
    private Ready callback;

    public interface Ready {
        public void onReady (NeuralNetwork network);
    }

    private static final int LAYERS = 1;

    public Predict (Context context, Ready callback) {
        this.context = context;
        this.train = false;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute () {
        List<Network> networks = NetworkSchema.find(context, null);
        if (networks.size() > 0){
            weights = format(networks);
            return;
        }
        train = true;
        int total = 24*7; // 24*7
        int[] frees = new int[]{};
        int weeks = 5;

        dataset = new double[total + (16*weeks*frees.length)][];
        out = new double[total + (16*weeks*frees.length)][1];

        int index = 0;
        for (int d=0; d<7; d++)
            for (int h=0; h<24; h++)
                dataset[index++] = new Input(d, h, 1).toArray();
        index = 0;
        for (int d=0; d<7; d++)
            for (int h=0; h<24; h++)
                out[index++] = new double[]{ 1 };

        for (int w=0; w<weeks; w++) {
            for (int i=0; i<frees.length; i++) {
                int f = frees[i];
                for (int h=8; h<24; h++) {
                    dataset[index] = new Input(f, h, 0).toArray();
                    out[index++] = new double[]{ 0 };
                }
            }
        }
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
        NeuralNetwork network = new NeuralNetwork(LAYERS, 8);
        if (!train) {
            network.load(weights);
        } else {
            network.learn(dataset, out);
            double[][][] exp = network.export();
            Network.save(context, exp);
        }
        return network;
    }

    @Override
    protected void onPostExecute(NeuralNetwork network){
        callback.onReady(network);
    }
}

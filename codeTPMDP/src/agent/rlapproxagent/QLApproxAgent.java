package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{
    FeatureFunction feature;
    double[] weights;
	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		this.feature = _featurefunction;
		//*** VOTRE CODE
        weights = new double[_featurefunction.getFeatureNb()];
        Arrays.fill(weights,0.0);
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
        double res = 0;
        int nb_features = this.feature.getFeatureNb();
        double[] value = this.feature.getFeatures(e,a);
        for (int i = 0; i<nb_features;++i)
            res += value[i]*weights[i];

        return res;
	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       	//inutile de verifier si e etat absorbant car dans runEpisode et threadepisode
		//arrete episode lq etat courant absorbant
		//*** VOTRE CODE
		double maxQvalue = getValeur(esuivant);
		double Qvalue = getQValeur(e,a);
		double[] feature = this.feature.getFeatures(e,a);
        for(int i = 0; i < weights.length; i++)
            weights[i] += alpha * (reward + gamma * maxQvalue - Qvalue) * feature[i];
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
		//*** VOTRE CODE
        Arrays.fill(this.weights,0.0);
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}

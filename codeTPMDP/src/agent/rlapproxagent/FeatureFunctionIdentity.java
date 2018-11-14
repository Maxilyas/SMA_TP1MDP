package agent.rlapproxagent;

import java.util.*;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	int size;
	int rank;
	HashMap<Etat,HashMap<Action,double[]>> feature;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		size = _nbEtat * _nbAction;
		rank = 0;
		feature = new HashMap<>();
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return size;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		if(feature.containsKey(e))
			if(feature.get(e).containsKey(a))
				return feature.get(e).get(a);

		double[] vec = new double[size];
		vec[rank] = 1;
		rank ++;

		if(feature.containsKey(e))
			feature.get(e).put(a, vec);
		else
		{
			HashMap<Action,double[]> temp = new HashMap<>();
			temp.put(a,vec);
			feature.put(e, temp);
		}
		return vec;
	}
	

}

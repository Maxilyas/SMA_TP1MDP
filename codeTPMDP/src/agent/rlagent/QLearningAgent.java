package agent.rlagent;

import java.util.*;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	
	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param Environnement
	 * @param nbS attention ici il faut tous les etats (meme obstacles) car Q avec tableau ...
	 * @param nbA
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();
		
		
	
	}


	
	
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)

	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)
		List<Action> returnactions = new ArrayList<Action>();
		List<Double> ListAll = new ArrayList<>();
		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
		}
		//*** VOTRE CODE
        for (int i =0; i< this.getActionsLegales(e).size();++i)
            ListAll.add(getQValeur(e,this.getActionsLegales(e).get(i)));

        double value = Collections.max(ListAll);

        for(int i = 0; i<this.getActionsLegales(e).size();++i)
            if (getQValeur(e,this.getActionsLegales(e).get(i)) == value)
                returnactions.add(this.getActionsLegales(e).get(i));

		return returnactions;
	}
	
	@Override
	public double getValeur(Etat e) {
		//*** VOTRE CODE

		List<Action> returnactions = new ArrayList<>(this.env.getActionsPossibles(e));
		List<Double> listElem = new ArrayList<>();

		for (int i = 0; i< returnactions.size();++i)
			listElem.add(getQValeur(e,returnactions.get(i)));

		if (listElem.size()>0)
		{
			double val = Collections.max(listElem);
			return val;
		}
		else
			return 0;

	}

	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
        double val = 0.0;
        if (qvaleurs.containsKey(e) && qvaleurs.get(e).containsKey(a))
            val = qvaleurs.get(e).get(a);

		return val;
	}
	

	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE
        HashMap<Action,Double> tmp = new HashMap<>();

        if (this.qvaleurs.get(e) == null)
            tmp.put(a,d);
        else {
            tmp = this.qvaleurs.get(e);
            tmp.put(a, d);
        }
        this.qvaleurs.put(e,tmp);

		if (d > vmax)
			vmax = d;
		if (d < vmin)
			vmin = d;
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		this.notifyObs();
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);
		//*** VOTRE CODE
        double value = (1-this.alpha)*this.getQValeur(e,a) + this.alpha*(reward + this.gamma * getValeur(esuivant));
        this.setQValeur(e,a,value);
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		this.qvaleurs.clear();
		this.episodeNb =0;
		this.notifyObs();
	}









	


}

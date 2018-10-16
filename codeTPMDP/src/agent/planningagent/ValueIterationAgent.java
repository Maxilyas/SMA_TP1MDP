package agent.planningagent;

import java.util.*;

import util.HashMapUtil;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V = new HashMap<>();
	
	/**
	 * 
	 * @param gamma
	 * @param nbIterations
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		//*** VOTRE CODE
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			this.V.put(etat, 0.0);
		}
	}
	
	
	
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon
		this.delta=0.0;
		//*** VOTRE CODE
		double value = 0.0;
		double goodValue = 0.0;
		ArrayList<Double> valueAction = new ArrayList<>();
		ArrayList<Double> valueAllEtat = new ArrayList<>();
        ArrayList<Double> diffHashDelta = new ArrayList<>();
        HashMap<Etat,Double> V_old = new HashMap<>(this.V);
		for (Etat etat:this.mdp.getEtatsAccessibles()){

			if (this.getMdp().estAbsorbant(etat)) {
			}
			else
			{

				for(Action action:this.mdp.getActionsPossibles(etat)) {
					try {
						for (Etat etatArrive : this.mdp.getEtatTransitionProba(etat, action).keySet()) {

							value = value + this.mdp.getEtatTransitionProba(etat, action).get(etatArrive) * (this.mdp.getRecompense(etat, action, etatArrive) + this.getGamma() * V_old.get(etatArrive));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					valueAction.add(value);
                    value = 0.0;
				}

                //System.out.println("Value action " + valueAction );
                goodValue = Collections.max(valueAction,null);
                valueAllEtat.add(goodValue);
                diffHashDelta.add(goodValue-V.get(etat));
                V.put(etat,goodValue);
                valueAction.clear();
			}
		}

		delta = Collections.max(diffHashDelta,null);

		this.vmax = Collections.max(valueAllEtat,null);
		this.vmin = Collections.min(valueAllEtat,null);

        // mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur de max pour tout s de V
		//vmin est la valeur de min pour tout s de V
		// ...
		
		//******************* laisser notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE

        List<Action> actions = getPolitique(e);

        int size = actions.size();
		if (size == 0){
            return null;

		}
        int random = rand.nextInt(size);
        return actions.get(random);


	}
	@Override
	public double getValeur(Etat _e) {
		//*** VOTRE CODE
		return V.get(_e);
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
        double value = 0.0;
        double maxValeurAction = 0.0;
        List<Action> returnactions = new ArrayList<Action>();
        HashMap<Etat,Double> V_old = new HashMap<>(this.V);

        for(Action action:this.mdp.getActionsPossibles(_e))
        {
            System.out.println("ACTION : " + action);
            try {
                for(Etat etatArrive:this.mdp.getEtatTransitionProba(_e,action).keySet())
                {
                    value = value + this.mdp.getEtatTransitionProba(_e,action).get(etatArrive)* (this.mdp.getRecompense(_e,action,etatArrive) + this.getGamma()*V_old.get(etatArrive));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (maxValeurAction < value){
                returnactions.clear();
                maxValeurAction = value;
                returnactions.add(action);
            }else if(maxValeurAction == value)
            {
                returnactions.add(action);
            }
            value = 0.0;
        }
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)


        //System.out.println(returnactions);
		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();

		
		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}

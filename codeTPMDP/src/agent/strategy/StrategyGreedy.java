package agent.strategy;

import java.util.*;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Etat;
/**
 * Strategie qui renvoit un choix aleatoire avec proba epsilon, un choix glouton (suit la politique de l'agent) sinon
 * @author lmatignon
 *
 */
public class StrategyGreedy extends StrategyExploration{
	/**
	 * parametre pour probabilite d'exploration
	 */
	protected double epsilon;
	private Random rand=new Random();
	
	
	
	public StrategyGreedy(RLAgent agent,double epsilon) {
		super(agent);
		this.epsilon = epsilon;
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		double d =rand.nextDouble();
		double sum = 0;
		int block = 0;
		List<Action> actions = new ArrayList<>();
		List<Double> ListMaxElem = new ArrayList<>();
		HashMap<Integer,Action> tmp =new HashMap<>();
		if (this.agent.getActionsLegales(_e).isEmpty()){
			return null;
		}
		for(int i = 0;i<this.agent.getActionsLegales(_e).size();++i)
		{
				ListMaxElem.add(this.agent.getQValeur(_e,this.agent.getActionsLegales(_e).get(i)));
		}
		double value = Collections.max(ListMaxElem);
		int addval = 2;
		for(int i =0; i< this.agent.getActionsLegales(_e).size();++i)
		{
			if(value == this.agent.getQValeur(_e,this.agent.getActionsLegales(_e).get(i)) && value != 0 && block ==0)
			{
			    block = 1;
				tmp.put(1,this.agent.getActionsLegales(_e).get(i));

			}else
            {
                tmp.put(addval,this.agent.getActionsLegales(_e).get(i));
                addval = addval +1;

            }

		}
		sum = (1 -epsilon);
		if(sum > d && tmp.containsKey(1))
		{
			return tmp.get(1) ;
		}else
		{
			if (addval > 2){
				int randInt = rand.nextInt(addval-2)+2;
				return tmp.get(randInt);
			}else {
				return tmp.get(1);
			}

		}

		//VOTRE CODE ICI

	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
		System.out.println("epsilon:"+epsilon);
	}

/*	@Override
	public void setAction(Action _a) {
		// TODO Auto-generated method stub
		
	}*/

}

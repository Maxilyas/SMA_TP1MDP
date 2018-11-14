package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import agent.rlagent.QLearningAgent;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

    int nbGhosts;
    int [] tabGhostsX;
    int [] tabGhostsY;
    int xPacman;
    int yPacman;
    int closestDot;
    StateGamePacman state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtatPacmanMDPClassic that = (EtatPacmanMDPClassic) o;
        return nbGhosts == that.nbGhosts &&
                xPacman == that.xPacman &&
                yPacman == that.yPacman &&
                closestDot == that.closestDot &&
                Arrays.equals(tabGhostsX, that.tabGhostsX) &&
                Arrays.equals(tabGhostsY, that.tabGhostsY);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(nbGhosts, xPacman, yPacman, closestDot);
        result = 31 * result + Arrays.hashCode(tabGhostsX);
        result = 31 * result + Arrays.hashCode(tabGhostsY);
        return result;
    }

    public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
	    nbGhosts = _stategamepacman.getNumberOfGhosts();
        tabGhostsX = new int[nbGhosts];
        tabGhostsY = new int[nbGhosts];
	    for (int i = 0;i< nbGhosts; ++i)
        {
            tabGhostsX[i] = _stategamepacman.getGhostState(i).getX();
            tabGhostsY[i] = _stategamepacman.getGhostState(i).getY();
        }
        xPacman = _stategamepacman.getPacmanState(0).getX();
	    yPacman = _stategamepacman.getPacmanState(0).getY();
	    closestDot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
	    state = _stategamepacman;


	}

    public int getDimensions()
    {
        int dim = 1;
        int sizeX = state.getMaze().getSizeX();
        int sizeY = state.getMaze().getSizeY();

        dim *= (sizeX * sizeX) - state.getMaze().getNbwall();
        dim *= (sizeY * sizeY) - state.getMaze().getNbwall();
        dim *= sizeX + sizeY;
        return dim;
    }
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}


}

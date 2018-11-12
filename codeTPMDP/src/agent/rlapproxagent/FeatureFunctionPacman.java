package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;
	
	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {

	}

	@Override
	public int getFeatureNb() {
		return 4;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[4];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}
	
		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));
		StateAgentPacman ghoststate_next = stategamepacman.moveGhostSimu(0,new ActionPacman(a.ordinal()));
		//*** VOTRE CODE
		vfeatures[0] = 1;

		int PacmanXnextState = pacmanstate_next.getX();
		int PacmanYnextState = pacmanstate_next.getY();
		int GhostXnextState = ghoststate_next.getX();
		int GhostYnextState = ghoststate_next.getX();
		int NbGhostClose = 0;

		for (int i =0 ; i< stategamepacman.getNumberOfGhosts();i++)
			if (GhostXnextState == PacmanXnextState && GhostYnextState== PacmanYnextState)
				NbGhostClose++;
		vfeatures[1] = NbGhostClose;

		if (stategamepacman.getClosestDot(pacmanstate_next) == 0)
			vfeatures[2] = 1;
		else
			vfeatures[2] = 0;

		int dist = stategamepacman.getClosestDot(pacmanstate_next);
		int sizeMazeX = stategamepacman.getMaze().getSizeX();
		int sizeMazeY = stategamepacman.getMaze().getSizeY();
		vfeatures[3] = dist/(sizeMazeX*sizeMazeY);
		
		
		
		return vfeatures;
	}

	public void reset() {
		vfeatures = new double[4];
		
	}

}

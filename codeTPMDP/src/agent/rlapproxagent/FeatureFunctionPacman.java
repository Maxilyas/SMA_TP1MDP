package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;

import java.util.*;

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
		//*** VOTRE CODE
		vfeatures[0] = 1;

		int PacmanXnextState = pacmanstate_next.getX();
		int PacmanYnextState = pacmanstate_next.getY();

		int NbGhostClose = 0;
        int[] GhostX = new int[stategamepacman.getNumberOfGhosts()];
        int[] GhostY = new int[stategamepacman.getNumberOfGhosts()];
		for (int i =0 ; i< stategamepacman.getNumberOfGhosts();i++) {
            GhostX[i] = stategamepacman.getGhostState(i).getX();
            GhostY[i] = stategamepacman.getGhostState(i).getY();
            if ((GhostX[i] == PacmanXnextState && Math.abs(PacmanYnextState - GhostY[i]) <= 1) || (GhostY[i] == PacmanYnextState && Math.abs(PacmanXnextState - GhostX[i]) <= 1))
                NbGhostClose++;
        }
		vfeatures[1] = NbGhostClose;

		if (stategamepacman.getMaze().isFood(PacmanXnextState,PacmanYnextState))
			vfeatures[2] = 1;
		else
			vfeatures[2] = 0;

		// Distance Manhattan
		//double dist = stategamepacman.getClosestDot(pacmanstate_next);
		double sizeMazeX = stategamepacman.getMaze().getSizeX();
		double sizeMazeY = stategamepacman.getMaze().getSizeY();
		double sizemap = sizeMazeX*sizeMazeY;

		// Parcours en largeur (marche moins bien que manhattan)
		double dist = BFS(stategamepacman,pacmanstate_next);
		vfeatures[3] = dist/sizemap;




		
		
		return vfeatures;
	}

    // BFS Search
	public double BFS(StateGamePacman state,StateAgentPacman next_state)
	{
		int R = state.getMaze().getSizeX();
		int C = state.getMaze().getSizeY();
		int sr = next_state.getX();
		int sc = next_state.getY();
		Queue<Integer> rq = new LinkedList<>();
		Queue<Integer> cq = new LinkedList<>();
		// NORD,SUD,EST,OUEST
		int[] dr = {0,0,1,-1};
		int[] dc = {1,-1,0,0};
		double move_count = 0;
		int nodes_left_in_layer = 1;
		int nodes_in_next_layer = 0;
		boolean reached_end = false;
		boolean [][] visited = new boolean [R][C];

		rq.offer(sr);
		cq.offer(sc);
		visited[sr][sc] = true;
		while (rq.size()>0)
		{
			int r = rq.poll();
			int c = cq.poll();
			if (state.getMaze().isFood(r,c) ||state.getMaze().isCapsule(r,c))
			{
				reached_end = true;
				break;
			}
			for(int i = 0; i<4;i++)
			{
				int rr = r + dr[i];
				int cc = c + dc[i];
				if (rr<0 || cc < 0)
					continue;
				if (rr>=R || cc>=C)
					continue;
				if (visited[rr][cc])
					continue;
				if (state.getMaze().isWall(rr,cc))
					continue;
				rq.offer(rr);
				cq.offer(cc);
				visited[rr][cc] = true;
				nodes_in_next_layer++;
			}
			nodes_left_in_layer--;
			if (nodes_left_in_layer == 0)
			{
				nodes_left_in_layer = nodes_in_next_layer;
				nodes_in_next_layer = 0;
				move_count++;
			}
		}
		if(reached_end)
			return move_count;
		else
			return 0;
	}

	public void reset() {
		vfeatures = new double[4];
		
	}

}

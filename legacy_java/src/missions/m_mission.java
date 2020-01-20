import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;


public class m_mission {

	
	
	int scouting = 0;
	int attacking = 0;
	
	public void update(Game game, m_list listM, m_build buildM, Player self, m_mission missionM)
	{
		for (mission x : listM.Missions)
		{
			x.execute(game, listM, buildM, self, missionM);
		}
		
		
		
		
	}

	
	
}

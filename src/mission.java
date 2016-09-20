import java.util.ArrayList;
import java.util.List;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;

import java.util.Collections;
import bwapi.Position;

public class mission {

	public	String type;
	public boolean capable; 
	public List<Unit> Assigned_Units = new ArrayList<Unit>();
	public List<UnitType> Desired_Units = new ArrayList<UnitType>();
	public List<bwapi.Position> Target;
	
	
	
	

	public void execute(Game game,  m_list listM , m_build buildM, Player self, m_mission missionM )
	{
		
		if( type == "scouting" )
		{
			if (Assigned_Units.size() == 0)
			{
				Assigned_Units.add( m_scout.addScout(game, listM, buildM, self) );
			}
			else
			{
				Unit scout = Assigned_Units.get(0);
				m_scout.Scout(scout, game, listM );
			}
		}
		
		
	}
	


	public void scout( Game game, m_list listM )
	{
		
		
		
		
		
	}


	
}

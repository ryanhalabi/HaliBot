import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Player;

public class playerInfo
{
	public Player Player;
	
	//positions:buildings
	public 	Map<Position,UnitType > positionBuilding =  new HashMap<Position, UnitType>();
	
	//player race
	public bwapi.Race race = bwapi.Race.None;
	
	//units capable of creating
	public List<UnitType> capabilities = new ArrayList<UnitType>();
	
	//locations occupied by the player
	public List<bwta.BaseLocation> locations = new ArrayList<bwta.BaseLocation>();

	//unit:unit_info
	public	Map<Unit,unitInfo> unitList = new HashMap<Unit,unitInfo>(); 
	
	//type:numbers
	public 	Map<UnitType, Integer> unitCount =  new HashMap<UnitType, Integer>();  
	
	//type:percentage
	public 	Map<UnitType, Integer> unitComposition =  new HashMap<UnitType, Integer>();  
	

	// updates unit counts/composition per player
	public void updateComposition( )
	{  
		Integer total = 0;
		for( UnitType u  : unitCount.keySet() )
		{
			if( ( !u.isWorker() ) && (  !u.isBuilding() ) && (u != UnitType.Zerg_Egg) && (u != UnitType.Zerg_Lair) && (u != UnitType.Zerg_Overlord)  )
			{
				total = total + unitCount.get(u);	
			}
		}
		
		for( UnitType u  : unitCount.keySet() )
		{
			if(  ( !u.isWorker() ) && (  !u.isBuilding() ) && (u != UnitType.Zerg_Egg) && (u != UnitType.Zerg_Lair) && (u != UnitType.Zerg_Overlord) )
			{
				unitComposition.put( u,   100 *unitCount.get(u)/total    );
			}
		}
		
		
		
		
	}
}


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bwapi.Position;
import bwapi.Unit;
import bwta.BWTA;

import java.util.List;





	public class baseData {
		
		public Integer baseOwner = 0;  //0 is neutral, -1 enemy, 1 player
		public Integer baseTimer = 0;  // how long till last scouted

		public boolean isStartLocation = false;
		
		public 	Map<Unit, List<Unit>> refinery =  new HashMap<Unit, List<Unit>>(); //refinery:list(workers)
		public 	Map<Unit, Unit> gasWorkers =  new HashMap<Unit, Unit>(); //worker:refinery
		
		public List<Unit> miners = new ArrayList< Unit >(); // miners
		
		public 	List<Unit> minerals =  new ArrayList<Unit>();
		
		
		public 	Map<String, Position > base_build_area =  new HashMap<String, Position>();
		
		public 	Map<Position, Boolean> baseExplorePoints =  new HashMap<Position, Boolean>();
		
		public Integer numgeysers =  0;
	
		
	}
	
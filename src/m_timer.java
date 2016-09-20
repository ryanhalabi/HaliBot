import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;


public class m_timer {

	
	

	public 	int depot_timer = 0;
	public 	int refinery_timer = 0;
	public 	int refinery_built_timer = 0;
	public 	int barracks_timer = 0;
	public 	int academy_timer = 0;
	public 	int bay_timer = 0;
	public 	int attack_timer = 0;
	public 	int i = 0;

	public 	int fiftyfive = 0;
	public 	int onesec = 0;
	public 	int tensec = 0;


	public long max = 0;
	public long lastTime = 0;
	
	public void frameTime( long duration )
	{
		if ( (duration > 55 )  &&  (duration < 1000))
		{
			fiftyfive +=1;
		}
		else if ( (duration >= 1000 )  &&  (duration < 10000))
		{
			onesec +=1;
		}
		else if ( (duration >= 10000 ) )
		{
			tensec +=1;
		}

	}





	//increment timer
	public int countdown(int x ){

		return ((int) Math.signum(  x) + 1)/2 *(x - 1);

	}

	public void countdownall(	Map<bwta.BaseLocation, baseData> baseDataL )
	{

		depot_timer = countdown(depot_timer);
		refinery_timer = countdown( refinery_timer);
		barracks_timer = countdown( barracks_timer);
		academy_timer = countdown(academy_timer);
		bay_timer = countdown(bay_timer);
		attack_timer = countdown(attack_timer);
		i +=1;
		
		for ( bwta.BaseLocation X : baseDataL.keySet() )
		{
				baseDataL.get(X).baseTimer = baseDataL.get(X).baseTimer + 1 ;
		}
		
	}



	
	
}

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;


public class unitInfo
{
	public int	unitID;
	public int	lastHealth;
	public int	lastShields;
	public Unit	unit;
	public Position lastPosition;
	public UnitType type;
	public boolean completed;



	public void updateUnit(  Unit u) 
	{
			
		unitID = u.getID();
		lastHealth = u.getHitPoints();
		lastShields = u.getShields();
		unit = u;
		lastPosition = u.getPosition();
		type = u.getType();
		completed = u.isCompleted();
	}

}


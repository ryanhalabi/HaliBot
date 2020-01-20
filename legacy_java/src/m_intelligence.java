import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;


public class m_intelligence {

	
	
	
	public List<UnitType> desiredUnits = new ArrayList<UnitType>();
	
	public List<UnitType> buildOrder = new ArrayList<UnitType>();
	
	
	public void determineComposition( m_list listM )
	{
		
		if( listM.enemy.race == bwapi.Race.Protoss )
		{
			
		}
		
		else if( listM.enemy.race == bwapi.Race.Terran )
		{
			
		}
		
		else if( listM.enemy.race == bwapi.Race.Zerg )
		{
			
		}
	}
	
	
	


	public void updateInfo(Game game , m_list listM )
	{

	
		// cycle through base locations and the information we have about them
		for( bwta.BaseLocation X : listM.baseDataMap.keySet())
		{

			//update if explore points have been seen or not
			for( Position P : listM.baseDataMap.get(X).baseExplorePoints.keySet() )
			{
				if( game.isVisible( P.toTilePosition()) &&  listM.baseDataMap.get(X).baseExplorePoints.get(P) == false )
				{
					listM.baseDataMap.get(X).baseExplorePoints.put(P, true);
				}
			}


			// update the base status 1/0/-1

			//assume neutral
			
			if( game.isVisible( X.getTilePosition())   )
			{

				baseData Y = listM.baseDataMap.get(X);
				Y.baseTimer = 0;
				listM.baseDataMap.put(X,Y);
				
			}
			if( game.isVisible( X.getTilePosition()) && listM.baseDataMap.get(X).baseOwner == 0  )
			{

				baseData Y = listM.baseDataMap.get(X);
				Y.baseOwner = 0;
				Y.baseTimer = 0;



				// check if units are around
				for ( Unit unit : game.getUnitsInRadius(X.getPosition(), 1000) )
				{
					
					
					//if player buildlings +1
					if ( unit.getPlayer() == game.self() && (bwta.BWTA.getRegion(unit.getPosition())  == X.getRegion() ) && ( unit.getType().isResourceDepot() )   &&  ( BWTA.getRegion( unit.getPosition() ) ==  BWTA.getRegion(  X.getPosition() )  ) )  // 10000 is me  (make sure to set this after the timer so we can check for 10000)
					{
						Y.baseOwner = 1;
						break;
					}
					
					// if enemy units then -1
					else if ( ( unit.getPlayer() == game.enemy()) && ( unit.getType().isBuilding() ) && (bwta.BWTA.getRegion(unit.getPosition())  == X.getRegion() ) && ( listM.baseDataMap.get(X).baseOwner != -1 ) &&  ( BWTA.getRegion( unit.getPosition() ) ==  BWTA.getRegion(  X.getPosition() )  )  )    // -1 is enemy
					{
						Y.baseOwner = -1;
						listM.enemyBaseFound = true;
						
						//if you only have one choke, and the guy in front of you is an enemy, then you must be enemy owned
						if ( X.getRegion().getChokepoints().size() == 1 )  		
						{ 
							for( bwta.BaseLocation XX : listM.baseDataMap.keySet())
							{
								bwta.BaseLocation YY = m_scout.findClosestBaseLoc(game, XX);
								baseData Z = listM.baseDataMap.get(Y);
								Z.baseOwner = -1;
								listM.baseDataMap.put(YY, Z);
							}
						}
						
						//if its enemy we're done
						break;


					}

	
				}


				listM.baseDataMap.put(X, Y);

			}
			
			
			
			//update mineral/gas info for base
			baseData Z = listM.baseDataMap.get(X);
			Z.minerals = 	X.getMinerals() ;
			
			
			
			int num = 0;
			for (Unit XX : game.getUnitsInRadius(X.getPosition(), 1000))
			{
				if( XX.getType() == UnitType.Resource_Vespene_Geyser && BWTA.getRegion(XX.getPosition())== X.getRegion())
				{
					num +=1;
				}
			}
			Z.numgeysers = num;
			
			
			
			listM.baseDataMap.put(X,Z);

			

			


		}
	}



	
	


	public void updateUnitInfo(Game game , m_list listM)
	{
//		System.out.println("enemy size: " + game.enemy().getUnits().size() );
		for( Unit u : game.enemy().getUnits())
		{
			
			if( listM.enemy.race == bwapi.Race.Unknown)
			{
				listM.enemy.race = u.getType().getRace();
			}
			
			
			if ( u.isVisible()) 
			{
				if ( !listM.enemy.unitList.keySet().contains(u)  )
				{
					unitInfo X = new unitInfo();
					X.updateUnit( u);
					listM.enemy.unitList.put(u, X);
					
					if ( !listM.enemy.unitCount.keySet().contains(u.getType())  )
					{
						listM.enemy.unitCount.put(u.getType(), 1);
					}
					else
					{
						listM.enemy.unitCount.put(u.getType() ,    listM.enemy.unitCount.get(u.getType()) +1   ) ;
					}
					
					listM.enemy.updateComposition();
				}
				else
				{
					listM.enemy.unitList.get(u).updateUnit(u); ;				
				}
			}
		}
		
	}
	
	
	
	
	
}

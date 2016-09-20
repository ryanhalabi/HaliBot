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


public class m_list {

	
	
	public boolean enemyBaseFound = false;

	public List<mission> Missions = new ArrayList< mission>();
	
	
	public 	bwta.BaseLocation start_base;
	public 	bwta.BaseLocation start_expo;
	public 	bwta.Region start_region;
	public 	List<bwta.BaseLocation> Start_Locs = new ArrayList<bwta.BaseLocation>();

	public playerInfo self = new playerInfo();
	public playerInfo enemy = new playerInfo();

	//unit lists
	public Unit Scout = null;
	public Map<Unit, bwta.BaseLocation> Workers =  new HashMap<Unit,bwta.BaseLocation>();
	public 	List<Unit> Infantry =  new ArrayList< Unit >();
	public 	List<Unit> Production=  new ArrayList< Unit >();
	
	public 	Map<UnitType, Integer> Buildings =  new HashMap<UnitType, Integer>();
	
	
	public List<typeTileBuilderTimeStatus> orderedBuild = new ArrayList< typeTileBuilderTimeStatus >();
	public 	Map< Unit, Unit> builderBuilding =  new HashMap<Unit, Unit>();  // buildee:building

	
	
	public Map<bwta.BaseLocation, baseData>  baseDataMap = new HashMap<bwta.BaseLocation, baseData> ();
	
	
	// regions
	public 	Map<bwta.Region, List<Position>> region_point_list = new HashMap<bwta.Region, List<Position>>();
	public 	Map<bwta.Region, List<bwta.Chokepoint>> region_choke_list = new HashMap<bwta.Region, List<bwta.Chokepoint>>();
	


	public void remove( Unit unit, m_build buildM, m_list listM, Player self)
	{

		if( unit.getPlayer()== self)
		{
//			
			
			for (mission x : Missions)
			{
				if ( x.Assigned_Units.contains( unit) )
				{
					x.Assigned_Units.remove(unit);
				}
			}
//			///////////
//			// SCOUT
//			///////////
//			if ( Scout == unit )
//			{
//					Scout = null;
//			}
			
			///////////
			// WORKER
			///////////
			if ( Workers.keySet().contains(unit) )
			{
				bwta.BaseLocation B = Workers.get(unit); // units base location 
				baseDataMap.get(B).miners.remove(unit); // remove from base:miners
				
				baseDataMap.get(B).refinery.get( baseDataMap.get(B).gasWorkers.get(unit)).remove(unit); //remove from gas:workers
				baseDataMap.get(B).gasWorkers.remove(unit); //remove from workers:gas
				
				
				Workers.remove(unit); // remove from workers:base
				
			
			}
	
			///////////
			// INFANTRY
			///////////
			if ( Infantry.contains(unit) )
			{
				Infantry.remove(unit);
			}
			
			///////////
			// PRODUCTION
			///////////
			if ( Production.contains(unit) )
			{
				Production.remove(unit);
			}
			
			///////////
			// BUILDINGS
			///////////
			
			if ( Buildings.keySet().contains(unit) )
			{
				Buildings.remove(unit);
			}
			
			///////////
			// REFINERY
			///////////
			
			
			if ( unit.getType() == UnitType.Terran_Refinery  )
			{
				for( Unit X : baseDataMap.get( unitBaseLocation(unit)).gasWorkers.keySet() ) // remove workers
				{
					if (baseDataMap.get( unitBaseLocation(unit)).gasWorkers.get(X) == unit)
					{
						baseDataMap.get( unitBaseLocation(unit)).gasWorkers.remove(unit); //remove from gas_worker:refinery
						baseDataMap.get( unitBaseLocation(unit)).refinery.get(unit).remove(X) ; // remove from refinery:gas_worker
						baseDataMap.get( unitBaseLocation(unit)).miners.add(X);
					}
				}
				baseDataMap.get( unitBaseLocation(unit)).refinery.remove(unit);
			}
		
			
			
		
			
			///////////
			// OREDERED BUILDINGS (NOT STARTED YET)
			///////////
			for ( typeTileBuilderTimeStatus X : orderedBuild)   //if builder dies while on way or constructing
			{
				if ( X.builder == unit )
				{
					X.builder = null;
					break;
				}
				
			}
			
			///////////
			// UNDER CONSTRUCT
			///////////
			for ( Unit X : builderBuilding.keySet() ) //check buildings, if dead, remove from under construct, add builder to workers, and set nextProduction to null
			{
				if ( X == unit )      //is the building dead?
				{
					Unit W = builderBuilding.get(X);
					
					
					
					
					builderBuilding.remove(X); //remove from under construction
					Workers.put( W , bwta.BWTA.getNearestBaseLocation( W.getPosition() ));
					
					baseData B = baseDataMap.get( bwta.BWTA.getNearestBaseLocation(W.getPosition()));
					baseDataMap.put( bwta.BWTA.getNearestBaseLocation(W.getPosition()) , B );
					if ( unit.getType() == UnitType.Terran_Supply_Depot)
					{
						buildM.nextSupply = null;
						break;
					}
					else if ( unit.getType() == UnitType.Terran_Barracks )
					{
						buildM.nextProduction = null;
						break;
					}
					else if ( unit.getType() == UnitType.Terran_Engineering_Bay || unit.getType() == UnitType.Terran_Academy )
					{
						buildM.nextTech = null;
						break;
					}
					else if ( unit.getType() == UnitType.Terran_Refinery )
					{
						buildM.nextEconomic = null;
						break;
					}
				}
				
	
				else if ( builderBuilding.get(X) == unit)   //is the builder dead?
				{
					Unit W = findClosestWorker( X.getPosition()   ) ;
					
					baseDataMap.get( Workers.get(W) ).miners.remove(W);
					builderBuilding.put(X, W );
					builderBuilding.get(X).rightClick( X  );
				}
			}
		}
		else if (unit.getPlayer().isEnemy(self))
	    {
		    enemy.unitList.remove(unit);
		    enemy.unitCount.put(   unit.getType(),   listM.enemy.unitCount.get(unit.getType() )  -1  );
		    enemy.updateComposition();
	    }
		
		
	}


	// search the buildling list to ensure its built
	public void checkOrderedBuild(Game game, Player self, m_list listM, m_economy econM, m_scout scoutM) {
		
		
		if ( listM.orderedBuild.size() != 0)
		{
//			 System.out.println( "ordered size: " + listM.orderedBuild.size() + "\n");
			for (typeTileBuilderTimeStatus X : listM.orderedBuild) 
			{

				// if builder is dead
				if (X.builder == null) 
				{
					X.builder = findClosestWorker( X.tilePosition.toPosition());
				}
	
				//if over time
				else if (X.time > 1500) 
				{
					Unit A = X.builder; //store old builder
					bwta.BaseLocation B = Workers.get(A); // store old base location
					A.stop(); 
					
					Workers.remove(A);
					X.builder = findClosestWorker( X.tilePosition.toPosition()); //find new worker
					X.time = 0; // reset timer
					Workers.put(A,B);
					
					listM.baseDataMap.get( Workers.get(A)).miners.add(A); // add old builder to miners list
				}
				
				else if (X.builder.isConstructing() &&  X.builder.getBuildUnit() != null ) //if it's being constructed
				{

					listM.builderBuilding.put( X.builder ,   X.builder.getBuildUnit() );
					listM.orderedBuild.remove(X);
					econM.removeExpense( X.unitType );
				}
				else //worker/building alive, move closer
				{
	
					// should change this to game.hasVision of all corners of the TP
					if (X.builder.getPosition().getDistance(X.tilePosition.toPosition()) > 160) 
					{
						X.builder.move(X.tilePosition.toPosition()); // if far away move to it
						X.status = "moving";
					} 
					
					else 
					{
	
						if ((X.builder.canBuild(X.unitType, X.tilePosition)) && (X.builder.hasPath(X.tilePosition.toPosition())))   //if close and can build, build
						{
							X.builder.build(X.unitType, X.tilePosition); // if close build it
							X.status = "building";
							// System.out.println( "  build!! \n");
						} 
						else if (   econM.availableGas  < 0 || econM.availableMin  <0)  //if close but no money, move to it
						{
							X.builder.move( X.tilePosition.toPosition()); // if close build it
							X.status = "insuff res";
						}
	//					else if ( ! X.builder.canBuild( X.unitType, X.tilePosition  ))  //if something else has arrived and blocked it, just resubmit the work order, new worker, new tile
	//					{
	//
	//						X.builder.stop();
	//						
	//
	//						if ( X.unitType == UnitType.Terran_Supply_Depot)
	//						{
	//							Build(game, self, nextSupply, listM, econM, scoutM, X.builder);
	//						}
	//						else if ( X.unitType == UnitType.Terran_Barracks )
	//						{
	//							Build(game, self, nextProduction, listM, econM, scoutM, X.builder);
	//						}
	//						else if ( X.unitType == UnitType.Terran_Engineering_Bay || X.unitType == UnitType.Terran_Academy )
	//						{
	//							Build(game, self, nextTech, listM, econM, scoutM, X.builder);
	//						}
	//						else if ( X.unitType == UnitType.Terran_Refinery )
	//						{
	//							Build(game, self, nextEconomic, listM, econM, scoutM, X.builder);
	//						}
	//						
	//						
	//						listM.orderedBuild.remove(X);
	//						econM.removeExpense( X.unitType );
	//						
	//					}
	
					}
				}
	

				X.time += 1;
			}
		}
	}
	
	public void updateUnderConstruct( m_build buildM )
	{

			for ( Unit X : builderBuilding.keySet())
			{	
//				System.out.println( "bb: " +X.getType() );
				if(  builderBuilding.get(X).isCompleted() )
				{
					baseDataMap.get(Workers.get( X )).miners.add(X ); // add builder back to miners

//					System.out.println( X.getType() );
					if ( builderBuilding.get(X).getType() == UnitType.Terran_Supply_Depot)
					{
						buildM.nextSupply = null;
					}
					else if ( builderBuilding.get(X).getType() == UnitType.Terran_Barracks )
					{
						buildM.nextProduction = null;
					}
					else if ( builderBuilding.get(X).getType() == UnitType.Terran_Engineering_Bay ||  builderBuilding.get(X).getType() == UnitType.Terran_Academy )
					{
						buildM.nextTech = null;
					}
					else if ( builderBuilding.get(X).getType() == UnitType.Terran_Refinery )
					{
						buildM.nextEconomic = null;
					}

					builderBuilding.remove(X);
				}
	
			}
	
	}
	
	
	// add units to appropriate list
	public void add(  Unit unit , m_mission missionM )
	{
		
				
		if ( unit.getType().isBuilding() )
		{
			if ( Buildings.keySet().contains( unit.getType() ) )
			{
				Buildings.put(   unit.getType()  ,    Buildings.get( unit.getType() ) + 1           )  ;
			}
			else
			{
				Buildings.put(   unit.getType()  ,  1         )  ;
			}
		}
		


		
	//	System.out.println(unit.getType() +"    is a refinery?    "+ unit.getType().isRefinery() );
		//System.out.printf("add \n");

		if ( (unit.getType() == UnitType.Terran_SCV) && ( missionM.scouting ==0 )   &&   (  Workers.size() > 2 ))
		{
			
			mission scouting_mission = new mission();
			scouting_mission.type = "scouting";
			scouting_mission.Assigned_Units.add( unit);
			
			Missions.add(scouting_mission);
			missionM.scouting = missionM.scouting+1;
			
			
			
			Scout =unit;
	//		System.out.println( "Scout added");
		}

		else if ( (unit.getType() == UnitType.Terran_SCV) && ( ( missionM.scouting == 1 ) ||  (  Workers.size() <= 2 )  ))
		{
			
			bwta.BaseLocation B = unitBaseLocation(unit);
			 

			baseDataMap.get( B ).miners.add(unit); //add worker to	base:miners list
			Workers.put(unit,B);	// add worker to worker:base list
		}
		
		else if ( unit.getType().isRefinery()   )
		{
//			System.out.println( "Refinery added");
			baseDataMap.get( unitBaseLocation(unit) ).refinery.put(unit, new ArrayList<Unit>()); 
			
		}
		

		else if ( (unit.getType() == UnitType.Terran_Marine)|| (unit.getType() == UnitType.Terran_Medic) ||(unit.getType() == UnitType.Terran_Firebat) )
		{
			Infantry.add(unit);
			//	unit.attack(A);
			//	game.printf( "Infantry Added");
		}


		else if ( (unit.getType() == UnitType.Terran_Factory)|| (unit.getType() == UnitType.Terran_Command_Center) ||(unit.getType() == UnitType.Terran_Barracks) )
		{
	//		System.out.println( "Production added");
			Production.add(unit);
		}

		

	}

	

			

	
	

	public bwta.BaseLocation unitBaseLocation(  Unit unit )
	{

		bwta.BaseLocation BB = null;
		double d = 1000000;
		for( bwta.BaseLocation B : bwta.BWTA.getRegion(unit.getPosition()).getBaseLocations() )
		{
			if ( B.getDistance(unit.getPosition()) <d )
			{
				BB = B;
				d = B.getDistance(unit.getPosition()) ;
			}
		}
		
		if (BB == null) //find closest alternate owned by us
		{
			d = 100000;
			for(bwta.BaseLocation X: bwta.BWTA.getBaseLocations())
			{
				if( baseDataMap.get(X).baseOwner==1 && X.getPosition().getDistance(unit.getPosition() ) < d )  
				{
					d = X.getPosition().getDistance(unit.getPosition() );
					BB = X;
				}
			}
		}
		
		return BB;
	}
	
	
			

	public Unit findClosestWorker(  Position X) {
		double d = 1000000;
		Unit B = null;
		for (Unit u : Workers.keySet()) {
			if ( (u.getPosition().getDistance(X) < d) && (u.hasPath(X)) &&  (  baseDataMap.get( Workers.get(u)).miners.contains(u)  ) ) //is it a miner/has path?
			{
				B = u;
				d = u.getPosition().getDistance(X);
			}

		}
		
		baseDataMap.get(  Workers.get(B) ).miners.remove(B);
		return B;
	}
	
	

		

	

	
	
}

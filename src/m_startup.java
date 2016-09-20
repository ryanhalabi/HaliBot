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
import bwta.BWTA;


public class m_startup {

	
	//fill variables
	public void onStart( Player self , Game game, m_list listM  )
	{
		
		//ENEMY RACE
		listM.enemy.race = game.enemy().getRace();
		listM.enemy.Player = game.enemy();
		listM.self.Player = game.self();

		//REGION DRAWING
		// create list of choke/regions for each region
		for (bwta.Region r : bwta.BWTA.getRegions()){     
			listM.region_point_list.put(   r, r.getPolygon().getPoints()  );  
			listM.region_choke_list.put( r, r.getChokepoints()   );	
		}

		
		
		//GENERATE DATA PER BASE LOCATION		
		for (bwta.BaseLocation b : bwta.BWTA.getBaseLocations())
		{     
			
			//INITIALIZE BASE DATA
			baseData W = new baseData();
			
			//BASE REGION
			bwta.Region r = b.getRegion(); //identify region its in
			
			//MINERAL GAS INFO
			W.minerals =   b.getMinerals() ;
			
			//START LOCATION STATUS
			if (b.isStartLocation() == true)
			{
				listM.Start_Locs.add(b);
				W.isStartLocation = true;
			}
			
			//CREATE EXPLORE POINTS
			Position center = b.getPosition(); 
			W.baseExplorePoints = findExplorePoints( game,  b, center);
			
			//BUILD AREA CALCULATION			
			W.base_build_area =  calculateBuildPositions( b,  r,  center,  game);

			
			listM.baseDataMap.put( b , W);
		}
		

		//FIND CLOSEST BASE LOCATION TO START
		Position sp =   self.getStartLocation().toPosition()  ;  
		double d = 500000;
		for( bwta.BaseLocation b : bwta.BWTA.getBaseLocations())
		{
			if ( b.getPosition().getDistance(sp) < d )
			{
				listM.start_base = b;
				d = b.getPosition().getDistance(sp);
			}
		}

		//STARTING REGION
		listM.start_region=   bwta.BWTA.getRegion(  self.getStartLocation().toPosition())  ;  

		
		//CLOSEST EXPO
		 d =10000000;
		 bwta.BaseLocation B = null ;

		 
		 for ( bwta.BaseLocation x :  BWTA.getBaseLocations())
		 {
			 if( x.getPosition().getDistance( listM.start_base) < d && ( x != listM.start_base) )
			 {
				 d =  x.getPosition().getDistance( listM.start_base );
				 B =  x;
			 }
		 }
		
		 listM.start_expo = B;
	

			
			//////////////////////
			// POPULATE BUILDINGS LIST
			/////////////////////
		 	listM.Buildings.put( UnitType.Terran_Academy  ,  0) ;
			listM.Buildings.put( UnitType.Terran_Armory , 0) ;
			listM.Buildings.put( UnitType.Terran_Barracks , 0) ;
			listM.Buildings.put( UnitType.Terran_Command_Center , 0) ;
			listM.Buildings.put( UnitType.Terran_Comsat_Station , 0) ;
			listM.Buildings.put( UnitType.Terran_Control_Tower , 0) ;
			listM.Buildings.put( UnitType.Terran_Covert_Ops , 0) ;
			listM.Buildings.put( UnitType.Terran_Engineering_Bay , 0) ;
			listM.Buildings.put( UnitType.Terran_Factory , 0) ;
			listM.Buildings.put( UnitType.Terran_Bunker , 0) ;
			listM.Buildings.put( UnitType.Terran_Machine_Shop , 0) ;
			listM.Buildings.put( UnitType.Terran_Missile_Turret , 0) ;
			listM.Buildings.put( UnitType.Terran_Nuclear_Silo , 0) ;
			listM.Buildings.put( UnitType.Terran_Physics_Lab , 0) ;
			listM.Buildings.put( UnitType.Terran_Refinery , 0) ;
			listM.Buildings.put( UnitType.Terran_Science_Facility , 0) ;
			listM.Buildings.put( UnitType.Terran_Starport   , 0) ;
			listM.Buildings.put( UnitType.Terran_Supply_Depot   , 0) ;
			
			
			
			
	}


	
	
public Map<String, Position > calculateBuildPositions(bwta.BaseLocation b, bwta.Region r, Position center, Game game)
{
	Map<String, Position > P = new HashMap<String, Position>(); // best points

	Map<Position, Double > base_point_score = new HashMap< Position, Double>();
	

	/////////////////
	// FIRST POINT   NON PRODUCTION
	////////////////

	//score points
	for ( Position p : r.getPolygon().getPoints()) 
	{	


		// get minimum distance
		// penalize too close to minerals
		// penalize too far from choke
		p = new Position(  (p.getX() + center.getX())/2 ,  (  p.getY() + center.getY())/2 );
		double BD = 10000;
		double CD = 10000;
		double a;
		//MINERAL PENALIZATION
		for (Unit aa : game.getUnitsInRectangle( new Position( p.getX()- 30, p.getX() - 30) , new Position( p.getX()+ 30, p.getX() + 30) ) ) 
		{
			if (  aa.getType().isResourceContainer() )  BD += -500;  
		}
		//DISTANCE TO BASE
		BD += p.getDistance(center);


		for( bwta.Chokepoint c :  r.getChokepoints())
		{
			a = .5*p.getDistance(c.getCenter());  //score is smaller if far from chokes
			if (a <CD)  CD = a;
		}
		a = p.getDistance(center);
		if (a <CD)  CD = a;
		base_point_score.put( p, CD);	
//		System.out.println(" scoring " + p + "with score:" + CD + "\n");
		
	}



	// best point: maximize distnace to choke/base/minerals
	double max = 0;
	Position best = null;
	for (Position xx : base_point_score.keySet())
	{
		if( (base_point_score.get(xx)) > max  && ( bwta.BWTA.getRegion(xx) == r) )
		{
			max = base_point_score.get(xx);
			best = xx;
		
		}
	}
	P.put( "nonproduction", best);


	/////////////////
	// SECOND POINT   PRODUCTION
	////////////////


	// second best
	base_point_score.clear();
	for ( Position p :  r.getPolygon().getPoints()) 
	{	
		p = new Position(  (p.getX() + center.getX())/2 ,  (  p.getY() + center.getY())/2 );
		double CD = 10000;
		for (Unit aa : game.getUnitsInRectangle( new Position( p.getX()- 30, p.getX() - 30) , new Position( p.getX()+ 30, p.getX() + 30) ) ) 
		{
			if (  aa.getType().isResourceContainer() ) 
			{
				CD += -500; 
			}
		}

		for( bwta.Chokepoint c :  r.getChokepoints())
		{
			double a;
			a = p.getDistance(c.getCenter());
			if (a <CD)  CD = a;
		}
		double a = p.getDistance(center);
		if (a <CD)  CD = a;
		a = .5*p.getDistance(best);
		if (a <CD)  CD = a;
		base_point_score.put( p, CD);		
	}

	max = 0;
	Position best2 = null;
	base_point_score.put( best,(double)0);
	for (Position xx : base_point_score.keySet() )
	{
		if( ( base_point_score.get(xx) > max )  && ( bwta.BWTA.getRegion(xx) == r) )
		{
			max = base_point_score.get(xx);
			best2 = xx;
		}
	}
	
	P.put( "production", best2);
	
	return P;
}
	

public Map<Position, Boolean> findExplorePoints(Game game, bwta.BaseLocation b, Position center)
{
	Map<Position, Boolean> A = new HashMap<Position, Boolean>();
	

	int x = center.getX();
	int y = center.getY();
	int dist = 32*32;
	
//	System.out.println(" center region " + bwta.BWTA.getRegion(center) + " vs" + b + "\n");
	for(int i=-dist; i<dist+1; i=i+32*6)
	{
		for(int j=-dist; j<dist+1; j=j+32*6)
		{
			Position Z = new Position(x+i, y+j);
			
//			System.out.println( bwta.BWTA.getRegion( Z) + " vs " + r +"\n");
			if( b.getRegion() == bwta.BWTA.getRegion(Z) &&   game.isWalkable( (int) (x+i)/8  , (int) (y+j)/8 )  ) // isBuildable(Z.toTilePosition()) 
			{
				A.put( Z, false) ;
			}
		}
		
	}
	
	A.remove(  center);
	
	return A;
	
}
	
	
}

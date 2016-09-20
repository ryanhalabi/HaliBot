import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Position;
import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.Position;
import bwta.BWTA;
import bwapi.Color;

public class m_scout {



	//SCOUT ALGORITHM
	//	1) find enemy
	//	2) scout enemy bases fully
	//	3) scout remaining bases
	public static void Scout(Unit Scout, Game game , m_list listM)
	{
		//////////////////
		//#1 HAVEN'T FOUND ENEMY YET, SO SCOUT CLOSEST BASE THAT HASN'T BEEN EXPLORED
		//////////////////
		if ( listM.enemyBaseFound == false) 
		{
			
			bwta.BaseLocation B = listM.start_base;
			double d = 1000000;
			for( bwta.BaseLocation X : listM.Start_Locs )
			{
				if ( ( listM.baseDataMap.get(X).baseOwner  == 0 ) &&	(!game.isExplored(X.getTilePosition()) )	 && (Scout.getPosition().getDistance( X.getPosition()) <d )  && (Scout.hasPath(X.getPosition()) ) )
				{
					d = Scout.getPosition().getDistance( X.getPosition())  ;  // BWTA.getGroundDistance(  start_base.getTilePosition() ,   X.getTilePosition()  );  TOO SLOW
					B = X;
				}
			}
			scoutMove(game, Scout, listM, B.getPosition());
		}


		//////////////////
		//#2 NEW PROTOCOL IF ENEMY HAS BEEN FOUND
		//////////////////
		else if ( listM.enemyBaseFound == true) 
		{
			bwta.BaseLocation B = null;
			double d = 1000000;
			
			//LOOK FOR ENEMY BASE WITH UNEXPLORED POINTS, CHOOSE CLOSEST ONE
			for( bwta.BaseLocation X : listM.baseDataMap.keySet() )
			{

				int reset = 200; // time to wait till reseting explore points
				//IF >2000 SINCE SCOUTED BASE, RESET EXPLORE POINTS
				if (listM.baseDataMap.get(X).baseTimer > reset && !listM.baseDataMap.get(X).baseExplorePoints.values().contains(false) )
				{
					for( Position P : listM.baseDataMap.get(X).baseExplorePoints.keySet() )
					{
						listM.baseDataMap.get(X).baseExplorePoints.put(P, false);
					}
				}
				
				if ( ( listM.baseDataMap.get(X).baseExplorePoints.values().contains( false ) )  && ( listM.baseDataMap.get(X).baseOwner == -1 )  && (Scout.getPosition().getDistance( X.getPosition()) <d )  && (Scout.hasPath(X.getPosition()) ) )
				{
					d = Scout.getPosition().getDistance( X.getPosition())  ;  
					// BWTA.getGroundDistance(  start_base.getTilePosition() ,   X.getTilePosition()  );  TOO SLOW
					B = X;
				}
			}
			

			
			Position Y = null;
			
			//FOUND ENEMY BASE WHICH NEEDS TO BE SCOUTED
			if ( B != null )
			{
				d = 1000000;
				for( Position X : listM.baseDataMap.get(B).baseExplorePoints.keySet() )
				{
					if ( (Scout.getPosition().getDistance( X) <d )  && (Scout.hasPath(X) )&& ( listM.baseDataMap.get(B).baseExplorePoints.get(X)== false ) )
					{
						d = Scout.getPosition().getDistance( X)  ;  // BWTA.getGroundDistance(  start_base.getTilePosition() ,   X.getTilePosition()  );  TOO SLOW
						Y = X;
					}
				}

				if ( Y != null)
				{
					scoutMove(game, Scout, listM, Y );
				}

			}
			
			//IF NO UNSCOUTED ENEMY BASE, SCOUT REMAINING NEUTRAL
			else 
			{

				d = -1;
				for( bwta.BaseLocation X : listM.baseDataMap.keySet() )
				{
					if ( ( listM.baseDataMap.get(X).baseOwner  == 0 )   && (Scout.hasPath(X.getPosition()) )  && ( listM.baseDataMap.get(X).baseTimer > d ))
					{
						d = listM.baseDataMap.get(X).baseTimer ;  
						B = X;
					}
				}

				
				if( B != null)
				{
					scoutMove(game, Scout, listM, B.getPosition());
				}
				else
				{
					scoutMove(game, Scout, listM, listM.start_base.getPosition() );
				}
			}
		}
	}






	// doesnt this return baseloc?????

	//scout
	public static bwta.BaseLocation findClosestBaseLoc( Game game,  bwta.BaseLocation baseloc  )
	{
		bwta.BaseLocation closest = null;
		double d = 100000000;
		for ( bwta.BaseLocation X : BWTA.getBaseLocations())
		{
			if ( (BWTA.getGroundDistance(baseloc.getTilePosition(), X.getTilePosition()) < d ) && X != baseloc)
			{
				d = BWTA.getGroundDistance(baseloc.getTilePosition(), X.getTilePosition()) ;
				closest = X;

			}
		}
		return closest;

	}






	
	
public static void scoutMove(Game game , Unit scout, m_list listM, Position position)
{

	Color blue = new Color(0,0,255);
	Color green = new Color(0,255,0);
	Color red = new Color(255,0,0);
	Color purple = new Color(152,50,204);
	Color teal = new Color(0,128,128);
    Color yellow = new Color(255,255,0);
    Color orange = new Color(255,165,0);

	List<Double> prohibited_angles = new ArrayList<Double>();
	int cdist = 200;
	game.drawCircleMap(scout.getPosition(),cdist, green);

    game.drawLineMap(scout.getPosition(), position, green);
//	game.setScreenPosition(scout.getX() - 320,  scout.getY()-200);

	double angle = scout.getAngle() ;

    double angle_closeness = 40;
    double tangle_closeness;
//  	game.drawLineMap(scout.getPosition(), new Position( (int) ( scout.getX() + cdist*Math.cos(angle)), (int) ( scout.getY() + cdist*Math.sin(angle)) ), teal);
//    game.drawTextMap( scout.getPosition() ,Double.toString(scout.getAngle()) );

    for ( Unit u : game.getUnitsInRadius( scout.getPosition() , cdist ))
	{
		if ( u.getPlayer() == listM.enemy.Player && u.getType().canAttack() && u.getDistance(scout) < u.getType().groundWeapon().maxRange() + 20)
		{
			// calculate angle of units to scout
			bwapi.Position x = scout.getPosition();
			bwapi.Position y = u.getPosition();
			
			angle = Math.atan2( y.getY() - x.getY(), y.getX() - x.getX());
            angle = angle <0 ? angle + 2*Math.PI : angle;

		    prohibited_angles.add(angle);

            game.drawLineMap(scout.getPosition(), u.getPosition(), red);
		    game.drawLineMap(scout.getPosition(), new Position( (int) ( scout.getX() + cdist*Math.cos(angle)), (int) ( scout.getY() + cdist*Math.sin(angle)) ), red);
            game.drawTextMap(new Position( (int) ( scout.getX() + cdist*Math.cos(angle)), (int) ( scout.getY() + cdist*Math.sin(angle)) ) ,Double.toString(angle) );

            tangle_closeness = Math.abs( scout.getAngle() - angle) ;
            tangle_closeness = tangle_closeness > Math.PI ? 2*Math.PI - tangle_closeness : tangle_closeness;

            angle_closeness = Math.min( tangle_closeness, angle_closeness);
		}
	}



	if ( prohibited_angles.size() >0 && angle_closeness < Math.PI/4)
	{
		game.drawCircleMap(scout.getPosition(),cdist, red);

		Collections.sort(prohibited_angles);
		List<Double> safe_angles = new ArrayList<Double>();

        if( prohibited_angles.size() == 1)
        {
            double one = (prohibited_angles.get(0) + 3*Math.PI/8 ) % 2*Math.PI;
            double two = prohibited_angles.get(0) - 3*Math.PI/8;
            two = two <0 ? two + 2*Math.PI : angle;

            safe_angles.add(two);
            safe_angles.add(one);

        }
        else
        {
            for (int i = 0; i < prohibited_angles.size() - 1; i++)
            {

                angle =  (prohibited_angles.get(i) + prohibited_angles.get(i+1) )/2 ;

                double size = 0;
                double angle1 = angle;
                double angle2 = angle;
                while( angle1  <  prohibited_angles.get(i + 1))
                {
                    size = (prohibited_angles.get(i + 1) - angle1);
                    if (size > Math.PI / 8)
                    {
                        game.drawLineMap(scout.getPosition(), new Position((int) (scout.getX() + cdist * Math.cos(angle1)), (int) (scout.getY() + cdist * Math.sin(angle1))), purple);
                        safe_angles.add(angle1);
                        game.drawLineMap(scout.getPosition(), new Position((int) (scout.getX() + cdist * Math.cos(angle2)), (int) (scout.getY() + cdist * Math.sin(angle2))), purple);
                        safe_angles.add(angle2);
                    }


                    angle1 += Math.PI/4;
                    angle2 -= Math.PI/4;

                }
            }



            //last and first angle
            angle = prohibited_angles.get(prohibited_angles.size() - 1) + (prohibited_angles.get(0) + 2 * Math.PI - prohibited_angles.get(prohibited_angles.size() - 1)) / 2;

            double size = 0;
            double angle1 = angle;
            double angle2 = angle;
            while( angle1  <  prohibited_angles.get(i + 1))
            {
                size = (prohibited_angles.get(0) + 2 * Math.PI - prohibited_angles.get(prohibited_angles.size() - 1));

                if (size > Math.PI / 8)
                {
                    game.drawLineMap(scout.getPosition(), new Position((int) (scout.getX() + cdist * Math.cos(angle1)), (int) (scout.getY() + cdist * Math.sin(angle1))), purple);
                    safe_angles.add(angle1);
                    game.drawLineMap(scout.getPosition(), new Position((int) (scout.getX() + cdist * Math.cos(angle2)), (int) (scout.getY() + cdist * Math.sin(angle2))), purple);
                    safe_angles.add(angle2);
                }


                angle1 += Math.PI/4;
                angle2 -= Math.PI/4;

            }



            double size = (prohibited_angles.get(0) + 2 * Math.PI - prohibited_angles.get(prohibited_angles.size() - 1));

            if (size > Math.PI / 4) {
                safe_angles.add(angle);
                game.drawLineMap(scout.getPosition(), new Position((int) (scout.getX() + cdist * Math.cos(angle)), (int) (scout.getY() + cdist * Math.sin(angle))), purple);

            }
        }


    // want to find closest angle to scouts angle

        int best = -1;
        double gap = 2*Math.PI;
        double dist;
        for (int  i = 0; i < safe_angles.size()-1; i++)
        {
            dist =  Math.abs(scout.getAngle() - safe_angles.get(i));
            dist = dist > Math.PI ? 2*Math.PI - dist : dist;



            Position target_position =  new Position( (int) ( scout.getX() + cdist*Math.cos(angle)), (int) ( scout.getY() + cdist*Math.sin(angle)) );

//            System.out.printf("gap: " + gap + " dist: " + dist + " walkable: " + game.isWalkable( Math.floorMod( target_position.getX(), 8), Math.floorMod(target_position.getY() ,8) ) +  "\n");


            if (dist < gap && game.isWalkable( Math.floorMod( target_position.getX(), 8), Math.floorMod(target_position.getY() ,8) ) )
            {
                gap = dist;
                best = i;

            }
        }


			// what to do if cant get around the unit?
			// pretend we've seen the target position and hopefully move on.
			// set base as enemy, need to find baselocation corresponding to current position
			
			
		if ( best == -1)
		{	
			for ( bwta.BaseLocation b : listM.baseDataMap.keySet() )
			{
				if (b.getPosition() == position)
				{
					 listM.baseDataMap.get(b).baseTimer = 0;
				}
				else if( listM.baseDataMap.get(b).baseExplorePoints.containsKey(position))
				{
					listM.baseDataMap.get( b ).baseExplorePoints.put(position, true);
					
				}
			}

            System.out.printf("no angle suitable \n");
		}
		else
        {
            Position best_position = new Position( (int) (cdist* Math.cos(safe_angles.get(best))) + scout.getPosition().getX(),
                    (int) (cdist*Math.sin(safe_angles.get(best))) + scout.getPosition().getY() );
            scout.move( best_position );
            game.drawLineMap(scout.getPosition(), best_position , yellow);
            System.out.printf("found good angle \n");

        }
		
	
		
	}
	else
	{
		scout.move(position);
//        System.out.printf("no need to divert \n");

    }

//	System.out.printf("end of scouting\n");
	
}




public static Unit addScout(Game game , m_list listM,  m_build buildM, Player self )
{
	
	Unit W = listM.Workers.keySet().iterator().next();
	
	listM.remove(W, buildM, listM, self);
	
	return W;
	
	
	
}



}


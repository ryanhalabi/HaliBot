import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.WalkPosition;
import bwapi.TilePosition;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.*;

import java.util.Random;


public class m_visualization {

	 Random rnd = new Random();
	 
	

	Color yellow = new Color(255,255,0);	 
	Color red = new Color(255,0,0);	 
	Color green = new Color(0,255,0);
	Color blue = new Color(0,0,255);
	Color white = new Color(255,255,255);
	Color orange = new Color(255,165,0);

	int i = 0;


	//visualization
	public void visualization(Game game,  	m_list listM , m_timer timerM, m_build buildM, m_economy econM, m_scout scoutM) 


	{


		
		int l = 0;
		
		for( Unit B : listM.enemy.unitList.keySet())
		{
//			game.drawTextScreen(  240, l, B +": " + listM.enemy.unitCount.get(B) );
			game.drawCircleMap (listM.enemy.unitList.get(B).lastPosition , 4 , red, true);
			
		}
		
		

		for( UnitType B : listM.enemy.unitComposition.keySet())
		{
			game.drawTextScreen(  240, l, B +": " + listM.enemy.unitComposition.get(B) );
//			game.drawCircleMap (listM.enemy.unitList.get(B).lastPosition , 4 , red, true);
			l = l+10;
			
		}
		
		
		
		
		game.drawTextScreen(  240, 80, "Enemy Race: " + listM.enemy.race );
	
		
		
		
		
		
		
		
		
		
		
		///////////////////////
		// DRAW TOP LEFT INFO PANEL
		///////////////////////
		
	 	game.drawTextScreen(  0, 0, "55 ms: " + timerM.fiftyfive + ", 1 sec: " + timerM.onesec + ", 10 sec: " + timerM.tensec + " last: " + timerM.lastTime + " max: "+ timerM.max);
    	game.drawTextScreen(  0, 10, "Workers: " + listM.Workers.size() );
    	game.drawTextScreen(  0, 20, "Infantry: " + listM.Infantry.size() );
    	game.drawTextScreen(  0, 30, "Production: " + listM.Production.size() );

    	game.drawTextScreen(  0, 50, "# under construct: " + listM.builderBuilding.size() );

    	game.drawTextScreen(  0, 60, "# ordered: " + listM.orderedBuild.size() );


//    	game.drawTextScreen(  0, 70, "frame: " + i );
//    	game.drawTextScreen(  0, 80, "Available Min = : " + game.self().minerals() + " - " + econM.minFutureExpense );
//    	game.drawTextScreen(  0, 90, "Available Gas = : " + game.self().gas() + " - " + econM.gasFutureExpense );
    	i = i+1;
    	game.drawTextScreen(  90, 40, "nextProduction: " + buildM.nextProduction );
    	game.drawTextScreen(  90, 10, "nextSupply: " + buildM.nextSupply );
    	game.drawTextScreen(  90, 20, "nextTech: " + buildM.nextTech );
    	game.drawTextScreen(  90, 30, "nextEco: " + buildM.nextEconomic );

  
    	
    	
    	
    
//	    

//		draw gas workers and refineries
//		
//
//		for( Unit R : listM.Refinery.keySet())
//		{
//			game.drawCircleMap ( R.getPosition() , 8 , blue, true);
//			for( Unit W: listM.Refinery.get(R))
//			{
//				game.drawCircleMap ( W.getPosition() , 8 , blue, true);
//				if ( listM.Workers.contains(W))
//				{
//					game.drawCircleMap ( W.getPosition() , 4 , green, true);
//				}
//			}
//		}
//		

		///////////////////////
		// DRAW BUILDER STATUS
    	///////////////////////
		for( typeTileBuilderTimeStatus X : listM.orderedBuild)
		{
			game.drawTextMap(  X.builder.getPosition() , X.status.toString() );
			game.drawCircleMap(  X.builder.getPosition(),  8 , orange, true);	
			game.drawCircleMap(  X.tilePosition.toPosition(),  8 , orange, true);	
		}
		
//		System.out.println("size of" +  listM.UnderConstruct.size() +  "\n");
		///////////////////////
		// DRAW BUILD BUILDER
		///////////////////////
		for( Unit X : listM.builderBuilding.keySet())
		{
//			System.out.println( "orderedbuild size:" +  listM.orderedBuild.size() +  "\n");
//			System.out.println( "underconstruct size:" +  listM.UnderConstruct.size() +  "\n");
			game.drawCircleMap ( X.getPosition() , 8 , orange, true);
			game.drawCircleMap ( listM.builderBuilding.get(X).getPosition() , 8 , orange, true);
			
		}
		
//		System.out.println("visualized2 \n");

		
		


		///////////////////////
		// DRAW BOUNDARIES
		///////////////////////
		for ( List<Position> points : listM.region_point_list.values() )
		{

			
			for (int i =0 ; i< points.size()-1 ; i++)
			{
				game.drawLineMap ( points.get(i), points.get(i+1), green);
			}
			game.drawLineMap ( points.get(points.size()-1), points.get(1), green);	
		}

		///////////////////////
		// DRAW CHOKES
		///////////////////////
		for ( List<bwta.Chokepoint> c : listM.region_choke_list.values())
		{
			for (int i =0 ; i< c.size() ; i++)
			{
				//game.drawCircleMap (c.get(i).getCenter(), (int)c.get(i).getWidth()/2 , green);
				game.drawCircleMap (c.get(i).getCenter(), 4 , red, true);
			}
		}

		///////////////////////
		// DRAW BUILD AREAS
		///////////////////////

		

		

		
//		System.out.println("bases");
		
		
		for ( bwta.BaseLocation A  : listM.baseDataMap.keySet() )	
		{
			
			
			game.drawTextMap(A.getPosition(),  listM.baseDataMap.get(A).baseTimer.toString() );
//
//			System.out.println("size" + listM.baseDataMap.keySet().size() +" \n");
		
//			System.out.println("check 1 :" + listM.baseDataMap.get(A).base_build_area + " \n");
			
//			draw build areas
			game.drawCircleMap ( listM.baseDataMap.get(A).base_build_area.get("nonproduction"), 16 , red, true);  //producer is blue
			game.drawCircleMap (  listM.baseDataMap.get(A).base_build_area.get("production"), 16 , blue, true);
			
			
//			System.out.println("check 2 \n");
			
			//draw scout status
			if ( listM.baseDataMap.get(A).baseOwner == 1 )  // 1 self
			{
				game.drawCircleMap ( A.getPoint() , 4 , green,true);
			}
			else if ( listM.baseDataMap.get(A).baseOwner == -1 )  // -1 enemy
			{
				game.drawCircleMap ( A.getPoint(), 4 , red,true);
			}
			else if ( listM.baseDataMap.get(A).baseOwner == 0 && listM.baseDataMap.get(A).baseTimer == 0 )  // 0 neutral and needs to be scouted
			{
				game.drawCircleMap ( A.getPoint(), 4 , blue,true);
			}
			if ( listM.baseDataMap.get(A).baseOwner == 0 )  // 0 neutral and already scouted recently
			{
				game.drawCircleMap ( A.getPoint(), 4 , white ,true);
			}


			
			rnd.setSeed(A.hashCode());
			
			Color rand = new Color(  rnd.nextInt() % 255, rnd.nextInt() % 255  , rnd.nextInt() % 255  );
			for ( Position X : listM.baseDataMap.get(A).baseExplorePoints.keySet() )
			{
					
					game.drawCircleMap( X, 6 , rand ,true);
					
					if ( listM.baseDataMap.get(A).baseExplorePoints.get(X) == false )
					{
						game.drawCircleMap( X, 2 , red ,true);
					}
					
				}
			}
//		System.out.printf( "drawing circle 2 \n");	
			
	}

	
	
	
	
	
}

import java.util.List;
import java.util.ArrayList;

import bwapi.*;
import bwta.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;


public class TestBot1 extends DefaultBWListener {
	
	private Mirror mirror = new Mirror();
    private Game game;
    private Player self;
    private Player enemy;
     
    Color red = new Color(255,0,0);	 
	Color green = new Color(0,255,0);
	Color blue = new Color(0,0,255);

	m_build buildM = new m_build();
	m_visualization visualizationM = new m_visualization();
	m_economy econM = new m_economy();
	m_list listM = new m_list();
	m_scout scoutM = new m_scout();
	m_startup startupM = new m_startup();
	m_timer timerM = new m_timer();
	training_manager trainM = new training_manager();
	m_intelligence intelM = new m_intelligence();
	m_mission missionM = new m_mission();
	

	
    public static void main(String[] args) 
    {
        new TestBot1().run();
    }
    
    
    public void run() 
    {
    	mirror.getModule().setEventListener(this);
    	mirror.startGame();
    }
	
    

    
    
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// UNIT CREATION
	
//    @Override
//    public void onUnitCreate(Unit unit) 
//    {
////    	System.out.println("Created" + unit.getType() + "\n");
////
////    	if ( unit.getPlayer() == self && unit.getType() == buildM.nextProduction)
////    	{
////        		buildM.checkOrderedBuild(game, self, listM, econM);   //adds to underConstruct list
//////        		buildM.nextProduction = null;
////    	}
//		
//    }

	
//    @Override
//    public void onUnitMorph(Unit unit)  //spits out refinery, but it's not complete
//    {    	  	
//    	
//    	if ( unit.getPlayer() == self &&  unit.getType() == buildM.nextEconomic )
//    	{
//    		System.out.println( "morphed" + unit.getType() + " and it is " + unit.isCompleted() );
//    		listM.add( unit );
//	    //	buildM.checkOrderedBuild(game, self, listM, econM);  //adds to underConstruct list
////	    	buildM.nextProduction = null;
//    	}
//    }
    
    
    @Override
    public void onUnitComplete(Unit unit ) 
    {    	
    	
    	if ( unit.getPlayer() == self)
    	{
//    		System.out.println("COMPLETED " + unit.getType() + " \n");
    		listM.add( unit , missionM );
    	} 	
    }
    	

    
    @Override
    public void onUnitDestroy(Unit unit) 
    {
    	//System.out.println("Morphed" + unit.getType() + "\n");
    	//on creation, update unit lists in startupMiable here
    	listM.remove( unit, buildM, listM, self );
    }
    

    @Override
    public void onSendText(String text) 
    {
		if( Integer.parseInt(text) == 1)
		{
			game.leaveGame();
		}
		game.setLocalSpeed(  Integer.parseInt(text));
    }
    

    @Override
    public void onStart() 
    {
    	game = mirror.getGame();
    	self = game.self();
    	enemy = game.enemy();
    	//System.out.printf( "startup \n");
    	
//    	game.sendText("black sheep wall");  	
    // game.sendText("show me the money");
    	game.sendText("operation cwal");
    	
    	//Use BWTA to analyze map
    	//This may take a few minutes if the map is processed first time!
    	System.out.println("Analyzing map...");
    	BWTA.readMap();
    	BWTA.analyze();
    	System.out.println("Map data ready");
    	game.setLocalSpeed(0);
    	game.enableFlag(1);
    	
    	// update locations and other stuff now
    	startupM.onStart( self, game , listM);
    }

    

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onFrame() 
    {

    	econM.updateAvailableResources(self);    	
    	long startTime = System.nanoTime();
    
    	//SET FOR DEBUG MESSAGE OUTPUT
		int on = 0;
    	
    	////////////////////
    	// VISUALIZATION MANAGER
    	////////////////////
    	visualizationM.visualization( game,	 listM ,  timerM,  buildM,  econM,  scoutM);
    	if (on ==1){		System.out.println(" \n  \n \n \n \n \n \n \n\n visualized "); }
    	
    	////////////////////  	
    	// TIMER MANAGER
    	////////////////////
    	timerM.countdownall( listM.baseDataMap);  //countdown timers, important to keep this above scoutM.basepresence
    	if (on ==1){	System.out.println("update timer ");}
    	
    	////////////////////
    	// MISSION MANAGER
    	////////////////////
    	missionM.update(game, listM, buildM, self, missionM);
    	
    	////////////////////
    	// INTELLIGENCE MANAGER
    	////////////////////
    	intelM.updateInfo( game ,listM);  //update info about bases
   		intelM.updateUnitInfo(game, listM);
   		if (on ==1){ System.out.println("scout m ");}
   		
    	////////////////////	
    	// ECONOMY MANAGER
    	////////////////////	
//   		System.out.printf( "min");
    	econM.gatherminerals(game, listM);  //gather resources
//    	System.out.printf( "gas");
    	econM.gathergas(self,game, listM, buildM); //gather gas
    	if (on ==1){ System.out.println("econ m ");}

    	////////////////////   
		// BUILDING MANAGER
    	////////////////////	
   		listM.updateUnderConstruct( buildM);
//    	System.out.println("update");
   		listM.checkOrderedBuild(game,self, listM, econM, scoutM);  //check how buildings we've ordered are doing
//    	System.out.println("check");
   		buildM.updateProdQeue( listM,  econM,  scoutM, self,  game);  //figure out next buildings to make
//    	System.out.println("qeueue");
   		if (on ==1){ System.out.println("build m "); }
   		
   		
   		
 		
    	////////////////////	
    	// TRAIN MANAGER
    	////////////////////	
    	trainM.trainNext(listM, econM, self, game);
    	if (on ==1){ System.out.println("train m");}  

          
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////TRAINING_ORDERS////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
          
          
   		
            
            
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////UNIT_COMMANDS//////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         




//////////////Infantry
        

        
        if ( self.completedUnitCount(UnitType.Terran_Marine) + self.completedUnitCount(UnitType.Terran_Medic) + self.completedUnitCount(UnitType.Terran_Firebat) >10 && (timerM.attack_timer == 0) )
        {
        	for (Unit myUnit : listM.Infantry) 
        	{
        		myUnit.attack(listM.start_expo.getPosition());
            }
        	timerM.attack_timer = 200;
 
        }
        
        
        
        
        
        
        
        //////////////////////////////
        // TIMER
        /////////////////////////////
        
        long endTime = System.nanoTime();
    	long duration = (endTime - startTime)/1000000 ;  //divide by 1000000 to get milliseconds.

    	if( timerM.max < duration && visualizationM.i >2)
    	{
    		timerM.max = duration;
    	}
    	timerM.lastTime = duration;
    	timerM.frameTime( duration );
    	
    

	
}
    
 
 //         System.out.println("Map data ready");
 

} 
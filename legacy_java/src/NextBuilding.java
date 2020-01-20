import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bwapi.*;
import bwta.*;


public class NextBuilding {

	//unit lists
	public 	UnitType Production=  null;
	public 	UnitType Tech =  null;

	
	
	



	public void nextProduction(Player self, Game game, List<Unit> Buildings, List<UnitType> OrderedBuildings)
{
		//Buildlings = list of owned buildings
		//OrderedBuildings = list of ordered buildings
		
		
    	///////////////////
    	//DEPOT
    	////////////////////
    	//if we're running out of supply and have enough minerals ...
    	if (( (float) self.supplyUsed()/self.supplyTotal()  > .75) 
    			&& (self.minerals() >= 100) 
    			&& ! OrderedBuildings.contains( UnitType.Terran_Supply_Depot ) ) 
    	{
    		Production =   UnitType.Terran_Supply_Depot ;
    	}

    	///////////////////
    	//REFINERY
    	////////////////////
    	//if we have enough minerals and want a refinery
    	else if (		 (self.supplyTotal() > 10 ) 
    			&& (self.minerals() >= 100  )
    			&&  ! OrderedBuildings.contains( UnitType.Terran_Refinery ) )
    	{
    		game.printf("REFINERY");
    		Production =   UnitType.Terran_Refinery ;
    	}


    	///////////////////
    	//BARRACKS
    	////////////////////
    	//if we have enough minerals and want a barracks
    	else if ((self.supplyTotal() > 20 ) 
    			&& (self.minerals() >= 150*(1.5*self.incompleteUnitCount( UnitType.Terran_Barracks)+1 ) ) 
    			&&  ! OrderedBuildings.contains( UnitType.Terran_Barracks ) ) 
    	{
    		game.printf( "BARRACKS");
    		Production =   UnitType.Terran_Barracks ;
    	}



    	///////////////////
    	//ACADEMY
    	////////////////////

    	else if ( (self.completedUnitCount( UnitType.Terran_Barracks) + self.incompleteUnitCount( UnitType.Terran_Barracks) > 0 ) 
    			&& (self.completedUnitCount( UnitType.Terran_Refinery) + self.incompleteUnitCount( UnitType.Terran_Refinery) > 0 ) 
    			&& ( self.minerals() >= UnitType.Terran_Academy.mineralPrice() )
    			&& ( self.gas() >= UnitType.Terran_Academy.gasPrice() )
    			&&  ! OrderedBuildings.contains( UnitType.Terran_Academy ) ) 
    	{
    		game.printf( "ACADEMY");
    		Production =   UnitType.Terran_Academy ;	
    	}





    	///////////////////
    	//ENGINEERING BAY
    	////////////////////

    	else if ( (self.completedUnitCount( UnitType.Terran_Barracks) + self.incompleteUnitCount( UnitType.Terran_Barracks) > 0 ) 
    			&& (self.completedUnitCount( UnitType.Terran_Refinery) + self.incompleteUnitCount( UnitType.Terran_Refinery) > 0 ) 
    			&& (self.completedUnitCount( UnitType.Terran_Academy) + self.incompleteUnitCount( UnitType.Terran_Academy) > 0 ) 
    			&& ( self.minerals() >= UnitType.Terran_Engineering_Bay.mineralPrice() )
    			&& ( self.gas() >= UnitType.Terran_Engineering_Bay.gasPrice() ) 
    			&&  ! OrderedBuildings.contains( UnitType.Terran_Engineering_Bay ) ) 
    	{
    		game.printf("Engineering_Bay");
    		Production =   UnitType.Terran_Engineering_Bay ;	
    	}
    	




}
	
//	
//	public void nextTech(Player self, Game game, List<Unit> Buildings)
//{
//		
//
//    	
//
//
//}


	
}

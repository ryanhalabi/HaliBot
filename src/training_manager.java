import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class training_manager {

	
	
	
	
	public void trainNext(m_list listM, m_economy econM,  Player self, Game game) 
	{
	
		for ( Unit X : listM.Production)
		{
			 
            if (  ! X.isTraining() )
            {
            	

                //COMMAND CENTER
                if ( (X.getType() == UnitType.Terran_Command_Center) 
                		&& ( econM.availableMin  >= UnitType.Terran_SCV.mineralPrice() ) 
                		&&( self.completedUnitCount(UnitType.Terran_Command_Center)*self.completedUnitCount(UnitType.Terran_SCV) <24 ) ) 
                {
                	X.train(UnitType.Terran_SCV);
                	econM.updateAvailableResources(self);
                }

                //BARRACKS
                else if (X.getType() == UnitType.Terran_Barracks)  
                {
                	
	                
	                if (  (econM.availableMin >= UnitType.Terran_Medic.mineralPrice()) 
	                		&& ( econM.availableGas >= UnitType.Terran_Medic.gasPrice()) 
	                		&& ( (float) self.completedUnitCount(UnitType.Terran_Marine)/ self.completedUnitCount(UnitType.Terran_Medic) > 5 ))
	                {
	                	X.train(UnitType.Terran_Medic);
	                	econM.updateAvailableResources(self);
	                }
	
	                //TRAIN FIREBAT
	                if (  ( econM.availableMin  >= UnitType.Terran_Firebat.mineralPrice()) 
	                		&& (econM.availableGas >= UnitType.Terran_Firebat.gasPrice()) 
	                		&& ( (float) self.completedUnitCount(UnitType.Terran_Marine)/ self.completedUnitCount(UnitType.Terran_Firebat) > 10 ))
	                {
	                	X.train(UnitType.Terran_Firebat);
	                	econM.updateAvailableResources(self);
	                }
	
	                //TRAIN MARINE
	                if (  ( econM.availableMin  >=  UnitType.Terran_Marine.mineralPrice() )  ) 
	                {
	                	X.train(UnitType.Terran_Marine);
	                	econM.updateAvailableResources(self);
	                }

                }
                
                
            }
			
		}
	
		
		
	}
	

}








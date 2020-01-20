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


public class m_economy {

	public Integer minFutureExpense = 0;
	public Integer availableMin = 0;
	public Integer gasFutureExpense = 0;
	public Integer availableGas = 0;
	
	
	
	

	//calculate real amount of money we have
	public void addExpense( UnitType X)
	{
		minFutureExpense = minFutureExpense + X.mineralPrice() ;
		gasFutureExpense = gasFutureExpense + X.gasPrice() ;
	}
	
	public void removeExpense( UnitType X)
	{
		minFutureExpense = minFutureExpense - X.mineralPrice() ;
		gasFutureExpense = gasFutureExpense - X.gasPrice() ;
	}

	public void updateAvailableResources( Player self)
	{
		availableMin = self.minerals() - minFutureExpense ;
		availableGas  = self.gas() - gasFutureExpense ;
	}
	


	//find free worker to construct
	public Unit findbuilder(  Set<Unit> Workers)
	{
		for (Unit myUnit : Workers) 
		{
			if ( (myUnit.getType() == UnitType.Terran_SCV) 
					&& ( !myUnit.isGatheringGas() ) 
					&& ( myUnit.isGatheringMinerals() ) 
					&& ( !myUnit.isConstructing()))
			{
			//	System.out.println( "heres a worker \n"); 
				return myUnit;
			}


		}
		return null; 
	}

	





	//gather gas
	public void gathergas(Player self, Game game,  m_list listM, m_build buildM )
{
		
		
		
		for ( bwta.BaseLocation B : listM.baseDataMap.keySet())
		{
			for ( Unit R:  listM.baseDataMap.get(B).refinery.keySet() )
			{
				if( R.isCompleted())
				{

					Unit W = null;
					
					
					if (  listM.baseDataMap.get(B).refinery.get(R).size() == 2)
					{ 
						if (listM.baseDataMap.get(B).miners.size() >0)
						{
							W = listM.baseDataMap.get(B).miners.get(0);
							W.stop();
							W.gather(R);
							listM.baseDataMap.get(B).gasWorkers.put(W, R); // add worker to the 	worker:refinery map
							listM.baseDataMap.get(B).refinery.get(R).add(W); // add worker to the 	refinery:worker list
							listM.baseDataMap.get(B).miners.remove(W);
						}
						
					}
					else if (  listM.baseDataMap.get(B).refinery.get(R).size() == 1)
					{ 
						for (int i = 1; i <= 2; i++)
						{
							if (listM.baseDataMap.get(B).miners.size() >0)
							{
								W = listM.baseDataMap.get(B).miners.get(0);
								W.stop();
								W.gather(R);
								listM.baseDataMap.get(B).gasWorkers.put(W, R); // add worker to the 	worker:refinery map
								listM.baseDataMap.get(B).refinery.get(R).add(W); // add worker to the 	refinery:worker list
								listM.baseDataMap.get(B).miners.remove(W);
							}							
						}
						
					}
					else if (  listM.baseDataMap.get(B).refinery.get(R).size() == 0)
					{ 
						for (int i = 1; i <= 3; i++)
						{
							if (listM.baseDataMap.get(B).miners.size() >0)
							{
								W = listM.baseDataMap.get(B).miners.get(0);
								W.stop();
								W.gather(R);
								listM.baseDataMap.get(B).gasWorkers.put(W, R); // add worker to the 	worker:refinery map
								listM.baseDataMap.get(B).refinery.get(R).add(W); // add worker to the 	refinery:worker list
								listM.baseDataMap.get(B).miners.remove(W);
							}							
						}
						
					}
				}
			}
				
		
			
			
		}
		
	

}
	
	

 
	//gather minerals
	public void gatherminerals(Game game, m_list listM)
	{
		//System.out.printf( "WORKER #" + Workers.size() + " \n");
		
		for ( bwta.BaseLocation B : listM.baseDataMap.keySet())
		{
			if ( B.getMinerals().size() >0 )
			{
				for ( Unit U : listM.baseDataMap.get(B).miners )
				{
					if ( !U.isGatheringMinerals() )  //change to !U.isGatheringMinerals()
					{
						U.gather(  B.getMinerals().get(0) );
					}
				}
				
			}
		}
		
		



	}
	
	
	
}

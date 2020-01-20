import java.util.ArrayList;
import java.util.List;

import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class m_build {

	public UnitType nextProduction = null;
	public UnitType nextSupply = null;
	public UnitType nextEconomic = null;
	public UnitType nextDefense = null;

	public UnitType nextTech = null;

	Color red = new Color(255, 0, 0);
	Color green = new Color(0, 255, 0);
	Color blue = new Color(0, 0, 255);
	Color white = new Color(255, 255, 255);

	// find build tile around location
	public TilePosition getBuildTile(UnitType buildingType,
			TilePosition aroundTile, Player self, Game game, bwta.BaseLocation B, m_list listM) {
		TilePosition ret = null;
		
		// IF A REFINERY
		double a = 10000;
		if (buildingType.isRefinery()) 
		{
			for (Unit n : game.getUnitsInRadius(B.getPosition(), 1000))
			{
				if ((n.getType() == UnitType.Resource_Vespene_Geyser)   &&   (n.getPosition().getDistance( self.getStartLocation().toPosition()) < a)  ) 
				{
					
					ret = n.getTilePosition();
					a = n.getPosition().getDistance( self.getStartLocation().toPosition());
				}
			}
			return ret;
		}

		// IF NOT A REFINERY
		int xc = aroundTile.getX();
		int yc = aroundTile.getY();

		TilePosition A;
		int height = 32 * buildingType.tileHeight();
		int width = 32 * buildingType.tileWidth();

		for (int i = 1; i < 20; i++) // check ith ring
		{
			for (int j = -i; j <= i; j++) // check top/left/right/bottom of ith
											// ring
			{

				A = new TilePosition(xc + j, yc + i); // top
				game.drawCircleMap(A.toPosition(), 8, green, true);
				if (BC(buildingType, A, aroundTile, height, width, game, B)) {
					return A;
				}

				A = new TilePosition(xc + j, yc - i); // bottom
				game.drawCircleMap(A.toPosition(), 8, green, true);
				if (BC(buildingType, A, aroundTile, height, width, game, B)) {
					return A;
				}

				A = new TilePosition(xc - i, yc + j); // left
				game.drawCircleMap(A.toPosition(), 8, green, true);
				if (BC(buildingType, A, aroundTile, height, width, game, B)) {
					return A;
				}

				A = new TilePosition(xc + i, yc + j); // right
				game.drawCircleMap(A.toPosition(), 8, green, true);
				if (BC(buildingType, A, aroundTile, height, width, game, B)) {
					return A;
				}
			}
		}

		game.printf("Unable to find suitable build position for "
				+ buildingType.toString());
		return ret;
	}

	// Checks nothing in way or too close and in same region
	public boolean BC(UnitType buildingType, TilePosition A, TilePosition aroundtile, int height, int width, Game game, bwta.BaseLocation B) {

		int l = 64;
		if ((bwta.BWTA.getRegion(A) == B.getRegion()) && game.canBuildHere(A, buildingType)) // is in same region?
		{
			if ((buildingType != UnitType.Terran_Missile_Turret)
					&& (buildingType != UnitType.Terran_Command_Center)
					&& (buildingType != UnitType.Terran_Refinery)) // if not
																	// cc/refin/turret
			{
				for (Unit u : game.getUnitsInRectangle(A.toPosition().getX() - l, A.toPosition().getY() - l, A.toPosition().getX() + width + l, A.toPosition().getY() + height + l)) 
				{
					if ((u.getType() == UnitType.Terran_Command_Center)	|| (u.getType() == UnitType.Terran_Refinery)
							|| (u.getType().isMineralField()) || (u.getType() == UnitType.Resource_Vespene_Geyser)) 
					{
						return false;
					}
				}
				return true;
			} 
			else 
			{
				return true;
			}

		}

		return false;
	}

	// given unit type, find place to build, build it.
	public void Build(Game game, Player self, UnitType UT, m_list listM, m_economy econM, m_scout scoutM, Unit builder) 
	{
		TilePosition A = null;
		// IF PRODUER FIND RIGHT SPOT
		String p = "nonproduction";
		if (UT.canProduce()) 
		{
			p = "production";
		}

		// System.out.println("Check 1 \n");
		for ( bwta.BaseLocation X : listM.baseDataMap.keySet()) 
		{
			if (( listM.baseDataMap.get(X).baseOwner == 1) && (getBuildTile( UT, listM.baseDataMap.get(X).base_build_area.get(p).toTilePosition(), self, game, X , listM) != null)) 
			{
				A = getBuildTile(UT, listM.baseDataMap.get(X).base_build_area.get(p).toTilePosition(), self, game, X, listM);
				break;
			}
		}


		
		typeTileBuilderTimeStatus X = new typeTileBuilderTimeStatus();
//		System.out.printf("ordered " + UT + "\n");

		X.unitType = UT;
		if ( builder == null)
		{
			X.builder = listM.findClosestWorker( A.toPosition());
		}
		else
		{
			X.builder = builder;
		}
		X.tilePosition = A;
		X.status = "ordered";
		listM.orderedBuild.add(X);
		econM.addExpense( UT);
		econM.updateAvailableResources(self);

//    	System.out.println("just ordered a building");
	}
	
	



	public void nextProduction(Player self, Game game, m_list listM, m_economy econM) {

		// /////////////////
		// BARRACKS
		// //////////////////
		// if we have enough minerals and want a barracks
		if ((self.supplyTotal() > 20)
				&& ( econM.availableMin >= 150 * (1.5 * self
						.completedUnitCount(UnitType.Terran_Barracks) + 1))) {
			game.printf("BARRACKS");
			nextProduction = UnitType.Terran_Barracks;
		}

	}

	public void nextSupply(Player self, Game game, m_list listM, m_economy econM) {
		// /////////////////
		// DEPOT
		// //////////////////

		// System.out.println("hi " + (float)
		// self.supplyUsed()/self.supplyTotal() + "\n" );

		// if we're running out of supply and have enough minerals ...
		if (((float) self.supplyUsed() / self.supplyTotal() > .75)
				&& (econM.availableMin >= 100)) {
			game.printf("depot");
			nextSupply = UnitType.Terran_Supply_Depot;
		}

	}

	public void nextTech(Player self, Game game, m_list listM, m_economy econM) {

		boolean needRefinery = false;
		// Buildlings = list of owned buildings
		// OrderedBuildings = list of ordered buildings

		// /////////////////
		// ACADEMY
		// //////////////////

		if ((self.completedUnitCount(UnitType.Terran_Barracks)
				+ self.incompleteUnitCount(UnitType.Terran_Barracks) > 0)
				&& (self.completedUnitCount(UnitType.Terran_Refinery)
						+ self.incompleteUnitCount(UnitType.Terran_Refinery) > 0)
				&& (econM.availableMin  >= UnitType.Terran_Academy.mineralPrice())
				&& (econM.availableGas >= UnitType.Terran_Academy.gasPrice())
				&& (listM.Buildings.get(UnitType.Terran_Academy) == 0)) {
			game.printf("ACADEMY");
			nextTech = UnitType.Terran_Academy;
		}

		// /////////////////
		// ENGINEERING BAY
		// //////////////////

		else if ((self.completedUnitCount(UnitType.Terran_Barracks)
				+ self.incompleteUnitCount(UnitType.Terran_Barracks) > 0)
				&& (self.completedUnitCount(UnitType.Terran_Refinery)
						+ self.incompleteUnitCount(UnitType.Terran_Refinery) > 0)
				&& (self.completedUnitCount(UnitType.Terran_Academy)
						+ self.incompleteUnitCount(UnitType.Terran_Academy) > 0)
				&& (econM.availableMin  >= UnitType.Terran_Engineering_Bay
						.mineralPrice())
				&& (econM.availableGas >= UnitType.Terran_Engineering_Bay.gasPrice())
				&& (listM.Buildings.get(UnitType.Terran_Engineering_Bay) < 2)) {
			game.printf("Engineering_Bay");
			nextTech = UnitType.Terran_Engineering_Bay;
		}
	}

	public void nextEconomic(Player self, Game game, m_list listM, m_economy econM, m_scout scoutM) {

		boolean needRefinery = false;
		// Buildlings = list of owned buildings
		// OrderedBuildings = list of ordered buildings

		check_geysers: 
		for ( bwta.BaseLocation X : listM.baseDataMap.keySet()) 
		{
			if ( listM.baseDataMap.get(X).baseOwner == 1 &&  listM.baseDataMap.get(X).numgeysers >0 ) 
			{

						needRefinery = true;
						break check_geysers;

			}
		}

		// /////////////////
		// REFINERY
		// //////////////////
		// if we have enough minerals and want a refinery
		if ((self.supplyTotal() > 10) && (econM.availableMin >= 100) && needRefinery == true) 
		{
			game.printf("REFINERY");
			nextEconomic = UnitType.Terran_Refinery;
		}

	}

	
	
	
	
	
	
	
	
	public void updateProdQeue(m_list listM, m_economy econM, m_scout scoutM,  Player self, Game game) {
	
	
	//	 if no production scheduled, find out if needed
		if (nextProduction == null )
	   {
			nextProduction(self,  game, listM, econM);
			if (nextProduction != null )
			{
				Build(game, self, nextProduction ,  listM , econM, scoutM ,null) ;
			}
		}
		if (nextEconomic == null )
	   {
			nextEconomic(self,  game, listM, econM, scoutM);
			if (nextEconomic != null )
			{
				Build(game, self, nextEconomic ,  listM , econM, scoutM,null) ;
			}
		}
		
		if (nextSupply == null )
	   {
			nextSupply(self,  game, listM,econM);
			if (nextSupply != null )
			{
				Build(game, self, nextSupply ,  listM , econM, scoutM,null) ;
			}
		}
		
		
		
		if (nextTech == null )
	   {
			nextTech(self,  game, listM, econM);
			if (nextTech != null )
			{
				Build(game, self, nextTech ,  listM, econM, scoutM,null) ;
			}
		}

	}
	

}








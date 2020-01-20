#pragma once
#include <BWAPI.h>

// Remember not to use "Broodwar" in any global class constructor!

class UnitInfo
{
	public:

		int unit_id;
		int	health;
		int	shields;
		BWAPI::Unit unit;
		BWAPI::Position position;
		BWAPI::UnitType type;
		bool completed;

		virtual void update(BWAPI::Unit unit);

};



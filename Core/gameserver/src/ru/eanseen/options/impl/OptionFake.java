package ru.eanseen.options.impl;

import org.aeonbits.owner.Config;

/**
 User: Eanseen
 Date: 23.05.2015
 Time: 13:51
 */
@Config.HotReload
@Config.Sources({
		"file:./options/Fake.properties",
})
public interface OptionFake extends Config
{
	boolean enable();

	int count();

	boolean runEventBigHunt();
	boolean runEventGladsBattle();
	boolean runEventLastHero();
	boolean runEventTvT();

	int startLevel();

	boolean spawnNoOffLoc();

	boolean saveToDB();

	int minEnchant();

	int maxEnchant();

	boolean allowHair();

	boolean allowSkin();

	double chanceAttackPvP();

	double chanceAttackPk();

	double chanceToPk();

	int attackRadius();

	int loadDelay();

	double sitChance();

	double stayChance();

	int sitTime();

	int stayTime();

	double randomWalkInTownChance();

	double MAX_HP();

	double MAX_MP();

	double POWER_ATTACK();

	double MAGIC_ATTACK();

	double RUN_SPEED();

	double POWER_DEFENCE();

	double MAGIC_DEFENCE();

	double PCRITICAL_RATE();

	double MCRITICAL_RATE();

	double changeMode();
}
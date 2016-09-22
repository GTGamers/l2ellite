package ru.eanseen.extensions.impl.mods.fake;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.utils.Location;
import ru.eanseen.options.Options;
import l2s.gameserver.instancemanager.ReflectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 User: Eanseen
 Date: 25.05.2015
 Time: 9:37
 */
public class ChangeMode implements Runnable
{
	private static final Logger _log = LoggerFactory.getLogger(ChangeMode.class);
	private final FakeAI ai;
	private final Player player;

	public ChangeMode(FakeAI ai)
	{
		this.ai = ai;
		player = ai.getActor();
	}

	@Override
	public void run()
	{
		if(player.isSitting())
		{
			player.standUp();
		}

		ai.playerStop();

		if(Rnd.chance(Options.fake.changeMode()))
		{
			ai.setPrevAction(System.currentTimeMillis() + Rnd.get(1000, 30000));
			ai.teleportToTown();
		}
		else
		{
			ai.setPrevAction(System.currentTimeMillis() + Rnd.get(1000, 30000));
			ai.setFakeActionType(FakeActionType.Farm);
			Location location = null;
			boolean notInWater = false;
			while(location == null && !notInWater)
			{
				location = GameObjectsStorage.getRandomMonsterByLevel(player.getLevel()).getLoc();
				if(location != null /*&& !(location.getX() == 0 && location.getY() == 0)*/)
				{
					location = Location.findPointToStay(location, 250, 1000, 0);
					/*if(location.getX() == 0 && location.getY() == 0)
					{
						location = null;
						continue;
					}*/
					notInWater = !World.isWater(location, ReflectionManager.DEFAULT);
				}
			}
			if(location != null)
			{
				if(location.getX() == 0 && location.getY() == 0)
					_log.info("ChangeMode: location: x=" + location.getX() + ", y=" + location.getY() + ", z=" + location.getZ());
				player.teleToLocation(location);
			}
			ThreadPoolManager.getInstance().schedule(this, Rnd.get(5 * 60 * 1000, 30 * 60 * 1000));
		}
	}
}
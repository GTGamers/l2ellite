package ru.eanseen.extensions.impl.mods.fake;

import l2s.commons.util.Rnd;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.ai.PlayerAI;
import l2s.gameserver.geodata.GeoEngine;
import l2s.gameserver.handler.items.IItemHandler;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Skill;
import l2s.gameserver.model.World;
import l2s.gameserver.model.base.RestartType;
import l2s.gameserver.model.instances.MonsterInstance;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.items.Inventory;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.c2s.RequestRestartPoint;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Location;
import ru.eanseen.options.Options;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 Eanseen
 06.10.2015
 */
public class FakeAI extends PlayerAI implements Runnable
{
	private Player player;

	public FakeAI(Player actor)
	{
		super(actor);
		player = actor;
		prevAction = System.currentTimeMillis() + Rnd.get(1000, 60 * 1000);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 500, 500);
		ThreadPoolManager.getInstance().schedule(new ChangeMode(this), Rnd.get(60000, 300000));
	}

	private ScheduledFuture<?> taskDead;
	private long prevAction = 0;
	private FakeActionType fakeActionType = FakeActionType.Town;

	int[][] startPath1 = {
			{
					-114376,
					258456,
					-1192
			},
			{
					-114376,
					257624,
					-1192
			},
			{
					-114360,
					257384,
					-1136
			},
	};
	int[][] startPath2_1 = {
			{
					-114824,
					256952,
					-1136
			},
			{
					-114744,
					256728,
					-1232
			},
			{
					-114376,
					256504,
					-1280
			},
			{
					-114376,
					255464,
					-1520
			},
	};
	int[][] startPath2_2 = {
			{
					-113928,
					256968,
					-1136
			},
			{
					-113960,
					256744,
					-1224
			},
			{
					-114376,
					256504,
					-1280
			},
			{
					-114376,
					255464,
					-1520
			},
	};

	int pathNumber = -1;
	int pathType;

	@Override
	public void runImpl()
	{
		// Обезоружили ((( Случается хуйня в жизни, одеваем пуху назад
		if(player.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND) == 0 && Rnd.chance(20))
		{
			for(ItemInstance item : player.getInventory().getItems())
			{
				if(item.isWeapon())
				{
					player.getInventory().equipItem(item);
					break;
				}
			}
		}

		if(player.isDead() && taskDead == null)
		{
			taskDead = ThreadPoolManager.getInstance().schedule(() -> {
				prevAction = System.currentTimeMillis() + Rnd.get(1000, 30000);
				teleportToTown();
				ThreadPoolManager.getInstance().schedule(() -> {
					player.doRevive();
					if(taskDead != null)
					{
						taskDead.cancel(true);
						taskDead = null;
					}
				}, 850);
			}, Rnd.get(2000, 15000));
			return;
		}

		player.setCurrentMp(player.getMaxMp());
		if(!player.isInPvPEvent() && !player.isStunned() && !player.isParalyzed() && !player.isSleeping() && !player.isAfraid() && !player.isAlikeDead() && !player.isFlyUp() && !player.isKnockBacked())
		{
			if(player.getCurrentHpRatio() < 0.66 && player.getLevel() >= 40)
			{
				ItemInstance item = ItemFunctions.createItem(1540);
				item.setOwnerId(player.getObjectId());
				IItemHandler handler = item.getTemplate().getHandler();
				if(handler != null)
					handler.useItem(player, item, false);
			}

			if(player.getCurrentCpRatio() < 0.66 && player.getLevel() >= 40)
			{
				ItemInstance item = ItemFunctions.createItem(5592);
				item.setOwnerId(player.getObjectId());
				IItemHandler handler = item.getTemplate().getHandler();
				if(handler != null)
					handler.useItem(player, item, false);
			}
		}

		if(player.isMoving)
		{
			return;
		}

		switch(fakeActionType)
		{
			case None:
				Location location = null;
				switch(pathType)
				{
					case 0:
						if(System.currentTimeMillis() < prevAction)
						{
							return;
						}
						pathNumber++;
						if(pathNumber < startPath1.length)
						{
							location = new Location(startPath1[pathNumber]);
						}
						else
						{
							pathType = Rnd.get(1, 2);
							pathNumber = -1;
							prevAction = System.currentTimeMillis() + Rnd.get(5000, 20 * 1000);
						}
						break;
					case 1:
						if(System.currentTimeMillis() < prevAction)
						{
							return;
						}
						pathNumber++;
						if(pathNumber < startPath2_1.length)
						{
							location = new Location(startPath2_1[pathNumber]);
						}
						else
						{
							setFakeActionType(FakeActionType.Town);
						}
						break;
					case 2:
						if(System.currentTimeMillis() < prevAction)
						{
							return;
						}
						pathNumber++;
						if(pathNumber < startPath2_2.length)
						{
							location = new Location(startPath2_2[pathNumber]);
						}
						else
						{
							setFakeActionType(FakeActionType.Town);
						}
						break;
				}
				if(location != null)
				{
					player.moveToLocation(Location.findAroundPosition(player, location, 0, 200), 0, true);
				}
				break;
			case Town:
			{
				if(System.currentTimeMillis() < prevAction)
				{
					return;
				}
				prevAction = System.currentTimeMillis() + Rnd.get(1000, 30 * 1000);
				if(player.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE || player.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE || player.getAI().getIntention() == CtrlIntention.AI_INTENTION_REST)
				{
					if(player.isSitting())
					{
						player.standUp();
					}
					else if(Rnd.chance(Options.fake.sitChance())) // посидим?
					{
						player.sitDown(null);
						prevAction = System.currentTimeMillis() + Rnd.get(30 * 1000, Options.fake.sitTime() * 60 * 1000);
						return;
					}
					if(Rnd.chance(Options.fake.stayChance())) // постоим в ступоре?
					{
						prevAction = System.currentTimeMillis() + Rnd.get(30 * 1000, Options.fake.stayTime() * 60 * 1000);
						return;
					}
					if(Rnd.chance(Options.fake.randomWalkInTownChance())) // просто прогуляемся по городу
					{
						boolean inPeace = false;
						Location loc = null;
						int i = 10;
						while(!inPeace && i > 0)
						{
							loc = Location.findPointToStay(player, player.getLoc(), 500, 1500);
							inPeace = player.isInZonePeace() && !player.isInWater();
							i--;
						}
						if(inPeace)
						{
							player.moveToLocation(loc, 0, true);
							return;
						}
					}
					//boolean inPeace = false;
					int i = 10;
					while(/*!inPeace &&*/ i > 0)
					{
						NpcInstance npc = World.getAroundNpcRandom(player, 2000, 250);
						if(npc != null)
						{
							//inPeace = npc.isInZonePeace() /*&& !npc.isInWater()*/;
							//if(inPeace)
							//{
								player.moveToLocation(npc.getLoc(), 0, true);
								return;
							//}
						}
						i--;
					}
				}
			}
			break;
			case Farm:
			{
				Creature target = player.getTargetCreature();
				if(target != null)
				{
					fight(target);
					return;
				}

				switch(player.getAI().getIntention())
				{
					case AI_INTENTION_ATTACK:

						if(target == null || target.isDead() || target.isInvisible())
						{
							playerStop();
						}
						//return;
				}

				List<Player> _aroundPlayers = World.getAroundPlayers(player, Options.fake.attackRadius(), 200);

				for(Player pvp : _aroundPlayers)
				{
					if(player != null && pvp != null && pvp.isVisible() && !pvp.isInvisible() && pvp.getPvpFlag() > 0 && !pvp.isAlikeDead() && Rnd.chance(Options.fake.chanceAttackPvP()) && GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), pvp.getX(), pvp.getY(), pvp.getZ(), player.getGeoIndex()) && player != null && pvp != null)
					{
						fight(pvp);
						return;
					}
				}

				for(Player pk : _aroundPlayers)
				{
					if(player != null && pk != null && pk.isVisible() && !pk.isInvisible() && pk.isPK() && !pk.isAlikeDead() && Rnd.chance(Options.fake.chanceAttackPk()) && GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), pk.getX(), pk.getY(), pk.getZ(), player.getGeoIndex()) && player != null && pk != null) //хз почему, но иногда к концу проверки чтото может быть NULL
					{
						fight(pk);
						return;
					}
				}

				if(Rnd.chance(Options.fake.chanceToPk()))
				{
					Player kill = World.getAroundRandomPlayer(player, Options.fake.attackRadius(), 200);
					if(player != null && kill != null && kill.isVisible() && !kill.isInvisible() && !kill.isAlikeDead() && GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), kill.getX(), kill.getY(), kill.getZ(), player.getGeoIndex()) && player != null && kill != null)  //хз почему, но иногда к концу проверки чтото может быть NULL
					{
						fight(kill);
						return;
					}
				}

				for(int i = 0; i <= 2000; i += 250)
				{
					MonsterInstance monster = World.getAroundMonsterRandom(player, i, 250);
					if(monster != null && !monster.isDead() && monster.getCurrentHp() >= 1 && GeoEngine.canMoveToCoord(player.getX(), player.getY(), player.getZ(), monster.getX(), monster.getY(), monster.getZ(), 0))
					{
						fight(monster);
						return;
					}
				}

				player.moveToLocation(Location.findPointToStay(player.getLoc(), 0, 2000, 0), 0, true);
			}
			break;
		}
	}

	public void playerStop()
	{
		player.setTarget(null);
		setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}

	public void teleportToTown()
	{
		setFakeActionType(FakeActionType.Town);
		player.teleToLocation(RequestRestartPoint.defaultLoc(RestartType.TO_VILLAGE, player), 0);
		ThreadPoolManager.getInstance().schedule(new ChangeMode(this), Rnd.get(30 * 1000, 60 * 1000));
	}

	public void fight(Creature target)
	{
		player.setTarget(target);

		Skill skill = null;

		int count = 10;

		while(count != 0)
		{
			count--;
			skill = player.getRandomSkill();
			if(skill != null)
			{
				break;
			}
		}

		if(skill != null)
		{
			setIntention(CtrlIntention.AI_INTENTION_CAST, skill, target);
		}
		else
		{
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
	}

	public void setFakeActionType(FakeActionType fakeActionType)
	{
		this.fakeActionType = fakeActionType;
	}

	public void setPrevAction(long prevAction)
	{
		this.prevAction = prevAction;
	}
}
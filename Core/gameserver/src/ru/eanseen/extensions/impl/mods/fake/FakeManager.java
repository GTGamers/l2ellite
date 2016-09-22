package ru.eanseen.extensions.impl.mods.fake;

import l2s.commons.dbutils.DbUtils;
import l2s.commons.util.Rnd;
import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.dao.CharacterSubclassDAO;
import l2s.gameserver.data.xml.holder.PlayerTemplateHolder;
import l2s.gameserver.database.DatabaseFactory;
import l2s.gameserver.idfactory.IdFactory;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.ShortCut;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.Experience;
import l2s.gameserver.model.base.NobleType;
import l2s.gameserver.model.base.Sex;
import l2s.gameserver.model.base.SubClassType;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.item.StartItem;
import l2s.gameserver.templates.player.PlayerTemplate;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Location;
import ru.eanseen.options.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;


/**
 Eanseen
 04.10.2015
 */
public class FakeManager
{
	private final Logger _log = LoggerFactory.getLogger(FakeManager.class);
	private static FakeManager ourInstance = new FakeManager();

	public static FakeManager getInstance()
	{
		return ourInstance;
	}

	private FakeManager()
	{
		if(Options.fake.enable())// && GameServerStartup.checkMod("Система эмуляции онлайна"))
		{
			ignorTemplates = new HashSet<>();
			ignorTemplates.add(14);//Колдун
			ignorTemplates.add(15);//Клерик
			ignorTemplates.add(28);//Последователь Стихий
			ignorTemplates.add(34);//Танцор Смерти
			ignorTemplates.add(41);//Последователь Тьмы
			load();
			// TODO FakeHook.getInstance();
		}
	}

	//------------------------------------------------------------------------------------------------------------------

	public static HashSet<Integer> ignorTemplates;
	public ArrayList<String> fakeNiks;
	protected int count = 0;
	private int[] templates = {
			0,
			//Воин
			10,
			//Мистик
			18,
			//Светлый Воин
			25,
			//Светлый Мистик
			31,
			//Темный Воин
			38,
			//Темный Мистик
			44,
			//Орк Боец
			49,
			//Орк Адепт
			53,
			//Подмастерье
	};

	//------------------------------------------------------------------------------------------------------------------

	private void load()
	{
		_log.info("Fakes: Start loading Fakes.");
		_log.info("Fakes: Load nicks.");
		fakeNiks = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet set = null;
		try
		{
			connection = DatabaseFactory.getInstance().getConnection();
			statement = connection.prepareStatement("SELECT nick FROM fake_nicks");
			set = statement.executeQuery();
			while(set.next())
			{
				fakeNiks.add(set.getString(1));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(connection, statement, set);
		}

		_log.info("Fakes: Nicks loaded.");
		_log.info("Fakes: Load existes fakes from DB.");
		try
		{
			connection = DatabaseFactory.getInstance().getConnection();
			statement = connection.prepareStatement("SELECT obj_Id FROM characters WHERE account_name=?");
			statement.setString(1, "fake");
			set = statement.executeQuery();
			while(set.next())
			{
				if(count < Options.fake.count())
				{
					final int id = set.getInt(1);

					if(GameObjectsStorage.getPlayer(id) != null)
					{
						continue;
					}

					Player player = Player.restore(id, true);

					player.setOfflineMode(false);
					player.setIsOnline(true);
					player.spawnMe();
					player.setRunning();

					FakeAI fakeAI = (FakeAI) player.getAI();
					fakeAI.setFakeActionType(player.isInZonePeace() ? FakeActionType.Town : FakeActionType.Farm);

					if(Options.fake.saveToDB())
						player.startAutoSaveTask();

					count++;
				}
			}
			_log.info("Fakes: Existes fakes from DB loaded.");
			_log.info("Fakes: Load new fakes.");
			for(; count < Options.fake.count(); count++)
			{
				ClassId class_id = ClassId.VALUES[templates[Rnd.get(templates.length)]];
				PlayerTemplate template = PlayerTemplateHolder.getInstance().getPlayerTemplate(class_id.getRace(), class_id, Sex.VALUES[Rnd.chance(50) ? 0 : 1]);
				Player player = new Player(IdFactory.getInstance().getNextId(), template, "fake");

				String name = fakeNiks.get(Rnd.get(fakeNiks.size()));

				while(CharacterDAO.getInstance().getObjectIdByName(name) != 0)
				{
					name = fakeNiks.get(Rnd.get(fakeNiks.size()));
				}

				player.setName(name);
				player.setTitle("");
				player.setHairStyle(0);
				player.setHairColor(0);
				player.setFace(0);
				player.setCreateTime(System.currentTimeMillis());

				if(!CharacterDAO.getInstance().insert(player))
				{
					return;
				}

				int level = Config.STARTING_LVL;
				double hp = class_id.getBaseHp(level);
				double mp = class_id.getBaseMp(level);
				double cp = class_id.getBaseCp(level);
				long exp = Experience.getExpForLevel(level);
				long sp = Config.STARTING_SP;
				boolean active = true;
				SubClassType type = SubClassType.BASE_CLASS;

				if(!CharacterSubclassDAO.getInstance().insert(player.getObjectId(), class_id.getId(), class_id.getId(), exp, sp, hp, mp, cp, hp, mp, cp, level, active, type, 0, 0, 140000, 0, 0))
				{
					return;
				}

				player.getSubClassList().restore();
				player.setLoc(Location.findAroundPosition(player, template.getStartLocation(), 0, 750));

				if(Config.NEW_CHAR_IS_NOBLE)
				{
					player.setNobleType(NobleType.NORMAL);
				}

				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
				player.setCurrentCp(0); // retail

				for(StartItem startItem : template.getStartItems())
				{
					ItemInstance item = ItemFunctions.createItem(startItem.getItemId());
					if(startItem.getEnchantLevel() > 0)
					{
						item.setEnchantLevel(startItem.getEnchantLevel());
					}

					long _count = startItem.getCount();
					if(item.isStackable())
					{
						item.setCount(_count);
						player.getInventory().addItem(item);
					}
					else
					{
						for(long n = 0; n < _count; n++)
						{
							item = ItemFunctions.createItem(startItem.getItemId());
							if(startItem.getEnchantLevel() > 0)
							{
								item.setEnchantLevel(startItem.getEnchantLevel());
							}
							player.getInventory().addItem(item);
						}
						if(item.isEquipable() && startItem.isEquiped())
						{
							player.getInventory().equipItem(item);
						}
					}

					if(item.getItemId() == 5588) // tutorial book
					{
						player.registerShortCut(new ShortCut(11, 0, ShortCut.TYPE_ITEM, item.getObjectId(), -1, 1));
					}
				}

				player.rewardSkills(false, false, false);

				if(player.getSkillLevel(1001) > 0) // Soul Cry
				{
					player.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1001, 1, 1));
				}
				if(player.getSkillLevel(1177) > 0) // Wind Strike
				{
					player.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 1177, 1, 1));
				}
				if(player.getSkillLevel(1216) > 0) // Self Heal
				{
					player.registerShortCut(new ShortCut(9, 0, ShortCut.TYPE_SKILL, 1216, 1, 1));
				}
				if(player.getSkillLevel(30001) > 0) // 하이드로 어택
				{
					player.registerShortCut(new ShortCut(1, 0, ShortCut.TYPE_SKILL, 30001, 1, 1));
				}

				// add attack, take, sit shortcut
				player.registerShortCut(new ShortCut(0, 0, ShortCut.TYPE_ACTION, 2, -1, 1));
				player.registerShortCut(new ShortCut(3, 0, ShortCut.TYPE_ACTION, 5, -1, 1));
				player.registerShortCut(new ShortCut(4, 0, ShortCut.TYPE_ACTION, 4, -1, 1));
				player.registerShortCut(new ShortCut(10, 0, ShortCut.TYPE_ACTION, 0, -1, 1));
				// понял как на панельке отобразить. нц софт 10-11 панели сделали(by VISTALL)
				// fly transform
				player.registerShortCut(new ShortCut(0, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 911, 1, 1));
				player.registerShortCut(new ShortCut(3, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 884, 1, 1));
				player.registerShortCut(new ShortCut(4, ShortCut.PAGE_FLY_TRANSFORM, ShortCut.TYPE_SKILL, 885, 1, 1));
				// air ship
				player.registerShortCut(new ShortCut(0, ShortCut.PAGE_AIRSHIP, ShortCut.TYPE_ACTION, 70, 0, 1));

				player.checkLevelUpReward();

				player.setOnlineStatus(true);

				player.store(false);
				player.getInventory().store();
				player.deleteMe();

				player = Player.restore(player.getObjectId(), true);

				player.setOfflineMode(false);
				player.setIsOnline(true);
				player.spawnMe();
				player.setRunning();

				FakeAI fakeAI = (FakeAI) player.getAI();
				fakeAI.setFakeActionType(FakeActionType.None);
				if(Options.fake.saveToDB())
					player.startAutoSaveTask();
			}
			_log.info("Fakes: New fakes loaded.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(connection, statement, set);
		}
		_log.info("Fakes Loaded:" + count);
	}
}

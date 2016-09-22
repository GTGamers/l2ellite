package ru.eanseen.extensions.impl.mods.fake;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.instancemanager.MapRegionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.mapregion.DomainArea;
import l2s.gameserver.templates.mapregion.RestartArea;
import l2s.gameserver.templates.mapregion.RestartPoint;
import l2s.gameserver.utils.Location;
import ru.eanseen.options.Options;

import java.util.ArrayList;

/**
 Eanseen
 16.08.2015
 */
public class FakeUtil
{
	private static FakeUtil ourInstance = new FakeUtil();

	public static FakeUtil getInstance()
	{
		return ourInstance;
	}

	private FakeUtil()
	{
	}

	public void setProf(Player player)
	{
		ClassId classId = player.getClassId();
		int jobLevel = classId.getClassLevel().ordinal();
		int level = player.getLevel();
		while((level >= 20 && jobLevel == 0) || (level >= 40 && jobLevel == 1) || (level >= 76 && jobLevel == 2) || (level >= 85 && jobLevel == 3))
		{
			ArrayList<Integer> profs = new ArrayList<>();
			for(ClassId cid : ClassId.values())
			{
				if(FakeManager.ignorTemplates.contains(cid.getId()))
				{
					continue;
				}
				if(cid.getId() > 136 && cid.getId() < 148)
				{
					continue;
				}
				if(cid.childOf(classId) && cid.getClassLevel().ordinal() == classId.getClassLevel().ordinal() + 1)
				{
					profs.add(cid.getId());
				}
			}

			int id = profs.get(Rnd.get(0, profs.size() - 1));
			classId = ClassId.values()[id];
			changeClass(player, classId);
			jobLevel = classId.getClassLevel().ordinal();
		}
	}

	private void changeClass(Player player, ClassId classId)
	{
		player.setClassId(classId.getId(), true);
		player.rewardSkills(true);
		player.broadcastUserInfo(true);
		player.store(true);
	}

	public void addAndEquip(Player player, int itemId)
	{
		ItemTemplate item = ItemHolder.getInstance().getTemplate(itemId);
		if(item == null)
		{
			System.out.println("null item " + itemId);
			return;
		}

		ItemInstance itemInstance = player.getInventory().addItem(itemId, 1);
		if(itemInstance.canBeEnchanted() && itemInstance.getGrade() != ItemGrade.NONE)
		{
			itemInstance.setEnchantLevel(Rnd.get(Options.fake.minEnchant(), Options.fake.maxEnchant()));
		}
		if(player.getInventory().getPaperdollItemId(itemInstance.getEquipSlot()) != 0)
		{
			player.getInventory().unEquipItemInBodySlot(itemInstance.getEquipSlot());
		}
		player.getInventory().equipItem(itemInstance);
	}
}
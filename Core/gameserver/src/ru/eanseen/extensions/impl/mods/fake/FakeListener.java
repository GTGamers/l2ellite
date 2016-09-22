package ru.eanseen.extensions.impl.mods.fake;

import l2s.gameserver.listener.actor.player.OnLevelChangeListener;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.listener.CharListenerList;
import l2s.gameserver.model.items.ItemInstance;
import ru.eanseen.extensions.Extension;
import ru.eanseen.extensions.impl.mods.fake.items.FakeTableItems;
import ru.eanseen.extensions.impl.mods.fake.items.TypeItems;

/**
 Eanseen
 14.10.2015
 */
@Extension
public class FakeListener implements OnLevelChangeListener
{
	public FakeListener()
	{
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onLevelChange(Player player, int oldLvl, int newLvl)
	{
		if(player.isFake())
		{
			FakeUtil.getInstance().setProf(player);
			player.rewardSkills(true, true, true);
			player.refreshExpertisePenalty();
			for(ItemInstance item : player.getInventory().getItems())
			{
				player.getInventory().removeItem(item);
			}
			for(int itemId : FakeTableItems.getInstance().getRandomItems(player, TypeItems.Accessory))
			{
				FakeUtil.getInstance().addAndEquip(player, itemId);
			}
			for(int itemId : FakeTableItems.getInstance().getRandomItems(player, TypeItems.Armor))
			{
				FakeUtil.getInstance().addAndEquip(player, itemId);
			}
			for(int itemId : FakeTableItems.getInstance().getRandomItems(player, TypeItems.Weapon))
			{
				FakeUtil.getInstance().addAndEquip(player, itemId);
			}
		}
	}
}
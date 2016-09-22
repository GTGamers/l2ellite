package ru.eanseen.extensions.impl.mods.communityboard.impl.services;

import l2s.gameserver.model.Player;
import ru.eanseen.utils.Elements;

/**
 Eanseen
 26.09.2015
 */
public abstract class ServiceAbstract
{
	public abstract boolean isEnable();
	public abstract String getName();
	public abstract String show(Player player);
	public abstract void execute(Player player, String[] val);

	public boolean checkPay(Player player, String[] price)
	{
		for(String s : price)
		{
			int itemId = Integer.parseInt(s.split(",")[0]);
			long count = Long.parseLong(s.split(",")[1]);

			if(player.getInventory().getCountOf(itemId) < count)
			{
				return false;
			}
		}

		for(String s : price)
		{
			int itemId = Integer.parseInt(s.split(",")[0]);
			long count = Long.parseLong(s.split(",")[1]);
			player.getInventory().destroyItemByItemId(itemId, count);
		}

		return true;
	}

	public String getHtm(Player player, String content, String[] price, String buttons)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(content);

		sb.append("<br>");

		if(price.length != 0)
		{
			sb.append("<font color=00B2EE>");
			sb.append("Цена: ");
			sb.append("</font>");

			for(String s : price)
			{
				int itemId = Integer.parseInt(s.split(",")[0]);
				long count = Long.parseLong(s.split(",")[1]);

				sb.append("<br1>");
				if(player.getInventory().getCountOf(itemId) >= count)
				{
					sb.append("<font color=00FF00>");
					sb.append(Elements.getItemName(itemId)).append(" - ").append(count).append("шт");
					sb.append("</font>");
				}
				else if(player.getInventory().getCountOf(itemId) < count)
				{
					sb.append("<font color=FFFF00>");
					sb.append(count).append(" ").append(Elements.getItemName(itemId));
					sb.append("</font>");
					sb.append("&nbsp;");
					sb.append("<font color=FF0000>");
					sb.append("(Не хватает - ").append(count).append(" ").append(Elements.getItemName(itemId)).append(")");
					sb.append("</font>");
				}
			}

			sb.append("<br>");
		}

		sb.append(buttons);

		return sb.toString();
	}
}
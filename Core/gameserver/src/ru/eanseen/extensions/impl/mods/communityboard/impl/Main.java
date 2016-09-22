package ru.eanseen.extensions.impl.mods.communityboard.impl;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.data.xml.holder.MultiSellHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.utils.HtmlUtils;
import ru.eanseen.extensions.impl.mods.communityboard.CommunityBoardAbstract;
import ru.eanseen.options.Options;

import java.io.File;
import java.util.ArrayList;

/**
 Eanseen
 23.09.2015
 */
public class Main extends CommunityBoardAbstract
{
	private ArrayList<Integer> allowShops = new ArrayList<>();

	public Main()
	{
		for(File file : new File("custom/multisell/communityboard").listFiles())
		{
			int allowShop = Integer.parseInt(file.getName().substring(0, file.getName().indexOf(".")));
			allowShops.add(allowShop);
		}
	}

	@Override
	protected void execute(Player player, String var, String[] val)
	{
		if(var.isEmpty())
		{
			String htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "main.htm", player);
			htm = htm.replace("<?player_name?>", String.valueOf(player.getName()));
			htm = htm.replace("<?player_class?>", String.valueOf(HtmlUtils.htmlClassName(player.getClassId().getId())));
			htm = htm.replace("<?player_level?>", String.valueOf(player.getLevel()));
			htm = htm.replace("<?player_clan?>", String.valueOf(player.getClan() != null ? player.getClan().getName() : "Нет"));
			htm = htm.replace("<?player_noobless?>", String.valueOf(player.isNoble() ? "Есть" : "Нет"));

			int playedTime = player.getOnlineTime() / 60;
			int minutes = playedTime % 60;
			int hours = ((playedTime - minutes) / 60) % 24;
			int days = (((playedTime - minutes) / 60) - hours) / 24;

			htm = htm.replace("<?online_time?>", days + " д. " + hours + " ч. " + minutes + " м.");
			htm = htm.replace("<?player_ip?>", String.valueOf(player.getIP()));
			send(htm, player, true);
		}
		else
		{
			switch(var)
			{
				case "shop":
				{
					String htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "shop.htm", player);
					send(htm, player, true);
					if(val.length != 0)
					{
						int allowShop = Integer.parseInt(val[0]);
						if(allowShops.contains(allowShop))
						{
							MultiSellHolder.getInstance().SeparateAndSend(allowShop, player, 0);
						}
					}
				}
				break;
				case "events":
				{
					String htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "events.htm", player);
					send(htm, player, true);
					switch(val[0])
					{
						case "gvg":
						{
							htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "gvg.htm", player);
							send(htm, player, true);
						}
						break;
						case "lh":
						{
							htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "lh.htm", player);
							send(htm, player, true);
						}
						break;
						case "tvt":
						{
							htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "tvt.htm", player);
							send(htm, player, true);
						}
						break;
						case "ctf":
						{
							htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "ctf.htm", player);
							send(htm, player, true);
						}
						break;
					}
				}
				break;
			}
		}
	}
}
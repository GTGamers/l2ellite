package ru.eanseen.extensions.impl.mods.communityboard.impl;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import ru.eanseen.extensions.impl.mods.communityboard.CommunityBoardAbstract;
import ru.eanseen.extensions.impl.mods.communityboard.impl.services.ServiceAbstract;
import ru.eanseen.options.Options;
import ru.eanseen.utils.Elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 Eanseen
 26.09.2015
 */
public class Services extends CommunityBoardAbstract
{
	private static Map<String, ServiceAbstract> services = new HashMap<>();

	public static void addService(ServiceAbstract service)
	{
		services.put(service.getClass().getSimpleName(), service);
	}

	@Override
	protected void execute(Player player, String var, String[] val)
	{
		String htm = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "services.htm", player);
		htm = htm.replace("%services%", getMenu());
		if(var.isEmpty())
		{
			htm = htm.replace("%content%", "Выберите сервис");
			send(htm, player, true);
		}
		else
		{
			ServiceAbstract service = services.get(var);
			if(service != null)
			{
				if(val[0].equals("execute"))
				{
					service.execute(player, Arrays.copyOfRange(val, 1, val.length));
				}
				htm = htm.replace("%content%", service.show(player));
				send(htm, player, true);
			}
		}
	}

	private String getMenu()
	{
		StringBuilder sb = new StringBuilder();
		for(ServiceAbstract service : services.values())
		{
			if(!service.isEnable())
			{
				continue;
			}
			sb.append(Elements.button(service.getName(), "_bbsservices;" + service.getClass().getSimpleName() + ":show", 150, 30));
			sb.append("<br1/>");
		}
		return sb.toString();
	}
}
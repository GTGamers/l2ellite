package ru.eanseen.extensions.impl.mods.communityboard;

import l2s.gameserver.data.htm.HtmCache;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ShowBoardPacket;
import ru.eanseen.options.Options;

/**
 Eanseen
 23.09.2015
 */
public abstract class CommunityBoardAbstract
{
	protected abstract void execute(Player player, String var, String[] val);

	protected void send(String htm, Player player, boolean useTemplate)
	{
		if(htm == null)
		{
			return;
		}

		if(useTemplate)
		{
			String template = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "template.htm", player);
			String menu = HtmCache.getInstance().getNotNull(Options.communityBoard.folder() + "menu.htm", player);
			htm = template.replace("%menu%", menu).replace("%content%", htm);
		}

		htm = htm.replaceAll("\t", "");
		htm = htm.replaceAll("\n", "");
		htm = htm.replaceAll("[\\s]{2,}", " ");
		htm = htm.replaceAll(">[\\s]{1,}<", "><");

		ShowBoardPacket.separateAndSend(htm, player);
	}
}
package npc.model;

import l2s.gameserver.model.Player;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.scripts.Functions;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.Location;
import l2s.gameserver.utils.NpcUtils;

/**
 * @author pchayka
 */
public final class MaguenTraderInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	public MaguenTraderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("request_collector"))
		{
			if(Functions.getItemCount(player, 15487) > 0)
				showChatWindow(player, "default/32735-2.htm");
			else
				Functions.addItem(player, 15487, 1);
		}
		else if(command.equalsIgnoreCase("request_maguen"))
		{
			NpcUtils.spawnSingle(18839, Location.findPointToStay(getSpawnedLoc(), 40, 100, getGeoIndex()), getReflection()); // wild maguen
			showChatWindow(player, "default/32735-3.htm");
		}
		else
			super.onBypassFeedback(player, command);
	}
}
package npc.model;

import l2s.gameserver.instancemanager.SoDManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.templates.npc.NpcTemplate;
import l2s.gameserver.utils.ReflectionUtils;

/**
 * @author pchayka
 */

public final class AllenosInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	private static final int tiatIzId = 110;

	public AllenosInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if(!canBypassCheck(player, this))
			return;

		if(command.equalsIgnoreCase("enter_seed"))
		{
			// Время открытого SoD прошло
			if(SoDManager.isAttackStage())
			{
				Reflection r = player.getActiveReflection(tiatIzId);
				if(r != null)
				{
					if(player.canReenterInstance(tiatIzId))
						player.teleToLocation(r.getTeleportLoc(), r);
				}
				else if(player.canEnterInstance(tiatIzId))
				{
					ReflectionUtils.enterReflection(player, tiatIzId);
				}
			}
			else
				SoDManager.teleportIntoSeed(player);
		}
		else
			super.onBypassFeedback(player, command);
	}
}
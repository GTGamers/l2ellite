package l2s.gameserver.handler.admincommands.impl;

import java.util.ArrayList;
import java.util.List;

import l2s.gameserver.data.xml.holder.ResidenceHolder;
import l2s.gameserver.handler.admincommands.IAdminCommandHandler;
import l2s.gameserver.instancemanager.MapRegionManager;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.Zone;
import l2s.gameserver.model.entity.residence.Castle;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.templates.mapregion.DomainArea;
import l2s.gameserver.utils.ItemFunctions;

public class AdminZone implements IAdminCommandHandler
{
	private static enum Commands
	{
		admin_zone_check,
		admin_region,
		admin_pos,
		admin_vis_count,
		admin_domain,
		admin_loc
	}

	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;

		if(activeChar == null || !activeChar.getPlayerAccess().CanTeleport)
			return false;

		switch(command)
		{
			case admin_zone_check:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Zone list:");
				List<Zone> zones = new ArrayList<Zone>();
				World.getZones(zones, activeChar.getLoc(), activeChar.getReflection());
				for(Zone zone : zones)
					activeChar.sendMessage(zone.getType().toString() + ", name: " + zone.getName() + ", state: " + (zone.isActive() ? "active" : "not active") + ", inside: " + zone.checkIfInZone(activeChar) + "/" + zone.checkIfInZone(activeChar.getX(), activeChar.getY(), activeChar.getZ()));

				break;
			}
			case admin_region:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Objects list:");
				for(GameObject o : activeChar.getCurrentRegion())
					if(o != null)
						activeChar.sendMessage(o.toString());
				break;
			}
			case admin_vis_count:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Players count: " + World.getAroundPlayers(activeChar).size());
				break;
			}
			case admin_pos:
			{
				String pos = activeChar.getX() + ", " + activeChar.getY() + ", " + activeChar.getZ() + ", " + activeChar.getHeading() + " Geo [" + (activeChar.getX() - World.MAP_MIN_X >> 4) + ", " + (activeChar.getY() - World.MAP_MIN_Y >> 4) + "] Ref " + activeChar.getReflectionId();
				activeChar.sendMessage("Pos: " + pos);
				break;
			}
			case admin_domain:
			{
				DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, activeChar);
				Castle castle = domain != null ? ResidenceHolder.getInstance().getResidence(Castle.class, domain.getId()) : null;
				if(castle != null)
					activeChar.sendMessage("Domain: " + castle.getName());
				else
					activeChar.sendMessage("Domain: Unknown");
				break;
			}
			case admin_loc:
			{
				System.out.println("x=\"" + activeChar.getX() + "\" y=\"" + activeChar.getY() + "\" z=\"" + activeChar.getZ());
				activeChar.sendMessage("Point saved.");
				ItemInstance temp = ItemFunctions.createItem(1060);
				temp.dropMe(activeChar, activeChar.getLoc());
			}
		}
		return true;
	}

	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
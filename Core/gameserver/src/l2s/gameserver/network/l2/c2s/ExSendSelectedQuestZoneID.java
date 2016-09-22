package l2s.gameserver.network.l2.c2s;

import l2s.gameserver.model.Player;

/**
 * @author Bonux
**/
public class ExSendSelectedQuestZoneID extends L2GameClientPacket
{
	private int _zoneId;

	@Override
	protected void readImpl()
	{
		_zoneId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
			return;

		//activeChar.sendMessage("ExSendSelectedQuestZoneID: " + _zoneId);
	}
}
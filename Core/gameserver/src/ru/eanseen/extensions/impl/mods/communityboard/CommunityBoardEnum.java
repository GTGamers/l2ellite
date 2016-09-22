package ru.eanseen.extensions.impl.mods.communityboard;

import l2s.gameserver.model.Player;
import ru.eanseen.extensions.impl.mods.communityboard.impl.Main;
import ru.eanseen.extensions.impl.mods.communityboard.impl.Services;

/**
 Eanseen
 23.09.2015
 */
public enum CommunityBoardEnum
{
	_bbshome(new Main()),
	_bbsservices(new Services());

	private CommunityBoardAbstract boardAbstract;

	CommunityBoardEnum(CommunityBoardAbstract boardAbstract)
	{
		this.boardAbstract = boardAbstract;
	}

	public void execute(Player player, String var, String[] val)
	{
		boardAbstract.execute(player, var, val);
	}
}
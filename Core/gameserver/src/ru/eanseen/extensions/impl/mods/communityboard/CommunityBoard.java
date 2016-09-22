package ru.eanseen.extensions.impl.mods.communityboard;

import l2s.gameserver.model.Player;

import java.util.StringTokenizer;

/**
 Eanseen
 23.09.2015
 */
public class CommunityBoard
{
	private static CommunityBoard ourInstance = new CommunityBoard();

	public static CommunityBoard getInstance()
	{
		return ourInstance;
	}

	private CommunityBoard()
	{
	}

	public void parse(String command, Player player)
	{
		StringTokenizer st = new StringTokenizer(command, ";:");

		String type = st.nextToken();
		String var = "";
		String[] val = new String[0];

		try
		{
			switch(st.countTokens())
			{
				case 1:
					var = st.nextToken();
					break;
				case 2:
					var = st.nextToken();
					val = st.nextToken().trim().split(" ");
					break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			CommunityBoardEnum boardEnum = CommunityBoardEnum.valueOf(type);
			boardEnum.execute(player, var, val);
		}
		catch(Exception ignored)
		{
		}
	}
}
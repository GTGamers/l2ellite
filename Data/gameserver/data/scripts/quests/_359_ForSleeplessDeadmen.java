package quests;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _359_ForSleeplessDeadmen extends Quest implements ScriptFile
{
	//Variables
	private static final int DROP_RATE = 10;

	private static final int REQUIRED = 60; //how many items will be paid for a reward

	//Quest items
	private static final int REMAINS = 5869;

	//Rewards
	private static final int PhoenixEarrPart = 6341;
	private static final int MajEarrPart = 6342;
	private static final int PhoenixNeclPart = 6343;
	private static final int MajNeclPart = 6344;
	private static final int PhoenixRingPart = 6345;
	private static final int MajRingPart = 6346;

	private static final int DarkCryShieldPart = 5494;
	private static final int NightmareShieldPart = 5495;

	//NPCs
	private static final int ORVEN = 30857;

	//Mobs
	private static final int DOOMSERVANT = 21006;
	private static final int DOOMGUARD = 21007;
	private static final int DOOMARCHER = 21008;
	private static final int DOOMTROOPER = 21009;

	@Override
	public void onLoad()
	{
	}

	@Override
	public void onReload()
	{
	}

	@Override
	public void onShutdown()
	{
	}

	public _359_ForSleeplessDeadmen()
	{
		super(false);
		addStartNpc(ORVEN);

		addKillId(DOOMSERVANT);
		addKillId(DOOMGUARD);
		addKillId(DOOMARCHER);
		addKillId(DOOMTROOPER);

		addQuestItem(REMAINS);
		addLevelCheck(60, 67);
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30857-06.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30857-07.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
		}
		else if(event.equalsIgnoreCase("30857-08.htm"))
		{
			st.setCond(1);
			//Vibor nagradi
			int chance = Rnd.get(100);
			int item;
			if(chance <= 16)
				item = PhoenixNeclPart;
			else if(chance <= 33)
				item = PhoenixEarrPart;
			else if(chance <= 50)
				item = PhoenixRingPart;
			else if(chance <= 58)
				item = MajNeclPart;
			else if(chance <= 67)
				item = MajEarrPart;
			else if(chance <= 76)
				item = MajRingPart;
			else if(chance <= 84)
				item = DarkCryShieldPart;
			else
				item = NightmareShieldPart;
			st.giveItems(item, 4, true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		int cond = st.getCond();
		if(id == CREATED)
		{
			if(st.getPlayer().getLevel() < 60 || st.getPlayer().getLevel() > 67)
			{
				st.exitCurrentQuest(true);
				htmltext = "30857-01.htm";
			}
			else
				htmltext = "30857-02.htm";
		}
		else if(id == STARTED)
		{
			if(cond == 3)
				htmltext = "30857-03.htm";
			else if(cond == 2 && st.getQuestItemsCount(REMAINS) >= REQUIRED)
			{
				st.takeItems(REMAINS, REQUIRED);
				st.setCond(3);
				htmltext = "30857-04.htm";
			}
		}
		else
			htmltext = "30857-05.htm";
		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(REMAINS);
		if(count < REQUIRED && Rnd.chance(DROP_RATE))
		{
			st.giveItems(REMAINS, 1);
			if(count + 1 >= REQUIRED)
			{
				st.playSound(SOUND_MIDDLE);
				st.setCond(2);
			}
			else
				st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
}
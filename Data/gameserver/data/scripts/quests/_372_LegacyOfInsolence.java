package quests;

import java.util.HashMap;
import java.util.Map;

import l2s.commons.util.Rnd;
import l2s.gameserver.model.instances.NpcInstance;
import l2s.gameserver.model.quest.Quest;
import l2s.gameserver.model.quest.QuestState;
import l2s.gameserver.scripts.ScriptFile;

public class _372_LegacyOfInsolence extends Quest implements ScriptFile
{
	// NPCs
	private static int HOLLY = 30839;
	private static int WALDERAL = 30844;
	private static int DESMOND = 30855;
	private static int PATRIN = 30929;
	private static int CLAUDIA = 31001;
	// Mobs
	private static int Spirit = 23044;
	private static int Wings = 23045;
	private static int Terestian = 23047;
	private static int Beglec = 23053;
	//group 2
	private static int knight_dark = 23054;
	private static int shaman_dark = 23055;
	//group 3
	private static int rain1 = 23063;
	private static int rain2 = 23064;
	private static int rain3 = 23065;
	private static int rain4 = 23066;
	private static int rain5 = 23067;
	private static int rain6 = 23068;
	private static int rain7 = 23074;
	private static int rain8 = 23075;
	private static int rain9 = 23111;
	private static int rain10 = 23112;
	// Items
	private static int Ancient_Red_Papyrus = 5966;
	private static int Ancient_Blue_Papyrus = 5967;
	private static int Ancient_Black_Papyrus = 5968;
	private static int Ancient_White_Papyrus = 5969;

	private static int[] Revelation_of_the_Seals_Range = {
			5972,
			5978
	};
	private static int[] Ancient_Epic_Chapter_Range = {
			5979,
			5983
	};
	private static int[] Imperial_Genealogy_Range = {
			5984,
			5988
	};
	private static int[] Blueprint_Tower_of_Insolence_Range = {
			5989,
			6001
	};
	// Rewards
	private static int[] Reward_Dark_Crystal = {
			5368,
			5392,
			5426
	};
	private static int[] Reward_Tallum = {
			5370,
			5394,
			5428
	};
	private static int[] Reward_Nightmare = {
			5380,
			5404,
			5430
	};
	private static int[] Reward_Majestic = {
			5382,
			5406,
			5432
	};
	// Chances
	private static int Three_Recipes_Reward_Chance = 1;
	private static int Two_Recipes_Reward_Chance = 2;
	private static int Adena4k_Reward_Chance = 2;

	private final Map<Integer, int[]> DROPLIST = new HashMap<Integer, int[]>();

	public _372_LegacyOfInsolence()
	{
		super(PARTY_ALL);
		addStartNpc(WALDERAL);

		addTalkId(HOLLY);
		addTalkId(DESMOND);
		addTalkId(PATRIN);
		addTalkId(CLAUDIA);

		addKillId(Spirit);
		addKillId(Wings);
		addKillId(Terestian);
		addKillId(Beglec);
		addKillId(knight_dark);
		addKillId(shaman_dark);
		addKillId(rain1);
		addKillId(rain2);
		addKillId(rain3);
		addKillId(rain4);
		addKillId(rain5);
		addKillId(rain6);
		addKillId(rain7);
		addKillId(rain8);
		addKillId(rain9);
		addKillId(rain10);

		DROPLIST.put(Spirit, new int[]{
				Ancient_Red_Papyrus,
				35
		});
		DROPLIST.put(Wings, new int[]{
				Ancient_Red_Papyrus,
				40
		});
		DROPLIST.put(Terestian, new int[]{
				Ancient_Red_Papyrus,
				45
		});
		DROPLIST.put(Beglec, new int[]{
				Ancient_Red_Papyrus,
				40
		});
		DROPLIST.put(knight_dark, new int[]{
				Ancient_Black_Papyrus,
				25
		});
		DROPLIST.put(shaman_dark, new int[]{
				Ancient_Black_Papyrus,
				25
		});
		DROPLIST.put(rain1, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain2, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain3, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain4, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain5, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain6, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain7, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain8, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain9, new int[]{
				Ancient_White_Papyrus,
				25
		});
		DROPLIST.put(rain10, new int[]{
				Ancient_White_Papyrus,
				25
		});		
		addLevelCheck(57, 75);
	}

	private static void giveRecipe(QuestState st, int recipe_id)
	{
		st.giveItems(recipe_id, 1);
	}

	private static boolean check_and_reward(QuestState st, int[] items_range, int[] reward)
	{
		for(int item_id = items_range[0]; item_id <= items_range[1]; item_id++)
			if(st.getQuestItemsCount(item_id) < 1)
				return false;

		for(int item_id = items_range[0]; item_id <= items_range[1]; item_id++)
			st.takeItems(item_id, 1);

		if(Rnd.chance(Three_Recipes_Reward_Chance))
		{
			for(int reward_item_id : reward)
				giveRecipe(st, reward_item_id);
			st.playSound(SOUND_JACKPOT);
		}
		else if(Rnd.chance(Two_Recipes_Reward_Chance))
		{
			int ignore_reward_id = reward[Rnd.get(reward.length)];
			for(int reward_item_id : reward)
				if(reward_item_id != ignore_reward_id)
					giveRecipe(st, reward_item_id);
			st.playSound(SOUND_JACKPOT);
		}
		else if(Rnd.chance(Adena4k_Reward_Chance))
			st.giveItems(ADENA_ID, 4000, false);
		else
			giveRecipe(st, reward[Rnd.get(reward.length)]);

		return true;
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int _state = st.getState();
		if(_state == CREATED)
		{
			if(event.equalsIgnoreCase("30844-6.htm"))
			{
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
			}
			else if(event.equalsIgnoreCase("30844-9.htm"))
				st.setCond(2);
			else if(event.equalsIgnoreCase("30844-7.htm"))
			{
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == STARTED)
			if(event.equalsIgnoreCase("30839-exchange"))
				htmltext = check_and_reward(st, Imperial_Genealogy_Range, Reward_Dark_Crystal) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("30855-exchange"))
				htmltext = check_and_reward(st, Revelation_of_the_Seals_Range, Reward_Majestic) ? "30855-2.htm" : "30855-3.htm";
			else if(event.equalsIgnoreCase("30929-exchange"))
				htmltext = check_and_reward(st, Ancient_Epic_Chapter_Range, Reward_Tallum) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("31001-exchange"))
				htmltext = check_and_reward(st, Revelation_of_the_Seals_Range, Reward_Nightmare) ? "30839-2.htm" : "30839-3.htm";
			else if(event.equalsIgnoreCase("30844-DarkCrystal"))
				htmltext = check_and_reward(st, Blueprint_Tower_of_Insolence_Range, Reward_Dark_Crystal) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Tallum"))
				htmltext = check_and_reward(st, Blueprint_Tower_of_Insolence_Range, Reward_Tallum) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Nightmare"))
				htmltext = check_and_reward(st, Blueprint_Tower_of_Insolence_Range, Reward_Nightmare) ? "30844-11.htm" : "30844-12.htm";
			else if(event.equalsIgnoreCase("30844-Majestic"))
				htmltext = check_and_reward(st, Blueprint_Tower_of_Insolence_Range, Reward_Majestic) ? "30844-11.htm" : "30844-12.htm";

		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int _state = st.getState();
		int npcId = npc.getNpcId();

		if(_state == CREATED)
		{
			if(npcId != WALDERAL)
				return htmltext;
			if(st.getPlayer().getLevel() >= 57 && st.getPlayer().getLevel() <= 75)
				htmltext = "30844-4.htm";
			else
			{
				htmltext = "30844-5.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(_state == STARTED)
			htmltext = String.valueOf(npcId) + "-1.htm";

		return htmltext;
	}

	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		if(qs.getState() != STARTED)
			return null;

		int[] drop = DROPLIST.get(npc.getNpcId());
		if(drop == null)
			return null;

		qs.rollAndGive(drop[0], 1, drop[1]);
		return null;
	}

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
}
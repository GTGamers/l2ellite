package ru.eanseen.extensions.impl.mods.communityboard.impl.services.impl;

import l2s.gameserver.Config;
import l2s.gameserver.dao.AccountBonusDAO;
import l2s.gameserver.dao.PremiumAccountRatesHolder;
import l2s.gameserver.handler.bbs.CommunityBoardManager;
import l2s.gameserver.handler.bbs.ICommunityBoardHandler;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.Bonus;
import l2s.gameserver.network.authcomm.AuthServerCommunication;
import l2s.gameserver.network.authcomm.gs2as.BonusRequest;
import l2s.gameserver.network.l2.s2c.ExBR_PremiumStatePacket;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;
import l2s.gameserver.utils.HtmlUtils;
import l2s.gameserver.utils.ItemFunctions;
import l2s.gameserver.utils.Util;
import ru.eanseen.extensions.Extension;
import ru.eanseen.extensions.impl.mods.communityboard.impl.Services;
import ru.eanseen.extensions.impl.mods.communityboard.impl.services.ServiceAbstract;
import ru.eanseen.options.Options;
import ru.eanseen.utils.Elements;

/**
 Eanseen
 15.10.2015
 */
@Extension
public class Premium extends ServiceAbstract
{
	public Premium()
	{
		Services.addService(this);
	}

	@Override
	public boolean isEnable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return "Премиум";
	}

	@Override
	public String show(Player player)
	{
		StringBuilder sb = new StringBuilder();

		if(!player.hasBonus())
		{
			int premiumId = 1;
			StringBuilder prHtm = new StringBuilder();
			prHtm.append("<table width=250>" +
					"<tr>" +
					"<td width=30></td><td width=60><font color=\"LEVEL\">Схема:</font></td><td width=160><?scheme_name?></td>" +
					"</tr>" +
					/* <td width=40></td> */
					/*"<tr>" +
					"<td width=30></td><td width=60><font color=\"LEVEL\">Период:</font></td><td width=120><?period?></td><td width=40></td>" +
					"</tr>" +*/
					"<tr>" +
					"<td width=30></td><td width=60><font color=\"LEVEL\">Рейты:</font></td><td width=160><font color=63CDFF>x<?exp_rate?></font></td>" +
					"</tr>" +
					/* <td width=120>EXP:</td><td width=40> */
					/*"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>SP:</td><td width=40><font color=63CDFF>x<?sp_rate?></font></td>" +
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>Дроп Адены:</td><td width=40><font color=63CDFF>x<?adena_drop_rate?></font></td>" +
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>Дроп вещей:</td><td width=40><font color=63CDFF>x<?items_drop_rate?></font></td>" +
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>Спойл:</td><td width=40><font color=63CDFF>x<?spoil_rate?></font></td>" +
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>Дроп в квестах:</td><td width=40><font color=63CDFF>x<?quest_drop_rate?></font></td>" +
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60></td><td width=120>Награда квестов:</td><td width=40><font color=63CDFF>x<?quest_reward_rate?></font></td>" +
					"</tr>" +*/
					"<tr>" +
					"<td width=30></td><td width=60><font color=\"LEVEL\">Бонусы:</font></td><td width=160><?bonus?></td>" +
					/* <td width=120>Шанс Заточки:</td><td width=40><font color=63CDFF>+<?enchant_chance?>%</font> */
					"</tr>" +
					"<tr>" +
					"<td width=30></td><td width=60><font color=\"LEVEL\">Цена:</font></td><td width=160><font color=63CDFF><?fee_item_count?></font> x <?fee_item_name?></td>" +
					"</tr>" +
					"</table>");

			final PremiumAccountRatesHolder.PremiumInfo info = PremiumAccountRatesHolder.getPremiumByGroupId(premiumId);

			String infoBlock = prHtm.toString();
			infoBlock = infoBlock.replace("<?scheme_id?>", String.valueOf(info.getGroupNumber()));
			infoBlock = infoBlock.replace("<?scheme_name?>", player.isLangRus() ? info.getGroupNameRu() : info.getGroupNameEn());
			infoBlock = infoBlock.replace("<?period?>", info.getDays() + " " + (info.isHours() ? player.isLangRus() ? "часа(ов)" : "hour(s)" : player.isLangRus() ? "дня(ей)" : "day(s)"));
			infoBlock = infoBlock.replace("<?fee_item_name?>", HtmlUtils.htmlItemName(info.getItemId()));
			infoBlock = infoBlock.replace("<?fee_item_count?>", Util.formatAdena(info.getItemCount()));
			infoBlock = infoBlock.replace("<?exp_rate?>", doubleToString(info.getExp()));
			/*infoBlock = infoBlock.replace("<?sp_rate?>", doubleToString(info.getSp()));
			infoBlock = infoBlock.replace("<?adena_drop_rate?>", doubleToString(info.getAdena()));
			infoBlock = infoBlock.replace("<?items_drop_rate?>", doubleToString(info.getDrop()));
			infoBlock = infoBlock.replace("<?spoil_rate?>", doubleToString(info.getSpoil()));
			infoBlock = infoBlock.replace("<?quest_drop_rate?>", doubleToString(info.getQDrop()));
			infoBlock = infoBlock.replace("<?quest_reward_rate?>", doubleToString(info.getQReward()));*/
			//infoBlock = infoBlock.replace("<?enchant_chance?>", doubleToString(info.getEnchantAdd()));

			String bonus = info.getEnchantAdd() > 0 ? "Шанс Заточки: <font color=63CDFF>+"+doubleToString(info.getEnchantAdd())+"%</font>" : "нет";
			infoBlock = infoBlock.replace("<?bonus?>", bonus);

			sb.append(infoBlock);
			sb.append("<br>");

			if(player.getInventory().getCountOf(info._itemId) >= info._itemCount)
			{
				sb.append(Elements.button("Купить", "_bbsservices;" + getClass().getSimpleName() + ":execute "+premiumId, 200, 30));
			}
			else if(player.getInventory().getCountOf(info._itemId) < info._itemCount)
			{
				sb.append("<font color=FF0000>");
				sb.append("(Не хватает - ").append(info._itemCount).append(" ").append(Elements.getItemName(info._itemId)).append(")");
				sb.append("</font>");
			}
		}
		else
		{
			sb.append("Премиум уже активирован");
		}
		return sb.toString();
	}

	@Override
	public void execute(Player player, String[] val)
	{
		final PremiumAccountRatesHolder.PremiumInfo info = PremiumAccountRatesHolder.getPremiumByGroupId(Integer.parseInt(val[0]));
		if(info == null)
		{
			return;
		}
		if(ItemFunctions.removeItem(player, info.getItemId(), info.getItemCount(), true) == info.getItemCount())
		{
			int startTime = (int) (System.currentTimeMillis() / 1000);
			if(player.getNetConnection().getBonusType() >= 1.)
			{
				int endTime = player.getNetConnection().getBonusExpire();
				if(endTime >= System.currentTimeMillis() / 1000L)
				{
					startTime = endTime;
				}
			}

			int bonusExpire;
			if(info.isHours())
			{
				bonusExpire = startTime + info.getDays() * 60 * 60;
			}
			else
			{
				bonusExpire = startTime + info.getDays() * 24 * 60 * 60;
			}

			switch(Config.SERVICES_RATE_TYPE)
			{
				case Bonus.BONUS_GLOBAL_ON_AUTHSERVER:
					AuthServerCommunication.getInstance().sendPacket(new BonusRequest(player.getAccountName(), info.getGroupNumber(), bonusExpire));
					break;
				case Bonus.BONUS_GLOBAL_ON_GAMESERVER:
					AccountBonusDAO.getInstance().insert(player.getAccountName(), info.getGroupNumber(), bonusExpire);
					break;
			}

			player.getNetConnection().setBonus(info.getGroupNumber());
			player.getNetConnection().setBonusExpire(bonusExpire);

			player.stopBonusTask();
			player.startBonusTask();

			if(player.getParty() != null)
			{
				player.getParty().recalculatePartyData();
			}

			player.broadcastPacket(new MagicSkillUse(player, player, 23128, 1, 1, 0));
			player.sendPacket(new ExBR_PremiumStatePacket(player, true));
		}
	}


	private static String doubleToString(double value)
	{
		int intValue = (int) value;
		if(intValue == value)
		{
			return String.valueOf(intValue);
		}
		return String.valueOf(value);
	}
}

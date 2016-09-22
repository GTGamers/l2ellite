package ru.eanseen.extensions.impl.mods.communityboard.impl.services.impl;

import l2s.gameserver.Config;
import l2s.gameserver.dao.CharacterDAO;
import l2s.gameserver.data.xml.holder.FakePlayersHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.ExIsCharNameCreatable;
import l2s.gameserver.utils.Util;
import ru.eanseen.extensions.Extension;
import ru.eanseen.extensions.impl.mods.communityboard.impl.Services;
import ru.eanseen.extensions.impl.mods.communityboard.impl.services.ServiceAbstract;
import ru.eanseen.options.Options;
import ru.eanseen.utils.Elements;

/**
 Eanseen
 26.09.2015
 */
@Extension
public class ChangeNick extends ServiceAbstract
{
	public ChangeNick()
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
		return "Смена имени";
	}

	@Override
	public String show(Player player)
	{
		if(!isEnable())
		{
			return "Сервис выключен";
		}

		StringBuilder content = new StringBuilder();
		content.append("<font color=\"LEVEL\">Введите новое имя:</font>");
		content.append("<edit var=\"nick\" width=200><br>");

		StringBuilder buttons = new StringBuilder();
		buttons.append(Elements.button("Сменить имя", "_bbsservices;" + getClass().getSimpleName() + ":execute $nick", 200, 30));

		return getHtm(player, content.toString(), Options.communityBoard.ChangeNickPrice(), buttons.toString());
	}

	@Override
	public void execute(Player player, String[] val)
	{
		if(!isEnable())
		{
			return;
		}

		String name = val[0];

		if(name == null || name.isEmpty())
		{
			return;
		}
		else if(!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE))
		{
			player.sendHtmError("Неверный формат");
			return;
		}
		else if(CharacterDAO.getInstance().getObjectIdByName(name) > 0)
		{
			player.sendHtmError("Имя занято");
			return;
		}
		else if(FakePlayersHolder.getInstance().getTemplate(name) != null)
		{
			player.sendHtmError("Имя занято");
			return;
		}

		if(!checkPay(player, Options.communityBoard.ChangeNickPrice()))
		{
			player.sendHtmError("Не хватает предметов");
			return;
		}

		player.reName(name, true);

		player.sendHtmSuccess("Смена имени прошла успешно");
	}
}
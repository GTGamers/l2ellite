//package ru.eanseen.extensions.impl.mods.communityboard.impl;
//
//import l2s.gameserver.data.htm.HtmCache;
//import l2s.gameserver.model.Player;
//import l2s.gameserver.network.l2.s2c.SystemMessage;
//import ru.eanseen.options.Options;
//
///**
// Eanseen
// 26.09.2015
// */
//public enum EnumService
//{
//	changeNick("Смена имени", false)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Смена имени"));
//					String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services/changeNick.htm");
//					sb.append(price(Options.communityBoard.servicePriceChangeNick()));
//					sb.append(htm);
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(Util.isValidNameEx(value[0]) != null)
//					{
//						switch(Util.isValidNameEx(value[0]))
//						{
//							case REASON_CREATION_FAILED:
//								message(player, "Указанное имя не может быть использовано");
//								return;
//							case REASON_16_ENG_CHARS:
//								message(player, "В указанном имени слишком много букв");
//								return;
//							case REASON_INCORRECT_NAME:
//								message(player, "Указано неверное имя");
//								return;
//							case REASON_NAME_ALREADY_EXISTS:
//								message(player, "Указанное Вами имя уже используется");
//								return;
//						}
//					}
//
//					if(!Merchant.pay(player, Options.communityBoard.servicePriceChangeNick()))
//					{
//						return;
//					}
//
//					player.setName(value[0]);
//					player.store();
//					CharNameTable.getInstance().addName(player);
//					player.broadcastUserInfo();
//
//					if(player.isInParty())
//					{
//						player.getParty().broadcastPacket(player, new PartySmallWindowDeleteAll());
//						player.getParty().getMembers().stream().filter(member -> member != player).forEach(member -> member.sendPacket(new PartySmallWindowAll(member, player.getParty())));
//					}
//
//					if(player.getClan() != null)
//					{
//						player.getClan().broadcastClanStatus();
//					}
//
//					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//					sm.addItemName(Options.communityBoard.servicePriceChangeNick()[0]);
//					sm.addItemNumber(Options.communityBoard.servicePriceChangeNick()[1]);
//					player.sendPacket(sm);
//
//					message(player, "Вы успешно сменили имя");
//				}
//			},
//	changeSex("Смена пола", false)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Смена пола"));
//					String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services/changeSex.htm");
//					sb.append(price(Options.communityBoard.servicePriceChangeSex()));
//					sb.append(htm);
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(player.getRace() == Race.Kamael)
//					{
//						message(player, "Для расы Камаель функция смены пола недоступна");
//						return;
//					}
//
//					if(!Merchant.pay(player, Options.communityBoard.servicePriceChangeSex()))
//					{
//						return;
//					}
//
//					player.getAppearance().setSex(!player.getAppearance().getSex());
//					player.broadcastUserInfo();
//					player.getLocationController().decay();
//					player.getLocationController().spawn(player.getX(), player.getY(), player.getZ());
//
//					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//					sm.addItemName(Options.communityBoard.servicePriceChangeSex()[0]);
//					sm.addItemNumber(Options.communityBoard.servicePriceChangeSex()[1]);
//					player.sendPacket(sm);
//
//					message(player, "Вы успешно сменили пол");
//				}
//			},
//	changeClanName("Смена имени клана", false)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Смена имени клана"));
//					String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services/changeClanName.htm");
//					sb.append(price(Options.communityBoard.servicePriceChangeClanName()));
//					sb.append(htm);
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(player.getClan() == null || !player.isClanLeader())
//					{
//						message(player, "У Вас нет клана либо Вы не являетесь его лидером");
//						return;
//					}
//					if(ClanTable.getInstance().getClanByName(value[0]) != null)
//					{
//						message(player, "Указанное Вами имя уже используется");
//						return;
//					}
//					if(!Util.isAlphaNumeric(value[0]) || value[0].isEmpty())
//					{
//						message(player, "Указано неверное имя");
//						return;
//					}
//					if(value[0].length() > 16)
//					{
//						message(player, "В указанном имени слишком много букв");
//						return;
//					}
//
//					if(!Merchant.pay(player, Options.communityBoard.servicePriceChangeClanName()))
//					{
//						return;
//					}
//
//					player.getClan().setName(value[0]);
//					player.getClan().updateClanInDB();
//
//					player.sendPacket(new PledgeShowInfoUpdate(player.getClan()));
//					player.sendPacket(new PledgeShowMemberListAll(player.getClan(), player));
//					player.sendPacket(new ExPledgeCount(player.getClan().getOnlineMembersCount()));
//
//					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//					sm.addItemName(Options.communityBoard.servicePriceChangeClanName()[0]);
//					sm.addItemNumber(Options.communityBoard.servicePriceChangeClanName()[1]);
//					player.sendPacket(sm);
//
//					message(player, "Вы успешно сменили название клана");
//				}
//			},
//	changeView("Сервис смены внешнего вида", true)
//			{
//				private final int[] allowClass = {
//						5,
//						10,
//						18,
//						25,
//						34,
//						38,
//						44,
//						49,
//						128,
//						53
//				};
//
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Смена внешнего вида"));
//					String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services/changeView.htm");
//					sb.append(price(Options.communityBoard.servicePriceChangeView()));
//					sb.append(htm);
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(!Util.isDigit(value[0]))
//					{
//						return;
//					}
//
//					int classId = Integer.parseInt(value[0]);
//
//					boolean allow = false;
//					for(int id : allowClass)
//					{
//						if(classId == id)
//						{
//							allow = true;
//							break;
//						}
//					}
//
//					if(!allow)
//					{
//						return;
//					}
//
//					if(!Merchant.pay(player, Options.communityBoard.servicePriceChangeView()))
//					{
//						return;
//					}
//
//					player.getVariablesController().set("customClass", classId);
//					player.getVariablesController().set("customRace", CharTemplateTable.getInstance().getTemplate(ClassId.getClassId(classId)).getRace().ordinal());
//
//					player.logout(false);
//
//					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//					sm.addItemName(Options.communityBoard.servicePriceChangeView()[0]);
//					sm.addItemNumber(Options.communityBoard.servicePriceChangeView()[1]);
//					player.sendPacket(sm);
//				}
//			},
//	changeColorNick("Смена цвета ника", false)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Смена цвета ника"));
//					String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services/changeColorNick.htm");
//					sb.append(price(Options.communityBoard.servicePriceChangeColorNick()));
//					StringBuilder colors = new StringBuilder();
//
//					colors.append("<table>");
//					colors.append("<tr>");
//
//					int i = 0;
//					int j = 0;
//					for(String color : Options.communityBoard.CommunityBoardChangeNickColors())
//					{
//						colors.append("<td fixwidth=100>");
//						colors.append("<a action=\"bypass -h _bbsservices;changeColorNick:").append(j).append("\"><font color=\"").append(color).append("\">").append(player.getName()).append("</font></a><br>");
//						colors.append("</td>");
//
//						i++;
//						j++;
//
//						if(i % 3 == 0)
//						{
//							i = 0;
//							colors.append("</tr>");
//							colors.append("<tr>");
//						}
//					}
//					colors.append("</tr>");
//					colors.append("</table>");
//
//					htm = htm.replace("%colors%", colors.toString());
//					sb.append(htm);
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(!Util.isDigit(value[0]))
//					{
//						return;
//					}
//
//					int type = Integer.parseInt(value[0]);
//
//					if(type >= Options.communityBoard.CommunityBoardChangeNickColors().length)
//					{
//						return;
//					}
//
//					if(!Merchant.pay(player, Options.communityBoard.servicePriceChangeColorNick()))
//					{
//						return;
//					}
//
//					value[0] = Options.communityBoard.CommunityBoardChangeNickColors()[type];
//
//					StringBuilder sb = new StringBuilder();
//					sb.append("0x").append(value[0].substring(4, 6)).append(value[0].substring(2, 4)).append(value[0].substring(0, 2));
//
//					player.getAppearance().setNameColor(Integer.decode(sb.toString()));
//					player.broadcastUserInfo();
//
//					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//					sm.addItemName(Options.communityBoard.servicePriceChangeColorNick()[0]);
//					sm.addItemNumber(Options.communityBoard.servicePriceChangeColorNick()[1]);
//					player.sendPacket(sm);
//
//					message(player, "Цвет ника успешно изменен");
//				}
//			},
//	penalty("Сервис снятия клан штрафа", true)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Штрафы"));
//					sb.append("<br>");
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//					sb.append("<font color=LEVEL>");
//					sb.append("Сервис позволяет снять штраф с персонажа и клана");
//					sb.append("</font>");
//					sb.append("</td></tr></table>");
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//
//					sb.append(price(Options.communityBoard.servicePricePenaltyPlayer()));
//
//					if(player.getClanJoinExpiryTime() > 0)
//					{
//						sb.append(GenerateElement.button("Снять штраф с персонажа", "-h _bbsservices;penalty:1", 300, 30));
//					}
//					else
//					{
//						sb.append(GenerateElement.button("У персонажа штрафа нет", "", 300, 30));
//					}
//
//					sb.append(price(Options.communityBoard.servicePricePenaltyClan()));
//
//					if(player.getClan() != null && player.getClan().getCharPenaltyExpiryTime() > 0)
//					{
//						sb.append(GenerateElement.button("Снять штраф с клана", "-h _bbsservices;penalty:2", 300, 30));
//					}
//					else
//					{
//						sb.append(GenerateElement.button("У клана штрафа нет", "", 300, 30));
//					}
//
//					sb.append("</td></tr></table>");
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					int type = Integer.parseInt(value[0]);
//					switch(type)
//					{
//						case 1:
//							if(!Merchant.pay(player, Options.communityBoard.servicePricePenaltyPlayer()))
//							{
//								break;
//							}
//							if(player.getClanJoinExpiryTime() > 0)
//							{
//								player.setClanJoinExpiryTime(0);
//
//								SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//								sm.addItemName(Options.communityBoard.servicePricePenaltyPlayer()[0]);
//								sm.addItemNumber(Options.communityBoard.servicePricePenaltyPlayer()[1]);
//								player.sendPacket(sm);
//							}
//							break;
//						case 2:
//							if(!Merchant.pay(player, Options.communityBoard.servicePricePenaltyClan()))
//							{
//								break;
//							}
//							if(player.getClan() != null && player.getClan().getCharPenaltyExpiryTime() > 0)
//							{
//								player.getClan().setCharPenaltyExpiryTime(0);
//								player.getClan().updateClanInDB();
//
//								SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//								sm.addItemName(Options.communityBoard.servicePricePenaltyClan()[0]);
//								sm.addItemNumber(Options.communityBoard.servicePricePenaltyClan()[1]);
//								player.sendPacket(sm);
//							}
//							break;
//					}
//					Services.getInstance().parse("_bbsservices;penaltyHtm", player);
//				}
//			},
//	premium("Премиум", false)
//			{
//				private String premiumInfo()
//				{
//					int typePremium = 0;
//
//					StringBuilder sb = new StringBuilder();
//
//					double chance;
//					String value;
//
//					sb.append("<table width=340>");
//					sb.append("<tr>");
//					sb.append("<td>");
//					chance = ConfigPremium.PremiumExpSpRate[typePremium];
//					if(chance > 1)
//					{
//						value = String.valueOf((int) (chance * 100 - 100));
//						sb.append("<font color=LEVEL>Exp/Sp: </font> +").append(value).append("%<br>");
//					}
//
//					chance = ConfigPremium.PremiumExpSpQuestRate[typePremium];
//					if(chance > 1)
//					{
//						value = String.valueOf((int) (chance * 100 - 100));
//						sb.append("<font color=LEVEL>Exp/Sp за квест: </font> +").append(value).append("%<br>");
//					}
//
//					chance = ConfigPremium.PremiumDropAdenaRateCount[typePremium];
//					if(chance > 1)
//					{
//						value = String.valueOf((int) (chance * 100 - 100));
//						sb.append("<font color=LEVEL>Дроп адены: </font> +").append(value).append("%<br>");
//					}
//
//					chance = ConfigPremium.PremiumDropItemRateCount[typePremium];
//					if(chance > 1)
//					{
//						value = String.valueOf((int) (chance * 100 - 100));
//						sb.append("<font color=LEVEL>Дроп предметов (кол-во): </font> +").append(value).append("%<br>");
//					}
//
//					chance = ConfigPremium.PremiumDropItemRateChance[typePremium];
//					if(chance > 1)
//					{
//						sb.append("<font color=LEVEL>Дроп предметов (шанс): </font> x").append(chance).append("<br>");
//					}
//
//					chance = ConfigPremium.PremiumRaidRateCount[typePremium];
//					if(chance > 1)
//					{
//						value = String.valueOf((int) (chance * 100 - 100));
//						sb.append("<font color=LEVEL>Дроп с Рэйд Боссов (кол-во): </font> +").append(value).append("%<br>");
//					}
//
//					chance = ConfigPremium.PremiumRaidRateChance[typePremium];
//					if(chance > 1)
//					{
//						sb.append("<font color=LEVEL>Дроп с Рэйд Боссов (шанс): </font> x").append(chance).append("<br>");
//					}
//
//					sb.append("</td>");
//					sb.append("</tr>");
//					sb.append("</table>");
//
//					sb.append("</body></html>");
//
//					return sb.toString();
//				}
//
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Премиум"));
//					sb.append("<br>");
//
//					sb.append(premiumInfo());
//
//					if(player.isPremiumState())
//					{
//						long endPremiumTime = player.getPremiumTime();
//						long remainingTime = (endPremiumTime - System.currentTimeMillis()) / 1000;
//						int days = (int) (remainingTime / 86400);
//						remainingTime = remainingTime % 86400;
//						int hours = (int) (remainingTime / 3600);
//						remainingTime = remainingTime % 3600;
//						int minutes = (int) (remainingTime / 60);
//						sb.append("<font color=00B2EE>Ваш премиум аккаунт будет активен: </font>");
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Дней:</font> ").append(days);
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Часов/Минут:</font> ").append(hours).append(":").append(minutes);
//					}
//					else
//					{
//						sb.append("<table>");
//
//						for(int i = 0; i < ConfigPremium.PremiumNames.length; i++)
//						{
//							String name = "Кол-во дней: <?days?>                   Цена: <?price?>";
//							name = name.replace("<?name?>", ConfigPremium.PremiumNames[i]);
//							name = name.replace("<?days?>", String.valueOf(ConfigPremium.PremiumDay[i]));
//							name = name.replace("<?price?>", ConfigPremium.PremiumCount[i] + " " + Merchant.getItemName(ConfigPremium.PremiumItemId[i]));
//							sb.append(GenerateElement.buttonTDTR(name, "-h _bbsservices;premium:" + i, 300, 30));
//						}
//
//						sb.append("</table>");
//					}
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(!Util.isDigit(value[0]))
//					{
//						return;
//					}
//
//					int type = Integer.parseInt(value[0]);
//
//					if(type >= ConfigPremium.PremiumNames.length)
//					{
//						return;
//					}
//
//					if(player.getPremiumTime() > System.currentTimeMillis())
//					{
//						message(player, "У Вас уже активирован премиум аккаунт.");
//						return;
//					}
//
//					if(Merchant.pay(player, ConfigPremium.PremiumItemId[type], ConfigPremium.PremiumCount[type]))
//					{
//						long currentPremiumTime = player.getPremiumTime();
//						if(currentPremiumTime < System.currentTimeMillis())
//						{
//							currentPremiumTime = System.currentTimeMillis();
//						}
//						long endPremiumTime = currentPremiumTime + (ConfigPremium.PremiumDay[type] * 24 * 60 * 60 * 1000L);
//						player.stopPremiumTask();
//
//						player.setPremiumEndTime(endPremiumTime);
//						player.setPremiumType(type);
//
//						player.startPremiumTask();
//						player.sendPacket(new ExBR_PremiumState(player.getObjectId(), 1));
//
//						player.sendPacket(new MagicSkillUse(player, ConfigPremium.PremiumUseSkill[type], 1, 0, 0));
//
//						player.startAbnormalEffect(AbnormalEffect.getByName(ConfigPremium.PremiumAbnormal[type]));
//						ThreadPoolManager.getInstance().scheduleGeneral(() -> {
//							if(player != null)
//							{
//								player.stopAbnormalEffect(AbnormalEffect.getByName(ConfigPremium.PremiumAbnormal[type]));
//							}
//						}, 30 * 1000);
//
//						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//						sm.addItemName(ConfigPremium.PremiumItemId[type]);
//						sm.addItemNumber(ConfigPremium.PremiumCount[type]);
//						player.sendPacket(sm);
//					}
//
//					Services.getInstance().parse("_bbsservices;premiumHtm", player);
//				}
//			},
//	levelUpClan("Сервис повышение уровня клана", false)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Повышение уровня клана"));
//					sb.append("<br>");
//
//					if(player.getClan() == null)
//					{
//						sb.append("<table><tr><td fixwidth=300 height=35 align=center>");
//						sb.append("<font color=LEVEL>");
//						sb.append("У Вас нет клана");
//						sb.append("</font>");
//						sb.append("</td></tr></table>");
//					}
//					else
//					{
//						sb.append("<table><tr><td fixwidth=300 align=center>");
//						sb.append("<font color=LEVEL>");
//						sb.append("Сервис позволяет повысить уровень клана");
//						sb.append("</font>");
//						sb.append("</td></tr></table>");
//
//						sb.append("<table><tr><td fixwidth=300 align=center>");
//						sb.append("<font color=LEVEL>");
//						sb.append("Текущий уровень:");
//						sb.append("</font>").append(" ").append(player.getClan().getLevel());
//						sb.append("</td></tr></table>");
//					}
//
//					String price = "";
//
//					if(player.getClan() != null)
//					{
//						switch (player.getClan().getLevel()) {
//							case 0:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo1());
//								break;
//							case 1:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo2());
//								break;
//							case 2:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo3());
//								break;
//							case 3:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo4());
//								break;
//							case 4:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo5());
//								break;
//							case 5:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo6());
//								break;
//							case 6:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo7());
//								break;
//							case 7:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo8());
//								break;
//							case 8:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo9());
//								break;
//							case 9:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo10());
//								break;
//							case 10:
//								price = price(Options.communityBoard.servicePriceLevelUpClanTo11());
//								break;
//							case 11:
//								price = "Достигнут максимальный уровень";
//								break;
//						}
//					}
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//
//					sb.append(price);
//
//					if(player.getClan() != null && player.getClan().getLevel() < 11)
//					{
//						sb.append(GenerateElement.button("Повысить до Уровня: " + (player.getClan().getLevel() + 1), "-h _bbsservices;levelUpClan", 300, 30));
//					}
//
//					sb.append("</td></tr></table>");
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					if(player.getClan() != null && player.getClan().getLevel() < 11)
//					{
//						int[] price = new int[2];
//						switch(player.getClan().getLevel())
//						{
//							case 0: price = Options.communityBoard.servicePriceLevelUpClanTo1(); break;
//							case 1: price = Options.communityBoard.servicePriceLevelUpClanTo2(); break;
//							case 2: price = Options.communityBoard.servicePriceLevelUpClanTo3(); break;
//							case 3: price = Options.communityBoard.servicePriceLevelUpClanTo4(); break;
//							case 4: price = Options.communityBoard.servicePriceLevelUpClanTo5(); break;
//							case 5: price = Options.communityBoard.servicePriceLevelUpClanTo6(); break;
//							case 6: price = Options.communityBoard.servicePriceLevelUpClanTo7(); break;
//							case 7: price = Options.communityBoard.servicePriceLevelUpClanTo8(); break;
//							case 8: price = Options.communityBoard.servicePriceLevelUpClanTo9(); break;
//							case 9: price = Options.communityBoard.servicePriceLevelUpClanTo10(); break;
//							case 10: price = Options.communityBoard.servicePriceLevelUpClanTo11(); break;
//						}
//						if(Merchant.pay(player, price[0], price[1]))
//						{
//							player.getClan().changeLevel(player.getClan().getLevel() + 1);
//
//							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//							sm.addItemName(price[0]);
//							sm.addItemNumber(price[1]);
//							player.sendPacket(sm);
//
//							message(player, "Вы повысили уровень клана");
//
//							Services.getInstance().parse("_bbsservices;levelUpClanHtm", player);
//						}
//					}
//				}
//			},
//	premiumLifeStone("Сервис премиум вставки Камня Жизни", true)
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Премиум вставка"));
//					sb.append("<br>");
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//					sb.append("<font color=LEVEL>");
//					sb.append("Сервис позволяет вставить желаемую аугментацию");
//					sb.append("</font>");
//					sb.append("</td></tr></table>");
//
//					sb.append("<table>");
//
//					L2ItemInstance wpn = player.getActiveWeaponInstance();
//
//					if(wpn != null && wpn.isAugmented())
//					{
//						sb.append("<tr>");
//						sb.append("<td align=center>");
//						sb.append("<font color=00B2EE>В [ ").append(wpn.getName()).append(" ] уже вставлен Камень Жизни.</font>");
//						sb.append("<br>");
//						//sb.append(GenerateElement.button("Убрать Камень Жизни", "-h _bbsaugment;200", 300, 30));
//						sb.append("<br>");
//						sb.append("</td>");
//						sb.append("</tr>");
//
//						/*
//						int augmentationId = wpn.getAugmentation().getAugmentationId();
//						int stats[] = new int[2];
//						stats[0] = 0x0000FFFF & augmentationId;
//						stats[1] = (augmentationId >> 16);
//
//						player.sendMessage("stats[0]=" + stats[0]);
//						player.sendMessage("stats[1]=" + stats[1]);
//						*/
//					}
//					else if(wpn != null && (wpn.getItem().getItemGrade().toString().equals("NONE") || wpn.getItem().getItemGrade().toString().equals("D")))
//					{
//						sb.append("<tr>");
//						sb.append("<td align=center width=300>");
//						sb.append("<font color=00B2EE>Оружие не может быть без ранга или ранга D.</font>");
//						sb.append("<br>");
//						sb.append("</td>");
//						sb.append("</tr>");
//					}
//					else if(wpn != null && !wpn.isAugmented())
//					{
//						for(int i = 0, j = 0; i < Options.communityBoard.serviceLifeStoneAugId().length; i++, j=i+i)
//						{
//
//							L2Skill skill = AugmentationData.getInstance().getAugSkill(Options.communityBoard.serviceLifeStoneAugId()[i]);
//
//							int id = skill.getId();
//							int lvl = skill.getLevel();
//
//							sb.append("<tr><td></td><td align=center>");
//							sb.append("<font color=00B2EE>");
//							sb.append("Цена: ");
//							sb.append("</font>");
//
//							String consume_item = Merchant.getItemName(Options.communityBoard.servicePriceLifeStone()[j]);
//							int consume_count = Options.communityBoard.servicePriceLifeStone()[(j+1)];
//
//							String count = Util.formatAdena(consume_count);
//
//							sb.append("<font color=32CD32>");
//							sb.append(consume_item).append(" - ").append(count).append(" шт");
//							sb.append("</font>");
//							sb.append("</td></tr>");
//
//							String button_part1 = "Вставить -> ";
//							String button_part2 = " (ур." + lvl + ")";
//							String skill_name = skill.getName();
//
//							if(skill_name.startsWith("Предметное"))
//							{
//								skill_name = skill.getName().substring(19);
//							}
//
//							String button_name = button_part1 + skill_name + button_part2;
//							String skill_id = "default_skill_id";
//
//							if(Options.communityBoard.serviceLifeStoneCustomSkillIcon())
//							{
//								skill_id = Options.communityBoard.serviceLifeStoneSkillIcon()[i];
//							}
//							else
//							{
//								skill_id = String.valueOf(id);
//								if (id < 10) {
//									skill_id = "000" + skill_id;
//								} else if (id < 100) {
//									skill_id = "00" + skill_id;
//								} else if (id < 1000) {
//									skill_id = "0" + skill_id;
//								}
//							}
//
//							sb.append("<tr>");
//							sb.append("<td align=center>").append("<img src=\"icon.skill").append(skill_id).append("\" width=32 height=32>").append("</td>");
//							sb.append("<td align=center>").append(GenerateElement.button(button_name, "-h _bbsservices;premiumLifeStone:" + i, 268, 35)).append("</td>");
//							sb.append("</tr>");
//						}
//						sb.append("<tr><td  height=25></td></tr>");
//
//					}
//					else
//					{
//						sb.append("<tr>");
//						sb.append("<td align=center width=300>");
//						sb.append("<font color=00B2EE>Для вставки Камня Жизни возьмите оружие в руки.").append("<br1>");
//						sb.append("Оружие не может быть без ранга или ранга D.</font>");
//						sb.append("</td>");
//						sb.append("<br>");
//						sb.append("</tr>");
//					}
//
//					sb.append("</table>");
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//					int index = Integer.parseInt(value[0]);
//
//					L2Skill skill = AugmentationData.getInstance().getAugSkill(Options.communityBoard.serviceLifeStoneAugId()[index]);
//					int augId = Options.communityBoard.serviceLifeStoneAugId()[index];
//
//					int[] price = new int[2];
//
//					price[0] = Options.communityBoard.servicePriceLifeStone()[index+index];
//					price[1] = Options.communityBoard.servicePriceLifeStone()[index+index+1];
//
//					if(!Merchant.pay(player, price[0],price[1]))
//					{
//						player.sendMessage("Не хватает предметов для покупки.");
//						return;
//					}
//
//					if(Merchant.pay(player, price[0], price[1]))
//					{
//						L2ItemInstance wpn = (player.getActiveWeaponInstance() == null) ? null : player.getActiveWeaponInstance();
//
//						if(wpn == null)
//						{
//							return;
//						}
//
//						// unequip item
//						if(wpn.isEquipped())
//						{
//							L2ItemInstance[] unequiped = player.getInventory().unEquipItemInSlotAndRecord(wpn.getLocationSlot());
//							InventoryUpdate iu = new InventoryUpdate();
//							for(L2ItemInstance itm : unequiped)
//							{
//								iu.addModifiedItem(itm);
//							}
//							player.sendPacket(iu);
//							player.broadcastUserInfo();
//						}
//
//						int lifeStoneLevel = Rnd.get(9 + 1); //метод возвращает от 0 до n-1. Максимальное количество уровней 9.
//						int lifeStoneGrade = Rnd.get(3 + 1); //метод возвращает от 0 до n-1. Максимальное количество уровней 4 (0-3).
//						int STAT_SUBBLOCKSIZE = 91;
//						int STAT_BLOCKSIZE = 3640;
//						int stat12,offset;
//						int stat34 = augId;
//
//						int temp = Rnd.get(2, 3);
//						if(lifeStoneGrade >= 2)
//						{
//							offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (temp - 2) * STAT_BLOCKSIZE + lifeStoneGrade * (10 * STAT_SUBBLOCKSIZE) + 1;
//						}
//						else
//						{
//							offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (temp - 2) * STAT_BLOCKSIZE + Rnd.get(0, 1) * (10 * STAT_SUBBLOCKSIZE) + 1;
//						}
//
//						stat12 = Rnd.get(offset, offset + STAT_SUBBLOCKSIZE - 1);
//
//						wpn.setAugmentation(new L2Augmentation(((stat34 << 16) + stat12), skill));
//
//						stat12 = 0x0000FFFF & wpn.getAugmentationId();
//						stat34 = wpn.getAugmentationId() >> 16;
//						player.sendPacket(new ExVariationResult(stat12, stat34, 1));
//
//						InventoryUpdate iu = new InventoryUpdate();
//						iu.addModifiedItem(wpn);
//						player.sendPacket(iu);
//
//						StatusUpdate su = new StatusUpdate(player);
//						su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
//						player.sendPacket(su);
//
//						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//						sm.addItemName(price[0]);
//						sm.addItemNumber(price[1]);
//						player.sendPacket(sm);
//					}
//
//					message(player, "Вы успешно вставили Камень жизни");
//
//					Services.getInstance().parse("_bbsservices;premiumLifeStoneHtm", player);
//				}
//			},
//	fakeAuraList("Список аур", true) //не является сервисом, так оформлен для красоты коммунки
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//
//					if(Options.communityBoard.FakeAuraEnable())
//					{
//						if (player.hasFakeAura())
//						{
//							sb.append(title("Аура куплена"));
//
//							String[] fakeAuraName = player.getFakeAuraName();
//							long endFakeAuraTime = fakeAuraName[0] == null ? 0 : Long.valueOf(player.getVariablesController().get(fakeAuraName[0]));
//							long remainingTime = (endFakeAuraTime - System.currentTimeMillis()) / 1000;
//							int days = (int) (remainingTime / 86400);
//							remainingTime = remainingTime % 86400;
//							int hours = (int) (remainingTime / 3600);
//							remainingTime = remainingTime % 3600;
//							int minutes = (int) (remainingTime / 60);
//							sb.append("<font color=00B2EE>").append(fakeAuraName[1]).append(" будет активна: </font>");
//							sb.append("<br1>");
//							sb.append("<font color=LEVEL>Дней:</font> ").append(days);
//							sb.append("<br1>");
//							sb.append("<font color=LEVEL>Часов/Минут:</font> ").append(hours).append(":").append(minutes);
//							sb.append("<br1>");
//							//sb.append(GenerateElement.button("Отменить", "-h _bbsservices;fakeAura:" + -1, 300, 30));
//						}
//						else
//						{
//
//							sb.append(title("Покупка аур"));
//							sb.append("<br>");
//
//							sb.append("<table><tr><td fixwidth=300 align=center>");
//							sb.append("<font color=LEVEL>");
//							sb.append("Нажмите на кнопку, для просмотра подробностей");
//							sb.append("</font>");
//							sb.append("</td></tr></table>");
//
//							sb.append("<table><tr><td fixwidth=300 align=center>");
//
//
//							sb.append("<br>");
//
//							sb.append(GenerateElement.button(Options.communityBoard.FakeAura1SystemNames()[1], "-h _bbsservices;fakeAura1Htm", 300, 30));
//							sb.append("<br>");
//
//							sb.append(GenerateElement.button(Options.communityBoard.FakeAura2SystemNames()[1], "-h _bbsservices;fakeAura2Htm", 300, 30));
//							sb.append("<br>");
//							sb.append("<br>");
//
//							sb.append("</td></tr></table>");
//						}
//					}
//					else
//					{
//						sb.append(title("Покупка аур"));
//						sb.append("<br>");
//
//						sb.append("<table><tr><td fixwidth=300 align=center>");
//						sb.append("<font color=LEVEL>");
//						sb.append("Покупка аур отключена");
//						sb.append("</font>");
//						sb.append("</td></tr></table>");
//						sb.append("<br>");
//						sb.append("<br>");
//					}
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//
//				}
//			},
//	fakeAura1("Фейк Аура Хаоса", false)
//			{
//
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title(Options.communityBoard.FakeAura1SystemNames()[1]));
//					sb.append("<br>");
//
//					if(player.hasFakeAura())
//					{
//						String[] fakeAuraName = player.getFakeAuraName();
//						long endFakeAuraTime = fakeAuraName[0] == null ? 0 : Long.valueOf(player.getVariablesController().get(fakeAuraName[0]));
//						long remainingTime = (endFakeAuraTime - System.currentTimeMillis()) / 1000;
//						int days = (int) (remainingTime / 86400);
//						remainingTime = remainingTime % 86400;
//						int hours = (int) (remainingTime / 3600);
//						remainingTime = remainingTime % 3600;
//						int minutes = (int) (remainingTime / 60);
//						sb.append("<font color=00B2EE>").append(fakeAuraName[1]).append(" будет активна: </font>");
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Дней:</font> ").append(days);
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Часов/Минут:</font> ").append(hours).append(":").append(minutes);
//						sb.append("<br1>");
//						//sb.append(GenerateElement.button("Отменить", "-h _bbsservices;fakeAura:" + -1, 300, 30));
//					}
//					else
//					{
//						sb.append("<table>");
//
//						for (int i = 0; i < Options.communityBoard.FakeAura1Types().length; i++) {
//							String name = "Кол-во дней: <?days?>                   Цена: <?price?>";
//							name = name.replace("<?name?>", Options.communityBoard.FakeAura1Types()[i]);
//							name = name.replace("<?days?>", String.valueOf(Options.communityBoard.FakeAura1Day()[i]));
//							name = name.replace("<?price?>", Options.communityBoard.FakeAura1Count()[i] + " " + Merchant.getItemName(Options.communityBoard.FakeAura1ItemId()[i]));
//							String sys_name = Options.communityBoard.FakeAura1SystemNames()[0];
//							sb.append(GenerateElement.buttonTDTR(name, "-h _bbsservices;fakeAura1:" + i + " " + sys_name, 300, 30));
//							//player.sendMessage("-h _bbsservices;fakeAura:" + i + " " + sys_name);
//						}
//						//String sys_name = Options.communityBoard.FakeAura1SystemNames()[0];
//						//player.sendMessage(sys_name);
//						sb.append("</table>");
//						sb.append("<br>");
//						sb.append("<br>");
//					}
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//
//					if(!Util.isDigit(value[0]))
//					{
//						return;
//					}
//
//					int type = Integer.parseInt(value[0]);
//					String sys_name = String.valueOf(value[1]);
//					//player.sendMessage("sys_name="+sys_name);
//
//					if((type >= Options.communityBoard.FakeAura1Types().length))
//					{
//						return;
//					}
//
//					if(player.getFakeAuraTime() > System.currentTimeMillis())
//					{
//						message(player, "У Вас уже активирована аура.");
//						return;
//					}
//
//					long fakeAuraTypeTime = 0;
//					int auraItemId = 0;
//					long auraItemCount = 0;
//
//					if(sys_name.equals(Options.communityBoard.FakeAura1SystemNames()[0]))
//					{
//						fakeAuraTypeTime = (Options.communityBoard.FakeAura1Day()[type] * 24 * 60 * 60 * 1000L);
//						auraItemId = Options.communityBoard.FakeAura1ItemId()[type];
//						auraItemCount = Options.communityBoard.FakeAura1Count()[type];
//					}
//					else
//					{
//						return;
//					}
//
//
//					if(Merchant.pay(player, auraItemId, auraItemCount))
//					{
//						long endFakeAuraTime = System.currentTimeMillis() + fakeAuraTypeTime;
//
//						player.stopFakeAuraTask();
//						player.getVariablesController().set(sys_name, endFakeAuraTime);
//						player.setFakeAuraEndTime(endFakeAuraTime);
//						player.startFakeAuraTask();
//
//						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//						sm.addItemName(auraItemId);
//						sm.addItemNumber(auraItemCount);
//						player.sendPacket(sm);
//					}
//
//					player.broadcastUserInfo();
//					Services.getInstance().parse("_bbsservices;fakeAuraListHtm", player);
//				}
//			},
//	fakeAura2("Фейк Аура Героя", false)
//			{
//
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title(Options.communityBoard.FakeAura2SystemNames()[1]));
//					sb.append("<br>");
//
//					if(player.hasFakeAura())
//					{
//						String[] fakeAuraName = player.getFakeAuraName();
//						long endFakeAuraTime = fakeAuraName[0] == null ? 0 : Long.valueOf(player.getVariablesController().get(fakeAuraName[0]));
//						long remainingTime = (endFakeAuraTime - System.currentTimeMillis()) / 1000;
//						int days = (int) (remainingTime / 86400);
//						remainingTime = remainingTime % 86400;
//						int hours = (int) (remainingTime / 3600);
//						remainingTime = remainingTime % 3600;
//						int minutes = (int) (remainingTime / 60);
//						sb.append("<font color=00B2EE>").append(fakeAuraName[1]).append(" будет активна: </font>");
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Дней:</font> ").append(days);
//						sb.append("<br1>");
//						sb.append("<font color=LEVEL>Часов/Минут:</font> ").append(hours).append(":").append(minutes);
//						sb.append("<br1>");
//						//sb.append(GenerateElement.button("Отменить", "-h _bbsservices;fakeAura:" + -1, 300, 30));
//					}
//					else
//					{
//						sb.append("<table>");
//
//						for (int i = 0; i < Options.communityBoard.FakeAura2Types().length; i++) {
//							String name = "Кол-во дней: <?days?>                   Цена: <?price?>";
//							name = name.replace("<?name?>", Options.communityBoard.FakeAura2Types()[i]);
//							name = name.replace("<?days?>", String.valueOf(Options.communityBoard.FakeAura2Day()[i]));
//							name = name.replace("<?price?>", Options.communityBoard.FakeAura2Count()[i] + " " + Merchant.getItemName(Options.communityBoard.FakeAura2ItemId()[i]));
//							String sys_name = Options.communityBoard.FakeAura2SystemNames()[0];
//							sb.append(GenerateElement.buttonTDTR(name, "-h _bbsservices;fakeAura2:" + i + " " + sys_name, 300, 30));
//							//player.sendMessage("-h _bbsservices;fakeAura:" + i + " " + sys_name);
//						}
//						//String sys_name = Options.communityBoard.FakeAura1SystemNames()[0];
//						//player.sendMessage(sys_name);
//						sb.append("</table>");
//						sb.append("<br>");
//						sb.append("<br>");
//					}
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//
//					if(!Util.isDigit(value[0]))
//					{
//						return;
//					}
//
//					int type = Integer.parseInt(value[0]);
//					String sys_name = String.valueOf(value[1]);
//					//player.sendMessage("sys_name="+sys_name);
//
//					if(type >= Options.communityBoard.FakeAura2Types().length)
//					{
//						return;
//					}
//
//					if(player.getFakeAuraTime() > System.currentTimeMillis())
//					{
//						message(player, "У Вас уже активирована аура.");
//						return;
//					}
//
//					long fakeAuraTypeTime = 0;
//					int auraItemId = 0;
//					long auraItemCount = 0;
//
//					if(sys_name.equals(Options.communityBoard.FakeAura2SystemNames()[0]))
//					{
//						fakeAuraTypeTime = (Options.communityBoard.FakeAura2Day()[type] * 24 * 60 * 60 * 1000L);
//						auraItemId = Options.communityBoard.FakeAura2ItemId()[type];
//						auraItemCount = Options.communityBoard.FakeAura2Count()[type];
//					}
//					else
//					{
//						return;
//					}
//
//
//					if(Merchant.pay(player, auraItemId, auraItemCount))
//					{
//						long endFakeAuraTime = System.currentTimeMillis() + fakeAuraTypeTime;
//
//						player.stopFakeAuraTask();
//						player.getVariablesController().set(sys_name, endFakeAuraTime);
//						player.setFakeAuraEndTime(endFakeAuraTime);
//						player.startFakeAuraTask();
//
//						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_S1_DISAPPEARED);
//						sm.addItemName(auraItemId);
//						sm.addItemNumber(auraItemCount);
//						player.sendPacket(sm);
//					}
//
//					player.broadcastUserInfo();
//					Services.getInstance().parse("_bbsservices;fakeAuraListHtm", player);
//				}
//			},
//	clanServices("Сервисы клана", true) //не является сервисом, так оформлен для красоты коммунки
//			{
//				@Override
//				protected String show(Player player)
//				{
//					StringBuilder sb = new StringBuilder();
//					sb.append(title("Сервисы клана"));
//					sb.append("<br>");
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//					sb.append("<font color=LEVEL>");
//					sb.append("Нажмите на кнопку, для просмотра подробностей");
//					sb.append("</font>");
//					sb.append("</td></tr></table>");
//
//					sb.append("<table><tr><td fixwidth=300 align=center>");
//
//					/*
//					<button value="Смена имени клана" action="bypass -h _bbsservices;changeClanNameHtm"
//					            width=150 height=30 back="L2UI_ct1.button_df" fore="L2UI_ct1.button_df"/>
//					<br1/>
//
//					<button value="Штрафы" action="bypass -h _bbsservices;penaltyHtm" width=150 height=30
//                                back="L2UI_CT1.Button_DF_Down" fore="L2UI_CT1.Button_DF"/>
//                    <br1/>
//
//                    <button value="Повышение уровня клана" action="bypass -h _bbsservices;levelUpClanHtm" width=150 height=30
//                                back="L2UI_CT1.Button_DF_Down" fore="L2UI_CT1.Button_DF"/>
//                    <br1/>
//                    <button value="Клановая заметка" action="bypass -h _bbsclan" width=150 height=30
//                                back="L2UI_CT1.Button_DF_Down" fore="L2UI_CT1.Button_DF"/>
//                    <br1/>
//
//					*/
//
//					sb.append("<br>");
//
//					sb.append(GenerateElement.button("Смена имени клана", "-h _bbsservices;changeClanNameHtm", 300, 30));
//					sb.append("<br>");
//
//					sb.append(GenerateElement.button("Штрафы", "-h _bbsservices;penaltyHtm", 300, 30));
//					sb.append("<br>");
//
//					sb.append(GenerateElement.button("Повышение уровня клана", "-h _bbsservices;levelUpClanHtm", 300, 30));
//					sb.append("<br>");
//
//					sb.append(GenerateElement.button("Клановая заметка", "-h _bbsclan", 300, 30));
//					sb.append("<br>");
//					sb.append("<br>");
//
//
//					sb.append("</td></tr></table>");
//
//					return sb.toString();
//				}
//
//				@Override
//				public void execute(Player player, String[] value)
//				{
//
//				}
//			};
//
//	private String name;
//	private boolean isPay;
//
//	protected String show(Player player)
//	{
//		return "";
//	}
//
//	public void execute(Player player, String[] value)
//	{
//	}
//
//	EnumService(String name, boolean isPay)
//	{
//		this.name = name;
//		this.isPay = isPay;
//	}
//
//	public boolean check()
//	{
//		return true; //GameServerStartup.checkMod(name);
//	}
//
//	public void view(Player player)
//	{
//		String htm = HtmCache.getInstance().getHtm(player.getLang(), Options.communityBoard.folder() + "services.htm");
//		if(!isPay || check())
//		{
//			htm = htm.replace("%content%", show(player));
//		}
//		else
//		{
//			StringBuilder sb = new StringBuilder();
//			sb.append("<table cellpadding=\"20\">");
//			sb.append("<tr>");
//			sb.append("<td>");
//			sb.append("<font color=\"B59A75\" name=\"hs11\">Для покупки данного сервиса посетите Личный Кабинет</font>");
//			sb.append("</td>");
//			sb.append("</tr>");
//			sb.append("</table>");
//			htm = htm.replace("%content%", sb.toString());
//		}
//		send(htm, player, true);
//	}
//
//	//------------------------------------------------------------------------------------------------------------------
//
//	private static String title(String title)
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append("<table><tr><td fixwidth=200 align=center><font color=B59A75 name=hs12>");
//		sb.append(title);
//		sb.append("</font></td></tr></table>");
//		return sb.toString();
//	}
//
//	private static String price(int[] price)
//	{
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("<br>");
//
//		sb.append("<font color=00B2EE>");
//		sb.append("Цена: ");
//		sb.append("</font>");
//
//		sb.append("<font color=32CD32>");
//		sb.append(Merchant.getItemName(price[0])).append(" - ").append(price[1]).append("шт");
//		sb.append("</font>");
//
//		sb.append("<br>");
//
//		return sb.toString();
//	}
//
//	private static void message(Player player, String message)
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append("<html><body><br>");
//		sb.append(message);
//		sb.append("</body></html>");
//
//		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
//		npcHtmlMessage.setHtml(sb.toString());
//		player.sendPacket(npcHtmlMessage);
//	}
//}
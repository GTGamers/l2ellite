package ru.eanseen.options.impl;

import org.aeonbits.owner.Config;

/**
 User: Eanseen
 Date: 07.04.2015
 Time: 10:31
 */
@Config.HotReload
@Config.Sources({
		"file:./options/CommunityBoard.properties",
})
public interface OptionCommunityBoard extends Config
{
	String folder();

	@Separator(";")
	String[] ChangeNickPrice();
}
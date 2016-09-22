package ru.eanseen.options;

import org.aeonbits.owner.ConfigFactory;
import ru.eanseen.options.impl.OptionCommunityBoard;
import ru.eanseen.options.impl.OptionFake;

/**
 User: Eanseen
 Date: 07.04.2015
 Time: 10:49
 */
public class Options
{
	public static OptionCommunityBoard communityBoard = ConfigFactory.create(OptionCommunityBoard.class);
	public static OptionFake fake = ConfigFactory.create(OptionFake.class);
}
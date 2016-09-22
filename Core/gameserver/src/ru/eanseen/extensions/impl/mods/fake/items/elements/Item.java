package ru.eanseen.extensions.impl.mods.fake.items.elements;

import javax.xml.bind.annotation.XmlAttribute;

/**
 Eanseen
 08.08.2015
 */
public class Item
{
	int id;

	public int getId()
	{
		return id;
	}

	@XmlAttribute
	public void setId(int id)
	{
		this.id = id;
	}
}
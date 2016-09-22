package ru.eanseen.extensions.impl.mods.fake.items.elements;

import ru.eanseen.extensions.impl.mods.fake.items.TypeArmors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 Eanseen
 09.08.2015
 */
public class Items
{
	TypeArmors type = TypeArmors.None;

	public TypeArmors getType()
	{
		return type;
	}

	@XmlAttribute
	public void setType(TypeArmors type)
	{
		this.type = type;
	}

	List<Item> items = new ArrayList<>();

	public List<Item> getItems()
	{
		return items;
	}

	@XmlElement(name = "item")
	public void setItems(List<Item> items)
	{
		this.items = items;
	}
}

package ru.eanseen.extensions.impl.mods.fake.items.elements;

import l2s.gameserver.templates.item.ItemGrade;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 Eanseen
 09.08.2015
 */
@XmlRootElement(name = "list")
public class ItemsAll
{
	ItemGrade grade;

	public ItemGrade getGrade()
	{
		return grade;
	}

	@XmlAttribute(name = "grade")
	public void setGrade(ItemGrade grade)
	{
		this.grade = grade;
	}

	List<Items> items = new ArrayList<>();

	public List<Items> getItems()
	{
		return items;
	}

	@XmlElement(name = "items")
	public void setItems(List<Items> items)
	{
		this.items = items;
	}
}
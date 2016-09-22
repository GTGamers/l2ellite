package ru.eanseen.extensions.impl.mods.fake.classarmortype;

import ru.eanseen.extensions.impl.mods.fake.items.TypeArmors;

import javax.xml.bind.annotation.XmlAttribute;

/**
 Eanseen
 16.08.2015
 */
public class ClassArmorType
{
	TypeArmors type;

	public TypeArmors getType()
	{
		return type;
	}

	@XmlAttribute
	public void setType(TypeArmors type)
	{
		this.type = type;
	}
}
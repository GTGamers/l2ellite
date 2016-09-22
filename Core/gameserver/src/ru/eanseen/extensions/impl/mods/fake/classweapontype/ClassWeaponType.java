package ru.eanseen.extensions.impl.mods.fake.classweapontype;

import l2s.gameserver.templates.item.WeaponTemplate;

import javax.xml.bind.annotation.XmlAttribute;

/**
 Eanseen
 16.08.2015
 */
public class ClassWeaponType
{
	WeaponTemplate.WeaponType type;

	public WeaponTemplate.WeaponType getType()
	{
		return type;
	}

	@XmlAttribute
	public void setType(WeaponTemplate.WeaponType type)
	{
		this.type = type;
	}
}
package ru.eanseen.extensions.impl.mods.fake.classweapontype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;

/**
 Eanseen
 16.08.2015
 */
public class ClassWeapon
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

	HashSet<ClassWeaponType> classWeaponTypes;

	public HashSet<ClassWeaponType> getClassWeaponTypes()
	{
		return classWeaponTypes;
	}

	@XmlElement(name = "weapon")
	public void setClassWeaponTypes(HashSet<ClassWeaponType> classWeaponTypes)
	{
		this.classWeaponTypes = classWeaponTypes;
	}
}

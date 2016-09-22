package ru.eanseen.extensions.impl.mods.fake.classweapontype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

/**
 Eanseen
 16.08.2015
 */
@XmlRootElement(name = "list")
public class ClassWeaponAll
{
	HashSet<ClassWeapon> classWeapons;

	public HashSet<ClassWeapon> getClassWeapons()
	{
		return classWeapons;
	}

	@XmlElement(name = "class")
	public void setClassWeapons(HashSet<ClassWeapon> classWeapons)
	{
		this.classWeapons = classWeapons;
	}
}
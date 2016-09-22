package ru.eanseen.extensions.impl.mods.fake.classarmortype;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashSet;

/**
 Eanseen
 16.08.2015
 */
public class ClassArmor
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

	HashSet<ClassArmorType> classArmorTypes;

	public HashSet<ClassArmorType> getClassArmorTypes()
	{
		return classArmorTypes;
	}

	@XmlElement(name = "armor")
	public void setClassArmorTypes(HashSet<ClassArmorType> classArmorTypes)
	{
		this.classArmorTypes = classArmorTypes;
	}
}

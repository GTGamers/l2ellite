package ru.eanseen.extensions.impl.mods.fake.classarmortype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

/**
 Eanseen
 16.08.2015
 */
@XmlRootElement(name = "list")
public class ClassArmorAll
{
	HashSet<ClassArmor> classArmors;

	public HashSet<ClassArmor> getClassArmors()
	{
		return classArmors;
	}

	@XmlElement(name = "class")
	public void setClassArmors(HashSet<ClassArmor> classArmors)
	{
		this.classArmors = classArmors;
	}
}

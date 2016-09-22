package ru.eanseen.extensions.impl.mods.fake.items;

import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.templates.item.ArmorTemplate;
import l2s.gameserver.templates.item.ItemGrade;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.WeaponTemplate;
import ru.eanseen.extensions.impl.mods.fake.classarmortype.ClassArmor;
import ru.eanseen.extensions.impl.mods.fake.classarmortype.ClassArmorAll;
import ru.eanseen.extensions.impl.mods.fake.classarmortype.ClassArmorType;
import ru.eanseen.extensions.impl.mods.fake.classweapontype.ClassWeapon;
import ru.eanseen.extensions.impl.mods.fake.classweapontype.ClassWeaponAll;
import ru.eanseen.extensions.impl.mods.fake.classweapontype.ClassWeaponType;
import ru.eanseen.extensions.impl.mods.fake.items.elements.Item;
import ru.eanseen.extensions.impl.mods.fake.items.elements.Items;
import ru.eanseen.extensions.impl.mods.fake.items.elements.ItemsAll;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 Eanseen
 08.08.2015
 */
public class FakeTableItems
{
	private static FakeTableItems ourInstance = new FakeTableItems();

	public static FakeTableItems getInstance()
	{
		return ourInstance;
	}

	private FakeTableItems()
	{
		load();
	}

	private void load()
	{
		for(File file : new File("data/fakes/items/accessorys").listFiles())
		{
			load(file, TypeItems.Accessory);
		}

		for(File file : new File("data/fakes/items/armors").listFiles())
		{
			load(file, TypeItems.Armor);
		}

		for(File file : new File("data/fakes/items/weapons").listFiles())
		{
			load(file, TypeItems.Weapon);
		}

		try
		{
			JAXBContext context = JAXBContext.newInstance(ClassArmorAll.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object object = unmarshaller.unmarshal(new File("data/fakes/ClassArmorType.xml"));

			ClassArmorAll classArmorAll = (ClassArmorAll) object;

			for(ClassArmor classArmor : classArmorAll.getClassArmors())
			{
				if(classArmorTypes.get(classArmor.getId()) == null)
				{
					classArmorTypes.put(classArmor.getId(), new ArrayList<>());
				}
				for(ClassArmorType classArmorType : classArmor.getClassArmorTypes())
				{
					classArmorTypes.get(classArmor.getId()).add(classArmorType.getType());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			JAXBContext context = JAXBContext.newInstance(ClassWeaponAll.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object object = unmarshaller.unmarshal(new File("data/fakes/ClassWeaponType.xml"));

			ClassWeaponAll classWeaponAll = (ClassWeaponAll) object;

			for(ClassWeapon classWeapon : classWeaponAll.getClassWeapons())
			{
				if(classWeaponTypes.get(classWeapon.getId()) == null)
				{
					classWeaponTypes.put(classWeapon.getId(), new ArrayList<>());
				}
				for(ClassWeaponType classWeaponType : classWeapon.getClassWeaponTypes())
				{
					classWeaponTypes.get(classWeapon.getId()).add(classWeaponType.getType());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

//		for(CrystalGrade grade : CrystalGrade.values())
//		{
//			try
//			{
//				System.out.println("accessorys " + grade + " - " + accessorys.get(grade).size());
//			}
//			catch(Exception e)
//			{
//				System.out.println("accessorys " + grade + " - 0");
//			}
//		}
//
//		for(CrystalGrade grade : CrystalGrade.values())
//		{
//			for(TypeArmors typeArmors : TypeArmors.values())
//			{
//				try
//				{
//					System.out.println("armors " + grade + " - " + "type " + typeArmors + " - " + armors.get(grade).get(typeArmors).size());
//				}
//				catch(Exception e)
//				{
//					System.out.println("armors " + grade + " - " + "type " + typeArmors + " - 0");
//				}
//			}
//		}
//
//		for(CrystalGrade grade : CrystalGrade.values())
//		{
//			for(L2WeaponType weaponType : L2WeaponType.values())
//			{
//				try
//				{
//					System.out.println("weapons " + grade + " - " + "type " + weaponType + " - " + weapons.get(grade).get(weaponType).size());
//				}
//				catch(Exception e)
//				{
//					System.out.println("weapons " + grade + " - " + "type " + weaponType + " - 0");
//				}
//			}
//		}
	}

	public Map<ItemGrade, List<Items>> accessorys = new HashMap<>();
	public Map<ItemGrade, Map<TypeArmors, List<Items>>> armors = new HashMap<>();
	public Map<ItemGrade, Map<WeaponTemplate.WeaponType, List<Item>>> weapons = new HashMap<>();

	public Map<Integer, List<TypeArmors>> classArmorTypes = new HashMap<>();
	public Map<Integer, List<WeaponTemplate.WeaponType>> classWeaponTypes = new HashMap<>();

	private void load(File file, TypeItems type)
	{
		try
		{
			switch(type)
			{
				case Accessory:
				{
					JAXBContext context = JAXBContext.newInstance(ItemsAll.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					Object object = unmarshaller.unmarshal(file);

					ItemsAll itemsAll = (ItemsAll) object;
					for(Items items : itemsAll.getItems())
					{
						if(accessorys.get(itemsAll.getGrade()) == null)
						{
							accessorys.put(itemsAll.getGrade(), new ArrayList<>());
						}
						accessorys.get(itemsAll.getGrade()).add(items);
					}
					break;
				}
				case Armor:
				{
					JAXBContext context = JAXBContext.newInstance(ItemsAll.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					Object object = unmarshaller.unmarshal(file);

					ItemsAll itemsAll = (ItemsAll) object;
					for(Items items : itemsAll.getItems())
					{
						if(armors.get(itemsAll.getGrade()) == null)
						{
							armors.put(itemsAll.getGrade(), new HashMap<>());
						}
						if(armors.get(itemsAll.getGrade()).get(items.getType()) == null)
						{
							armors.get(itemsAll.getGrade()).put(items.getType(), new ArrayList<>());
						}
						armors.get(itemsAll.getGrade()).get(items.getType()).add(items);
					}
					break;
				}
				case Weapon:
				{
					JAXBContext context = JAXBContext.newInstance(ItemsAll.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					Object object = unmarshaller.unmarshal(file);

					ItemsAll itemsAll = (ItemsAll) object;
					for(Items items : itemsAll.getItems())
					{
						for(Item item : items.getItems())
						{
							ItemTemplate i = ItemHolder.getInstance().getTemplate(item.getId());
							if(i instanceof WeaponTemplate)
							{
								WeaponTemplate weapon = (WeaponTemplate) i;
								if(weapons.get(itemsAll.getGrade()) == null)
								{
									weapons.put(itemsAll.getGrade(), new HashMap<>());
								}
								if(weapon.isMagicWeapon())
								{
									if(weapons.get(itemsAll.getGrade()).get(WeaponTemplate.WeaponType.MAGIC) == null)
									{
										weapons.get(itemsAll.getGrade()).put(WeaponTemplate.WeaponType.MAGIC, new ArrayList<>());
									}
									weapons.get(itemsAll.getGrade()).get(WeaponTemplate.WeaponType.MAGIC).add(item);
								}
								else
								{
									if(weapons.get(itemsAll.getGrade()).get(weapon.getItemType()) == null)
									{
										weapons.get(itemsAll.getGrade()).put(weapon.getItemType(), new ArrayList<>());
									}
									weapons.get(itemsAll.getGrade()).get(weapon.getItemType()).add(item);
								}
							}
						}
					}
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public List<Integer> getRandomItems(Player player, TypeItems type)
	{
		ArrayList<Integer> itemsList = new ArrayList<>();
		switch(type)
		{
			case Accessory:
			{
				List<Items> items = accessorys.get(ItemGrade.values()[player.expertiseIndex]);
				itemsList.addAll(items.get(Rnd.get(items.size())).getItems().stream().map(Item::getId).collect(Collectors.toList()));
				break;
			}
			case Armor:
			{
				TypeArmors armorType = null;
				try
				{
					List<TypeArmors> typeArmorsList = classArmorTypes.get(player.getClassId().getId());
					armorType = typeArmorsList.get(Rnd.get(typeArmorsList.size()));
					List<Items> items = armors.get(ItemGrade.values()[player.expertiseIndex]).get(armorType);
					itemsList.addAll(items.get(Rnd.get(items.size())).getItems().stream().map(Item::getId).collect(Collectors.toList()));
				}
				catch(Exception e)
				{
					System.out.println("--------------------------");
					System.out.println(player);
					System.out.println(player.expertiseIndex);
					System.out.println(armorType);
					System.out.println(player.getClassId().getId());
					System.out.println("--------------------------");
				}
				break;
			}
			case Weapon:
			{
				WeaponTemplate.WeaponType weaponType = null;
				try
				{
					List<WeaponTemplate.WeaponType> weaponTypeList = classWeaponTypes.get(player.getClassId().getId());
					weaponType = weaponTypeList.get(Rnd.get(weaponTypeList.size()));
					List<Item> items = weapons.get(ItemGrade.values()[player.expertiseIndex]).get(weaponType);
					itemsList.add(items.get(Rnd.get(items.size())).getId());
				}
				catch(Exception e)
				{
					System.out.println("--------------------------");
					System.out.println(player);
					System.out.println(player.expertiseIndex);
					System.out.println(weaponType);
					System.out.println(player.getClassId().getId());
					System.out.println("--------------------------");
				}
				break;
			}
		}
		return itemsList;
	}
}
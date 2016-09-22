package ru.eanseen.extensions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ExtensionManager
{
	private static ExtensionManager ourInstance = new ExtensionManager();

	public static ExtensionManager getInstance()
	{
		return ourInstance;
	}

	private ExtensionManager()
	{
		executeCoreScripts();
	}

	private static void executeCoreScripts()
	{
		Collection<Class<?>> classes = getClassesForPackage("ru.eanseen.extensions.impl");

		classes.stream().filter(c -> c.isAnnotationPresent(Extension.class)).forEach(c -> {
			try
			{
				c.getConstructor().newInstance();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	private static void addScript(Collection<Class<?>> classes, String name)
	{
		try
		{
			Class<?> cl = Class.forName(name);
			if(cl != null)
			{
				classes.add(cl);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static Collection<Class<?>> getClassesForPackageInDir(File directory, String packageName, Collection<Class<?>> classes)
	{
		if(!directory.exists())
		{
			return classes;
		}
		File[] files = directory.listFiles();
		if(files != null)
		{
			for(File file : files)
			{
				if(file.isDirectory())
				{
					getClassesForPackageInDir(file, packageName + "." + file.getName(), classes);
				}
				else if(file.getName().endsWith(".class"))
				{
					addScript(classes, packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				}
			}
		}
		return classes;
	}

	private static void getClassesForPackageInJar(URL url, String packagePath, Collection<Class<?>> classes)
	{
		JarInputStream stream;
		try
		{
			stream = new JarInputStream(url.openStream()); // may want better way to open url connections
			JarEntry entry = stream.getNextJarEntry();
			while(entry != null)
			{
				String name = entry.getName();
				int i = name.lastIndexOf("/");
				if(i > 0 && name.endsWith(".class") && name.substring(0, i).startsWith(packagePath))
				{
					addScript(classes, name.substring(0, name.length() - 6).replace("/", "."));
				}
				entry = stream.getNextJarEntry();
			}
			stream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Collection<Class<?>> getClassesForPackage(String packageName)
	{
		String packagePath = packageName.replace(".", "/");
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Collection<Class<?>> classes = new HashSet<>();
		try
		{
			Enumeration<URL> resources = classLoader.getResources(packagePath);
			ArrayList<File> dirs = new ArrayList<>();
			while(resources.hasMoreElements())
			{
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			for(File directory : dirs)
			{
				getClassesForPackageInDir(directory, packageName, classes);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		ArrayList<URL> jarUrls = new ArrayList<>();
		while(classLoader != null)
		{
			if(classLoader instanceof URLClassLoader)
			{
				for(URL url : ((URLClassLoader) classLoader).getURLs())
				{
					if(url.getFile().endsWith("gameserver.jar"))
					{
						jarUrls.add(url);
					}
				}
			}
			classLoader = classLoader.getParent();
		}
		for(URL url : jarUrls)
		{
			getClassesForPackageInJar(url, packagePath, classes);
		}
		return classes;
	}
}
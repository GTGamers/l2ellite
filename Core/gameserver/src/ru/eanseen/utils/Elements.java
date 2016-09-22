package ru.eanseen.utils;

/**
 Eanseen
 26.09.2015
 */
public class Elements
{
	public static String button(String value, String bypass, int width, int height)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<button value=\"");
		sb.append(value);
		sb.append("\" action=\"bypass ");
		sb.append(bypass);
		sb.append("\" width=");
		sb.append(width);
		sb.append(" height=");
		sb.append(height);
		sb.append(" back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		return sb.toString();
	}

	public static String getItemName(int itemId)
	{
		return "&#" + itemId + ";";
	}
}
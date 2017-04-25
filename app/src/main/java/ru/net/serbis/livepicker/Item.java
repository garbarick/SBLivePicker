package ru.net.serbis.livepicker;
import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.service.wallpaper.*;

public class Item implements Comparable
{
    private String label;
    private Drawable icon;
    private String service;
    private String settings;
    private String packageName;

    public Item(String label, Drawable icon, WallpaperInfo info)
    {
        this.label = label;
        this.icon = icon;
        service = info.getServiceName();
        settings = info.getSettingsActivity();
        packageName = info.getPackageName();
    }

    public String getLabel()
    {
        return label;
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public Intent getSetIntent()
    {
        Intent result = new Intent(WallpaperService.SERVICE_INTERFACE);
        result.setClassName(packageName, service);
        return result;
    }
    
    public boolean hasSettings()
    {
        return settings != null;
    }
    
    public Intent getSettingsInten()
    {
        Intent result = new Intent();
        result.setComponent(new ComponentName(packageName, settings));
        return result;
    }
     
    @Override
    public int compareTo(Object object)
    {
        return label.compareTo(((Item) object).label);
    }

    @Override
    public boolean equals(Object object)
    {
        return service.equals(((Item) object).service);
    }
}

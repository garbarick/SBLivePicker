package ru.net.serbis.livepicker;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.service.wallpaper.*;
import android.view.*;
import android.widget.*;
import java.lang.reflect.*;
import java.util.*;

public class Main extends Activity 
{   
    private ListView list;
    private List<Item> items;
    private ListAdapter adapter;
    private Item current;
    private Button set;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initItems();
        initlist();
        initItemClick();
        initSetButton();
        initSettingsButton();
    }

    private void initItems()
    {
        items = new ArrayList<Item>();
        PackageManager manager = getPackageManager();
        for (ResolveInfo resolvInfo : manager.queryIntentServices(new Intent(WallpaperService.SERVICE_INTERFACE), PackageManager.GET_META_DATA))
        {
            WallpaperInfo info = null;
            try
            {
                info = new WallpaperInfo(this, resolvInfo);
            }
            catch (Exception e)
            {
                continue;
            }

            Item item = new Item(
                info.loadLabel(manager).toString(),
                info.loadIcon(manager),
                info
            );
            items.add(item);
        }
        Collections.sort(items);
    }

    private void initlist()
    {
        list = (ListView) findViewById(R.id.list);
        adapter = new ListAdapter(this, R.layout.main, items);
        list.setAdapter(adapter);
    }
    
    private void initItemClick()
    {
        list.setOnItemClickListener(
            new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView parent, View view, int position, long id)
                {
                    adapter.setSelected(position);
                    current = adapter.getItem(position);
                    adapter.notifyDataSetChanged();
                    
                    set.setEnabled(true);
                    settings.setEnabled(current.hasSettings());
                }
            }
        );
    }
    
    private void initSetButton()
    {
        set = (Button) findViewById(R.id.set);
        set.setEnabled(false);
        set.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    setWallpaper(current);
                }
            }
        );
    }

    private void setWallpaper(Item item)
    {
        try
        {
            WallpaperManager manager = WallpaperManager.getInstance(this);
            Method getIWallpaperManagerMethod = WallpaperManager.class.getMethod("getIWallpaperManager");
            Object internalManager = getIWallpaperManagerMethod.invoke(manager);
            Method setWallpaperComponentMethod = internalManager.getClass().getMethod("setWallpaperComponent", ComponentName.class);
            setWallpaperComponentMethod.invoke(internalManager, item.getSetIntent().getComponent());
            finish();
        }
        catch (Exception e)
        {
            Log.info(this, "error on set", e);
        }
        finish();
    }

    private void initSettingsButton()
    {
        settings = (Button) findViewById(R.id.settings);
        settings.setEnabled(false);
        settings.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    startSettings(current);
                }
            }
        );
    }
    
    private void startSettings(Item item)
    {
        startActivity(item.getSettingsInten());
    }
}

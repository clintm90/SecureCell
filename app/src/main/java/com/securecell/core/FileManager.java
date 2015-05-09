package com.securecell.core;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.util.ArrayList;
import java.util.List;

public class FileManager extends ActionBarActivity
{
	private ListView MainContainer;
	private List<EnumFile> enumFile = new ArrayList<EnumFile>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_file_manager);

		Storage storage = null;
		if (SimpleStorage.isExternalStorageWritable())
		{
			storage = SimpleStorage.getExternalStorage();
		}
		else
		{
			storage = SimpleStorage.getInternalStorage(mContext);
		}

		MainContainer = (ListView) findViewById(R.id.fileManager);

		FileManagerAdapter fileManagerAdapter = new FileManagerAdapter(this, enumFile);

		fileManagerAdapter.add(new EnumFile(this, "Documents", "/sdcard/data/documents", getResources().getDrawable(R.drawable.ic_file_directory), "3,4Mo"));
		fileManagerAdapter.add(new EnumFile(this, "Sample1.txt", "/sdcard/data/documents/sample1.txt", getResources().getDrawable(R.drawable.ic_file_text), "1,2Mo"));

		MainContainer.setAdapter(fileManagerAdapter);

		overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
	}

	public void NewFile(MenuItem item)
	{
		Intent i = new Intent(this, FilePickerActivity.class);

		i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
		i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
		i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

		i.putExtra(FilePickerActivity.EXTRA_START_PATH, "/storage/emulated/0/");

		startActivityForResult(i, 0);
	}

	public void NewDirectory(MenuItem item)
	{
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0 && resultCode == Activity.RESULT_OK)
		{
			/*if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false))
			{
				// For JellyBean and above
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				{
					ClipData clip = data.getClipData();

					if (clip != null)
					{
						for (int i = 0; i < clip.getItemCount(); i++)
						{
							Uri uri = clip.getItemAt(i).getUri();
							// Do something with the URI
						}
					}
					// For Ice Cream Sandwich
				} else
				{
					ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);

					if (paths != null)
					{
						for (String path : paths)
						{
							Uri uri = Uri.parse(path);
							// Do something with the URI
						}
					}
				}

			} else
			{
				Uri uri = data.getData();
				// Do something with the URI
			}*/
		}
	}

	@Override
	public void onBackPressed()
	{
		setResult(RESULT_OK, new Intent().putExtra("result", 1));
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_file_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();

		switch (id)
		{
			case android.R.id.home:
				setResult(RESULT_OK, new Intent().putExtra("result", 1));
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
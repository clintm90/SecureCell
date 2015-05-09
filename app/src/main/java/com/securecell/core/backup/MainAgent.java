package com.securecell.core.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class MainAgent extends BackupAgentHelper
{
	// The names of the SharedPreferences groups that the application maintains.  These
	// are the same strings that are passed to getSharedPreferences(String, int).
	static final String PREFS_DISPLAY = "displayprefs";
	static final String PREFS_SCORES = "highscores";

	// An arbitrary string used within the BackupAgentHelper implementation to
	// identify the SharedPreferencesBackupHelper's data.
	static final String MY_PREFS_BACKUP_KEY = "myprefs";

	// Simply allocate a helper and install it
	public void onCreate()
	{
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS_DISPLAY, PREFS_SCORES);
		addHelper(MY_PREFS_BACKUP_KEY, helper);
	}
}
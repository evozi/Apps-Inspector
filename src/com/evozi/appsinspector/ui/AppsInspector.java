/*******************************************************************************
 * Copyright (c) 2011 Evozi (xDragonZ) ; email@evozi.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.evozi.appsinspector.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;

import com.evozi.appsinspector.Advertiser;
import com.evozi.appsinspector.R;
import com.evozi.appsinspector.helper.ActionItem;
import com.evozi.appsinspector.helper.AppInfo;
import com.evozi.appsinspector.helper.DialogHelper;
import com.evozi.appsinspector.helper.QuickAction;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 Apps Inspector detects frameworks used by your installed applications. 
 It's a great way to see what advertising agencies other developers use, 
 and what tools they have integrated.
 
         Contributors: - havexz ; Chris ; Drake Justice KDE ; sven
        
         TODO [High Priority Task] Sort the list in order , implement filter by framework types 
 		 TODO [Low Priority Task] Menu , About section
 */

public class AppsInspector extends Activity {

	private ProgressDialog progressDialog;

	private static final int ID_OPEN = 1;
	private static final int ID_MORE = 2;
	private static final int ID_MARKET = 3;
	private static final int ID_UNINSTALL = 4;

	private static final String TAG = "AppsInspector";
	private BroadcastReceiver mRegisteredReceiver;

	private int mSelectedRow = 0;
    private int dosystem = 0;
    private boolean running = false; 
    private Properties known_apps = new Properties();

	GetPackageTask AppsInspectorTask = null;

    private static final String [] nullhosts={};
         
    public static final Advertiser[] all_advertisers = {        
            new Advertiser("com/burstly/","Burstly","http://www.burstly.com/",nullhosts),
            new Advertiser("com/inmobi/androidsdk/","InMobi","http://www.inmobi.com/",nullhosts),
            new Advertiser("com/millennialmedia/","Millennialmedia","http://www.millennialmedia.com/",nullhosts),
            new Advertiser("com/greystripe/","Greystripe","http://www.greystripe.com/",nullhosts),
            new Advertiser("com/jumptap/","Jumptap","http://www.jumptap.com/",nullhosts),
            new Advertiser("cn/domob/","Domob","http://www.domob.cn/",nullhosts),
            new Advertiser("cn/com/opda/android/","Opda","http://www.opda.com.cn/",nullhosts),
            new Advertiser("com/casee/","Casee","http://www.casee.cn/",nullhosts),
            new Advertiser("com/mobclix/android/","Mobclix","http://www.mobclix.com/",nullhosts),
            new Advertiser("com/zestadz/","Zestadz","http://www.komlimobile.com/",nullhosts),
            new Advertiser("com/komlimobile/","Komli Media","http://www.komlimobile.com/",nullhosts),
            new Advertiser("com/adwhirl/","Adwhirl","https://www.adwhirl.com/",nullhosts),
            new Advertiser("de/madvertise/","Madvertise","http://madvertise.com/",nullhosts),
            new Advertiser("com/mdotm/ads","MdotM","http://mdotm.com/",nullhosts),
            new Advertiser("com/qwapi/","Qwapi","http://qwapi.com",nullhosts),
            new Advertiser("com/vdopia/","Vdopia","http://www.vdopia.com/",nullhosts),
            new Advertiser("com/pontiflex/","Pontiflex","http://www.pontiflex.com/",nullhosts),
            new Advertiser("com/cauly/","Cauly","http://www.cauly.net/",nullhosts),
            new Advertiser("com/medialets/","Medialets","http://www.medialets.com/",nullhosts),
            new Advertiser("com/smaato/","Smaato","http://www.smaato.com/",nullhosts),
            new Advertiser("com/papaya/","Papaya","http://papayamobile.com/",nullhosts),
            new Advertiser("com/adknowledge/","Adknowledge","http://www.adknowledge.com/",nullhosts),
            new Advertiser("com/amobee/","Amobee","http://amobee.com/",nullhosts),
            new Advertiser("com/inneractive/","Inneractive","http://inner-active.com/",nullhosts),
            new Advertiser("com/adserver/","Adserver","http://www.moceanmobile.com/",nullhosts),
            new Advertiser("com/tapit/","Tapit","http://tapit.com/",nullhosts),
            new Advertiser("com/appenda/","Appenda","http://www.appenda.com/",nullhosts),
            new Advertiser("com/iac/notification/","IAC Search & Media, Inc.","http://www.iac.com/",nullhosts),
            new Advertiser("net/youmi/","Youmi","http://www.youmi.net/",nullhosts),
            new Advertiser("com/adwo/","Adwo","http://www.adwo.com/",nullhosts),
            new Advertiser("com/vpon/","Vpon","http://www.vpon.com/",nullhosts),
            new Advertiser("com/adchina/","AdChina","http://www.adschina.com/",nullhosts),
            new Advertiser("com/urbanairship/push/","Urban Airship","http://www.urbanairship.com",nullhosts),
            new Advertiser("com/xtify/android/sdk/","Xtify","http://www.xtify.com",nullhosts),           
            new Advertiser("com/mopub/mobileads/MoPubView","MoPub","http://www.mopub.com/",nullhosts),  
            new Advertiser("com/admarvel/android/ads/","Admarvel","http://www.admarvel.com/",nullhosts),
            new Advertiser("com/nexage/android/","Nexage","http://www.nexage.com/",nullhosts),
            new Advertiser("jp/co/nobot/libAdMaker/","AdMaker","http://www.nobot.co.jp/",nullhosts),
            new Advertiser("buzzcity/android/sdk/","BuzzCity","http://www.buzzCity.com/",nullhosts),
            new Advertiser("mobi/vserv/android/adengine","VSERV","http://www.vserv.mobi/",nullhosts),
            new Advertiser("com/adfonic/android/","AdFonic","http://www.adfonic.com/",nullhosts),
            new Advertiser("com/adsmogo/","Adsmogo","http://www.adsmogo.com/help/adsmogo_android_sdk.aspx",nullhosts),
            new Advertiser("com/oneriot/OneRiotAd","OneRiot","http://oneriot.com/",nullhosts),
            new Advertiser("com/mobfox/sdk/MobFoxView","Mobfox","http://www.mobfox.com/",nullhosts),
            new Advertiser("com/google/ads/AdView","AdMob","http://www.admob.com/",nullhosts),
            new Advertiser("com/google/ads/GoogleAdView","AdSense","http://adwords.google.com/",nullhosts),
            //AirPush
            new Advertiser("com/airpush/","Airpush","http://www.airpush.com/",nullhosts),
            new Advertiser("com/Leadbolt/","Leadbolt","http://leadbolt.com/",nullhosts)
            
            /**
            //Licensing Service
            "Google Licensing Service", "com.android.vending.licensing.ILicensingService"
            "SlideMe SlideLock", "com.slideme.slidelock.License"
            "Amazon Licensing", "com.amazon.venezia.service.verify.IApplicationVerificationService"
            
            //Analytics
             new Advertiser("com/mobclick/","Umeng","http://www.umeng.com/",nullhosts),
             new Advertiser("com/a/a/c","Umeng","http://www.umeng.com/",nullhosts),
             new Advertiser("com/flurry/android/","Flurry","http://www.flurry.com/",nullhosts),
             new Advertiser("com/google/android/apps/analytics/GoogleAnalyticsTracker","Google Analytics","http://www.google.com/",nullhosts),
             new Advertiser("com/localytics/android/LocalyticsSession","Localytics","http://www.localytics.com/",nullhosts),
             new Advertiser("com/claritics/android/ClariticsDataService","Claritics","http://www.claritics.com/",nullhosts),
            **/
    };

                
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
                
                // Get the known apps 
                File known_appscache = new File(getCacheDir(), "known_apps");
                try
                {
                    FileInputStream known_appsstream = new FileInputStream(known_appscache);
                    known_apps.load(known_appsstream);
                } 
                catch (FileNotFoundException ex) 
                {
                    Log.v(TAG, "known_apps not found.");
                }
                catch (IOException ex) 
                {
                    Log.v(TAG, "known_apps not loaded.");
                }

		AppsInspectorTask = new GetPackageTask();
		AppsInspectorTask.execute();
		registerIntentReceivers();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRegisteredReceiver != null) {
            unregisterReceiver(mRegisteredReceiver);
        }
    }
    

    //Receives notifications when applications are added/removed.
    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: this is a bit brute force.  We should probably get the action and package name
            //       from the intent and just add to or delete from the mPackageInfoList
        	rescan();
        }
    }

    private void registerIntentReceivers() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mRegisteredReceiver = new ApplicationsIntentReceiver();
        registerReceiver(mRegisteredReceiver, filter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            menu.add(0, 0, 0, "Delete Scan Cache").setIcon(android.R.drawable.ic_menu_delete);
            menu.add(0, 1, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
            menu.add(1, 2, 0, "Exit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
            //menu.add(1, 3, 0, getString(R.string.test));
            //menu.add(1, 4, 0, getString(R.string.test));
            return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
            case 0:
            	    deleteCache(this);
                    break;
            case 1:
    				Toast.makeText(
    						AppsInspector.this,
    						"Coming Soon",
    						Toast.LENGTH_SHORT).show();
                    break;
            case 2:
        	        this.finish();
                    break;
            }
            return false;
    }
    
    // Delete Cache - Test
 	public static void deleteCache(Context context) {
 		try {
 			File dir = context.getCacheDir();
 			if (dir != null && dir.isDirectory()) {
 				deleteDir(dir);
 				Toast.makeText(context, "Cache Cleared",
 						Toast.LENGTH_SHORT).show();
 			}
 		} catch (Exception e) {
 		}
 	}
 	
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public void onBackPressed() {
		DialogHelper.showExitDialog(AppsInspector.this);
	}
	
	//Ask user want to rescan or not
	public void rescan() {			
		
		if(running == false ) { 
		running = true;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Apps Inspector detected that an apps have been installed or uninstalled?\nDo you want to rescan?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AppsInspectorTask = new GetPackageTask();
								AppsInspectorTask.execute();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		
		AlertDialog alert = builder.create();
		
		alert.setOnDismissListener(new OnDismissListener() {
	        public void onDismiss(DialogInterface dialog) {
	        	running = false;
             }
	       });
		
		alert.show();
		}
	}

	public void onRefreshButtonClick(View button) {
		if(running == false) { 
		AppsInspectorTask = new GetPackageTask();
		AppsInspectorTask.execute();
		}
	}
	
	public class Sorter implements Comparator<AppInfo>{

        public int compare(AppInfo arg0, AppInfo arg1) {
                return arg0.appName.compareTo(arg1.appName);
        }
    }

    private String[] analyse_apk(String apkpath, Advertiser[] advertisers) throws IOException
        {
          ArrayList<String> adLibs = new ArrayList<String>();
          
          if(apkpath!=null && advertisers!=null)
          {
            FileInputStream fileinputstream=null;
            //ZipFile apk = null;
            File dalvikfile = new File("/data/dalvik-cache/"+apkpath.substring(1).replace("/", "@")+"@classes.dex");  
            Log.v(TAG, "analyse_apk open dalvik-cache file :" + dalvikfile.getAbsolutePath());
            if(dalvikfile.exists())
            {
              fileinputstream = new FileInputStream(dalvikfile);
            }
            else
            {
              Log.v(TAG, "analyse_apk huh? no dalvik-cache? Implement unziping classes.dex from apk in "+apkpath);
              // unzip classes.dex
              //apk = new ZipFile(apkpath);
              //ZipEntry classesentry = apk.getEntry("classes.dex");
              //fileinputstream =  apk.getInputStream(classesentry);
            }
            
            if(fileinputstream!=null)
            {
              ByteArrayOutputStream bos = new ByteArrayOutputStream();
              byte[] buffer = new byte[8192];
              
              for (int readNum; (readNum = fileinputstream.read(buffer)) != -1;)
              {
                byte[] stringsbuffer = new byte[readNum];
                int stringscursor=0;
                
                for (int i=0;i<readNum;i++)
                {
                  byte b = buffer[i];
                  if(b>32 && b<127)
                  {
                    stringsbuffer[stringscursor++]=b;
                  }
                }
                bos.write(stringsbuffer, 0, stringscursor); 
              }
              fileinputstream.close();              
              String juststrings = bos.toString();
              bos.close();
//              if(apk != null)
//              {
//                apk.close();
//              }

              // Match the Advertising Class-Names
              for (Advertiser advertiser : advertisers)
              {
                if(juststrings.toString().indexOf("L"+advertiser.ClassPrefix)>0)
                {
                  adLibs.add(advertiser.ReferenceName);
                }
              }
            }
          }
          String [] adLibsarray = new String[adLibs.size()];
          adLibs.toArray(adLibsarray);
          return(adLibsarray);
        }
        

	private class GetPackageTask extends
			AsyncTask<Void, Integer, List<PackageInfo>> {

		private String current_package;

		protected List<PackageInfo> doInBackground(Void... params) {
				
			Set<PackageInfo> adPackages = new HashSet<PackageInfo>();
			PackageManager pm = getPackageManager();
			

			// It'd be simpler to just use pm.getInstalledPackages here, but apparently it's broken
			List<ApplicationInfo> appInfos = pm.getInstalledApplications(0);
			Log.d(TAG, "Total system & user apps : " + appInfos.size());

			//Collections.sort(appInfos, new ApplicationInfo.DisplayNameComparator(pm)); 
			
			int current_progress = 0; 
      	    int apps_size = 0; 
      	    
      	    //Dirty trick to get total apps size , I can't figure out the way to get total size for non user and user apps
    		for(ApplicationInfo appInfo : appInfos) {
  	          if((appInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0 || dosystem == 1 )
  	          {    
    				int total_apps_size = apps_size++;
    				progressDialog.setMax(total_apps_size);
    				// This will delay the scan little late abit because it will wait this to finish , if I not mistaken
  	          }
      		}


			for (ApplicationInfo appInfo : appInfos) { 
				
				//If running is 'true' then continue the scanning task
					if(isCancelled()){
						  break;
				    	}     
				                // Filter System apps	
                                if((appInfo.flags&ApplicationInfo.FLAG_SYSTEM) == 0 || dosystem == 1 )
                                {			
                                    try {
										int i = current_progress++;
                                		publishProgress(i);

                                          //PackageInfo pkgInfo = pm.getPackageInfo(appInfo.packageName,PackageManager.PKG_INSTALL_COMPLETE);
                                            PackageInfo pkgInfo = pm.getPackageInfo(appInfo.packageName, PackageManager.GET_SHARED_LIBRARY_FILES);
                                        
                                            String frame = "Scanning package "+appInfo.loadLabel(getPackageManager()).toString()+" ("+pkgInfo.packageName+")";
                                            Log.v(TAG, frame);
                                            
                                            this.current_package = appInfo.loadLabel(getPackageManager()).toString();

                                            String known_advertisers = known_apps.getProperty(pkgInfo.packageName);

                                            if(known_advertisers!=null)
                                            {
                                              Log.v(TAG, "Already known app: "+appInfo.packageName);
                                            }
                                            else
                                            {
                                              try {
                                              String [] advertisers = analyse_apk(appInfo.publicSourceDir,all_advertisers);

                                              known_advertisers=join(advertisers);
                                             
                                              known_apps.setProperty(pkgInfo.packageName, known_advertisers);
                                              Log.d(TAG, "set " + pkgInfo.packageName + " = " + known_advertisers );                               

                                              } catch (IOException ex) {
                                                  Log.e(TAG, "Problems with analyse_apk" + pkgInfo.packageName);
 
                                                  Logger.getLogger(AppsInspector.class.getName()).log(Level.SEVERE, null, ex);
                                              }
                                            }
                                            if(known_advertisers.length()>0)
                                            {
                                              adPackages.add(pkgInfo);       
                                            }

                                    } catch (NameNotFoundException e) {
                                            Log.e(TAG, "Managed to not find a package we know about");
                                    }
                                }
			}
			Log.v(TAG, "return new ArrayList");
                        
                        // Store the known apps 
                        File known_appscache = new File(getCacheDir(), "known_apps");
                        try
                        {
                          FileOutputStream known_appsstream = new FileOutputStream(known_appscache);
                          known_apps.store(known_appsstream, "");
                        } 
                        catch (FileNotFoundException ex) 
                        {
                            Log.v(TAG, "known_apps not saved.");
                        }
                        catch (IOException ex) 
                        {
                            Log.v(TAG, "known_apps not saved.");
                        }
                        
			// adPackages is what you returning from your getPackages function
            // Log.d(TAG, "adPackages : " + adPackages);
			return new ArrayList<PackageInfo>(adPackages);
		}

    public String join(String[] array)
    {
        if (array.length == 0) return "";

        if (array.length == 1)
        {
         if (array[0] == null) return "";
         return array[0];
        }

        StringBuilder result = new StringBuilder();
        for (int i=0; i<array.length-1; i++)
        {
         if (array[i] != null)
        {
            result.append(array[i]).append(", ");
         }
        }
        result.append(array[array.length-1]);
         
        return result.toString();
    }


        
    @Override
		protected void onPostExecute(final List<PackageInfo> result) {
			ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
	            
			//java.util.Collections.sort(result, new Sorter());
         	List<PackageInfo> packages = result;
	
        	ListView app_listView = (ListView) findViewById(R.id.listview);
        	
			final int total_packages = packages.size();
		    Log.v(TAG, "Total packages found : " + total_packages);

			for (int i = 0; i < total_packages; i++) {
				PackageInfo packageInfo = packages.get(i);
				AppInfo tmpInfo = new AppInfo();
				
				tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
						getPackageManager()).toString();
				tmpInfo.packageName = packageInfo.packageName;
				tmpInfo.versionName = packageInfo.versionName;
				tmpInfo.versionCode = packageInfo.versionCode;
				
				tmpInfo.appIcon = packageInfo.applicationInfo
						.loadIcon(getPackageManager());
                                tmpInfo.adPackagePrefix=known_apps.getProperty(packageInfo.packageName);
               
				// TODO Sort by packages name
				// Collections.sort(packages);
                java.util.Collections.sort(appList, new Sorter());
				appList.add(tmpInfo);
				Log.d(TAG, "tmpInfo: " + tmpInfo);
			}

			for (int i = 0; i < appList.size(); i++) {
				appList.get(i).print();
			}

			// Populate data to listView
			AppAdapter appAdapter = new AppAdapter(AppsInspector.this,appList);
			app_listView.setDividerHeight(5);
			app_listView.setTextFilterEnabled(true);
			//appAdapter.getFilter().filter("a");
			//appAdapter.notifyDataSetChanged();
			//app_listView.invalidate();

			if (app_listView != null) {
				app_listView.setAdapter(appAdapter);
			}
			
 
			// QuickAction
			ActionItem runItem = new ActionItem(ID_OPEN, "Run", getResources()
					.getDrawable(R.drawable.ic_menu_play_clip));
			ActionItem moreItem = new ActionItem(ID_MORE, "Info",
					getResources().getDrawable(R.drawable.ic_menu_info_details));
			ActionItem marketItem = new ActionItem(ID_MARKET, "Market",
					getResources().getDrawable(R.drawable.ic_market));
			ActionItem uninstallItem = new ActionItem(ID_UNINSTALL,
					"Uninstall", getResources().getDrawable(
							R.drawable.ic_menu_delete));

			final QuickAction mQuickAction = new QuickAction(
					AppsInspector.this);

			mQuickAction.addActionItem(runItem);
			mQuickAction.addActionItem(moreItem);
			mQuickAction.addActionItem(marketItem);
			mQuickAction.addActionItem(uninstallItem);
			
			// setup the action item click listener
			mQuickAction
					.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {

						public void onItemClick(QuickAction quickAction,
								int pos, int actionId) {

							PackageInfo pkg = result.get(mSelectedRow);
							
							Log.i(TAG, "onSelect: Pos:" + pos + " Row:"
									+ mSelectedRow + " Pkg:" + pkg.packageName);

							if (actionId == ID_OPEN) {
								Log.i(TAG, "onClick: Pos" + pos + " Row:"
										+ mSelectedRow + " Pkg:"
										+ pkg.packageName);
								// Try to open activity
								try {
									Intent i = new Intent(Intent.ACTION_MAIN);
									PackageManager manager = getPackageManager();
									i = manager
											.getLaunchIntentForPackage(pkg.packageName);
									i.addCategory(Intent.CATEGORY_LAUNCHER);
									startActivity(i);
								} catch (Exception e) {
									Toast.makeText(
											AppsInspector.this,
											"ERROR : Can't find "
													+ pkg.packageName
													+ " Did you uninstalled?",
											Toast.LENGTH_SHORT).show();
									Log.w(TAG, "ERROR : Can't find "
											+ pkg.packageName
											+ " Did you uninstalled?");
								}

							} else if (actionId == ID_MORE) {
								// http://stackoverflow.com/questions/5536330/how-to-show-details-for-installed-application-on-android
								if (Build.VERSION.SDK_INT >= 9) {
									// on 2.3 and newer, use APPLICATION_DETAILS_SETTINGS with proper URI
									try {
										Uri packageURI = Uri.parse("package:"
												+ pkg.packageName);
										Intent intent = new Intent(
												"android.settings.APPLICATION_DETAILS_SETTINGS",
												packageURI);
										startActivity(intent);
									} catch (Exception e) {
										Toast.makeText(
												AppsInspector.this,
												"ERROR : Can't find Did you uninstalled?",
												Toast.LENGTH_SHORT).show();
										Log.w(TAG,
												"Can't find Did you uninstalled?");
									}
								} else {
									// on older Androids, use trick to show app details
									try {
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										intent.setClassName(
												"com.android.settings",
												"com.android.settings.InstalledAppDetails");
										intent.putExtra(
												"com.android.settings.ApplicationPkgName",
												pkg.packageName);
										intent.putExtra("pkg", pkg.packageName);
										startActivity(intent);
									} catch (Exception e) {
										Toast.makeText(
												AppsInspector.this,
												"ERROR : Can't find  Did you uninstalled?",
												Toast.LENGTH_SHORT).show();
										Log.w(TAG,
												"Can't find Did you uninstalled?");
									}
								}

							} else if

							(actionId == ID_MARKET) {
								try {
									Intent intent = new Intent(
											Intent.ACTION_VIEW);
									intent.setData(Uri
											.parse("market://details?id="
													+ pkg.packageName));
									startActivity(intent);
								} catch (Exception e) {
									Toast.makeText(getApplicationContext(),
											"No Market application found",
											Toast.LENGTH_SHORT).show();
									Log.w(TAG, "Can't open Market with "
											+ pkg.packageName
											+ " Did you uninstalled?");
								}
							} else if

							(actionId == ID_UNINSTALL) {
								try {
									Intent i = new Intent(Intent.ACTION_DELETE);
									i.setData(Uri.parse("package:"
											+ pkg.packageName));
									startActivity(i);
								} catch (Exception e) {
									Toast.makeText(
											AppsInspector.this,
											"Can't find " + pkg.packageName
													+ " Did you uninstalled?",
											Toast.LENGTH_SHORT).show();
									Log.w(TAG, "ERROR : Can't find "
											+ pkg.packageName
											+ " Did you uninstalled?");
								}

							} else {
								Log.e(TAG, "ERROR : POS: " + pos + " ROW: "
										+ mSelectedRow + " PKG: "
										+ pkg.packageName
										+ "How did you get here?");
							}
						}
					});

			// setup on dismiss listener, set the icon back to normal
			mQuickAction
					.setOnDismissListener(new PopupWindow.OnDismissListener() {
						public void onDismiss() {
						// mMoreIv.setImageResource(R.drawable.ic_list_more);
						}
					});

			app_listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mSelectedRow = position; // set the selected row
					mQuickAction.show(view);
				}
			});
		}
    
    

		public class AppAdapter extends BaseAdapter {
	    
		   //Context context;
			ArrayList<AppInfo> dataList = new ArrayList<AppInfo>();

			public AppAdapter(Context context, ArrayList<AppInfo> inputDataList) {
			   //this.context = context;
				
				dataList.clear();

				for (int i = 0; i < inputDataList.size(); i++) {
					dataList.add(inputDataList.get(i));      
				}
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return dataList.size();
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return dataList.get(position);
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;

			}

			public View getView(int position, View convertView, ViewGroup parent) {
				View v = convertView;
				final AppInfo appUnit = dataList.get(position);

				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.app_row, null);
					v.setEnabled(true);
				}

				TextView appName = (TextView) v.findViewById(R.id.appName);
				TextView DetectedAddons = (TextView) v.findViewById(R.id.DetectedAddons);
				ImageView appIcon = (ImageView) v.findViewById(R.id.AppIcon);

				if (appName != null)
					appName.setText(appUnit.appName + " " + appUnit.versionName);
				if (appIcon != null)
					appIcon.setImageDrawable(appUnit.appIcon);
				if (DetectedAddons != null)
					DetectedAddons.setText(appUnit.adPackagePrefix);

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					running = false;
				}

				return v;
			}
		}
		
	    @Override
			protected void onProgressUpdate(Integer... progress) {
	    	// increment progress bar by progress value
			progressDialog.setProgress(progress[0]);
			progressDialog.setMessage("Scanning ...\n" + this.current_package);
			}

		@Override
		protected void onPreExecute() {
			running = true;
			//TODO Show the current scanned package name
			progressDialog = new ProgressDialog(AppsInspector.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle("Please Wait ...");
			progressDialog.setMessage("Loading ...");
			
			//Don't run the scanning when canceled
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
			          public void onCancel(DialogInterface dialog) {
			              AppsInspectorTask.cancel(true);
						  //Notify user that scanning is canceled
							Toast.makeText(AppsInspector.this,"Scanning canceled",Toast.LENGTH_SHORT).show();
							running = false;
			          }
			    });
			   
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}
	}
}
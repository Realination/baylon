package baylon.app;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Troll173 on 2/11/2016.
 */
public class Backup {
    public static Timer timer;
    public static Timer archiveTimer;

    public Backup(String dir,int interval){

        archiveTimer = new Timer();
        archiveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH-mm");
                Date date = new Date();



                    try {
                        String executeCmd = "mysqldump -u " + Settings.user + " -p" + Settings.pass + " " + Settings.dbname + " -r " + dir + "/FBU" + dateFormat.format(date) + ".sql";
                        System.out.println(executeCmd);
                        Process runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",executeCmd});
                        int processComplete = runtimeProcess.waitFor();
                        if (processComplete == 0) {
                            System.out.println("Backup taken successfully @ " + dir + File.separatorChar+"MGB" + dateFormat.format(date) + ".sql");
                        } else {
                            System.out.println("Could not take mysql backup @ " + dir + File.separatorChar+"MGB" + dateFormat.format(date) + ".sql");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                int archiveCount = new File(dir).list().length;
                    while (archiveCount > 3) {

                        File directory = new File(dir);
                        File[] files = directory.listFiles();

                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File f1, File f2) {
                                return Long.compare(f1.lastModified(), f2.lastModified());
                            }
                        });

                        System.out.println(files[0].getName());
                        files[0].delete();
                        archiveCount = new File(dir).list().length;
                    }










            }
        },0,interval);


    }

}

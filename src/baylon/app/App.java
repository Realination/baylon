/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baylon.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import baylon.app.*;

/**
 *
 * @author DarkMatter
 */
public class App {
    	private static HashMap<String, Class>  functions;
        private static HashMap<String,Class> models;

	// Singleton
		private App() {
            functions = new HashMap<String, Class>();
            models = new HashMap<String,Class>();

        }
		  
		private static App instance;
		  public static App getInstance(){
		    if ( instance == null ){
		      synchronized(App.class){
		        if ( instance == null ){
		          instance = new App();
		          
		        }
		      } 
		    }


              Path path = Paths.get("target/classes/baylon/models");




              Object[] files = new Object[0];
              try {
                  files = Files.list(path).toArray();
              } catch (IOException e) {
                  e.printStackTrace();
              }

              for (Object file: files){

                  String sep = File.separatorChar+"";

                  String[] fileArr = file.toString().replace(sep,"/").split("/");
                  String modelName = fileArr[fileArr.length-1].replace(".class","");

                  try {
                      Class mclass = ClassLoader.getSystemClassLoader().loadClass("baylon.models." + modelName);
                      Object mc = mclass.getMethod("getInstance");
                      models.put("modelName",mclass.getClass());

                  } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                  } catch (NoSuchMethodException e) {
                      e.printStackTrace();
                  }
//                  System.out.println(modelName);

              }



              return instance;
		  }



            public Class getModel(String modelName){
                return models.get(modelName);
            }


			public void addHelper(String id, Class value) {

				if(!functions.containsKey(id)){

                    functions.put(id, value);
					       
				}else{
                    functions.remove(id);
                    functions.put(id, value);
				}
			}

			public Class getHelper(String identifier) {
			    return functions.get(identifier);
			}
			
			public boolean hasKey(String Key){
				return functions.containsKey(Key);
				
			}

			public HashMap<String, Class> getAllHelpers() {
			    return functions;
			}

			public void remove(String key){
                functions.remove(key);
			}
			
			public int count(){
				return functions.size();
			}
			
			public void clean() {
                functions.clear();
			  
			}
		  
}

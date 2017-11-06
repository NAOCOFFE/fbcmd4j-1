package org.fbcmd4j;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.Post;


public class Utils {
	private static final Logger logger = LogManager.getLogger(Utils.class);
	public static Properties loadConfigFile(String folderName, String fileName) throws IOException {
	Properties props = new Properties();
	Path configFile = Paths.get(folderName, fileName);
	props.load(Files.newInputStream(configFile));
	BiConsumer<Object, Object> emptyProperty = (k, v) -> {
		if(((String)v).isEmpty())
		logger.info("La propiedad '" + k + "' esta vacía");};
		props.forEach(emptyProperty);
		return props;}
		
	public static Facebook configFacebook(Properties props) {
		Facebook fb = new FacebookFactory().getInstance();
		fb.setOAuthAppId(props.getProperty("oauth.appId"), props.getProperty("oauth.appSecret"));
		fb.setOAuthPermissions(props.getProperty("oauth.permissions"));
		fb.setOAuthAccessToken(new AccessToken(props.getProperty("oauth.accessToken"), null));
		return fb;}
	
	public static void printPost(Post p) {
		if(p.getStory() != null){
			System.out.println("Story: " + p.getStory());}
		if(p.getMessage() != null){
			System.out.println("Mensaje: " + p.getMessage());}
		System.out.println("--------------------------------");
	}
	
public static void postStatus(String msg, Facebook fb) {
		try {fb.postStatusMessage(msg);} 
		catch (FacebookException e) {logger.error(e);}		
	}
	
}

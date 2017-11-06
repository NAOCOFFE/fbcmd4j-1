package facebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.Post;
import facebook4j.ResponseList;

public class Main {
	static final Logger logger = LogManager.getLogger(Main.class);
	// Recuerda cambiar el path antes de exportarlo sino, el jar requerirá un
	// directorio llamado config para guardar las configuraciones
	private static final String CONFIG_DIR = "config";
	private static final String CONFIG_FILE = "fbcmd4j.properties";

	public static void main(String[] args) {
		logger.info("Iniciando app");
		Facebook fb =  null;
		Properties props = null;
		// Carga propiedades
		try {
			props = Utils.loadConfigFile(CONFIG_DIR, CONFIG_FILE);
		} catch (IOException ex) {
			logger.error(ex);
		}
		
		int seleccion;
		try {
			Scanner scanner = new Scanner(System.in);
			while(true) {
				// Inicio Menu
				fb = Utils.configFacebook(props);
				System.out.println("Cliente de Facebook\n\n");
				System.out.println("Opciones: \n");
				System.out.println("(1) NewsFeed \n");
				System.out.println("(2) Wall \n");
				System.out.println("(3) Publicar Estado \n");
				System.out.println("(4) Publicar Link \n");
				System.out.println("(5) Salir \n\n");
				System.out.println("Por favor ingrese una opción:");
				// Fin de Menu
				try {
					seleccion= scanner.nextInt();
					scanner.nextLine();
					switch (seleccion) {
					case 1:
						System.out.println("Mostrando NewsFeed...");
						ResponseList<Post> newsFeed = fb.getFeed();
						for (Post p : newsFeed) {
							Utils.printPost(p);
						}
						SV("NewsFeed", newsFeed,scanner);
						break;
					case 2:
						System.out.println("Mostrando Wall...");
						ResponseList<Post> wall = fb.getPosts();
						for (Post p : wall) {
							Utils.printPost(p);
						}		
						SV("Wall", wall, scanner);
						break;
					case 3:
						System.out.println("Escribe tu estado: ");
						String estado = scanner.nextLine();
						Utils.postStatus(estado, fb);
						break;
					case 4:
						System.out.println("Ingresa el link: ");
						String link = scanner.nextLine();
						Utils.postLink(link, fb);
						break;
					case 5:
						System.out.println("Gracias");
						System.exit(0);
						break;
					default:
						logger.error("Opción inválida");
						break;
					}
				} catch (InputMismatchException ex) {
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error("Opción inválida. %s. \n", ex.getClass());
				} catch (FacebookException ex) {
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error(ex.getErrorMessage());
				} catch (Exception ex) {
					System.out.println("Ocurrió un errror, favor de revisar log.");
					logger.error(ex);
					
				}
				System.out.println();
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
	
	
	public static void SV(String fileName, ResponseList<Post> posts, Scanner scanner) {
		System.out.println("¿Quieres guardar lo mostrado en un archivo txt?");
		String seleccion= scanner.nextLine();
		
		if (seleccion.contains("Si") || seleccion.contains("si")) {
			List<Post> post = new ArrayList<>();
			int num = 0;

			while(num <= 0) {
				try {
					System.out.println("¿Cuantas lineas quieres guaradar?");
					num = Integer.parseInt(scanner.nextLine());					
			
					if(num <= 0) {
						System.out.println("Ingrese un numero valido!!!!!");
					} else {
						for(int i = 0; i<num; i++) {
							if(i>posts.size()-1) break;
							post.add(posts.get(i));
						}
					}
				} catch(NumberFormatException e) {
					logger.error(e);
				}
			}

			Utils.SP(fileName, post);
		}
	}
}
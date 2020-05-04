import java.io.*; 

public class AntCode {
	public static String path = "build.xml";
 	public static java.io.Console console = System.console();
    public static void main(String[] args) throws Exception {
    	String username = new String();
    	String password = new String();
    	String action = "retrieve";
    	
    	if(args.length > 0) {
    		if(args.length == 1) {
    				action = args[0];
    				username = getUserName();
	       			password = getPassword();
    		} else if(args.length == 2){
    			action = args[0];
	       		username = getUserNameFromCommand(args[1]);
	       		password = getPassword();
    		// } else if(args.length < 3) {
    		// 	System.out.println("Enter atlease 1/3 arguments");
    		// 	System.out.println("1.Action (retrieve / deploy)\n2.Username (name@env / fullusername)\n3.Password");
    		// 	System.exit(0);
    		} else {
    			action = args[0];
	       		username = getUserNameFromCommand(args[1]);
	       		password = args[2];
    		}

    	} else {
	      	username = getUserName();
	       	password = getPassword();
	       	action = getActionName();
    	}
        
        String buildXml = getStringFromFile();
		String updatedBuildXml = buildXml.replace("{username}", username);
		updatedBuildXml = updatedBuildXml.replace("{password}", password);
		FileWriter myWriter = new FileWriter(path);
	    myWriter.write(updatedBuildXml.trim());
	    myWriter.close();
		// Run a command
		executeCommand("ant " + action);
		myWriter = new FileWriter(path);
	    myWriter.write(buildXml);
	    myWriter.close();
	}
	public static String getUserName() {
		return getUserNameFromCommand(console.readLine("Username: "));
	}
	public static String getActionName() {
		return getUserNameFromCommand(console.readLine("Action (retrieve/deploy): "));
	}
	public static String getPassword() {
		return new String(console.readPassword("Password: "));
	}
	public static void executeCommand(String command) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("cmd.exe", "/c", command);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if(!line.contains("Buildfile:")) {
					System.out.println(line);
				}
			}
			int exitVal = process.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getUserNameFromCommand(String username) {
		String[] list = username.split("@");
		if(list.length > 1 && list.length == 2) {
			if(!list[1].contains(".com")) {
				return list[0] + "@minusculetechnologies.com." + list[1];
			}
		} else {
			System.out.println("Invalid Username\n format should be (name@env / fullusername)");
			System.exit(0);
		}
		return username;
	}
	public static String getStringFromFile() {
		String buildXml = "";
		try {
			File file = new File(path); 
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String st;
			while ((st = br.readLine()) != null) {
			  	buildXml+= st.replace(">",">\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildXml;
	}
}
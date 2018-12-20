package app.server;

import java.util.Date;

public class WebConsoleController {

	private final CommandHandler handler;
	
	public WebConsoleController(CommandHandler handler) {
		this.handler = handler;
	}

    public String get( WebConsoleFormData formData) {
    	 System.out.println("console served at " + (new Date()).toString());
    	 return "console";
    }

    public String post(WebConsoleFormData formData) {
        System.out.println("console served at " + (new Date()).toString());

        handler.hdlCmd(formData.getCmd(), formData);
        return "console";
    }

}
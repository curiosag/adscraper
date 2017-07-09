package app.server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/console" )
public class WebConsoleController {

	private final CommandHandler handler;
	
	public WebConsoleController(CommandHandler handler) {
		this.handler = handler;
	}

    @RequestMapping(method={RequestMethod.GET})
    public String get(@ModelAttribute WebConsoleFormData formData, Model model) {
    	 System.out.println("console served at " + (new Date()).toString());
    	 return "console";
    }

    @RequestMapping(method={RequestMethod.POST})
    public String post(@ModelAttribute WebConsoleFormData formData, Model model, BindingResult result) {
        System.out.println("console served at " + (new Date()).toString());

        handler.hdlCmd(formData.getCmd(), formData);
        return "console";
    }

}
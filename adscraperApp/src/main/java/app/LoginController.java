package app;

import app.dto.LoginForm;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller

public class LoginController {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(ModelMap model){
        return "login";
    }

    // is taken over by formlogin in websecurityconfig and will never get called here
    @RequestMapping(value = "/in", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginForm loginForm,
                        BindingResult result, Model model) throws ServletException {
        try {
            request.login(loginForm.getUsername(), loginForm.getPassword());
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            if (savedRequest != null) {
                return "redirect:" + savedRequest.getRedirectUrl();
            } else {
                return "redirect:/";
            }

        } catch (ServletException authenticationFailed) {
            result.rejectValue(null, "authentication.failed");
            model.addAttribute("loginerror", true);
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }

}
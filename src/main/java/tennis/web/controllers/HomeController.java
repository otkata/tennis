package tennis.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController{

    @GetMapping("/")
    public ModelAndView home(){
        return super.view("index");
    }

    @GetMapping("/home")
    public ModelAndView home(ModelAndView modelAndView, HttpSession session){
        if(session.getAttribute("username") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {


            modelAndView.setViewName("home");

        }

        return modelAndView;
    }
}

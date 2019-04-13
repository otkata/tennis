package org.softuni.tennis.web.controllers;

import org.softuni.tennis.error.TournamentNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {
    @ExceptionHandler({TournamentNotFoundException.class})
    public ModelAndView handleTournamentNotFound(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

}

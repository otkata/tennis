package org.softuni.tennis.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.tennis.domain.models.binding.TournamentAddBindingModel;
import org.softuni.tennis.domain.models.service.TournamentServiceModel;
import org.softuni.tennis.domain.models.view.TournamentAllViewModel;
import org.softuni.tennis.domain.models.view.TournamentDetailsViewModel;
import org.softuni.tennis.service.CategoryService;
import org.softuni.tennis.service.CloudinaryService;
import org.softuni.tennis.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tournaments")
public class TournamentController extends BaseController {

    private final TournamentService tournamentService;
    private final CloudinaryService cloudinaryService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public TournamentController(TournamentService tournamentService, CloudinaryService cloudinaryService, CategoryService categoryService, ModelMapper modelMapper) {
        this.tournamentService = tournamentService;
        this.cloudinaryService = cloudinaryService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addTournament() {
        return super.view("tournament/add-tournament");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addTournamentConfirm(@ModelAttribute TournamentAddBindingModel model) throws IOException {
        TournamentServiceModel tournamentServiceModel = this.modelMapper.map(model, TournamentServiceModel.class);
        tournamentServiceModel.setCategories(
                this.categoryService.findAllCategories()
                        .stream()
                        .filter(c -> model.getCategories().contains(c.getId()))
                        .collect(Collectors.toList())
        );
        tournamentServiceModel.setImageUrl(
                this.cloudinaryService.uploadImage(model.getImage())
        );

        this.tournamentService.addTournament(tournamentServiceModel);

        return super.redirect("/tournaments/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allTournaments(ModelAndView modelAndView) {
        modelAndView.addObject("tournaments", this.tournamentService.findAllTournaments()
                .stream()
                .map(p -> this.modelMapper.map(p, TournamentAllViewModel.class))
                .collect(Collectors.toList()));

        return super.view("tournament/all-tournaments", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsTournament(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("tournament", this.modelMapper.map(this.tournamentService.findTournamentById(id), TournamentDetailsViewModel.class));

        return super.view("tournament/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editTournament(@PathVariable String id, ModelAndView modelAndView) {
        TournamentServiceModel tournamentServiceModel = this.tournamentService.findTournamentById(id);
        TournamentAddBindingModel model = this.modelMapper.map(tournamentServiceModel, TournamentAddBindingModel.class);
        model.setCategories(tournamentServiceModel.getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()));

        modelAndView.addObject("tournament", model);
        modelAndView.addObject("tournamentId", id);

        return super.view("tournament/edit-tournament", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editTournamentConfirm(@PathVariable String id, @ModelAttribute TournamentAddBindingModel model) {
        this.tournamentService.editTournament(id, this.modelMapper.map(model, TournamentServiceModel.class));

        return super.redirect("/tournaments/details/" + id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteTournament(@PathVariable String id, ModelAndView modelAndView) {
        TournamentServiceModel tournamentServiceModel = this.tournamentService.findTournamentById(id);
        TournamentAddBindingModel model = this.modelMapper.map(tournamentServiceModel, TournamentAddBindingModel.class);
        model.setCategories(tournamentServiceModel.getCategories().stream().map(c -> c.getName()).collect(Collectors.toList()));

        modelAndView.addObject("tournament", model);
        modelAndView.addObject("tournamentId", id);

        return super.view("tournament/delete-tournament", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteTournamentConfirm(@PathVariable String id) {
        this.tournamentService.deleteTournament(id);

        return super.redirect("/tournament/all");
    }

    @GetMapping("/fetch/{category}")
    @ResponseBody
    public List<TournamentAllViewModel> fetchByCategory(@PathVariable String category) {
        if(category.equals("all")) {
            return this.tournamentService.findAllTournaments()
                    .stream()
                    .map(tournament -> this.modelMapper.map(tournament, TournamentAllViewModel.class))
                    .collect(Collectors.toList());
        }

        return this.tournamentService.findAllByCategory(category)
                .stream()
                .map(tournament -> this.modelMapper.map(tournament, TournamentAllViewModel.class))
                .collect(Collectors.toList());
    }

}

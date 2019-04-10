package org.softuni.tennis.service;

import org.modelmapper.ModelMapper;
import org.softuni.tennis.domain.entities.Category;
import org.softuni.tennis.domain.entities.Tournament;
import org.softuni.tennis.domain.models.service.TournamentServiceModel;
import org.softuni.tennis.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.tournamentRepository = tournamentRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public TournamentServiceModel addTournament(TournamentServiceModel tournamentServiceModel) {
        Tournament tournament = this.modelMapper.map(tournamentServiceModel, Tournament.class);

        return this.modelMapper.map(this.tournamentRepository.saveAndFlush(tournament), TournamentServiceModel.class);
    }

    @Override
    public List<TournamentServiceModel> findAllTournaments() {
        return this.tournamentRepository.findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, TournamentServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public TournamentServiceModel findTournamentById(String id) {
        return this.tournamentRepository.findById(id)
                .map(p -> this.modelMapper.map(p, TournamentServiceModel.class))
                .orElseThrow(() -> new IllegalArgumentException());
    }

    @Override
    public TournamentServiceModel editTournament(String id, TournamentServiceModel tournamentServiceModel) {
        Tournament tournament = this.tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        tournamentServiceModel.setCategories(
                this.categoryService.findAllCategories()
                        .stream()
                        .filter(c -> tournamentServiceModel.getCategories().contains(c.getId()))
                .collect(Collectors.toList())
        );

        tournament.setName(tournamentServiceModel.getName());
        tournament.setDescription(tournamentServiceModel.getDescription());
        tournament.setPrice(tournamentServiceModel.getPrice());
        tournament.setCategories(
                tournamentServiceModel.getCategories()
                .stream()
                .map(c -> this.modelMapper.map(c, Category.class))
                .collect(Collectors.toList())
        );

        return this.modelMapper.map(this.tournamentRepository.saveAndFlush(tournament), TournamentServiceModel.class);
    }

    @Override
    public void deleteTournament(String id) {
        Tournament tournament = this.tournamentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        this.tournamentRepository.delete(tournament);
    }

    @Override
    public List<TournamentServiceModel> findAllByCategory(String category) {


        return this.tournamentRepository.findAll()
                .stream()
                .filter(tournament -> tournament.getCategories().stream().anyMatch(categoryStream -> categoryStream.getName().equals(category)))
                .map(tournament -> this.modelMapper.map(tournament, TournamentServiceModel.class))
                .collect(Collectors.toList());
    }

}

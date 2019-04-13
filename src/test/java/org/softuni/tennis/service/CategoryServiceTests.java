package org.softuni.tennis.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.tennis.domain.entities.Category;
import org.softuni.tennis.domain.models.service.CategoryServiceModel;
import org.softuni.tennis.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTests {

    @Autowired
    private   CategoryRepository categoryRepository;

    private   ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
    }


    @Test
    public void categoryService_addCategoryCorrectValues_ReturnsCorrect(){
        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        CategoryServiceModel toBeAdd = new CategoryServiceModel();
        toBeAdd.setName("WTA");

        CategoryServiceModel actual = categoryService.addCategory(toBeAdd);
        CategoryServiceModel expected = this.modelMapper
                .map(this.categoryRepository.findAll().get(0), CategoryServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
    }


    @Test(expected = Exception.class)
    public void categoryService_addCategoryNullValues_ThrowsException() {

        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        CategoryServiceModel toBeAdd = new CategoryServiceModel();
        toBeAdd.setName(null);

        categoryService.addCategory(toBeAdd);

    }

    @Test
    public void categoryService_editCategoryCorrectValues_ReturnsCorrect(){
        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        Category category = new Category();
        category.setName("WTA");

        category= this.categoryRepository.saveAndFlush(category);

        CategoryServiceModel toBeEdited = new CategoryServiceModel();
        toBeEdited.setId(category.getId());
        toBeEdited.setName("Gosho");

        CategoryServiceModel actual = categoryService.editCategory(category.getId(), toBeEdited);
        CategoryServiceModel expected = this.modelMapper.map(this.categoryRepository.findAll().get(0), CategoryServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());


    }

    @Test(expected = Exception.class)
    public void categoryService_editCategoryNullValues_ThrowsException() {
        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        Category category = new Category();
        category.setName("WTA");

        category= this.categoryRepository.saveAndFlush(category);

        CategoryServiceModel toBeEdited = new CategoryServiceModel();
        toBeEdited.setId(category.getId());
        toBeEdited.setName(null);

        categoryService.editCategory(category.getId(), toBeEdited);
    }

    @Test
    public void categoryService_deleteCategoryCorrectValues_ReturnsCorrect(){
        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        Category category = new Category();
        category.setName("WTA");

        category = this.categoryRepository.saveAndFlush(category);

        categoryService.deleteCategory(category.getId());
        long expectedCount = 0;
        long actualCount = this.categoryRepository.count();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void categoryService_deleteInvalidValues_ThrowsException() {
        CategoryService categoryService = new CategoryServiceImpl(this.categoryRepository, this.modelMapper);

        Category category = new Category();
        category.setName("WTA");

        category= this.categoryRepository.saveAndFlush(category);

        categoryService.deleteCategory("InvalidId");

    }

    }

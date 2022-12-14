package com.server.graphQL.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.server.graphQL.entity.Author;
import com.server.graphQL.entity.Tutorial;
import com.server.graphQL.repo.AuthorRepo;
import com.server.graphQL.repo.TutorialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javassist.NotFoundException;

import java.util.Optional;

@Component
public class Mutation implements GraphQLMutationResolver {
    @Autowired
    private AuthorRepo authorRepository;
    @Autowired
    private TutorialRepo tutorialRepository;

//    @Autowired
//    public Mutation(AuthorRepo authorRepository, TutorialRepo tutorialRepository) {
//        this.authorRepository = authorRepository;
//        this.tutorialRepository = tutorialRepository;
//    }

    public Author createAuthor(String name, Integer age) {
        Author author = new Author();
        author.setName(name);
        author.setAge(age);

        authorRepository.save(author);

        return author;
    }

    public Author updateAuthor(Long id, String name, Integer age) {
        Optional<Author> optAuthor = authorRepository.findById(id);
        Author author = new Author();
        Author newA = new Author();
        if (optAuthor.isPresent()) {
            author = optAuthor.get();
            author.setId(id);
            author.setName(name);
            author.setAge(age);

            authorRepository.save(author);
        }

        return author;
    }

    public Tutorial createTutorial(String title, String description, Long authorId) {
        Tutorial tutorial = new Tutorial();
        tutorial.setAuthor(new Author(authorId));
        tutorial.setTitle(title);
        tutorial.setDescription(description);

        tutorialRepository.save(tutorial);

        return tutorial;
    }

    public boolean deleteTutorial(Long id) {
        tutorialRepository.deleteById(id);
        return true;
    }

    public Tutorial updateTutorial(Long id, String title, String description) throws NotFoundException {
        Optional<Tutorial> optTutorial = tutorialRepository.findById(id);

        if (optTutorial.isPresent()) {
            Tutorial tutorial = optTutorial.get();

            if (title != null)
                tutorial.setTitle(title);
            if (description != null)
                tutorial.setDescription(description);

            tutorialRepository.save(tutorial);
            return tutorial;
        }

        throw new NotFoundException("Not found Tutorial to update!");
    }

}

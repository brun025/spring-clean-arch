package br.com.api.application.user.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.api.IntegrationTest;
import br.com.api.domain.pagination.SearchQuery;
import br.com.api.domain.user.User;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

import java.util.stream.Stream;

@IntegrationTest
public class ListUsersUseCaseIT {

    @Autowired
    private ListUsersUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void mockUp() {
        final var users = Stream.of(
                        User.newUser("user Filmes", "email1@gmail.com", "12345", true),
                        User.newUser("user Netflix", "email2@gmail.com", "12345", true),
                        User.newUser("user Amazon", "email3@gmail.com", "12345", true),
                        User.newUser("user Documentários", "email4@gmail.com", "12345", true),
                        User.newUser("user Sports", "email5@gmail.com", "12345", true),
                        User.newUser("user Kids", "email6@gmail.com", "12345", true),
                        User.newUser("user Series", "email7@gmail.com", "12345", true)
                )
                .map(UserEntity::from)
                .toList();

        userRepository.saveAllAndFlush(users);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,user Filmes",
            "net,0,10,1,1,user Netflix",
            "ZON,0,10,1,1,user Amazon",
            "KI,0,10,1,1,user Kids",
            //"email6,0,10,1,1,user Kids",
           // "email3,0,10,1,1,user Amazon",
    })
    public void givenAValidTerm_whenCallsListUsers_shouldReturnUsersFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedUserName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedUserName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,user Amazon",
            "name,desc,0,10,7,7,user Sports",
            "createdAt,asc,0,10,7,7,user Filmes",
            "createdAt,desc,0,10,7,7,user Series",
    })
    public void givenAValidSortAndDirection_whenCallsListUsers_thenShouldReturnUsersOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedUserName
    ) {
        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedUserName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,user Amazon;user Documentários",
            "1,2,2,7,user Filmes;user Kids",
            "2,2,2,7,user Netflix;user Series",
            "3,2,1,7,user Sports",
    })
    public void givenAValidPage_whenCallsListUsers_shouldReturnUsersPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedUsersName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedUsersName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }
}

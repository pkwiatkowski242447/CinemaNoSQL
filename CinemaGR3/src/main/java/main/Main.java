package main;

import com.datastax.oss.driver.api.core.CqlSession;
import model.managers.implementations.ClientManager;
import model.managers.implementations.MovieManager;
import model.managers.implementations.TicketManager;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.exceptions.repositories.delete_exceptions.RepositoryDeleteException;
import model.repositories.implementations.CassandraClient;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try(CqlSession session = CassandraClient.initializeCassandraSession()) {

            ClientRepository clientRepository = new ClientRepository(session);
            MovieRepository movieRepository = new MovieRepository(session);
            TicketRepository ticketRepository = new TicketRepository(session);

            ClientManager clientManager = new ClientManager(clientRepository);
            MovieManager movieManager = new MovieManager(movieRepository);
            TicketManager ticketManager = new TicketManager(clientRepository, movieRepository, ticketRepository);

            Instant movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime().toInstant();
            Instant movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime().toInstant();
            Instant movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime().toInstant();

            Instant reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime().toInstant();
            Instant reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime().toInstant();
            Instant reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime().toInstant();

            String clientNo1Name = "John";
            String clientNo1Surname = "Smith";
            int clientNo1Age = 21;

            String clientNo2Name = "Mary";
            String clientNo2Surname = "Jane";
            int clientNo2Age = 18;

            String clientNo3Name = "Vincent";
            String clientNo3Surname = "Vega";
            int clientNo3Age = 40;

            int screeningRoomNo1Number = 10;
            int screeningRoomNo1NumberOfAvailSeats = 45;
            String movieNo1Title = "Harry Potter and The Goblet of Fire";
            double movieNo1BasePrice = 20.05;

            int screeningRoomNo2Number = 5;
            int screeningRoomNo2NumberOfAvailSeats = 90;
            String movieNo2Title = "The Da Vinci Code";
            double movieNo2BasePrice = 40.5;

            int screeningRoomNo3Number = 19;
            int screeningRoomNo3NumberOfAvailSeats = 120;
            String movieNo3Title = "A Space Odyssey";
            double movieNo3BasePrice = 59.99;

            Client clientNo1 = clientManager.create(clientNo1Name, clientNo1Surname, clientNo1Age);
            Client clientNo2 = clientManager.create(clientNo2Name, clientNo2Surname, clientNo2Age);
            Client clientNo3 = clientManager.create(clientNo3Name, clientNo3Surname, clientNo3Age);

            Movie movieNo1 = movieManager.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
            Movie movieNo2 = movieManager.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
            Movie movieNo3 = movieManager.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);

            Ticket ticketNo1 = ticketManager.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieID(), clientNo1.getClientID());
            Ticket ticketNo2 = ticketManager.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieID(), clientNo2.getClientID());
            Ticket ticketNo3 = ticketManager.createReducedTicket(movieTimeNo3, reservationTimeNo3, movieNo3.getMovieID(), clientNo3.getClientID());

            try {
                List<Client> listOfClients = clientManager.findAll();
                int numOfClientsBefore = listOfClients.size();
                for (Client client : listOfClients) {
                    System.out.println(client.toString());
                }

                List<Ticket> listOfTickets = ticketManager.findAll();
                int numOfTicketsBefore = listOfTickets.size();
                for (Ticket ticket : listOfTickets) {
                    System.out.println(ticket.toString());
                }

                List<Movie> listOfMovies = movieManager.findAll();
                int numOfMoviesBefore = listOfMovies.size();
                for (Movie movie : listOfMovies) {
                    System.out.println(movie.toString());
                }

                for (Ticket ticket : listOfTickets) {
                    ticketManager.getTicketRepository().delete(ticket);
                }

                int numOfTicketsAfter = ticketManager.findAll().size();

                for (Movie movie : listOfMovies) {
                    movieManager.getMovieRepository().delete(movie);
                }

                int numOfMoviesAfter = movieManager.findAll().size();

                for (Client client : listOfClients) {
                    clientManager.getClientRepository().delete(client);
                }

                int numOfClientsAfter = clientManager.findAll().size();

                System.out.println("Number of tickets before: " + numOfTicketsBefore);
                System.out.println("Number of tickets after: " + numOfTicketsAfter);

                System.out.println("Number of movies before: " + numOfMoviesBefore);
                System.out.println("Number of movies after: " + numOfMoviesAfter);

                System.out.println("Number of clients before: " + numOfClientsBefore);
                System.out.println("Number of clients after: " + numOfClientsAfter);
            } catch (RepositoryDeleteException exception) {
                throw new RuntimeException("Error while removing objects from repository.", exception);
            }
        } catch (Exception exception) {
            throw new RuntimeException("Unknown exception was thrown.", exception);
        }
    }
}

package main;

import model.constants.GeneralConstants;
import model.exceptions.MongoConfigNotFoundException;
import model.exceptions.managers.create_exceptions.ClientManagerCreateException;
import model.exceptions.managers.create_exceptions.MovieManagerCreateException;
import model.exceptions.managers.create_exceptions.TicketManagerCreateException;
import model.exceptions.managers.delete_exceptions.DeleteManagerException;
import model.exceptions.managers.read_exceptions.ReadManagerException;
import model.managers.implementations.ClientManager;
import model.managers.implementations.MovieManager;
import model.managers.implementations.TicketManager;
import model.model.Client;
import model.model.Movie;
import model.model.Ticket;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            ClientRepository clientRepository = new ClientRepository(GeneralConstants.PROD_DB_NAME);
            MovieRepository movieRepository = new MovieRepository(GeneralConstants.PROD_DB_NAME);
            TicketRepository ticketRepository = new TicketRepository(GeneralConstants.PROD_DB_NAME);

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

            Client clientNo1;
            Client clientNo2;
            Client clientNo3;

            try {
                clientNo1 = clientManager.create(clientNo1Name, clientNo1Surname, clientNo1Age);
                clientNo2 = clientManager.create(clientNo2Name, clientNo2Surname, clientNo2Age);
                clientNo3 = clientManager.create(clientNo3Name, clientNo3Surname, clientNo3Age);
            } catch (ClientManagerCreateException exception) {
                throw new RuntimeException("Sample clients could not be created in the repository..", exception);
            }

            Movie movieNo1;
            Movie movieNo2;
            Movie movieNo3;

            try {
                movieNo1 = movieManager.create(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
                movieNo2 = movieManager.create(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
                movieNo3 = movieManager.create(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);
            } catch (MovieManagerCreateException exception) {
                throw new RuntimeException("Sample movies could not be created in the repository..", exception);
            }

            try {
                ticketManager.createNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1.getMovieID(), clientNo1.getClientID());
                ticketManager.createReducedTicket(movieTimeNo2, reservationTimeNo2, movieNo2.getMovieID(), clientNo2.getClientID());
                ticketManager.createReducedTicket(movieTimeNo3, reservationTimeNo3, movieNo3.getMovieID(), clientNo3.getClientID());
            } catch (TicketManagerCreateException exception) {
                throw new RuntimeException("Sample tickets could not be created in the repository.", exception);
            }

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
                    ticketManager.delete(ticket);
                }

                int numOfTicketsAfter = ticketManager.findAll().size();

                for (Movie movie : listOfMovies) {
                    movieManager.delete(movie);
                }

                int numOfMoviesAfter = movieManager.findAll().size();

                for (Client client : listOfClients) {
                    clientManager.delete(client);
                }

                int numOfClientsAfter = clientManager.findAll().size();

                System.out.println("Number of tickets before: " + numOfTicketsBefore);
                System.out.println("Number of tickets after: " + numOfTicketsAfter);

                System.out.println("Number of movies before: " + numOfMoviesBefore);
                System.out.println("Number of movies after: " + numOfMoviesAfter);

                System.out.println("Number of clients before: " + numOfClientsBefore);
                System.out.println("Number of clients after: " + numOfClientsAfter);
            } catch (ReadManagerException exception) {
                throw new RuntimeException("Exception occurred when performing read operations.", exception);
            } catch (DeleteManagerException exception) {
                throw new RuntimeException("Exception occurred when performing delete operations.", exception);
            }
        } catch (MongoConfigNotFoundException exception) {
            throw new RuntimeException("Configuration file for connection with MongoDB cluster was not found.", exception);
        }
    }
}

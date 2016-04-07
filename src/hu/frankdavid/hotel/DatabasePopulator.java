package hu.frankdavid.hotel;

import com.mysema.query.jpa.impl.JPAQuery;
import eu.codearte.fairyland.Fairy;
import eu.codearte.fairyland.producer.person.Person;
import hu.frankdavid.hotel.entity.Guest;
import hu.frankdavid.hotel.entity.Reservation;
import hu.frankdavid.hotel.entity.Room;
import hu.frankdavid.hotel.entity.querydsl.QRoom;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.logging.Logger;

@Startup
@Singleton
public class DatabasePopulator {
    Fairy fairy = Fairy.create();

    Random random = new Random();

    private static final int NUM_ROOMS = 5;

    private static final int NUM_MAX_PAST_RESERVATIONS = 5;

    private static final int NUM_MAX_FUTURE_RESERVATIONS = 5;

    @PersistenceContext
    EntityManager em;

    Logger logger = Logger.getLogger(getClass().getName());

    @PostConstruct
    public void populate() {
        if(new JPAQuery(em).from(QRoom.room).exists())
            return;
        logger.info("Populating database...");
        List<Integer> prices = Arrays.asList(10000, 15000);
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < NUM_ROOMS; i++) {
            Room room = new Room();
            room.setNumber(i + "");
            room.setSeaView(random.nextBoolean());
            room.setPricePerNight(room.isSeaView() ? 30000 : randomItem(prices));
            room.setSmoking(random.nextBoolean());
            room.setNumberOfBeds(random.nextInt(3) + 1);
            rooms.add(room);
            em.persist(room);
        }
        for (int i = 0; i < NUM_ROOMS; i++) {
            Guest guest = new Guest();
            Person person = fairy.person();
            guest.setName(person.fullName());
            guest.setPhone(person.telephoneNumber());
            guest.setEmail(person.email());
            guest.setPasswordHash("password");
            em.persist(guest);

            {
                Reservation reservation = new Reservation();
                reservation.setGuest(guest);
                reservation.setRoom(rooms.get(i));
                reservation.setStartDate(new Date());
                reservation.setEndDate(fairy.dateProducer().randomDateBetweenNowAndFuturePeriod(Period.days(10)).toDate());
                em.persist(reservation);
            }
            for (int j = 0; j < random.nextInt(NUM_MAX_PAST_RESERVATIONS); j++) {
                Reservation reservation = new Reservation();
                reservation.setGuest(guest);
                reservation.setRoom(randomItem(rooms));
                DateTime randomDateInThePast = fairy.dateProducer().randomDateInThePast(4);
                reservation.setStartDate(randomDateInThePast.toDate());
                reservation.setEndDate(randomDateInThePast.withFieldAdded(DurationFieldType.days(), random.nextInt(10) + 1).toDate());
                em.persist(reservation);
            }
            for (int j = 0; j < random.nextInt(NUM_MAX_FUTURE_RESERVATIONS); j++) {
                Reservation reservation = new Reservation();
                reservation.setGuest(guest);
                reservation.setRoom(randomItem(rooms));
                DateTime randomDateInTheFuture = fairy.dateProducer().randomDateBetweenNowAndFuturePeriod(Period.years(2));
                reservation.setStartDate(randomDateInTheFuture.toDate());
                reservation.setEndDate(randomDateInTheFuture.withFieldAdded(DurationFieldType.days(), random.nextInt(10) + 1).toDate());
                em.persist(reservation);
            }
            logger.info("Populating database done");
        }
    }

    private <T> T randomItem(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}

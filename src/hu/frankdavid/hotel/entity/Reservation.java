package hu.frankdavid.hotel.entity;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.List;

@Entity
public class Reservation extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    private Guest guest;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private Date createdAt = new Date();

    private boolean checkedIn;

    private boolean checkedOut;

    private Long fullPrice = 0L;

    @OneToMany(mappedBy = "reservation")
    @XmlTransient
    private List<Purchase> purchases;


    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getFullPrice() {
        return fullPrice;
    }

    public void calculateFullPrice() {
        fullPrice = room.getPricePerNight() * getNights() + getTotalPurchases();
    }

    public boolean isCancellable() {
        return new Duration(new DateTime(), new DateTime(startDate)).getStandardDays() >= 7;
    }

    @XmlTransient
    public int getTotalPurchases() {
        int s = 0;
        for (Purchase purchase : purchases) {
            s += purchase.getAmount();
        }
        return s;
    }

    public void checkIn() {
        checkedIn = true;
    }

    public void checkOut() {
        if (!checkedIn)
            throw new IllegalStateException("Cannot check out a reservation that has not been checked in");
        checkedOut = true;
    }

    public boolean isCheckInAvailable() {
        return
                (new DateTime(startDate).withTimeAtStartOfDay().equals(new DateTime().withTimeAtStartOfDay()) ||
                        new DateTime(startDate).withTimeAtStartOfDay().isBefore(new DateTime().withTimeAtStartOfDay())) &&
                        !isCheckedIn();
    }

    public boolean isCheckOutAvailable() {
        return
                checkedIn &&
                        (new DateTime(endDate).withTimeAtStartOfDay().equals(new DateTime().withTimeAtStartOfDay()) ||
                                new DateTime(endDate).withTimeAtStartOfDay().isBefore(new DateTime().withTimeAtStartOfDay())) &&
                        !isCheckedOut();
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public long getNights() {
        return new Duration(new DateTime(startDate), new DateTime(endDate)).getStandardDays();
    }
}

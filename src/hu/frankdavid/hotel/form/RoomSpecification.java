package hu.frankdavid.hotel.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomSpecification {

    private Date startDate;

    @Future
    private Date endDate;

    private boolean smoking;

    private boolean seaView;

    @Min(1)
    private Integer numberOfBeds;

    private Long wholePrice;

    private Integer pricePerNight;

    private Long roomId;

    @AssertTrue
    @XmlTransient
    public boolean isEndDateIsAfterStartDate() {
        return endDate.after(startDate);
    }

    @AssertTrue
    @XmlTransient
    public boolean isStartDateAfterToday() {
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        return startDate.after(today);
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

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(Boolean smoking) {
        if(smoking != null)
            this.smoking = smoking;
    }

    public boolean isSeaView() {
        return seaView;
    }

    public void setSeaView(Boolean seaView) {
        if(seaView != null)
            this.seaView = seaView;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public Long getWholePrice() {
        return wholePrice;
    }

    public void setWholePrice(Long wholePrice) {
        this.wholePrice = wholePrice;
    }

    public Integer getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Integer pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @XmlTransient
    public Long getRoomId() {
        return roomId;
    }
}

package hu.frankdavid.hotel.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PurchaseForm {
    private String roomNumber;

    @Min(value = 1, message = "Az értéknek 0-nál nagyobbnak kell lennie")
    private Integer amount;

    private String description;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

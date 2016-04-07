package hu.frankdavid.hotel.webservice;

import hu.frankdavid.hotel.entity.User;
import hu.frankdavid.hotel.form.RoomSpecification;
import hu.frankdavid.hotel.service.GuestService;
import hu.frankdavid.hotel.service.ReservationService;
import hu.frankdavid.hotel.service.UserService;
import hu.frankdavid.hotel.webservice.dto.Purchase;
import org.jboss.resteasy.spi.UnauthorizedException;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebService(endpointInterface = "hu.frankdavid.hotel.webservice.GuestWS", name="guestService")
public class GuestWSImpl implements GuestWS {

    @EJB
    GuestService guestService;

    @EJB
    ReservationService reservationService;

    @EJB
    UserService userService;

    @Override
    public List<Purchase> listPurchases(String guestEmail, String guestPassword, Long reservationId) throws IncorrectCredentialsFault, UnauthorizedFault {
        User guest = guestService.findGuestByEmailAndPassword(guestEmail, guestPassword);
        if(guest == null)
            throw new IncorrectCredentialsFault();
        try {
            List<hu.frankdavid.hotel.entity.Purchase> purchases = reservationService.findPurchasesForReservationWithAuthorization(reservationId, guest);
            if(purchases == null)
                return null;
            else
                return purchaseListToPurchaseDTOList(purchases);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedFault();
        }
    }

    @Override
    public boolean makeReservation(String guestEmail, String guestPassword, Date startDate, Date endDate, int numberOfBeds, boolean smoking, boolean seaView) throws IncorrectCredentialsFault {
        User user = userService.authenticate(guestEmail, guestPassword);
        if(user == null)
            throw new IncorrectCredentialsFault();
        RoomSpecification roomSpecification = new RoomSpecification();
        roomSpecification.setStartDate(startDate);
        roomSpecification.setEndDate(endDate);
        roomSpecification.setNumberOfBeds(numberOfBeds);
        roomSpecification.setSmoking(smoking);
        roomSpecification.setSeaView(seaView);
        boolean reservationResult = reservationService.makeReservation(roomSpecification);
        return reservationResult;
    }

    private List<Purchase> purchaseListToPurchaseDTOList(List<hu.frankdavid.hotel.entity.Purchase> purchases) {
        List<Purchase> purchaseDTOs = new ArrayList<>();
        for (hu.frankdavid.hotel.entity.Purchase purchase : purchases) {
            Purchase dto = new Purchase();
            dto.setAmount(purchase.getAmount());
            dto.setCreatedAt(purchase.getCreatedAt());
            dto.setDescription(purchase.getDescription());
            purchaseDTOs.add(dto);
        }
        return purchaseDTOs;
    }
}

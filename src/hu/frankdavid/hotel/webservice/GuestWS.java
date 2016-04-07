package hu.frankdavid.hotel.webservice;

import hu.frankdavid.hotel.webservice.dto.Purchase;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;

@WebService
public interface GuestWS {
    @WebMethod
    List<Purchase> listPurchases(
            @WebParam(name = "guestEmail") String guestEmail,
            @WebParam(name = "guestPassword") String guestPassword,
            @WebParam(name = "reservationId") Long reservationId) throws IncorrectCredentialsFault, UnauthorizedFault;

    @WebMethod
    boolean makeReservation(
            @WebParam(name = "guestEmail") String guestEmail,
            @WebParam(name = "guestPassword") String guestPassword,
            @WebParam(name = "startDate") Date startDate,
            @WebParam(name = "endDate") Date endDate,
            @WebParam(name = "numberOfBeds") int numberOfBeds,
            @WebParam(name = "smoking") boolean smoking,
            @WebParam(name = "seaView") boolean seaView
    ) throws IncorrectCredentialsFault;
}

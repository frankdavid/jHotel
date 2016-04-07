package hu.frankdavid.hotel.entity.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import hu.frankdavid.hotel.entity.Reservation;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = 1560178822L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final BooleanPath checkedIn = createBoolean("checkedIn");

    public final BooleanPath checkedOut = createBoolean("checkedOut");

    public final BooleanPath checkInAvailable = createBoolean("checkInAvailable");

    public final BooleanPath checkOutAvailable = createBoolean("checkOutAvailable");

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final QGuest guest;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<hu.frankdavid.hotel.entity.Purchase, QPurchase> purchases = this.<hu.frankdavid.hotel.entity.Purchase, QPurchase>createList("purchases", hu.frankdavid.hotel.entity.Purchase.class, QPurchase.class, PathInits.DIRECT2);

    public final QRoom room;

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QReservation(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QReservation(PathMetadata<?> metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.guest = inits.isInitialized("guest") ? new QGuest(forProperty("guest")) : null;
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room")) : null;
    }

}


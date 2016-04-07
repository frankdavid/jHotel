package hu.frankdavid.hotel.entity.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import hu.frankdavid.hotel.entity.Guest;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QGuest is a Querydsl query type for Guest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGuest extends EntityPathBase<Guest> {

    private static final long serialVersionUID = 783412754L;

    public static final QGuest guest = new QGuest("guest");

    public final QUser _super = new QUser(this);

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    //inherited
    public final StringPath passwordHash = _super.passwordHash;

    public final StringPath phone = createString("phone");

    public final ListPath<hu.frankdavid.hotel.entity.Reservation, QReservation> reservations = this.<hu.frankdavid.hotel.entity.Reservation, QReservation>createList("reservations", hu.frankdavid.hotel.entity.Reservation.class, QReservation.class, PathInits.DIRECT2);

    public final SetPath<Object, SimplePath<Object>> roles = this.<Object, SimplePath<Object>>createSet("roles", Object.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public QGuest(String variable) {
        super(Guest.class, forVariable(variable));
    }

    public QGuest(Path<? extends Guest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGuest(PathMetadata<?> metadata) {
        super(Guest.class, metadata);
    }

}


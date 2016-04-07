package hu.frankdavid.hotel.entity.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import hu.frankdavid.hotel.entity.Room;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = -1498427039L;

    public static final QRoom room = new QRoom("room");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath number = createString("number");

    public final NumberPath<Integer> numberOfBeds = createNumber("numberOfBeds", Integer.class);

    public final NumberPath<Integer> pricePerNight = createNumber("pricePerNight", Integer.class);

    public final BooleanPath seaView = createBoolean("seaView");

    public final BooleanPath smoking = createBoolean("smoking");

    public QRoom(String variable) {
        super(Room.class, forVariable(variable));
    }

    public QRoom(Path<? extends Room> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoom(PathMetadata<?> metadata) {
        super(Room.class, metadata);
    }

}


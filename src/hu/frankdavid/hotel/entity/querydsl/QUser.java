package hu.frankdavid.hotel.entity.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import hu.frankdavid.hotel.entity.User;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1498334127L;

    public static final QUser user = new QUser("user");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath passwordHash = createString("passwordHash");

    public final SetPath<Object, SimplePath<Object>> roles = this.<Object, SimplePath<Object>>createSet("roles", Object.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata<?> metadata) {
        super(User.class, metadata);
    }

}


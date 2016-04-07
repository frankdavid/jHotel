package hu.frankdavid.hotel.entity.querydsl;

import static com.mysema.query.types.PathMetadataFactory.*;
import hu.frankdavid.hotel.entity.Employee;


import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = -1890221004L;

    public static final QEmployee employee = new QEmployee("employee");

    public final QUser _super = new QUser(this);

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath passwordHash = _super.passwordHash;

    public final SetPath<Object, SimplePath<Object>> roles = this.<Object, SimplePath<Object>>createSet("roles", Object.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

    public QEmployee(String variable) {
        super(Employee.class, forVariable(variable));
    }

    public QEmployee(Path<? extends Employee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata);
    }

}


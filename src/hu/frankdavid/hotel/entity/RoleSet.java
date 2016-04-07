package hu.frankdavid.hotel.entity;

import java.util.*;

public class RoleSet implements Set<Role> {
    private final Set<Role> roles;

    public RoleSet(Role... roles) {
        this(new HashSet<>(Arrays.asList(roles)));
    }

    public RoleSet() {
        this(new HashSet<Role>());
    }

    public RoleSet(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public int size() {
        return roles.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if(roles.contains(o))
            return true;
        for (Role role : this) {
            if(role.getIncludedRoles().contains(o))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<Role> iterator() {
        return roles.iterator();
    }

    @Override
    public Object[] toArray() {
        return roles.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return roles.toArray(a);
    }

    @Override
    public boolean add(Role role) {
        return roles.add(role);
    }

    @Override
    public boolean remove(Object o) {
        return roles.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if(!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Role> c) {
        return roles.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return roles.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return roles.removeAll(c);
    }

    @Override
    public void clear() {
        roles.clear();
    }

    public Set<Role> flatten() {
        return flattenInto(new HashSet<Role>());
    }

    private Set<Role> flattenInto(Set<Role> set) {
        for (Role role : this) {
            boolean added = set.add(role);
            if(added)
                role.getIncludedRoles().flattenInto(set);
        }
        return set;
    }

    public Set<Role> delegate() {
        return delegate();
    }
}

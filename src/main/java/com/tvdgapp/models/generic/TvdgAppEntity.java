package com.tvdgapp.models.generic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

public abstract class TvdgAppEntity<K extends Serializable & Comparable<K>, E extends TvdgAppEntity<K, ?>>
        implements Serializable, Comparable<E> {

    private static final long serialVersionUID = -3988499137919577054L;

    public static final Collator DEFAULT_STRING_COLLATOR = Collator.getInstance(Locale.ENGLISH);

    static {
        DEFAULT_STRING_COLLATOR.setStrength(Collator.PRIMARY);
    }

    /**
     * Sets the value of the unique identifier.
     *
     * @param id id
     */

    public abstract K getId();

    /**
     * Sets the value of the unique identifier.
     *
     * @param id id
     */

    public abstract void setId(K id);

    /**
     * Indicates whether the object has already been persisted or not
     *
     * @return true if the object has not yet been persisted
     */
    @JsonIgnore
    public boolean isNew() { return getId() == null; }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }

        if (Hibernate.getClass(object) != Hibernate.getClass(this)) {
            return false;
        }

        TvdgAppEntity<K, E> entity = (TvdgAppEntity<K, E>) object; // treated above but Hibernate wrapper
        K id = getId();

        if (id == null) {
            return false;
        }

        return id.equals(entity.getId());
    }


    @Override
    public int hashCode() { return getClass().hashCode();}

    public int compareTo(E o) {
        if (this == o) {
            return 0;
        }
        return this.getId().compareTo(o.getId());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("entity.");
        builder.append(Hibernate.getClass(this).getSimpleName());
        builder.append("<");
        builder.append(getId());
        builder.append("-");
        builder.append(super.toString());
        builder.append(">");

        return builder.toString();
    }

}

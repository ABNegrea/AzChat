package ReteaDeSocializare.Domain;

import java.io.Serializable;

/**
 *
 * @param <ID> type of id
 */
public class Entity<ID> implements Serializable
{

    private static final long serialVersionUid = 123456789L;
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }


}
package cloud.grup.cloudgrupRESTAPI.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "_id", nullable = false)
    private Long _id;
    @Column(name = "url", nullable = false)
    private String url;
    @Column(name = "key", nullable = false)
    private String key;
    @Column(name = "expiration_date", nullable = false)
    private Calendar expiration_date;
    @Column(name = "access_rate", nullable = false)
    private int access_rate;

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        accessed();
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Calendar getExpirationDate() {
        return expiration_date;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expiration_date = expirationDate;
    }

    public void initExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 1);
        this.expiration_date = cal;
    }

    public int getAccessRate() {
        return this.access_rate - 2;
    }

    public void initAccessRate() {
        this.access_rate = 0;
    }

    public void accessed() {
        ++access_rate;
    }

    public String toString1() {
        return this.getId()+" "+this.getUrl()+" "+this.getKey();
    }
}


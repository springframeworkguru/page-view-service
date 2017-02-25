package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by jt on 2/22/17.
 */
@Entity
public class PageView {
    @Id
    @GeneratedValue
    private Long id;

    private String pageUrl;
    private Date pageViewDate;
    private String correlationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public Date getPageViewDate() {
        return pageViewDate;
    }

    public void setPageViewDate(Date pageViewDate) {
        this.pageViewDate = pageViewDate;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}

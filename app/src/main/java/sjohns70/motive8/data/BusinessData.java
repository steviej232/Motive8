/* Copyright statement */

package sjohns70.motive8.data;

import java.io.Serializable;

/**
 * BusinessData.java
 *
 * This class represents a Business including logo, business id, description of the business, and
 * company name.
 */

public class BusinessData implements Serializable {
    private String description;
    private String company_name;
    private String logo;
    private String id;
    private String phone;
    private String placeId;

    public BusinessData() {}

    public BusinessData(String descript, String name, String image, String uniqueId, String pho) {
        description = descript;
        company_name = name;
        logo = image;
        id = uniqueId;
        phone = pho;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPhone(){ return phone;}

    public void setPhone(String p){ phone = p;}

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}

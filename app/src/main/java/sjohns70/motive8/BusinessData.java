package sjohns70.motive8;


import java.io.Serializable;

/**
 * Created by KendallGassner on 10/6/16.
 */

public class BusinessData implements Serializable {
    private String description;
    private String company_name;
    private String logo;
    private String id;

    public BusinessData() {}

    public BusinessData(String descript, String name, String image, String uniqueId){
        description = descript;
        company_name = name;
        logo = image;
        id = uniqueId;

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

}

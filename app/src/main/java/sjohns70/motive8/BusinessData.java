package sjohns70.motive8;

import android.media.Image;

/**
 * Created by KendallGassner on 10/6/16.
 */

public class BusinessData {
    private String description;
    private String companyName;
    private Image logo;
    private String id;

    public BusinessData(String descript, String name, Image image, String uniqueId){
        description = descript;
        companyName = name;
        logo = image;
        id = uniqueId;

    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String bio) {
        this.description = bio;
    }
}

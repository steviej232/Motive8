/* Copyright statement */

package sjohns70.motive8;

/**
 * RewardsData.java
 *
 * This class represents a coupon with associated business information and a coupon description.
 */
public class RewardsData {

    private String id;
    private String business_id;
    private String name;
    private String description;
    private int value;

    public RewardsData(){};

    public RewardsData(String id, String businessId, String rewardsName, String description, int value){
        id = id;
        business_id = businessId;
        name = rewardsName;
        description = description;
        value = value;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

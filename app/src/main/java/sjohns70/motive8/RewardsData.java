package sjohns70.motive8;

/**
 * Created by KendallGassner on 10/6/16.
 */

public class RewardsData {

    private String id;
    private String businessId;
    private String rewardsName;
    private String description;
    private int value;

    public RewardsData(String id, String businessId, String rewardsName, String description, int value){
        id = id;
        businessId = businessId;
        rewardsName = rewardsName;
        description = description;
        value = value;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getRewardsName() {
        return rewardsName;
    }

    public void setRewardsName(String rewardsName) {
        this.rewardsName = rewardsName;
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



}

package MLSClient;

public class LicenceToken {
    private String licenceUserName;
    private boolean licence;
    private String expired;

    public LicenceToken(String licenceUserName, boolean licence, String expired) {
        this.licenceUserName = licenceUserName;
        this.licence = licence;
        this.expired = expired;
    }

    public String getLicenceUserName() {
        return licenceUserName;
    }

    public boolean isLicence() {
        return licence;
    }

    public String getExpired() {
        return expired;
    }

    @Override
    public String toString() {
        return "LicenceToken{" +
                "licenceUserName='" + licenceUserName + '\'' +
                ", licence=" + licence +
                ", expired='" + expired + '\'' +
                '}';
    }
}

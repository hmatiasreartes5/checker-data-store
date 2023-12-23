package checker.dto;

public class HashData {
    private String Hash;
    private boolean isMigratedNewDS;

    public String getHash() { return Hash; }

    public void setHash(String hash) { Hash = hash; }

    public Boolean isMigratedNewDS() { return isMigratedNewDS; }

    public void setMigratedNewDS(Boolean migratedNewDS) { isMigratedNewDS = migratedNewDS; }

}

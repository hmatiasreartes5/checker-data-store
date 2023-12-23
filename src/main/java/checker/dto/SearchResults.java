package checker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResults {
    private int limit;
    private int total;
    private int offset;
    private String mode;
    private List<JSONObject> results;

    public List<JSONObject> getResults() { return results; }

    public void setResults(List<JSONObject> results) { this.results = results; }

    public List<String> getDataHash(List<JSONObject> jsonList) {
        List<String> dataHash = new ArrayList<>();
        for (JSONObject json : jsonList) {
            dataHash.add(getSHA256(json.toString()));
        }
        return dataHash;
    }

    public static String getSHA256(String value) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            hash = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
}

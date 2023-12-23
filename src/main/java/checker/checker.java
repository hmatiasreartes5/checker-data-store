package checker;

import checker.dto.HashData;
import checker.dto.SearchResults;
import checker.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class checker {

    static RestTemplate client = new RestTemplate();
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args){

        List<String> OldsDs = search(Utils.SEARCH_OLD_DATA_STORE);
        List<String> NewDs = search(Utils.SEARCH_OLD_DATA_STORE);
        List<HashData> hashData = checkEquals(OldsDs, NewDs);
        writeFile(hashData);
    }

    static List<String> search(String endPoint) {
        String url = UriComponentsBuilder.fromHttpUrl(endPoint)
                .queryParam("", "")
                .queryParam("", "")
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        if (endPoint.equalsIgnoreCase(Utils.SEARCH_NEW_DATA_STORE))
            headers.set("token", Utils.TOKEN);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> result = client.exchange(url, HttpMethod.GET, entity, String.class);

        List<String> dataHashes = new ArrayList<>();
        try {
            SearchResults searchResults = objectMapper.readValue(result.getBody(), SearchResults.class);
            dataHashes = searchResults.getDataHash(searchResults.getResults());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return dataHashes;
    }

    static List<HashData> checkEquals(List<String> dataOldsDs, List<String> dataNewsDs){
        List<HashData> hashData = new ArrayList<>();
        for (String dataOldDs : dataOldsDs) {
            HashData hash = new HashData();
            hash.setHash(dataOldDs);
            for (String dataNewDs : dataNewsDs) {
                if (dataOldDs.equalsIgnoreCase(dataNewDs)){
                    hash.setMigratedNewDS(true);
                }
            }
            hashData.add(hash);
        }
        return hashData;
    }

    static void writeFile(List<HashData> hashData) {
        List<String> results = new ArrayList<>();
        for (HashData hash : hashData) {
            JSONObject json = new JSONObject();
            json.put("HashCode", hash.getHash());
            json.put("isMigratedNewDs", hash.isMigratedNewDS());
            results.add(json.toString());
        }

        File file = new File("resultado.txt");
        try {
            Files.write(file.toPath(), results, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

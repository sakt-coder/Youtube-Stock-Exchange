package com.ytse.youtubestockexchange.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ytse.youtubestockexchange.models.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.ytse.youtubestockexchange.constants.YtseConstants.API_KEY;
import static com.ytse.youtubestockexchange.constants.YtseConstants.API_URL;

@Service
public class GAPIService {

    Queue<Channel> buffer = new LinkedList<Channel>();

    Logger logger = LoggerFactory.getLogger(GAPIService.class);

    public ResponseEntity<?> search(String query) {
        List<Channel> results = new ArrayList<>();
        JsonNode rootNode;
        try {
            rootNode = HTTPGet(String.format(
                "%s/search?part=snippet&q=%s&type=channel&key=%s",
                    API_URL, query, API_KEY
            ));
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus().value()).body(Map.of("Message", "Search Failed"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("Message", "Search Failed"));
        }
        for (int i = 1; i <= rootNode.get("pageInfo").get("resultsPerPage").asInt(); i++) {
            JsonNode item = rootNode.get("items").get(i - 1).get("snippet");
            results.add(new Channel(item.get("title").asText(), item.get("channelId").asText()));
        }

        List<String> queryList = new ArrayList<String>();
        for(Channel channel: results)
            queryList.add(channel.channelId);
        List<Long> priceList = getPrices(queryList);
        for(int i=0;i<results.size();i++)
            results.get(i).sharePrice = priceList.get(i);
            
        return ResponseEntity.ok().body(results);
    }

    public List<Long> getPrices(List<String> queryList) {
        logger.info(queryList.toString());
        if(queryList.size()==0)
            return null;
        JsonNode rootNode;
        StringBuilder query = new StringBuilder();
        for(String channelId: queryList)
            query.append("&id="+channelId);
        try {
            rootNode = HTTPGet(String.format(
                "%s/channels?part=statistics%s&maxResults=%d&key=%s",
                    API_URL, query.toString(), queryList.size(), API_KEY
            ));
        }
        catch (ResponseStatusException e) {
            logger.error("Channel Stats query failed");
            return null;
        }
        catch (Exception e) {
            logger.error("Channel Stats internal server exception");
            return null;
        }
        List<Long> retList = new ArrayList<>();
        for (int i = 0; i < queryList.size(); i++) {
            JsonNode item = rootNode.get("items").get(i).get("statistics");
            long viewCount = item.get("viewCount").asLong();
            long videoCount = item.get("videoCount").asLong();
            retList.add(viewCount/videoCount);
        }
        return retList;
    }

    private JsonNode HTTPGet(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new ResponseStatusException(HttpStatus.valueOf(responseCode));
        }
        else {
            StringBuilder json = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String s;
            while ((s = br.readLine()) != null) {
                json.append(s);
            }
            br.close();

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            return mapper.readTree(json.toString());
        }
    }
}

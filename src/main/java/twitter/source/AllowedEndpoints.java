package twitter.source;

import java.util.ArrayList;
import java.util.List;

public class AllowedEndpoints {

    private List<String> allowedEndpoints;

    public AllowedEndpoints() {
        this.allowedEndpoints = new ArrayList<>();
    }

    public void addEndpoint(String endpoint) {
        allowedEndpoints.add(endpoint);
    }

    public boolean isEndpointAllowed(String endpoint) {
        return allowedEndpoints.contains(endpoint);
    }
}

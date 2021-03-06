package org.ovirt.engine.core.vdsbroker.vdsbroker;

import java.util.Collections;
import java.util.Map;

public class LeaseInfoReturn {
    private static final String STATUS = "status";
    private static final String INFO = "info";

    private Status status;
    private Map<String, String> leaseInfo = Collections.emptyMap();

    @SuppressWarnings("unchecked")
    public LeaseInfoReturn(Map<String, Object> innerMap) {
        status = new Status((Map<String, Object>) innerMap.get(STATUS));
        leaseInfo = (Map<String, String>) innerMap.get(INFO);
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getLeaseInfo() {
        return leaseInfo;
    }
}

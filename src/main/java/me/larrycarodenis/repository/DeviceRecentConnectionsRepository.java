package me.larrycarodenis.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Repository
public class DeviceRecentConnectionsRepository extends HashMap<Long, DeviceRecentConnectionsRepository.RecentConnection> {
    public class RecentConnection {
        private String lastIp;
        private LocalDateTime lastConnection;

        public RecentConnection(String lastIp, LocalDateTime lastConnection) {
            this.lastIp = lastIp;
            this.lastConnection = lastConnection;
        }

        public String getLastIp() {
            return lastIp;
        }

        public LocalDateTime getLastConnection() {
            return lastConnection;
        }
    }

    public void saveConnection(Long id, String ipAddress) {
        if (ipAddress.substring(0, 2).equalsIgnoreCase("0:"))
            ipAddress = "127.0.0.1";
        put(id, new RecentConnection(ipAddress, LocalDateTime.now()));
    }
}

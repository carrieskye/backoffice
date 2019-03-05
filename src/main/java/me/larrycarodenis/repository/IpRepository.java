package me.larrycarodenis.repository;

import me.larrycarodenis.domain.Ip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public class IpRepository {

    public List<Ip> ips = new ArrayList<>();

    public void save(Ip ip)
    {
        ips.add(ip);
    }

    public List<Ip> findAll()
    {
        return ips;
    }
}

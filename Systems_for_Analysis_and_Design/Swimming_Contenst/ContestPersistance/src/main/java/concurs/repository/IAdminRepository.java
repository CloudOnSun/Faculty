package concurs.repository;

import concurs.domain.Admin;

public interface IAdminRepository extends Repository<Admin, Integer>{

    public Admin getAccount(String email, String password);
}

package moe.takanashihoshino.nyaniduserserver.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.SqlUtils.NyanIDuser;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.SqlUtils.Service.NyanidUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NyanidUserServiceImpl implements NyanidUserService {

    @Autowired
    private NyanIDuserRepository nyanIDuserRepository;

    @Override
    public NyanIDuser save(NyanIDuser nyanIDuser) {
        return nyanIDuserRepository.save(nyanIDuser);
    }

}

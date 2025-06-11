package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.NyanIDuser;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.NyanIDuserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.NyanidUserService;
import org.springframework.stereotype.Service;

@Service
public class NyanidUserServiceImpl implements NyanidUserService {


    private final NyanIDuserRepository nyanIDuserRepository;

    public NyanidUserServiceImpl(NyanIDuserRepository nyanIDuserRepository) {
        this.nyanIDuserRepository = nyanIDuserRepository;
    }

    @Override
    public NyanIDuser save(NyanIDuser nyanIDuser) {
        return nyanIDuserRepository.save(nyanIDuser);
    }

}

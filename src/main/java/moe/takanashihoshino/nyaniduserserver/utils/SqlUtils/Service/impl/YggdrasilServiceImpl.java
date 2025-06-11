package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;

import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.YggdrasilRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.YggdrasilService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Yggdrasil;
import org.springframework.stereotype.Service;

@Service
public class YggdrasilServiceImpl implements YggdrasilService {


    private final YggdrasilRepository yggdrasilRepository;

    public YggdrasilServiceImpl(YggdrasilRepository yggdrasilRepository) {
        this.yggdrasilRepository = yggdrasilRepository;
    }

    @Override
    public Yggdrasil save(Yggdrasil yggdrasil) {
        return yggdrasilRepository.save(yggdrasil);
    }
}

package moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.impl;


import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.YggdrasilPlayerRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.YggdrasilPlayerService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.YggdrasilPlayer;
import org.springframework.stereotype.Service;

@Service
public class YggdrasilPlayerImpl implements YggdrasilPlayerService {


    private final YggdrasilPlayerRepository yggdrasilPlayerRepository;

    public YggdrasilPlayerImpl(YggdrasilPlayerRepository yggdrasilPlayerRepository) {
        this.yggdrasilPlayerRepository = yggdrasilPlayerRepository;
    }

    @Override
    public YggdrasilPlayer save(YggdrasilPlayer yggdrasilPlayer) {
        return yggdrasilPlayerRepository.save(yggdrasilPlayer);
    }
}

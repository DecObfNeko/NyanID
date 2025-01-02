package moe.takanashihoshino.nyaniduserserver.utils.Command.CommandList;

import moe.takanashihoshino.nyaniduserserver.utils.Command.Command;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.BanUserList;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.AccountsRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.UserDevicesRepository;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.BanUserService;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Service.UserDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class UserManagerCommand implements Command {


    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UserDevicesRepository userDevicesRepository;

    @Autowired
    private BanUserRepository banUserRepository;

    @Autowired
    private UserDevicesService userDevicesService;

    @Autowired
    private BanUserService banUserService;




    private static Logger logger = Logger.getLogger("NyanID");
    @Override
    public String getName() {
        return "/ac";
    }

    @Override
    public String getDescription() {
        return "对账号进行管理,使用方法:[/ac unban/ban(arg: reason)/remove/create(args: email password username)/change(args: userdevices/username/more data) uid/email/username]";
    }

    @Override
    public void execute(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InterruptedException {
        if (args.length > 1){
            String t1 = args[0];
            if (!t1.equals("create") && !t1.equals("change")){
                switch (t1) {
                    case "ban":
                        if (!(accountsRepository.find(args[1]) == null)){
                            String uid = accountsRepository.find(args[1]);
                            List<Object[]> resultList = banUserRepository.findByUid(uid);
                            if (resultList.isEmpty()) {
                                String banid = String.valueOf(UUID.nameUUIDFromBytes((args[1]+LocalDateTime.now()).getBytes(StandardCharsets.UTF_8)));
                                BanUserList banUserList = new BanUserList();
                                banUserList.setBanID(banid);
                                banUserList.setUid(uid);
                                banUserList.setBanTime(LocalDateTime.now());
                                if (args.length < 3){
                                    banUserList.setReason("This account has been banned for violating our User Agreement. 杂鱼喵~");
                                }else {
                                    banUserList.setReason(args[2]);
                                }
                                banUserList.setActive(true);
                                banUserList.setBannedBy("TakanashiNyaphthalene");
                                banUserService.save(banUserList);
                                logger.info("Banned User ["+uid+"] and BanID:"+banid);
                            }else {
                                BanUserList banUserList = null;
                                if (!resultList.isEmpty()) {
                                    Object[] result = resultList.get(0);
                                    banUserList = new BanUserList();
                                    banUserList.setBanID((String) result[0]);
                                    banUserList.setBanTime((LocalDateTime) result[1]);
                                    banUserList.setReason((String) result[2]);
                                    banUserList.setBannedBy((String) result[3]);
                                    logger.info("User ["+uid+"] is Banned . Reason: ["+ result[2]+"], BanID: ["+result[0]+"], BanTime: ["+result[1]+"], BannedBy: ["+result[3]+"].");
                                }
                            }
                        }else {
                            logger.warning("用户不存在杂鱼喵~");
                        }
                        break;
                    case "remove":

                        break;




                    default:
                        logger.warning("参数错误,请输入ban/remove/create/change ");
                        break;
                }


            }else {




            }




            }else {
            logger.warning("参数错误,请输入ban/remove/create/change ");
            }























        }
    }


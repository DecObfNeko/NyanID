package moe.takanashihoshino.nyaniduserserver.server.web.Public;

import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.utils.SqlUtils.Repository.BanUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("api/zako/res/{type}/{data}")
public class GetImgResource {

    @Autowired
    private BanUserRepository banUserRepository;
    @GetMapping
    public Object GetImgResource(@PathVariable String type, @PathVariable String data, HttpServletResponse response) throws IOException {
        if (banUserRepository.findBanIDByUid(data) == null) {
            switch (type) {
                case "avatar":

                    Path UserAvatar = Paths.get("Data/UserAvatar/UA-" + data + ".png");
                    if (UserAvatar == null) {
                        return ErrRes.NotFoundResourceException("Not Found User Avatar", response);
                    } else {
                        File file = new File(UserAvatar.toString());
                        if (file.exists()) {
                            response.setContentType("image/png");
                            InputStream in = new FileInputStream(file);
                            byte[] bytes = in.readAllBytes();
                            return bytes;
                        } else {
                            return ErrRes.NotFoundResourceException("Not Found User Avatar", response);
                        }
                    }

                case "":


                default:
                    return ErrRes.NotFoundResourceException("Not Found Resource", response);




            }
        } else {
            response.setContentType("application/json");
            return ErrRes.NotFoundResourceException("Not Found User Avatar", response);
        }
    }
}

package moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer;

import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.utils.ErrUtils.ErrRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("textures/{data}")
public class Textures {

    @GetMapping
    public Object GetImgResource( @PathVariable String data, HttpServletResponse response) throws IOException {
        Path SKINTexture = Paths.get("Data/YggdrasilTexture/" + data + ".png");
            File file = new File(SKINTexture.toString());
            if (file.exists()) {
                response.setContentType("image/png");
                InputStream in = new FileInputStream(file);
                byte[] bytes = in.readAllBytes();
                in.close();
                return bytes;
            } else {
                return ErrRes.NotFoundResourceException("Not Found Texture", response);
            }


    }
}

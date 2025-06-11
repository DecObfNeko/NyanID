package moe.takanashihoshino.nyaniduserserver.server.YggdrasilServer.Authserver.Json;

import lombok.Getter;
import lombok.Setter;

public class TexturesJson {
    @Getter
    @Setter
    public static class TextureMetadata {
        private String model;
        public TextureMetadata(String model) {
            this.model = model;
        }

    }
    @Getter
    @Setter
    public static class SkinTexture {
        private String url;
        private TextureMetadata metadata;
        public SkinTexture(String url, TextureMetadata metadata) {
            this.url = url;
            this.metadata = metadata;
        }
    }

}